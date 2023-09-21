package example.micronaut;

import org.json.JSONObject;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import example.micronaut.domain.FuncRequest;
import example.micronaut.domain.FuncResponse;
import example.micronaut.utils.S3Utils;
import io.micronaut.function.aws.MicronautRequestHandler;
import jakarta.inject.Inject;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

public class FunctionRequestHandler
        extends MicronautRequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String BUCKET_NAME = "s3-presigned-example-nyata";

    @Inject
    private ObjectMapper objectMapper;

    @Override
    public APIGatewayProxyResponseEvent execute(APIGatewayProxyRequestEvent request) {

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try {
            FuncRequest req = objectMapper.readValue(request.getBody(), FuncRequest.class);
            FuncResponse res = runProcess(req);

            response.setStatusCode(200);
            response.setBody(objectMapper.writeValueAsString(res));

        } catch (Exception e) {
            response.setStatusCode(500);
            JSONObject obj = new JSONObject();
            obj.put("error", "error");
            response.setBody(obj.toString());
        }
        return response;
    }

    /**
     * メイン処理
     * @param request
     * @return
     */
    private FuncResponse runProcess(FuncRequest request) {

        String url = "";

        switch (request.getAction()) {
        case "upload":
            url = upload(request.getFileName());
            break;
        case "download":
            url = download(request.getFileName());
            break;
        default:
            break;
        }

        FuncResponse response = new FuncResponse();
        response.setResult("OK");
        response.setUrl(url);
        return response;
    }

    /**
     * ダウンロード
     * @param fileName
     * @return 書名付きURL
     */
    private String download(String fileName) {
        Region region = Region.AP_NORTHEAST_1;
        S3Presigner presigner = S3Presigner.builder()
                .region(region)
                .build();

        String url = S3Utils.getPresignedUrl(presigner, BUCKET_NAME, fileName);
        presigner.close();

        return url;
    }

    /**
     * アップロード
     * @param fileName
     */
    private String upload(String fileName) {
        Region region = Region.AP_NORTHEAST_1;
        S3Presigner presigner = S3Presigner.builder()
                .region(region)
                .build();

        String url = S3Utils.signBucket(presigner, BUCKET_NAME, fileName);
        presigner.close();

        return url;
    }
}
