package com.nhnacademy.jdbc.student.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

public class Student {

    public Student(String id, String name, GENDER gender, Integer age) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.createdAt = LocalDateTime.now();
    }

    public enum GENDER{
        M,F
    }

    private final String id;
    private final String name;
    private final GENDER gender;
    private final Integer age;
    private final LocalDateTime createdAt;

    public String getId(){
        return id;
    }
    public String getName() {
        return name;
    }
    public GENDER getGender() {
        return gender;
    }
    public Integer getAge() {
        return age;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    //todo#0 필요한 method가 있다면 추가합니다.
}
