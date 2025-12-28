package com.concepts.core.repos;

import com.concepts.core.models.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureFlagRepository
        extends JpaRepository<FeatureFlag, String> {
}
