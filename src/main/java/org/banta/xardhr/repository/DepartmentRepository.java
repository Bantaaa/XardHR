package org.banta.xardhr.repository;

import org.banta.xardhr.domain.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT d FROM Department d WHERE d.parentDepartment IS NULL")
    List<Department> findTopLevelDepartments();

    @Query("SELECT d FROM Department d WHERE d.parentDepartment.id = ?1")
    List<Department> findChildDepartments(Long parentId);
}