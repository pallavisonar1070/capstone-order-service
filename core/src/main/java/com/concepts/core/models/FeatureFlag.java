package com.concepts.core.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "feature_flags")
@Getter
@Setter
public class FeatureFlag {
    @Id
    private String featureKey;

    private boolean enabled;

    private LocalDateTime updatedAt;
}
