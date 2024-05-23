package com.DoAn.ShoeShop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "color")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String color;

    @ManyToMany(mappedBy = "colors", cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    private List<Product> products = new ArrayList<>();

}
