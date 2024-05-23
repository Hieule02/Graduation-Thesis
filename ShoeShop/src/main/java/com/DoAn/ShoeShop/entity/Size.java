package com.DoAn.ShoeShop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "size")
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private Integer size;

    @ManyToMany(mappedBy = "sizes")
    @JsonIgnore
    @ToString.Exclude
    private List<Product> products = new ArrayList<>();

}
