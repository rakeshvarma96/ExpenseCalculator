package validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class APIRequestValidator {
    public static boolean isValidCreateUser(String json) throws IOException, ProcessingException {
        String jsonSchema = new String(Files.readAllBytes(Paths.get("/Users/rakesh/Downloads/Projects/src/main/java/jsonSchemas/AddUser.json")));
        final JsonNode data = JsonLoader.fromString(json);
        final JsonNode schema = JsonLoader.fromString(jsonSchema);

        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        JsonValidator validator = factory.getValidator();

        ProcessingReport report = validator.validate(schema, data);
        return report.isSuccess();
    }

    public static boolean isValidExpense(String json) throws IOException, ProcessingException {
        String jsonSchema = new String(Files.readAllBytes(Paths.get("/Users/rakesh/Downloads/Projects/src/main/java/jsonSchemas/AddExpense.json")));
        final JsonNode data = JsonLoader.fromString(json);
        final JsonNode schema = JsonLoader.fromString(jsonSchema);

        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        JsonValidator validator = factory.getValidator();

        ProcessingReport report = validator.validate(schema, data);
        return report.isSuccess();
    }
}
