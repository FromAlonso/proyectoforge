package com.proyecto.work.service;

import com.proyecto.work.model.User;
import com.proyecto.work.model.Widget;
import com.proyecto.work.repository.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WidgetService {
    
    @Autowired
    private WidgetRepository widgetRepository;

    public List<Widget> getUserWidgets(User user) {
        return widgetRepository.findByUserAndVisibleTrue(user);
    }

    public Widget saveWidget(Widget widget) {
        return widgetRepository.save(widget);
    }

    public void deleteWidget(Long id) {
        widgetRepository.deleteById(id);
    }

    public void toggleVisibility(Long id, boolean visible) {
        widgetRepository.findById(id).ifPresent(widget -> {
            widget.setVisible(visible);
            widgetRepository.save(widget);
        });
    }
    
    public Widget createDefaultWidget(User user, String type) {
        Widget widget = new Widget(type, "default", user);
        return widgetRepository.save(widget);
    }
    
    public List<Widget> getUserWidgetsByType(User user, String type) {
        return widgetRepository.findByUserAndType(user, type);
    }
}
