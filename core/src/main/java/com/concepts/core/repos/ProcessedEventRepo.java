package com.concepts.core.repos;

import com.concepts.core.models.ProcessEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepo extends JpaRepository<ProcessEvent, String> {
}
