package example.micronaut.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class FuncRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String path;
    private String httpMethod;
    private String body;

}
