package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
  private AccountDAO accountDAO;

  public AccountService(){
    accountDAO = new AccountDAO();
  }

  public Account addAccount(Account account){
    return accountDAO.insertAccount(account);
  }

  public boolean usernameExists(String username){
    return accountDAO.usernameExists(username);
  }

  public Account getAccoutByLogin(String username, String password){
    return accountDAO.getAccountByLogin(username, password);
  }

}
