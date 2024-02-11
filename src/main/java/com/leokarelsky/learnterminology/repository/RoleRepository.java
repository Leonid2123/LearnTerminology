package com.leokarelsky.learnterminology.repository;

import com.leokarelsky.learnterminology.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
