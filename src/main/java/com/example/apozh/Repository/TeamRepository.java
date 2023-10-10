package com.example.apozh.Repository;

import com.example.apozh.entity.Teams;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TeamRepository extends JpaRepository<Teams, Long> {
    Teams findByName(String name);

}
