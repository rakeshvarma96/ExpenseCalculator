package resources;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import entry.ExpenseMain;
import lib.Util;
import models.ExpenseMeta;
import models.User;
import validators.APIRequestValidator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
            return Response.status(400).entity("Insufficient input params").build();
        }
        JsonNode jsonNode = Util.parseJSON(json);
        String id = jsonNode.get("id").textValue();
        String name = jsonNode.get("name").textValue();
        String phone = jsonNode.get("phone").textValue();
        ExpenseMain expenseMain = ExpenseMain.getInstance();
        String res = expenseMain.addUser(new User(id, name, phone));
        if(res.equals("success"))
            return Response.status(200).entity("success").build();
        return Response.status(400).entity(res).build();
    }

    @Path("/user/get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> showUsers() {
        ExpenseMain expenseMain = ExpenseMain.getInstance();
        List<User> res = expenseMain.showUsers();
        return res;
    }

    @Path("/expense/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addExpense(String json) throws IOException, ProcessingException {
        if(!APIRequestValidator.isValidExpense(json)) {
            return Response.status(400).entity("Insufficient input params").build();
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
        ExpenseMain expenseMain = ExpenseMain.getInstance();
        ExpenseMeta expenseMeta = new ExpenseMeta(name, description, paidBy, participants, contributions, amount, type);
        String res = expenseMain.addExpense(expenseMeta);
        if(res.equals("success"))
            return Response.status(200).entity("success").build();
        return Response.status(400).entity(res).build();
    }

    @Path("expense/balance/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> viewBalForUser(@QueryParam("userId") String userId) {
        ExpenseMain expenseMain = ExpenseMain.getInstance();
        return expenseMain.viewBalForUser(userId);
    }

    @Path("expense/viewAllBalances")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> viewAllBalances() {
        ExpenseMain expenseMain = ExpenseMain.getInstance();
        return expenseMain.viewAllBalances();
    }
}
