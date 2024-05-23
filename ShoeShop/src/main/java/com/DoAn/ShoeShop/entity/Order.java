package com.DoAn.ShoeShop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String receiverName;
    @NonNull
    private String receiverAddress;
    @NonNull
    private String receiverPhone;
    @NonNull
    private String paymentMethod;
    @NonNull
    private Long productAmount;
    @NonNull
    private Long shippingFee;
    @CreationTimestamp
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(updatable = false)
    private LocalDateTime createAt;
    @NonNull
    private String status;

    //user
    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    //order detail
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<OrderDetail> orderDetails;
}
