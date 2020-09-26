package com.dreambuilders.springbatch.repository;

import com.dreambuilders.springbatch.model.COVIDReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface COVIDRepository extends JpaRepository<COVIDReport, Long> {
}
