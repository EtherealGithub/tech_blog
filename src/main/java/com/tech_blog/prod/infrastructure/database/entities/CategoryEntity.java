package com.tech_blog.prod.infrastructure.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="categories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"posts"})
public class CategoryEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")
    private Long id;

    @Column(nullable=false, length=60, unique=true)
    private String name;

    //
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<PostEntity> posts = new ArrayList<>();
}
