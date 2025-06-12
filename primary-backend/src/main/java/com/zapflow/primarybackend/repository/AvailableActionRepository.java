package com.zapflow.primarybackend.repository;

import com.zapflow.primarybackend.entity.AvailableAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableActionRepository extends JpaRepository<AvailableAction, String> {
}
