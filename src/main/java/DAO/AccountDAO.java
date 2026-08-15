package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

  public Account insertAccount(Account account){
    Connection connection = ConnectionUtil.getConnection();
    try {
      String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
      PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

      preparedStatement.setString(1, account.getUsername());
      preparedStatement.setString(2, account.getPassword());

      preparedStatement.executeUpdate();
      ResultSet resultSet = preparedStatement.getGeneratedKeys();

      if(resultSet.next()){
        int generatedAccountId = (int) resultSet.getLong(1);
        return new Account(generatedAccountId, account.getUsername(), account.getPassword());
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  public boolean usernameExists(String username){
    Connection connection = ConnectionUtil.getConnection();

    try {
      String sql = "SELECT account_id, username, password FROM account WHERE username = ?;";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);

      preparedStatement.setString(1, username);
      ResultSet resultSet = preparedStatement.executeQuery();

      return resultSet.next();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return false;
    }
  }

  public Account getAccountByLogin(String username, String password){
    Connection connection = ConnectionUtil.getConnection();

    try {
      String sql = "SELECT account_id, username, password FROM account WHERE username = ? AND password = ?;";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);

      preparedStatement.setString(1, username);
      preparedStatement.setString(2, password);
      ResultSet resultSet = preparedStatement.executeQuery();

      if(resultSet.next()){
        int accountId = resultSet.getInt("account_id");
        return new Account(accountId, username, password);
      }

      return null;
    } catch (SQLException e){
      System.out.println(e.getMessage());
      return null;
    }
    
  }

}
