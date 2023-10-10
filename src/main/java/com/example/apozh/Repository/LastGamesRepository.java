package com.example.apozh.Repository;


import com.example.apozh.entity.LastGames;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LastGamesRepository extends JpaRepository<LastGames, Long> {
    LastGames findTopByOrderByIdDesc();


}
