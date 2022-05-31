package com.finleap.incidentmanagement.exception;

public class IncidentManagementNotFoundException extends RuntimeException {

    public IncidentManagementNotFoundException(String entity, String id) {
        super(String.format("%s with id : %s is not found in the system", entity, id));
    }
}
