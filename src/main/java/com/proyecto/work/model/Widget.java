package com.proyecto.work.model;

import jakarta.persistence.*;

@Entity
@Table(name = "widgets")
public class Widget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // "progress", "pomodoro", "music"

    @Column(nullable = false)
    private String position = "default";

    private String color = "default";

    @Column(nullable = false)
    private boolean visible = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Constructors
    public Widget() {}

    public Widget(String type, String position, User user) {
        this.type = type;
        this.position = position;
        this.user = user;
    }

    //Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}