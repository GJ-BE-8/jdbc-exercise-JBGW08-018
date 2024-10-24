package com.nhnacademy.jdbc.user.repository.impl;

import com.nhnacademy.jdbc.user.domain.User;
import com.nhnacademy.jdbc.user.repository.UserRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Slf4j
public class StatementUserRepository implements UserRepository {

    Connection connection;
    StatementUserRepository() {
        this.connection = DbUtils.getConnection();
    }
    @Override
    public Optional<User> findByUserIdAndUserPassword(String userId, String userPassword) {
        //todo#1 아이디, 비밀번호가 일치하는 User 조회
        String sql=String.format("Select * from users where user_id= '%s' and user_password='%s'",userId,userPassword);
        try(Statement Statement = connection.createStatement())
        {
            ResultSet resultSet = Statement.executeQuery(sql);
            if(resultSet.next())
            {
                User user=new User(
                        resultSet.getString("user_id"),
                        resultSet.getString("user_name"),
                        resultSet.getString("user_password")
                );
                return Optional.of(user);
            }
        }catch(SQLException e)
        {
            log.error(e.getMessage(),e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(String userId) {
        //#todo#2-아이디로 User 조회
        String sql=String.format("Select * from users where user_id='%s'",userId);
        try(Statement statement= connection.createStatement())
        {
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            if(resultSet.next())
            {
                User user=new User(resultSet.getString("user_id"),
                        resultSet.getString("user_name"),
                        resultSet.getString("user_password"));
                return Optional.of(user);
            }
        }catch(SQLException e)
        {
            log.error(e.getMessage(),e);
        }
        return Optional.empty();
    }

    @Override
    public int save(User user) {
        //todo#3- User 저장
        String sql=String.format("Insert into users(user_id,user_name,user_password) values('%s','%s','%s')",user.getUserId(),user.getUserName(),user.getUserPassword());
        try(Statement statement= connection.createStatement())
        {
            statement.execute(sql);
            return statement.getUpdateCount();
        }catch(SQLException e)
        {
            log.error(e.getMessage(),e);
        }
        return 0;
    }

    @Override
    public int updateUserPasswordByUserId(String userId, String userPassword) {
        //todo#4-User 비밀번호 변경
        String sql=String.format("Update users set user_password='%s' where user_id='%s'",userPassword,userId);
        try(Statement statement = connection.createStatement())
        {
            statement.execute(sql);
            return statement.getUpdateCount();
        }catch(SQLException e)
        {
            log.error(e.getMessage(),e);
        }
        return 0;
    }

    @Override
    public int deleteByUserId(String userId) {
        //todo#5 - User 삭제
        String sql=String.format("Delete from users where user_id='%s'",userId);
        try(Statement statement = connection.createStatement())
        {
            statement.execute(sql);
            return statement.getUpdateCount();
        }catch(SQLException e)
        {
            log.error(e.getMessage(),e);
        }
        return 0;
    }

}
