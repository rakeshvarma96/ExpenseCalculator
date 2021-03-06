package lib;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import models.APIResponse;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.Response;

public class Util {
    public static boolean isValidString(String s) {
        return !StringUtils.isBlank(s);
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^[0-9]{10}$";
        return phoneNumber.matches(regex);
    }
    public static JsonNode parseJSON(String json) throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();

        ObjectMapper mapper = new ObjectMapper(factory);
        return mapper.readTree(json);
    }

    public static Response formatResponse(APIResponse apiResponse) {
        int statusCode = apiResponse.getStatusCode();
        String json = new Gson().toJson(apiResponse);
        return Response.status(statusCode).entity(json).build();
    }
}
