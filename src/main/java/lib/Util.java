package lib;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;

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
        JsonNode rootNode = mapper.readTree(json);
        return rootNode;
    }
}
