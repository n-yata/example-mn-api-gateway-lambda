package example.micronaut;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FunctionRequestHandlerTest {

    private static FunctionRequestHandler handler;

    @BeforeAll
    public static void setupServer() {
        handler = new FunctionRequestHandler();
    }

    @AfterAll
    public static void stopServer() {
        if (handler != null) {
            handler.getApplicationContext().close();
        }
    }

    @Test
    public void testHandler() {
        FuncRequest request = new FuncRequest();
        request.setHttpMethod("GET");
        request.setPath("/");
        FuncResponse response = handler.execute(request);
        assertEquals(200, response.getStatusCode());
        assertEquals("{\"message\":\"Hello World\"}", response.getBody());
    }
}
