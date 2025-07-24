package com.example.scenic_spokes_backend.repositories;

import com.example.scenic_spokes_backend.entities.PackingListItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackingListItemRepository extends JpaRepository<PackingListItem, Integer> {
}
