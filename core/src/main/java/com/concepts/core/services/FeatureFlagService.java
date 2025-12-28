package com.concepts.core.services;

import com.concepts.core.models.FeatureFlag;
import com.concepts.core.repos.FeatureFlagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service

@Slf4j
public class FeatureFlagService {
    public FeatureFlagService(FeatureFlagRepository repository) {
        this.repository = repository;
    }

    private final FeatureFlagRepository repository;

    public boolean isEnabled(String featureKey) {
        FeatureFlag flag = repository.findById(featureKey).orElse(null);
        return flag != null && flag.isEnabled();
    }
}
