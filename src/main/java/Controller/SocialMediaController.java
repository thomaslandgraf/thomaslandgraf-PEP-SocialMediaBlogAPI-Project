package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService;

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

        // if(!accountService.geAccoutByLogin(account.getUsername(), account.getPassword())){
        //     context.status(401);
        // }
    }
}