package com.proyecto.work.service;

import com.proyecto.work.model.Task;
import com.proyecto.work.model.User;
import com.proyecto.work.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getUserTasksByCategory(User user, String category) {
        return taskRepository.findByUserAndCategory(user, category);
    }

    public List<Task> getUserTasks(User user) {
        return taskRepository.findByUser(user);
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public double calculateProgress(User user, String category) {
        List<Task> categoryTasks = taskRepository.findByUserAndCategory(user, category);
        if (categoryTasks.isEmpty()) {
            return 0;
        }
        
        long completedTasks = categoryTasks.stream()
                .filter(Task::isCompleted)
                .count();
        
        return (double) completedTasks / categoryTasks.size() * 100;
    }
    
    public void toggleTaskCompletion(Long taskId) {
        taskRepository.findById(taskId).ifPresent(task -> {
            task.setCompleted(!task.isCompleted());
            taskRepository.save(task);
        });
    }
    
    public List<Task> getCompletedTasks(User user) {
        return taskRepository.findByUserAndCompleted(user, true);
    }
}