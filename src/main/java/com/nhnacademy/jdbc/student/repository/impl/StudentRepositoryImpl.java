package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import java.sql.*;
import java.util.Optional;

@Slf4j
public class StudentRepositoryImpl implements StudentRepository {

    @Override
    public int save(Connection connection, Student student){
        //todo#2 학생등록

        return 0;
    }

    @Override
    public Optional<Student> findById(Connection connection,String id){
        //todo#3 학생조회

        return Optional.empty();
    }

    @Override
    public int update(Connection connection,Student student){
        //todo#4 학생수정

        return 0;
    }

    @Override
    public int deleteById(Connection connection,String id){
        //todo#5 학생삭제

        return 0;
    }

}