package com.birthday.birthday.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "wishes")
@Data
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Името е задължително")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Пожеланието е задължително")
    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = false)
    private boolean approved = false;

    @Column(nullable = false)
    private boolean visibleToAll = true;
}
