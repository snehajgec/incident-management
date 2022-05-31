package com.finleap.incidentmanagement.service;

import com.finleap.incidentmanagement.dto.IncidentDTO;
import com.finleap.incidentmanagement.dto.IncidentResponse;
import com.finleap.incidentmanagement.entity.IncidentReport;
import com.finleap.incidentmanagement.entity.IncidentStatus;
import com.finleap.incidentmanagement.entity.User;
import com.finleap.incidentmanagement.exception.IncidentManagementInternalServerException;
import com.finleap.incidentmanagement.exception.IncidentManagementNotFoundException;
import com.finleap.incidentmanagement.exception.IncidentManagementUnauthorizedException;
import com.finleap.incidentmanagement.repository.IncidentRepository;
import com.finleap.incidentmanagement.repository.UserRepository;
import com.finleap.incidentmanagement.service.impl.IncidentServiceImpl;
import com.finleap.incidentmanagement.testbuilder.TestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class IncidentServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private IncidentRepository incidentRepository;

    @InjectMocks
    private IncidentServiceImpl service;

    @Test
    public void testCreateIncidentReportWithNoAssignee() {
        TestBuilder.getIncidentResponse().setAssigneeId(null);
        when(userRepository.findById(1)).thenReturn(Optional.of(TestBuilder.getCreatorUser()));
        when(incidentRepository.save(any())).thenReturn(TestBuilder.getIncidentReport());
        IncidentResponse incidentResponse = service.createIncidentReport(TestBuilder.getIncidentDTOWithoutAssignee(), 1);
        assertEquals(incidentResponse, TestBuilder.getIncidentResponse());
    }

    @Test
    public void testCreateIncidentReportWithAssignee() {
        when(userRepository.findById(1)).thenReturn(Optional.of(TestBuilder.getCreatorUser()));
        when(userRepository.findById(2)).thenReturn(Optional.of(TestBuilder.getAssigneeUser()));
        when(incidentRepository.save(any())).thenReturn(TestBuilder.getIncidentReport());
        IncidentResponse incidentResponse = service.createIncidentReport(TestBuilder.getIncidentDTOWithAssignee(), 1);
        assertEquals(incidentResponse, TestBuilder.getIncidentResponse());
    }

    @Test
    public void testWhenCreateIncidentReportThrowsException() {
        TestBuilder.getIncidentResponse().setAssigneeId(null);
        when(userRepository.findById(1)).thenReturn(Optional.of(TestBuilder.getCreatorUser()));
        when(incidentRepository.save(any())).thenThrow(new DataAccessException(""){});
        assertThrows(IncidentManagementInternalServerException.class, () -> {
            service.createIncidentReport(TestBuilder.getIncidentDTOWithoutAssignee(), 1);
        });
    }

    @Test
    public void testGetAllIncidentReports() {
        when(incidentRepository.findAll()).thenReturn(List.of(TestBuilder.getIncidentReport()));
        List<IncidentResponse> response = service.getAllIncidentReports();
        assertEquals(response, List.of(TestBuilder.getIncidentResponse()));
    }

    @Test
    public void testWhenGetAllIncidentReportsThrowsException() {
        when(incidentRepository.findAll()).thenThrow(new DataAccessException(""){});
        assertThrows(IncidentManagementInternalServerException.class, () -> {
            service.getAllIncidentReports();
        });
    }

    @Test
    public void testUpdateIncidentReportWhenCreatorUpdatesCurrentAssignee() {
        IncidentDTO incidentDTOWithAssignee = TestBuilder.getIncidentDTOWithAssignee();
        incidentDTOWithAssignee.setAssigneeId(3);
        User user = TestBuilder.getAssigneeUser();
        user.setUserId(3);
        IncidentResponse expectedIncidentResponse = TestBuilder.getIncidentResponse();
        expectedIncidentResponse.setAssigneeId(3);
        when(incidentRepository.findById(1)).thenReturn(Optional.of(TestBuilder.getIncidentReport()));
        when(userRepository.findById(3)).thenReturn(Optional.of(user));
        when(incidentRepository.save(any())).thenReturn(TestBuilder.getUpdatedIncidentReport());
        IncidentResponse incidentResponse = service.updateIncidentReport(1,1, incidentDTOWithAssignee);
        assertEquals(expectedIncidentResponse, incidentResponse);
    }

    @Test
    public void testUpdateIncidentReportWhenCreatorUpdatesAssignee() {
        when(incidentRepository.findById(1)).thenReturn(Optional.of(TestBuilder.getIncidentReportWithNoAssignee()));
        when(userRepository.findById(2)).thenReturn(Optional.of(TestBuilder.getAssigneeUser()));
        when(incidentRepository.save(any())).thenReturn(TestBuilder.getIncidentReport());
        IncidentResponse incidentResponse = service.updateIncidentReport(1,1, TestBuilder.getIncidentDTOWithAssignee());
        assertEquals(TestBuilder.getIncidentResponse(), incidentResponse);
    }

    @Test
    public void testUpdateIncidentReportWithNewStatus() {
        IncidentDTO incidentDTO = TestBuilder.getIncidentDTOWithAssignee();
        incidentDTO.setStatus("CLOSED");
        IncidentResponse expectedIncidentResponse = TestBuilder.getIncidentResponse();
        expectedIncidentResponse.setStatus("CLOSED");
        IncidentReport incidentReport = TestBuilder.getIncidentReport();
        incidentReport.setStatus(IncidentStatus.CLOSED);
        when(incidentRepository.findById(1)).thenReturn(Optional.of(TestBuilder.getIncidentReport()));
        when(incidentRepository.save(any())).thenReturn(incidentReport);
        IncidentResponse incidentResponse = service.updateIncidentReport(2,1, incidentDTO);
        assertEquals(expectedIncidentResponse, incidentResponse);
    }


    @Test
    public void testWhenUpdateIncidentReportThrowsException() {
        when(incidentRepository.findById(1)).thenReturn(Optional.of(TestBuilder.getIncidentReportWithNoAssignee()));
        when(userRepository.findById(2)).thenReturn(Optional.of(TestBuilder.getAssigneeUser()));
        when(incidentRepository.save(any())).thenThrow(new DataAccessException("") {});
        assertThrows(IncidentManagementInternalServerException.class, () -> {
            service.updateIncidentReport(1, 1, TestBuilder.getIncidentDTOWithAssignee());
        });
    }

    @Test
    public void testUpdateIncidentReportWhenIncidentNotFound() {
        when(incidentRepository.findById(3)).thenReturn(Optional.empty());
        assertThrows(IncidentManagementNotFoundException.class, () -> {
            service.updateIncidentReport(1, 3, TestBuilder.getIncidentDTOWithAssignee());
        });
    }

    @Test
    public void testUpdateIncidentReportWhenUserIsNotAuthorized() {
        when(incidentRepository.findById(1)).thenReturn(Optional.of(TestBuilder.getIncidentReportWithNoAssignee()));
        assertThrows(IncidentManagementUnauthorizedException.class, () -> {
            service.updateIncidentReport(5, 1, TestBuilder.getIncidentDTOWithAssignee());
        });
    }

    @Test
    public void testUpdateIncidentReportWhenNewAssigneeIsNotFound() {
        IncidentDTO incidentDTO = TestBuilder.getIncidentDTOWithAssignee();
        incidentDTO.setAssigneeId(5);
        when(incidentRepository.findById(1)).thenReturn(Optional.of(TestBuilder.getIncidentReportWithNoAssignee()));
        when(userRepository.findById(5)).thenReturn(Optional.empty());
        assertThrows(IncidentManagementNotFoundException.class, () -> {
            service.updateIncidentReport(1, 1, incidentDTO);
        });
    }

    @Test
    public void testDeleteIncidentReport() {
        when(incidentRepository.findById(1)).thenReturn(Optional.of(TestBuilder.getIncidentReport()));
        doNothing().when(incidentRepository).delete(any(IncidentReport.class));
        service.deleteIncidentReport(1, 1);
        verify(incidentRepository, times(1)).delete(any(IncidentReport.class));
    }

    @Test
    public void testDeleteIncidentReportWhenIncidentNotFound() {
        when(incidentRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IncidentManagementNotFoundException.class, () -> {
            service.deleteIncidentReport(1, 1);
        });
    }

    @Test
    public void testWhenDeleteIncidentReportThrowsException() {
        when(incidentRepository.findById(1)).thenReturn(Optional.of(TestBuilder.getIncidentReport()));
        doThrow(new DataAccessException("") {}).when(incidentRepository).delete(any(IncidentReport.class));
        assertThrows(IncidentManagementInternalServerException.class, () -> {
            service.deleteIncidentReport(1, 1);
        });
    }


}
