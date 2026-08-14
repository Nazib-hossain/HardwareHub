package com.hardware.inventory.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "contact_email", length = 200)
    private String contactEmail;

    @Column(length = 50)
    private String phone;

    @ManyToMany(mappedBy = "suppliers")
    @Builder.Default
    private Set<Product> products = new HashSet<>();
}
