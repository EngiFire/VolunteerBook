package com.example.myapplication.models;

public class ApplicationEvent {
    public String id, eventName, userId, userName, userSecName, status;

    public ApplicationEvent(){

    }

    public ApplicationEvent(String id, String eventName, String userId, String userName, String userSecName, String status) {
        this.id = id;
        this.eventName = eventName;
        this.userId = userId;
        this.userName = userName;
        this.userSecName = userSecName;
        this.status = status;
    }
}
