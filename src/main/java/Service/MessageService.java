package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
  private MessageDAO messageDAO;
  private AccountDAO accountDAO;

  public MessageService(){
    messageDAO = new MessageDAO();
    accountDAO = new AccountDAO();
  }

  public Message postMessage(Message message){
    if(message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()){
      return null;
    }

    if(message.getMessage_text().length() >= 255){
      return null;
    }

    if(!accountDAO.accountExists(message.getPosted_by())){
      return null;
    }

    return messageDAO.insertMessage(message);
  }

  public List<Message> getAllMessages(){
    return messageDAO.getAllmessages();
  }

  public Message getMessageById(int message_id){
    return messageDAO.getMessageById(message_id);
  }

  public Message deleteMessageById(int message_id){
    Message messageToDelete = getMessageById(message_id);

    if(messageToDelete != null){
      messageDAO.deleteMessageById(message_id);
      return messageToDelete;
    }
    return null;
  }

  public Integer updateMessageById(String message_text, int message_id){
    if(message_text == null || message_text.trim().isEmpty()){
      return 0; 
    }

    if(message_text.length() >= 255){
      return 0;
    }

    return messageDAO.updateMessageById(message_text, message_id);
  }

  public List<Message> getAllMessagesByUser(int posted_by){
    return messageDAO.getAllMessegesByUser(posted_by);
  }
}
