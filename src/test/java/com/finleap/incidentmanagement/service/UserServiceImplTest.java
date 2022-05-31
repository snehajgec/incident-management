package com.finleap.incidentmanagement.service;

import com.finleap.incidentmanagement.dto.UserDTO;
import com.finleap.incidentmanagement.entity.User;
import com.finleap.incidentmanagement.exception.IncidentManagementInternalServerException;
import com.finleap.incidentmanagement.exception.IncidentManagementNotFoundException;
import com.finleap.incidentmanagement.repository.UserRepository;
import com.finleap.incidentmanagement.service.impl.UserServiceImpl;
import com.finleap.incidentmanagement.testbuilder.TestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
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
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    public void testSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(TestBuilder.getCreatorUser());
        UserDTO userDTO = service.saveUser(TestBuilder.getCreatorUserDto());
        assertEquals(TestBuilder.getCreatorUserDto(), userDTO);
    }

    @Test
    public void testFindAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(TestBuilder.getCreatorUser()));
        List<UserDTO> userDTO = service.findAllUsers();
        assertEquals(List.of(TestBuilder.getCreatorUserDto()), userDTO);

        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        List<UserDTO> response = service.findAllUsers();
        assertEquals(Collections.emptyList(), response);
    }

    @Test
    public void testFindByUserId() {
        when(userRepository.findById(1)).thenReturn(Optional.of(TestBuilder.getCreatorUser()));
        UserDTO userDTO = service.findByUserId(1);
        assertEquals(TestBuilder.getCreatorUserDto(), userDTO);
    }

    @Test
    public void testEditUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(TestBuilder.getCreatorUser()));
        when(userRepository.save(any(User.class))).thenReturn(TestBuilder.getCreatorUser());
        UserDTO userDTO = service.editUser(1, TestBuilder.getCreatorUserDto());
        assertEquals(TestBuilder.getCreatorUserDto(), userDTO);
    }


    @Test
    public void testEditUserWhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IncidentManagementNotFoundException.class, () -> {
            service.editUser(1, TestBuilder.getCreatorUserDto());
        });
    }

    @Test
    public void testDeleteUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(TestBuilder.getCreatorUser()));
        doNothing().when(userRepository).delete(any(User.class));
        service.deleteUser(1);
        verify(userRepository, times(1)).delete(any(User.class));
    }


    @Test
    public void testDeleteUserWhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IncidentManagementNotFoundException.class, () -> {
            service.deleteUser(1);
        });
    }
}
