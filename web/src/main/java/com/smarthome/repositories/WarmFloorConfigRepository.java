package com.smarthome.repositories;

import com.smarthome.warmfloor.WarmFloorConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarmFloorConfigRepository extends JpaRepository<WarmFloorConfig, Long> {
}
