package com.finleap.incidentmanagement.exception;

public class IncidentManagementInternalServerException extends RuntimeException{
    public IncidentManagementInternalServerException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public IncidentManagementInternalServerException(String message) {
        super(message);
    }
}
