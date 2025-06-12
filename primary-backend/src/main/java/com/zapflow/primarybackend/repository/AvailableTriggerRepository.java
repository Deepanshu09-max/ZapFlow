package com.zapflow.primarybackend.repository;

import com.zapflow.primarybackend.entity.AvailableTrigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableTriggerRepository extends JpaRepository<AvailableTrigger, String> {
}
