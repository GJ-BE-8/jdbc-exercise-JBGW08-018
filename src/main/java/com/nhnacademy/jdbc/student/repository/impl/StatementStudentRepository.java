package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Optional;

@Slf4j
public class StatementStudentRepository implements StudentRepository {
    private final Connection connection;
    public StatementStudentRepository() {
        this.connection = DbUtils.getConnection();
    }

    @Override
    public int save(Student student) {
        if (connection == null) {
            log.error("Database connection is null.");
            return 0;
        }

        String sql = String.format("INSERT INTO students (id, name, gender, age, created_at) VALUES ('%s', '%s', '%s', %d, '%s')",
                student.getId(), student.getName(), student.getGender().name(), student.getAge(), Timestamp.valueOf(student.getCreatedAt()));

        try (Statement statement = connection.createStatement()) {
            int rowsAffected = statement.executeUpdate(sql);
            return rowsAffected;
        } catch (SQLException e) {
            log.error("Error saving student: {}", e.getMessage());
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackException) {
                log.error("Rollback failed: {}", rollbackException.getMessage());
            }
            return 0;
        }
    }

    @Override
    public Optional<Student> findById(String id) {
        String sql = String.format("SELECT id, name, gender, age, created_at FROM students WHERE id = '%s'", id);
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                Student student = new Student(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        Student.GENDER.valueOf(resultSet.getString("gender")),
                        resultSet.getInt("age")
                );
                return Optional.of(student);
            }
        } catch (SQLException e) {
            log.error("Error finding student by id {}: {}", id, e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public int update(Student student) {
        String sql = String.format("UPDATE students SET name = '%s', gender = '%s', age = %d WHERE id = '%s'",
                student.getName(), student.getGender().name(), student.getAge(), student.getId());

        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.error("Error updating student: {}", e.getMessage());
            return 0;
        }
    }

    @Override
    public int deleteById(String id) {
        String sql = String.format("DELETE FROM students WHERE id = '%s'", id);

        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.error("Error deleting student by id {}: {}", id, e.getMessage());
            return 0;
        }
    }
}