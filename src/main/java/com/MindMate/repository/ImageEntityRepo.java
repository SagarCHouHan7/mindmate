package com.MindMate.repository;

import com.MindMate.model.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageEntityRepo extends JpaRepository<ImageEntity , Long> {
}
