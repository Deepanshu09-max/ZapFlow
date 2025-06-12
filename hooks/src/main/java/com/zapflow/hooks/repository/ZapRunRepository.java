package com.zapflow.hooks.repository;

import com.zapflow.hooks.entity.ZapRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZapRunRepository extends JpaRepository<ZapRun, String> {
}
