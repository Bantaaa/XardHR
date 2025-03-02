package org.banta.xardhr.repository;

import org.banta.xardhr.domain.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    @Query("SELECT u FROM AppUser u WHERE u.department.id = ?1")
    List<AppUser> findByDepartmentId(Long departmentId);
}