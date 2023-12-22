package com.example.apozh.Repository;

import com.example.apozh.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    boolean existsByImageUrl(String imageUrl);

    List<Photo> findByBlockNumber(int blockNumber);
}
