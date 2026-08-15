package DAO;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

  public Message insertMessage(Message message){
    Connection connection = ConnectionUtil.getConnection();
    try {
      String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
      PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

      preparedStatement.setInt(1, message.getPosted_by());
      preparedStatement.setString(2, message.getMessage_text());
      preparedStatement.setLong(3, message.getTime_posted_epoch());

      preparedStatement.executeUpdate();
      ResultSet resultSet = preparedStatement.getGeneratedKeys();

      if(resultSet.next()){
        int generatedMessageId = (int) resultSet.getLong(1);
        return new Message(generatedMessageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
      }

    } catch (SQLException e){
      System.out.println(e.getMessage());
    }
    return null;
  }

  public List<Message> getAllmessages(){
    Connection connection = ConnectionUtil.getConnection();
    List<Message> listOfMessages = new ArrayList<>();
    
    try {

      String sql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM message;";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);

      ResultSet resultSet = preparedStatement.executeQuery();

      while(resultSet.next()){
        listOfMessages.add(new Message(resultSet.getInt("message_id"), resultSet.getInt("posted_by"), resultSet.getString("message_text"), resultSet.getLong("time_posted_epoch")));
      }

      return listOfMessages;

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return listOfMessages;
    }
  }
}
