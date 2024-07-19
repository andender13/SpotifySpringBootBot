package com.example.springbootmusictgbot.repository;

import com.example.springbootmusictgbot.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
     User findByName(String email);
     User findByChatId(Long chatId);
}
