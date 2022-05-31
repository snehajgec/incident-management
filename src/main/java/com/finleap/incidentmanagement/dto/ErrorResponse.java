package com.finleap.incidentmanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private int errorStatusCode;
    private String errorMessage;
}
