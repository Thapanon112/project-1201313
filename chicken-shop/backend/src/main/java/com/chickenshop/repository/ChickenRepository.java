package com.chickenshop.repository;

import com.chickenshop.entity.Chicken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChickenRepository extends JpaRepository<Chicken, Long> {
    List<Chicken> findByType(String type);
}