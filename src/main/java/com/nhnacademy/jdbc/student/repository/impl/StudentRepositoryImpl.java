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
        String sql="Insert into students values(?,?,?,?,?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);)
        {
            preparedStatement.setString(1,student.getId());
            preparedStatement.setString(2,student.getName());
            preparedStatement.setString(3,student.getGender().toString());
            preparedStatement.setInt(4,student.getAge());
            preparedStatement.setTimestamp(5, java.sql.Timestamp.valueOf(student.getCreatedAt()));
            return preparedStatement.executeUpdate();


        }catch(SQLException e){
            log.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public Optional<Student> findById(Connection connection,String id){
        //todo#3 학생조회
        String sql="select * from students where id=?";
        try(PreparedStatement preparedStatement= connection.prepareStatement(sql))
        {
            preparedStatement.setString(1,id);
            ResultSet result=preparedStatement.executeQuery();
            if(result.next())
            {
                Student student=new Student(result.getString("id"),
                        result.getString("name"),
                        Student.GENDER.valueOf(result.getString("gender")),
                        result.getInt("age"),
                        result.getTimestamp("created_at").toLocalDateTime());
                return Optional.of(student);

            }
        }catch(SQLException e)
        {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public int update(Connection connection,Student student){
        //todo#4 학생수정
        String sql="update students set name=?,gender=?,age=?,created_at=? where id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1,student.getName());
            preparedStatement.setString(2,student.getGender().toString());
            preparedStatement.setInt(3,student.getAge());
            preparedStatement.setTimestamp(4,java.sql.Timestamp.valueOf(student.getCreatedAt()));
            preparedStatement.setString(5,student.getId());
            return preparedStatement.executeUpdate();
        }catch(SQLException e)
        {
            log.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public int deleteById(Connection connection,String id){
        //todo#5 학생삭제
        String sql="delete from students where id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1,id);
            return preparedStatement.executeUpdate();
        }catch(SQLException e)
        {
            log.error(e.getMessage());
        }
        return 0;
    }

}