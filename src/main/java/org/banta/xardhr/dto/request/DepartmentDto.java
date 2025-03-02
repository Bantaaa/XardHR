package org.banta.xardhr.dto.request;

import lombok.Data;

@Data
public class DepartmentDto {
    private String id; // Change from Long to String
    private String name;
    private String description;
    private String headId;
    private String head;
    private String parentId;
    private Integer employeeCount;
    private Integer projects;
}
