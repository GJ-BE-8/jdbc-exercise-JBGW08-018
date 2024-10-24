package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Optional;

@Slf4j
public class PreparedStatementStudentRepository implements StudentRepository {
    private final Connection connection;
    public PreparedStatementStudentRepository() {
        this.connection = DbUtils.getConnection();
    }
    @Override
    public int save(Student student){
        //todo#1 학생 등록
        if(connection==null)
        {
            log.error("Database connection is null");
            return 0;
        }
        String sql=String.format("INSERT INTO students (id,name,gender,age,created_at) VALUES (?, ?, ?, ?, ?)");
        try(PreparedStatement preparedStatement=connection.prepareStatement(sql))
        {
            preparedStatement.setString(1,student.getId());
            preparedStatement.setString(2,student.getName());
            preparedStatement.setString(3,student.getGender().toString());
            preparedStatement.setInt(4,student.getAge());
            preparedStatement.setTimestamp(5,Timestamp.valueOf(student.getCreatedAt()));
            int result = preparedStatement.executeUpdate();
            log.debug("save : result {}",result);
            return result;
        }catch(SQLException e)
        {
            log.error("Error saving studnet:{}",e.getMessage());
            try {
                    connection.rollback();
            } catch (SQLException rollbackException) {
                log.error("Rollback failed: {}", rollbackException.getMessage());
            }
            return 0;
        }
    }

    @Override
    public Optional<Student> findById(String id){
        //todo#2 학생 조회
        String sql="SELECT * FROM students WHERE id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1,id);

            ResultSet result=preparedStatement.executeQuery();
            if(result.next())
            {
                Student student= new Student(result.getString("id"),
                        result.getString("name"),
                        Student.GENDER.valueOf(result.getString("gender")),
                        result.getInt("age"),
                        result.getTimestamp("created_at").toLocalDateTime());
                return Optional.of(student);
            }
        }catch(SQLException e)
        {
            log.error("Error getting student by id:{}",e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public int update(Student student){
        //todo#3 학생 수정 , name 수정
        String sql="UPDATE students SET name=?,gender=?,age=? WHERE id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1,student.getName());
            preparedStatement.setString(2,student.getGender().toString());
            preparedStatement.setInt(3,student.getAge());
            preparedStatement.setString(4,student.getId());
            return preparedStatement.executeUpdate();
        }catch(SQLException e)
        {
            log.error("Error saving studnet:{}",e.getMessage());
            return 0;
        }

    }

    @Override
    public int deleteById(String id){
        //todo#4 학생 삭제
        String sql="DELETE FROM students WHERE id=?";
        try(PreparedStatement preparedStatement =connection.prepareStatement(sql))
        {
            preparedStatement.setString(1,id);
            return preparedStatement.executeUpdate();
        }catch(SQLException e)
        {
            log.error("Error saving studnet:{}",e.getMessage());
            return 0;
        }

    }

}
