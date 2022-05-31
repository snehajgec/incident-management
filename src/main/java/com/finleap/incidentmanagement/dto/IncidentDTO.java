package com.finleap.incidentmanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IncidentDTO {

    private String title;
    private String status;
    private Integer assigneeId;
    private String description;
    private String comments;
}
