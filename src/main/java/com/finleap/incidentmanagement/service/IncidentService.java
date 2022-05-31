package com.finleap.incidentmanagement.service;

import com.finleap.incidentmanagement.dto.IncidentDTO;
import com.finleap.incidentmanagement.dto.IncidentResponse;

import java.util.List;

public interface IncidentService {
    IncidentResponse createIncidentReport(IncidentDTO incidentDTO, Integer creatorId);

    List<IncidentResponse> getAllIncidentReports();

    IncidentResponse updateIncidentReport(Integer userId, Integer incidentId, IncidentDTO incidentDTO);

    void deleteIncidentReport(Integer userId, Integer incidentId);
}
