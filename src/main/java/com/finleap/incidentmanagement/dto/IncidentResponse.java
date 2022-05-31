package com.finleap.incidentmanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class IncidentResponse {
    private Integer incidentId;
    private String title;
    private String status;
    private Integer creatorId;
    private Integer assigneeId;
    private String description;
    private String comments;
}
