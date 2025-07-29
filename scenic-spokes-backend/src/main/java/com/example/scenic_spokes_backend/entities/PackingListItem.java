package com.example.scenic_spokes_backend.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "packing_list_items")
public class PackingListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private int id;

    private String itemName;
    private int quantity;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "packing_list_id")
    private PackingList packingList;
}
