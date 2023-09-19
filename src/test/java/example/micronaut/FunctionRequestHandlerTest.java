package example.micronaut;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import example.micronaut.domain.FuncRequest;
import example.micronaut.domain.FuncResponse;
import example.micronaut.repository.MutterRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public class FunctionRequestHandlerTest {

    @Inject
    private MutterRepository mutterRepository;

    @Inject
    private FunctionRequestHandler handler;

    @BeforeEach
    void beforeEach() {
    }

    @Test
    public void testHandler() {

        /* 期待値 */
        String expected = "{\"findKey\":\"testName\",\"pass\":\"pass\",\"name\":\"testName\",\"id\":1,\"text\":\"testText\",\"HTTPMethd\":\"GET\"}";

        /* 引数設定 */
        FuncRequest request = new FuncRequest();
        request.setHttpMethod("GET");
        request.setPath("/");

        JSONObject body = new JSONObject();
        body.put("id", 1);
        body.put("pass", "pass");
        body.put("findKey", "testName");
        request.setBody(body.toString());

        /* 呼び出し */
        FuncResponse response = handler.execute(request);

        /* 判定 */
        assertEquals(200, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }
}
