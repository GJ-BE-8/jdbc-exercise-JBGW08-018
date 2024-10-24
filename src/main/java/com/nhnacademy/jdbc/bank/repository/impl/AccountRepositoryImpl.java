package com.nhnacademy.jdbc.bank.repository.impl;

import com.nhnacademy.jdbc.bank.domain.Account;
import com.nhnacademy.jdbc.bank.repository.AccountRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {

    public Optional<Account> findByAccountNumber(Connection connection, long accountNumber){
        //todo#1 계좌-조회

        String sql="Select * from account where account_number = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return Optional.of(new Account(Long.parseLong(resultSet.getString("account_number")),
                        resultSet.getString("name"),
                        resultSet.getInt("balance"))
                );
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int save(Connection connection, Account account) {
        //todo#2 계좌-등록, executeUpdate() 결과를 반환 합니다.
        String sql="insert into account values(?,?,?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, account.getAccountNumber());
            preparedStatement.setString(2, account.getName());
            preparedStatement.setLong(3,account.getBalance());

            return preparedStatement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public int countByAccountNumber(Connection connection, long accountNumber){
        int count=0;
        //todo#3 select count(*)를 이용해서 계좌의 개수를 count해서 반환
        String sql="Select count(*) from account where account_number = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                count=resultSet.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return count;
    }

    @Override
    public int deposit(Connection connection, long accountNumber, long amount){
        //todo#4 입금, executeUpdate() 결과를 반환 합니다.
        String sql="update account set balance = balance + ? where account_number =?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, amount);
            preparedStatement.setLong(2, accountNumber);
            return preparedStatement.executeUpdate();
        }catch(SQLException e){
            try {
                connection.rollback();  // 트랜잭션 롤백
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();

        }
        return 0;
    }

    @Override
    public int withdraw(Connection connection, long accountNumber, long amount){
        //todo#5 출금, executeUpdate() 결과를 반환 합니다.
        String sql="update account set balance = balance -? where account_number =?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1,amount);
            preparedStatement.setLong(2, accountNumber);
            return preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteByAccountNumber(Connection connection, long accountNumber) {
        //todo#6 계좌 삭제, executeUpdate() 결과를 반환 합니다.
        String sql="delete from account where account_number = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, accountNumber);
            return preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
}
