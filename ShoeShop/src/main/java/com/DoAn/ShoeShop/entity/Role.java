package com.DoAn.ShoeShop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "role")
@ToString(onlyExplicitlyIncluded = true, includeFieldNames = false)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @ToString.Include
    private String name;

    //user
    @ManyToMany(mappedBy = "roles", cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    private Set<User> users = new HashSet<>();
}
