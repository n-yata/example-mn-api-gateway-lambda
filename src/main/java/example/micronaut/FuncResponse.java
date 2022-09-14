package example.micronaut;

import java.io.Serializable;

import lombok.Data;

@Data
public class FuncResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String body;
    private int statusCode;
}
