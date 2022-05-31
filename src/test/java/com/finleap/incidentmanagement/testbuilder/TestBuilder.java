package com.finleap.incidentmanagement.testbuilder;

import com.finleap.incidentmanagement.dto.IncidentDTO;
import com.finleap.incidentmanagement.dto.IncidentResponse;
import com.finleap.incidentmanagement.dto.UserDTO;
import com.finleap.incidentmanagement.entity.*;

import java.util.Set;

public class TestBuilder {

    public static IncidentDTO getIncidentDTOWithoutAssignee() {
        return  IncidentDTO.builder().title("Error xyz").description("desc").comments("comments").build();
    }

    public static IncidentDTO getIncidentDTOWithAssignee() {
        return IncidentDTO.builder().title("Error xyz").assigneeId(2).description("desc").comments("comments").build();
    }

    public static User getCreatorUser() {
        User user = new User();
        user.setUserId(1);
        user.setName("Creator");
        return user;
    }

    public static User getAssigneeUser() {
        User user = new User();
        user.setUserId(2);
        user.setName("Assignee");
        return user;
    }

    public static IncidentResponse getIncidentResponse() {
        return IncidentResponse.builder().incidentId(1).status("NEW").assigneeId(2).creatorId(1).title("Error xyz").description("desc").comments("comments").build();
    }

    public static IncidentReport getIncidentReport() {
        IncidentUser assignee = new IncidentUser();
        assignee.setIncidentUserId(1);
        assignee.setUser(getAssigneeUser());
        assignee.setUserType(UserType.ASSIGNEE);

        IncidentUser creator = new IncidentUser();
        creator.setIncidentUserId(2);
        creator.setUser(getCreatorUser());
        creator.setUserType(UserType.CREATOR);

        IncidentReport incidentReport = new IncidentReport();
        incidentReport.setIncidentId(1);
        incidentReport.setTitle("Error xyz");
        incidentReport.setStatus(IncidentStatus.NEW);
        incidentReport.setDescription("desc");
        incidentReport.setComments("comments");

        assignee.setIncident(incidentReport);
        creator.setIncident(incidentReport);
        incidentReport.setIncidentUsers(Set.of(assignee, creator));
        return incidentReport;
    }

    public static IncidentReport getUpdatedIncidentReport() {
        IncidentUser assignee = new IncidentUser();
        User user = getAssigneeUser();
        user.setUserId(3);
        assignee.setIncidentUserId(2);
        assignee.setUser(user);
        assignee.setUserType(UserType.ASSIGNEE);

        IncidentUser creator = new IncidentUser();
        creator.setIncidentUserId(1);
        creator.setUser(getCreatorUser());
        creator.setUserType(UserType.CREATOR);

        IncidentReport incidentReport = new IncidentReport();
        incidentReport.setIncidentId(1);
        incidentReport.setTitle("Error xyz");
        incidentReport.setStatus(IncidentStatus.NEW);
        incidentReport.setDescription("desc");
        incidentReport.setComments("comments");

        assignee.setIncident(incidentReport);
        creator.setIncident(incidentReport);
        incidentReport.setIncidentUsers(Set.of(assignee, creator));
        return incidentReport;
    }

    public static IncidentReport getIncidentReportWithNoAssignee() {
        IncidentUser creator = new IncidentUser();
        creator.setIncidentUserId(1);
        creator.setUser(getCreatorUser());
        creator.setUserType(UserType.CREATOR);

        IncidentReport incidentReport = new IncidentReport();
        incidentReport.setIncidentId(1);
        incidentReport.setTitle("Error xyz");
        incidentReport.setStatus(IncidentStatus.NEW);
        incidentReport.setDescription("desc");
        incidentReport.setComments("comments");

        creator.setIncident(incidentReport);
        incidentReport.setIncidentUsers(Set.of(creator));
        return incidentReport;
    }

    public static UserDTO getCreatorUserDto() {
        return UserDTO.builder().userId(1).name("Creator").build();
    }

    public static UserDTO getAssigneeUserDto() {
        return UserDTO.builder().userId(2).name("Assignee").build();
    }

    public static IncidentUser getAssignedIncidentUser() {
        IncidentUser incidentUser = new IncidentUser();
        incidentUser.setIncidentUserId(1);
        incidentUser.setIncident(getIncidentReport());
        incidentUser.setUser(getAssigneeUser());
        incidentUser.setUserType(UserType.ASSIGNEE);
        return incidentUser;
    }

    public static IncidentUser getCreatorIncidentUser() {
        IncidentUser incidentUser = new IncidentUser();
        incidentUser.setIncidentUserId(2);
        incidentUser.setIncident(getIncidentReport());
        incidentUser.setUser(getCreatorUser());
        incidentUser.setUserType(UserType.CREATOR);
        return incidentUser;
    }

}
