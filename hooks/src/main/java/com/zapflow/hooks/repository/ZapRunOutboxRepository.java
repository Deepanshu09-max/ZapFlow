package com.zapflow.hooks.repository;

import com.zapflow.hooks.entity.ZapRunOutbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZapRunOutboxRepository extends JpaRepository<ZapRunOutbox, String> {
}
