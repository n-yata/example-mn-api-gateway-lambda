package example.micronaut;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import example.micronaut.domain.FuncRequest;
import example.micronaut.domain.FuncResponse;
import io.micronaut.function.aws.MicronautRequestHandler;
import jakarta.inject.Inject;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

public class FunctionRequestHandler
        extends MicronautRequestHandler<FuncRequest, FuncResponse> {
    @Inject
    private ObjectMapper objectMapper;

    @Override
    public FuncResponse execute(FuncRequest input) {
        FuncResponse response = new FuncResponse();

        JSONObject obj = new JSONObject(input.getBody());
        obj.put("HTTPMethd", input.getHttpMethod());

        upload(obj.optString("fileName"));

        response.setBody(obj.toString());
        response.setStatusCode(200);

        return response;
    }

    /**
     * アップロード
     * @param fileName
     */
    private void upload(String fileName) {
        Region region = Region.AP_NORTHEAST_1;
        S3Presigner presigner = S3Presigner.builder()
                .region(region)
                .build();

        String bucketName = "s3-presigned-example-nyata";

        signBucket(presigner, bucketName, fileName);
        presigner.close();
    }

    /**
     * アップロードUtil
     * @param presigner
     * @param bucketName
     * @param keyName
     */
    private static void signBucket(S3Presigner presigner, String bucketName, String keyName) {

        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .contentType("text/plain")
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            String myURL = presignedRequest.url().toString();
            System.out.println("Presigned URL to upload a file to: " + myURL);
            System.out.println("Which HTTP method needs to be used when uploading a file: "
                    + presignedRequest.httpRequest().method());

            // Upload content to the Amazon S3 bucket by using this URL.
            URL url = presignedRequest.url();

            // Create the connection and use it to upload the new object by using the presigned URL.
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write("This text was uploaded as an object by using a presigned URL.");
            out.close();

            connection.getResponseCode();
            System.out.println("HTTP response code is " + connection.getResponseCode());

        } catch (S3Exception | IOException e) {
            e.getStackTrace();
        }
    }
}
