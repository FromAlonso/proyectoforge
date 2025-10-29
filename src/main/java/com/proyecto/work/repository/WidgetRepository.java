package com.proyecto.work.repository;

import com.proyecto.work.model.User;
import com.proyecto.work.model.Widget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WidgetRepository extends JpaRepository<Widget, Long> {
    List<Widget> findByUserAndVisibleTrue(User user);
    List<Widget> findByUser(User user);
    List<Widget> findByUserAndType(User user, String type);
}
