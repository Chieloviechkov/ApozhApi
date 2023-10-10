package com.example.apozh.bot;

public enum UserState {
    DEFAULT, ENTER_NEWS_TITLE, ENTER_NEWS_DESCRIPTION, ENTER_NEWS_PHOTO, DELETE_NEWS, ENTER_ACHIEVEMENT_TITLE,
    ENTER_ACHIEVEMENT_DESCRIPTION, ENTER_ACHIEVEMENT_PHOTO, DELETE_ACHIEVEMENT, ENTER_MATCH_DETAILS,
    ENTER_FOOTBALLER_TO_DELETE, ENTER_MAIN_CAST;
    private String data;

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

}