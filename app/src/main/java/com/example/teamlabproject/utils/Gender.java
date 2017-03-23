package com.example.teamlabproject.utils;

public enum Gender {

    MALE("male"),
    FEMALE("female");

    private final String text;

    Gender(String text){
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static Gender fromString(String text){
        if (text != null){
            for (Gender gender : Gender.values()){
                if (text.equalsIgnoreCase(gender.text)){
                    return gender;
                }
            }
        }
        return null;
    }
}
