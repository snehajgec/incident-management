package com.finleap.incidentmanagement.exception;

public class IncidentManagementUnauthorizedException extends RuntimeException {

    public IncidentManagementUnauthorizedException(String userId, String incidentId) {
        super(String.format("User with userId : %s is not authorized to modify the incident with incidentId: %s", userId, incidentId));
    }
}
