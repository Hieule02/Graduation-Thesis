package com.DoAn.ShoeShop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    @NonNull
    @Positive
    private Long price;
    @NonNull
    @PositiveOrZero
    private Integer quantity;
    @NonNull
    private String image;
    @Column(name = "total_rate", columnDefinition = "int default 0")
    @PositiveOrZero
    private int totalRate;
    @Column(name = "rated_times", columnDefinition = "int default 0")
    @PositiveOrZero
    private int ratedTimes;
    @Column(name = "sold_quantity", columnDefinition = "int default 0")
    @PositiveOrZero
    private int soldQuantity;
    @CreationTimestamp
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(updatable = false)
    private LocalDateTime createAt;
    @UpdateTimestamp
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime updateAt;

    @Column(columnDefinition = "bit default 0")
    private boolean isDeleted;

    //category
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Category category;

    //color
    @ManyToMany(cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    @JoinTable(name = "product_color",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "color_id"))
    private List<Color> colors;

    //size
    @ManyToMany(cascade = CascadeType.MERGE)
    @JsonIgnore
    @ToString.Exclude
    @JoinTable(name = "product_size",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "size_id"))
    private List<Size> sizes;

    //product review
    @OneToMany(mappedBy = "product", cascade = CascadeType.MERGE)
    private List<ProductReview> productReviews;
}
