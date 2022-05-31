package com.finleap.incidentmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.finleap.incidentmanagement.dto.IncidentDTO;
import com.finleap.incidentmanagement.service.IncidentService;
import com.finleap.incidentmanagement.testbuilder.TestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncidentController.class)
@ExtendWith(SpringExtension.class)
public class IncidentControllerTest {

    @MockBean
    private IncidentService incidentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateIncidentReport() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String request = writer.writeValueAsString(TestBuilder.getIncidentDTOWithAssignee());
        String response = writer.writeValueAsString(TestBuilder.getIncidentResponse());

        when(incidentService.createIncidentReport(any(IncidentDTO.class), any(Integer.class))).thenReturn(TestBuilder.getIncidentResponse());
        mockMvc.perform(post( "/v1/incident-report")
                .contentType(MediaType.APPLICATION_JSON)
                .header("creatorId", 1)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(response));
    }

    @Test
    public void testGetAllIncidentReports() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String response = writer.writeValueAsString(List.of(TestBuilder.getIncidentResponse()));
        when(incidentService.getAllIncidentReports()).thenReturn(List.of(TestBuilder.getIncidentResponse()));
        mockMvc.perform(get( "/v1/incident-reports"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(response));
    }

    @Test
    public void testUpdateIncidentReport() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String request = writer.writeValueAsString(TestBuilder.getIncidentDTOWithAssignee());
        String response = writer.writeValueAsString(TestBuilder.getIncidentResponse());
        when(incidentService.updateIncidentReport(1, 1, TestBuilder.getIncidentDTOWithAssignee())).thenReturn(TestBuilder.getIncidentResponse());
        mockMvc.perform(patch( "/v1/incident-report/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("userId", 1)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(response));
    }

    @Test
    public void testDeleteIncidentReport() throws Exception {
        doNothing().when(incidentService).deleteIncidentReport(1, 1);
        mockMvc.perform(delete( "/v1/incident-report/1")
                .header("userId", 1))
                .andExpect(status().isNoContent());
    }
}
