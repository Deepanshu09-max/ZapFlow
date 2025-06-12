package com.zapflow.hooks.repository;

import com.zapflow.hooks.entity.Zap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZapRepository extends JpaRepository<Zap, String> {
    // Query methods as needed
}
