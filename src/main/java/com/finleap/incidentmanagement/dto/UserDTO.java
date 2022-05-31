package com.finleap.incidentmanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Integer userId;
    private String name;
}
