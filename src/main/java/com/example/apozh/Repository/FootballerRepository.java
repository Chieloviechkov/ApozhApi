package com.example.apozh.Repository;

import com.example.apozh.entity.Footballer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FootballerRepository extends JpaRepository<Footballer, Long> {
    Footballer findByLastNameAndFirstName(String lastName, String firstName);
}
