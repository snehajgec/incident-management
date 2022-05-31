package com.finleap.incidentmanagement.service.impl;

import com.finleap.incidentmanagement.dto.IncidentDTO;
import com.finleap.incidentmanagement.dto.IncidentResponse;
import com.finleap.incidentmanagement.entity.*;
import com.finleap.incidentmanagement.exception.IncidentManagementInternalServerException;
import com.finleap.incidentmanagement.exception.IncidentManagementNotFoundException;
import com.finleap.incidentmanagement.exception.IncidentManagementUnauthorizedException;
import com.finleap.incidentmanagement.repository.IncidentRepository;
import com.finleap.incidentmanagement.repository.UserRepository;
import com.finleap.incidentmanagement.service.IncidentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IncidentServiceImpl implements IncidentService {

    private final UserRepository userRepository;
    private final IncidentRepository incidentRepository;

    private static final String USER_ENTITY = "User";
    private static final String INCIDENT_ENTITY = "Incident";

    @Autowired
    public IncidentServiceImpl(UserRepository userRepository,
                               IncidentRepository incidentRepository) {
        this.userRepository = userRepository;
        this.incidentRepository = incidentRepository;
    }

    @Override
    public IncidentResponse createIncidentReport(IncidentDTO incidentDTO, Integer creatorId) {
        IncidentReport incidentReport = new IncidentReport();
        incidentReport.setTitle(incidentDTO.getTitle());
        incidentReport.setDescription(incidentDTO.getDescription());
        incidentReport.setComments(incidentDTO.getComments());

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IncidentManagementNotFoundException(USER_ENTITY, incidentDTO.getAssigneeId().toString()));

        IncidentUser creatorUser = new IncidentUser();
        creatorUser.setIncident(incidentReport);
        creatorUser.setUser(creator);
        creatorUser.setUserType(UserType.CREATOR);

        if(incidentDTO.getAssigneeId() == null) {
            incidentReport.setStatus(IncidentStatus.NEW);
            incidentReport.setIncidentUsers(Set.of(creatorUser));
        } else {
            incidentReport.setStatus(incidentDTO.getAssigneeId() == null ? IncidentStatus.NEW : IncidentStatus.ASSIGNED);
            User assignee = userRepository.findById(incidentDTO.getAssigneeId())
                    .orElseThrow(() -> new IncidentManagementNotFoundException(USER_ENTITY, incidentDTO.getAssigneeId().toString()));
            IncidentUser assigneeUser = new IncidentUser();
            assigneeUser.setIncident(incidentReport);
            assigneeUser.setUser(assignee);
            assigneeUser.setUserType(UserType.ASSIGNEE);
            incidentReport.setIncidentUsers(Set.of(assigneeUser, creatorUser));
        }
        try {
            incidentReport = incidentRepository.save(incidentReport);
            return mapToIncidentResponse(incidentReport);
        } catch (Exception ex) {
            throw new IncidentManagementInternalServerException("Unable to save incident report due to the following exception : " + ex.getMessage());
        }
    }

    @Override
    public List<IncidentResponse> getAllIncidentReports() {
        try {
            List<IncidentReport> incidentReports = incidentRepository.findAll();
            return incidentReports != null ? incidentReports.stream().map(incidentReport ->
                    mapToIncidentResponse(incidentReport)).collect(Collectors.toList()) : Collections.EMPTY_LIST;
        } catch (Exception ex) {
            throw new IncidentManagementInternalServerException("Unable to fetch incident reports due to the following exception : " + ex.getMessage());
        }
    }

    @Override
    public IncidentResponse updateIncidentReport(Integer userId, Integer incidentId, IncidentDTO incidentDTO) {
        IncidentReport incidentReport = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new IncidentManagementNotFoundException(INCIDENT_ENTITY,incidentId.toString()));

        IncidentUser user = incidentReport.getIncidentUsers().stream().filter(incidentUser -> incidentUser.getUser().getUserId().equals(userId)).findAny()
                .orElseThrow(() -> new IncidentManagementUnauthorizedException(incidentDTO.getAssigneeId().toString(), incidentDTO.getAssigneeId().toString()));

        Set<IncidentUser> incidentUsers = incidentReport.getIncidentUsers();

        if(UserType.ASSIGNEE.equals(user.getUserType())) {
            incidentReport.setStatus(IncidentStatus.valueOf(incidentDTO.getStatus()));
        } else if(UserType.CREATOR.equals(user.getUserType()) && incidentDTO.getAssigneeId() != null){
            IncidentUser assignee = incidentUsers.stream().filter(incidentUser -> UserType.ASSIGNEE.equals(incidentUser.getUserType())).findAny().orElse(null);
            User newUser = userRepository.findById(incidentDTO.getAssigneeId()).orElseThrow(() ->  new IncidentManagementNotFoundException(USER_ENTITY, incidentDTO.getAssigneeId().toString()));
            if(assignee != null && !incidentDTO.getAssigneeId().equals(assignee.getUser().getUserId())) {
                incidentUsers.remove(assignee);
                assignee.setUser(newUser);
                incidentReport.setIncidentUsers(Set.of(assignee));
            } else if (assignee == null) {
                IncidentUser newAssignee = new IncidentUser();
                newAssignee.setIncident(incidentReport);
                newAssignee.setUser(newUser);
                newAssignee.setUserType(UserType.ASSIGNEE);
                incidentReport.setStatus(IncidentStatus.ASSIGNED);
                incidentReport.setIncidentUsers(Set.of(newAssignee));
            }
        }
        incidentReport.setTitle(incidentDTO.getTitle() != null ? incidentDTO.getTitle() : incidentReport.getTitle());
        incidentReport.setDescription(incidentDTO.getDescription() != null ? incidentDTO.getDescription() : incidentReport.getDescription());
        incidentReport.setComments(incidentDTO.getComments() != null ? incidentDTO.getComments() : incidentReport.getComments());
        try {
            incidentReport = incidentRepository.save(incidentReport);
            return mapToIncidentResponse(incidentReport);
        } catch (Exception ex) {
            throw new IncidentManagementInternalServerException("Unable to update incident report due to the following exception : " + ex.getMessage());
        }
    }

    @Override
    public void deleteIncidentReport(Integer userId, Integer incidentId) {
        IncidentReport incidentReport = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new IncidentManagementNotFoundException(INCIDENT_ENTITY, incidentId.toString()));

        IncidentUser user = incidentReport.getIncidentUsers().stream().filter(incidentUser ->
                incidentUser.getUser().getUserId().equals(userId) && UserType.CREATOR.equals(incidentUser.getUserType())).findAny()
                .orElseThrow(() -> new IncidentManagementUnauthorizedException(userId.toString(), incidentId.toString()));
        try {
            incidentRepository.delete(incidentReport);
        } catch (Exception ex) {
            throw new IncidentManagementInternalServerException("Unable to delete incident report due to the following exception : " + ex.getMessage());
        }

    }

    private IncidentResponse mapToIncidentResponse(IncidentReport incidentReport) {
        IncidentResponse incidentResponse = IncidentResponse.builder().incidentId(incidentReport.getIncidentId()).title(incidentReport.getTitle())
                .status(incidentReport.getStatus().name()).description(incidentReport.getDescription())
                .comments(incidentReport.getComments()).build();
        Set<IncidentUser> incidentUsers = incidentReport.getIncidentUsers();
        IncidentUser creator = incidentUsers.stream().filter(incidentUser -> UserType.CREATOR.equals(incidentUser.getUserType())).findAny().orElse(null);
        IncidentUser assignee = incidentUsers.stream().filter(incidentUser -> UserType.ASSIGNEE.equals(incidentUser.getUserType())).findAny().orElse(null);
        incidentResponse = incidentResponse.toBuilder().assigneeId(assignee != null ? assignee.getUser().getUserId() : null).creatorId(creator.getUser().getUserId()).build();
        return incidentResponse;
    }

}
