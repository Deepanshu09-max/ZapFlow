package com.zapflow.processor.repository;

import com.zapflow.processor.entity.ZapRunOutbox;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZapRunOutboxRepository extends JpaRepository<ZapRunOutbox, String> {
    
    @Query("SELECT o FROM ZapRunOutbox o ORDER BY o.id")
    List<ZapRunOutbox> findPendingOutboxEntries(Pageable pageable);
}
