package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users") // Название таблицы, чтобы избежать конфликтов с зарезервированными словами
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100) // Ограничение длины строки
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 150) // Уникальность для email
    private String email;

    @Column(name = "password", nullable = false, length = 150) // Пароль не должен быть уникальным
    private String password;

    @Column(name = "is_admin", nullable = false) // Поле булевого типа, исправлено имя
    private Boolean isAdmin;
}
