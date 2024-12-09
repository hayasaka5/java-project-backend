package com.example.demo.repositories;

import com.example.demo.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Репозиторий для управления сущностью Post.
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    
    /**
     * Метод для поиска всех постов пользователя по его ID.
     * @param authorId ID автора
     * @return Список постов
     */
    List<Post> findAllByAuthorId(Long authorId);
}
