package com.finleap.incidentmanagement.repository;

import com.finleap.incidentmanagement.entity.IncidentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentRepository extends JpaRepository<IncidentReport, Integer> {
}
