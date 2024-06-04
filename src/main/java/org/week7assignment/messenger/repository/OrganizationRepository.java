package org.week7assignment.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.week7assignment.messenger.model.Organization;

import java.util.UUID;
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    boolean existsByName(String name);
}
