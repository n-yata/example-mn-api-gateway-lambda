package example.micronaut;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    }
}
