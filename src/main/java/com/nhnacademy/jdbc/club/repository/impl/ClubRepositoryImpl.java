package com.nhnacademy.jdbc.club.repository.impl;

import com.nhnacademy.jdbc.club.domain.Club;
import com.nhnacademy.jdbc.club.repository.ClubRepository;

import java.sql.*;
import java.util.Optional;

public class ClubRepositoryImpl implements ClubRepository {

    @Override
    public Optional<Club> findByClubId(Connection connection, String clubId) {
        //todo#3 club 조회
        String sql= "select * from jdbc_club where club_id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, clubId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                Club club=new Club(resultSet.getString("club_id"),
                        resultSet.getString("club_name"),
                        resultSet.getTimestamp("club_created_at").toLocalDateTime());
                return Optional.of(club);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public int save(Connection connection, Club club) {
        //todo#4 club 생성, executeUpdate() 결과를 반환
        if(countByClubId(connection, club.getClubId()) > 0) {
            throw new RuntimeException("이미 있는 클럽입니다.");
        }
        String sql = "INSERT INTO jdbc_club VALUES (?, ?, ?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, club.getClubId());
            preparedStatement.setString(2, club.getClubName());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(club.getClubCreatedAt()));

            return preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(Connection connection, Club club) {
        //todo#5 club 수정, clubName을 수정합니다. executeUpdate()결과를 반환
        String sql="update jdbc_club set club_name=? where club_id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1, club.getClubName());
            preparedStatement.setString(2, club.getClubId());
            return preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteByClubId(Connection connection, String clubId) {
        //todo#6 club 삭제, executeUpdate()결과 반환
        String sql="delete from jdbc_club where club_id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1,clubId);
            return preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int countByClubId(Connection connection, String clubId) {
        //todo#7 clubId에 해당하는 club의 count를 반환
        String sql="select count(*) from jdbc_club where club_id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql))
        {
            preparedStatement.setString(1,clubId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
}
