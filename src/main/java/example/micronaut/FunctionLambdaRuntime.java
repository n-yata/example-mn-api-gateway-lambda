package example.micronaut;

import java.net.MalformedURLException;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import example.micronaut.domain.FuncRequest;
import example.micronaut.domain.FuncResponse;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.function.aws.runtime.AbstractMicronautLambdaRuntime;

public class FunctionLambdaRuntime extends
        AbstractMicronautLambdaRuntime<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent, FuncRequest, FuncResponse> {
    public static void main(String[] args) {
        try {
            new FunctionLambdaRuntime().run(args);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Nullable
    protected RequestHandler<FuncRequest, FuncResponse> createRequestHandler(
            String... args) {
        return new FunctionRequestHandler();
    }
}
