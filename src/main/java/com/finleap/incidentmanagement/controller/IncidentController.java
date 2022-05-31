package com.finleap.incidentmanagement.controller;

import com.finleap.incidentmanagement.dto.IncidentDTO;
import com.finleap.incidentmanagement.dto.IncidentResponse;
import com.finleap.incidentmanagement.service.IncidentService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/v1")
public class IncidentController {

    private final IncidentService incidentService;

    @Autowired
    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PostMapping("/incident-report")
    public ResponseEntity<IncidentResponse> createIncidentReport(@RequestHeader @NotNull Integer creatorId,
                                                 @RequestBody IncidentDTO incidentDTO) {
        return new ResponseEntity<>(incidentService.createIncidentReport(incidentDTO, creatorId), HttpStatus.CREATED);
    }

    @GetMapping("/incident-reports")
    public ResponseEntity<List<IncidentResponse>> getAllIncidentReports() {
        return new ResponseEntity<>(incidentService.getAllIncidentReports(), HttpStatus.OK);
    }

    @PatchMapping("/incident-report/{incidentId}")
    public ResponseEntity<IncidentResponse> updateIncidentReport(@RequestHeader @NotNull Integer userId,
                                                                 @PathVariable("incidentId") @NotNull Integer incidentId,
                                                                 @RequestBody IncidentDTO incidentDTO) {
        return new ResponseEntity<>(incidentService.updateIncidentReport(userId, incidentId, incidentDTO), HttpStatus.OK);
    }

    @DeleteMapping("/incident-report/{incidentId}")
    public ResponseEntity<Object> deleteIncidentReport(@RequestHeader @NotNull Integer userId,
                                                       @PathVariable("incidentId") @NotNull Integer incidentId) {
        incidentService.deleteIncidentReport(userId, incidentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
