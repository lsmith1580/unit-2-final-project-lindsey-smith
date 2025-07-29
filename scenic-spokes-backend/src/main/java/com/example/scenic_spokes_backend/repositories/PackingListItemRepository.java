package com.example.scenic_spokes_backend.repositories;

import com.example.scenic_spokes_backend.entities.PackingListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackingListItemRepository extends JpaRepository<PackingListItem, Integer> {
}
