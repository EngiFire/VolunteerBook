package com.example.myapplication.models;

import androidx.annotation.NonNull;

public class Event {
    public String id, name, direction, data, responsible, place, description;
    public Integer quantity, participant;

    public Event(String id, String name, String direction, String data, String responsible, String place, String description, Integer quantity, Integer participant) {
        this.id = id;
        this.name = name;
        this.direction = direction;
        this.data = data;
        this.responsible = responsible;
        this.place = place;
        this.description = description;
        this.quantity = quantity;
        this.participant = participant;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", direction='" + direction + '\'' +
                ", data='" + data + '\'' +
                ", responsible='" + responsible + '\'' +
                ", place='" + place + '\'' +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    public Event(){

    }
}
