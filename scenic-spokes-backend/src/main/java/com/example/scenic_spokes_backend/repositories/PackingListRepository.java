package com.example.scenic_spokes_backend.repositories;

import com.example.scenic_spokes_backend.entities.PackingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackingListRepository extends JpaRepository<PackingList, Integer> {
}
