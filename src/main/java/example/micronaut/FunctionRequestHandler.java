package example.micronaut;

import org.json.JSONObject;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micronaut.function.aws.MicronautRequestHandler;
import jakarta.inject.Inject;

public class FunctionRequestHandler
        extends MicronautRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Inject
    ObjectMapper objectMapper;

    @Override
    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent input) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        JSONObject obj = new JSONObject(input.getBody());
        obj.put("HTTPMethd", input.getHttpMethod());

        response.setStatusCode(200);
        response.setBody(obj.toString());
        return response;
    }
}
