package com.proyecto.work.repository;

import com.proyecto.work.model.Task;
import com.proyecto.work.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserAndCategory(User user, String category);
    List<Task> findByUser(User user);
    long countByUserAndCompleted(User user, boolean completed);
    long countByUser(User user);
    List<Task> findByUserAndCompleted(User user, boolean completed);
}