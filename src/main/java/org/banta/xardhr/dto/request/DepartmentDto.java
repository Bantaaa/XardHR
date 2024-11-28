package org.banta.xardhr.dto.request;

import lombok.Data;

@Data
public class DepartmentDto {
    private String name;
    private String description;
    private Long departmentHeadId;
}
