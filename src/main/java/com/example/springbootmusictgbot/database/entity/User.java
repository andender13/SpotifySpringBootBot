package com.example.springbootmusictgbot.database.entity;

import com.example.springbootmusictgbot.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "chat_id")
    private Long chatId;

}
