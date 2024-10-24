package com.nhnacademy.jdbc.bank.service.impl;

import com.nhnacademy.jdbc.bank.domain.Account;
import com.nhnacademy.jdbc.bank.exception.AccountAreadyExistException;
import com.nhnacademy.jdbc.bank.exception.AccountNotFoundException;
import com.nhnacademy.jdbc.bank.exception.BalanceNotEnoughException;
import com.nhnacademy.jdbc.bank.repository.AccountRepository;
import com.nhnacademy.jdbc.bank.repository.impl.AccountRepositoryImpl;
import com.nhnacademy.jdbc.bank.service.BankService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository;

    public BankServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account getAccount(Connection connection, long accountNumber){
        //todo#11 계좌-조회
        String sql= "Select * from account where account_number = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, accountNumber);
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                Account account=new Account(resultSet.getInt("account_number"),
                        resultSet.getString("name"),
                        resultSet.getInt("balance"));
                return account;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
       return null;
    }

    @Override
    public void createAccount(Connection connection, Account account){
        //todo#12 계좌-등록
        String sql="insert into account(?,?,?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, account.getAccountNumber());
            preparedStatement.setString(2, account.getName());
            preparedStatement.setLong(3,account.getBalance());
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean depositAccount(Connection connection, long accountNumber, long amount){
        //todo#13 예금, 계좌가 존재하는지 체크 -> 예금실행 -> 성공 true, 실패 false;
        String sql="Select * from account where account_number =?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, accountNumber);
            preparedStatement.setLong(2, amount);
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                sql="Update account set balance = balance+ ? where account_number = ?";
                preparedStatement.setLong(1, amount);
                preparedStatement.setLong(2, accountNumber);
                preparedStatement.executeUpdate();
                return true;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean withdrawAccount(Connection connection, long accountNumber, long amount){
        //todo#14 출금, 계좌가 존재하는지 체크 ->  출금가능여부 체크 -> 출금실행, 성공 true, 실폐 false 반환
        String sql="Select * from account where account_number =?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, accountNumber);
            preparedStatement.setLong(2, amount);
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                sql="Update account set balance = balance - ? where account_number = ?";
                preparedStatement.setLong(1, amount);
                preparedStatement.setLong(2, accountNumber);
                preparedStatement.executeUpdate();
                return true;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void transferAmount(Connection connection, long accountNumberFrom, long accountNumberTo, long amount){
        //todo#15 계좌 이체 accountNumberFrom -> accountNumberTo 으로 amount만큼 이체
        if(isExistAccount(connection, accountNumberFrom) && isExistAccount(connection, accountNumberTo)){
            String sqlWithdraw = "UPDATE account SET balance = balance - ? WHERE account_number = ?";
            String sqlDeposit = "UPDATE account SET balance = balance + ? WHERE account_number = ?";

            try {
                // 트랜잭션 시작
                connection.setAutoCommit(false);

                // 출금 처리
                try (PreparedStatement psWithdraw = connection.prepareStatement(sqlWithdraw)) {
                    psWithdraw.setLong(1, amount);
                    psWithdraw.setLong(2, accountNumberFrom);
                    psWithdraw.executeUpdate();
                }

                // 입금 처리
                try (PreparedStatement psDeposit = connection.prepareStatement(sqlDeposit)) {
                    psDeposit.setLong(1, amount);
                    psDeposit.setLong(2, accountNumberTo);
                    psDeposit.executeUpdate();
                }

                // 트랜잭션 성공 시 커밋
                connection.commit();
            }catch(SQLException e){
                try{
                    connection.rollback();
                }catch(SQLException e1){
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean isExistAccount(Connection connection, long accountNumber){
        //todo#16 Account가 존재하면 true , 존재하지 않다면 false
        String sql="Select * from account where account_number =?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setLong(1, accountNumber);
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void dropAccount(Connection connection, long accountNumber) {
        //todo#17 account 삭제
        if(isExistAccount(connection, accountNumber)){
            String sql="Delete from account where account_number = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
            {
                preparedStatement.setLong(1, accountNumber);
                preparedStatement.executeUpdate();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

}