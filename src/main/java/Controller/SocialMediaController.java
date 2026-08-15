package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postNewAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void postNewAccountHandler(Context context) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        if(account.getUsername() == null || account.getUsername().trim().isEmpty()){
            context.status(400);
            return;
        }

        if(account.getPassword() == null || account.getPassword().length() < 4){
            context.status(400);
            return;
        }

        if(accountService.usernameExists(account.getUsername())) {
            context.status(409);
            return;
        }
        Account addAccount = accountService.addAccount(account);

        if(addAccount != null){
            context.status(200);
            context.json(mapper.writeValueAsString(addAccount));
        } else {
            context.status(400);
        }
    }

    private void postLoginHandler(Context context) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        Account accountLogin = accountService.getAccoutByLogin(account.getUsername(), account.getPassword());

        if(accountLogin != null){
            context.status(200);
            context.json(mapper.writeValueAsString(accountLogin));
        } else {
            context.status(401);
        }
    }

    private void postMessageHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);

        Message addMessage = messageService.postMessage(message);

        if(addMessage != null) {
            context.status(200);
            context.json(mapper.writeValueAsString(addMessage));
        } else {
            context.status(400);
        }
    }

    private void getAllMessagesHandler(Context context) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();

        List<Message> getAllMessages = messageService.getAllMessages();
        context.status(200);
        context.json(mapper.writeValueAsString(getAllMessages));
    }
}