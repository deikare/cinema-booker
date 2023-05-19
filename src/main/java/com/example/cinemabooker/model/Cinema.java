package com.example.cinemabooker.model;

import com.example.cinemabooker.services.interfaces.EntityInterface;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Cinemas")
public class Cinema extends AbstractEntity implements EntityInterface {
    private String name;
    @OneToMany(mappedBy = "cinema", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Room> rooms = new HashSet<>();


    public Cinema() {
    }

    public Cinema(String name) {
        this.name = name;
    }

    public void addRoom(Room room) {
        rooms.add(room);
        room.setCinema(this);
    }

    @Override
    public String toString() {
        return "Cinema{" +
                "name='" + name + '\'' +
                ", entityId='" + entityId + '\'' +
                "} ";
    }

    public String getName() {
        return name;
    }
}
