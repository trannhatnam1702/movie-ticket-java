package com.example.movie.Repository;

import com.example.movie.Entity.Reservation;
import com.example.movie.Entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Roles, Long> {
    @Query("SELECT r.roleId FROM Roles r WHERE r.Name = ?1")
    Long getRoleIdByName(String roleName);

    @Query("SELECT r.roleId FROM Roles r WHERE r.Name = ?1")
    Long getRoleNameByName(String roleName);

    @Query(value = "SELECT name FROM roles WHERE role_id = ?1", nativeQuery = true)
    String getNameByRoleId(Long role_id);
}
