package com.schoolmoney.pl.files.storage.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static software.amazon.awssdk.regions.Region.EU_WEST_1;

@Slf4j
@Service
public class StorageService {
    private final static Long EXPIRATION_TIME = 10L; // Expiration time in minutes

    @Value("${storage.s3.bucketName}")
    private String BUCKET_NAME;
    private final S3Client s3Client;

    @Autowired
    public StorageService(
            @Value("${storage.s3.accessKey}") String accessKey,
            @Value("${storage.s3.secretKey}") String secretKey,
            @Value("${storage.s3.endpoint}") String endpoint
    ) {
        AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                                .region(EU_WEST_1)
                                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                                .endpointOverride(URI.create(endpoint))
                                .forcePathStyle(true)
                                .build();

    }

    public void createBucket() {
        s3Client.createBucket(b -> b.bucket(BUCKET_NAME));
    }

    public PutObjectResponse uploadFile(String key, UUID organizationId, String contentType, ByteArrayOutputStream stream) throws IOException {

        Map<String, String> metadata = new HashMap<>();
        metadata.put("organization_id", organizationId.toString());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                            .bucket(BUCKET_NAME)
                                                            .contentType(contentType)
                                                            .key(key)
                                                            .metadata(metadata)
                                                            .build();

        try (stream; InputStream in = new ByteArrayInputStream(stream.toByteArray())) {
            return s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(in, stream.size()));
        } catch (IOException e) {
            throw new IOException("Error uploading file to S3", e);
        }

    }

    public ResponseInputStream<GetObjectResponse> getFile(String key) {
        return s3Client.getObject(b -> b.bucket(BUCKET_NAME)
                                        .key(key),
                                  ResponseTransformer.toInputStream());
    }

    public void deleteFile(String key) {
        s3Client.deleteObject(b -> b.bucket(BUCKET_NAME)
                                    .key(key));
    }

    public String createPresignedGetUrl(String keyName) {
        if (keyName == null || keyName.isEmpty()) {
            return null;
        }
        try (S3Presigner presigner = S3Presigner.builder()
                                                .region(s3Client.serviceClientConfiguration()
                                                                .region())
                                                .endpointOverride(s3Client.serviceClientConfiguration()
                                                                          .endpointOverride()
                                                                          .orElse(null))
                                                .credentialsProvider(s3Client.serviceClientConfiguration()
                                                                             .credentialsProvider())
                                                .serviceConfiguration(S3Configuration.builder()
                                                                                     .pathStyleAccessEnabled(true)
                                                                                     .build())
                                                .build()) {

            GetObjectRequest objectRequest = GetObjectRequest.builder()
                                                             .bucket(BUCKET_NAME)
                                                             .key(keyName)
                                                             .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                                                                            .signatureDuration(
                                                                                    Duration.ofMinutes(EXPIRATION_TIME))
                                                                            .getObjectRequest(objectRequest)
                                                                            .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            return presignedRequest.url()
                                   .toExternalForm();
        }
    }

}
