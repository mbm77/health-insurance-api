package com.mbm.healthinsurance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mbm.healthinsurance.entity.ClaimSequence;

@Repository
public interface ClaimSequenceRepository 
        extends JpaRepository<ClaimSequence, Long> {


    Optional<ClaimSequence> findBySequenceYear(Integer year);

}
