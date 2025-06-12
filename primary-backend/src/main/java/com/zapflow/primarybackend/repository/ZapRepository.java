package com.zapflow.primarybackend.repository;

import com.zapflow.primarybackend.entity.Zap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZapRepository extends JpaRepository<Zap, String> {
    List<Zap> findByUserId(Integer userId);
    
    @Query("SELECT z FROM Zap z LEFT JOIN FETCH z.actions a LEFT JOIN FETCH a.type LEFT JOIN FETCH z.trigger t LEFT JOIN FETCH t.type WHERE z.userId = :userId")
    List<Zap> findByUserIdWithDetails(@Param("userId") Integer userId);
    
    @Query("SELECT z FROM Zap z LEFT JOIN FETCH z.actions a LEFT JOIN FETCH a.type LEFT JOIN FETCH z.trigger t LEFT JOIN FETCH t.type WHERE z.id = :zapId AND z.userId = :userId")
    Optional<Zap> findByIdAndUserIdWithDetails(@Param("zapId") String zapId, @Param("userId") Integer userId);
}
