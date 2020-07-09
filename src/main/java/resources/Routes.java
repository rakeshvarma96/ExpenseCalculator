package resources;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import controllers.ExpenseController;
import lib.Util;
import models.APIResponse;
import models.ExpenseMeta;
import models.User;
import validators.APIRequestValidator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("/v1")
public class Routes {
    @Path("/hc")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMessage() {
        return "Server returning 200 status\n";
    }

    @Path("/user/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String json) throws IOException, ProcessingException {
        if(!APIRequestValidator.isValidCreateUser(json)) {
            return Util.formatResponse(new APIResponse(400, "Insufficient input params", "failed", null));
        }
        JsonNode jsonNode = Util.parseJSON(json);
        String id = jsonNode.get("id").textValue();
        String name = jsonNode.get("name").textValue();
        String phone = jsonNode.get("phone").textValue();
        ExpenseController controller = ExpenseController.getInstance();
        APIResponse response = controller.addUser(new User(id, name, phone));
        return Util.formatResponse(response);
    }

    @Path("/user/get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response showUsers() {
        ExpenseController controller = ExpenseController.getInstance();
        return Util.formatResponse(controller.showUsers());
    }

    @Path("/expense/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addExpense(String json) throws IOException, ProcessingException {
        if(!APIRequestValidator.isValidExpense(json)) {
            return Util.formatResponse(new APIResponse(400, "Insufficient input params", "failed", null));
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode arrayNode;
        JsonNode jsonNode = Util.parseJSON(json);
        String name = jsonNode.get("name").textValue();
        String description = jsonNode.get("notes").textValue();
        String paidBy = jsonNode.get("paidBy").textValue();
        arrayNode = jsonNode.get("participants");
        List<String> participants = mapper.readValue(arrayNode.traverse(), new TypeReference<ArrayList<String>>(){});
        arrayNode = jsonNode.get("contributions");
        List<Double> contributions = null;
        if(arrayNode != null && !arrayNode.isNull())
            contributions = mapper.readValue(arrayNode.traverse(), new TypeReference<List<Double>>() {});
        double amount = jsonNode.get("amount").asDouble();
        String type = jsonNode.get("type").textValue();
        ExpenseController controller = ExpenseController.getInstance();
        ExpenseMeta expenseMeta = new ExpenseMeta(name, description, paidBy, participants, contributions, amount, type);
        APIResponse response = controller.addExpense(expenseMeta);
        return Util.formatResponse(response);
    }

    @Path("expense/balance/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewBalForUser(@QueryParam("userId") String userId) {
        ExpenseController controller = ExpenseController.getInstance();
        return Util.formatResponse(controller.viewBalForUser(userId));
    }

    @Path("expense/viewAllBalances")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewAllBalances() {
        ExpenseController controller = ExpenseController.getInstance();
        return Util.formatResponse(controller.viewAllBalances());
    }

    @Path("group/add/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addGroup(String json) throws IOException, ProcessingException {
        if(!APIRequestValidator.isValidCreateGroup(json)) {
            return Util.formatResponse(new APIResponse(400, "Insufficient input params", "failed", null));
        }
        JsonNode jsonNode = Util.parseJSON(json);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode arrayNode;
        String name = jsonNode.get("name").textValue();
        arrayNode = jsonNode.get("users");
        List<String> users = mapper.readValue(arrayNode.traverse(), new TypeReference<ArrayList<String>>() {});
        ExpenseController expenseController = ExpenseController.getInstance();
        APIResponse response = expenseController.addGroup(name, users);
        return Util.formatResponse(response);
    }

    @Path("group/addUser/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUserToGroup(String json) throws IOException, ProcessingException {
        if(!APIRequestValidator.isValidCreateGroup(json)) {
            return Util.formatResponse(new APIResponse(400, "Insufficient input params", "failed", null));
        }
        JsonNode jsonNode = Util.parseJSON(json);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode arrayNode;
        String name = jsonNode.get("name").textValue();
        arrayNode = jsonNode.get("users");
        List<String> users = mapper.readValue(arrayNode.traverse(), new TypeReference<ArrayList<String>>() {});
        ExpenseController expenseController = ExpenseController.getInstance();
        APIResponse response = expenseController.addUsersToGroup(name, users);
        return Util.formatResponse(response);
    }
}
