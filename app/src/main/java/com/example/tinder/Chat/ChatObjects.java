package com.example.tinder.Chat;

public class ChatObjects {

    private String message;
    private Boolean currentUser;
    public ChatObjects(String message, Boolean currentUser){
        this.message = message;
        this.currentUser = currentUser;

    }


    public String getMessage(){
        return message;
    }
    public void setMessage(String userID){
        this.message = message;
    }

    public Boolean getCurrentUser(){
        return currentUser;
    }
    public void setCurrentUser(Boolean currentUser){
        this.currentUser = currentUser;
    }
}

