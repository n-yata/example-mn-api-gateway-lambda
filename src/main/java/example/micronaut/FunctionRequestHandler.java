package example.micronaut;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.micronaut.function.aws.MicronautRequestHandler;
import jakarta.inject.Inject;

public class FunctionRequestHandler
        extends MicronautRequestHandler<FuncRequest, FuncResponse> {
    @Inject
    ObjectMapper objectMapper;

    @Override
    public FuncResponse execute(FuncRequest input) {
        FuncResponse response = new FuncResponse();

        //        String json = StringUtils.EMPTY_STRING;
        //        try {
        //            json = objectMapper.writeValueAsString(input);
        //        } catch (JsonProcessingException e) {
        //            e.printStackTrace();
        //        }
        //        response.setBody(json);

        JSONObject obj = new JSONObject(input.getBody());
        obj.put("HTTPMethd", input.getHttpMethod());
        response.setBody(obj.toString());

        response.setStatusCode(200);

        return response;
    }
}
