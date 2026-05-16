package com.finsight.service;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @PostConstruct
    public void initialiseBucket() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build()
                );
                log.info("Created MinIO bucket: {}", bucketName);
            } else {
                log.info("MinIO bucket already exists: {}", bucketName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialise MinIO bucket: " + e.getMessage(), e);
        }
    }

    public String uploadFile(MultipartFile file, Long userId) {
        try {
            String minioKey = generateKey(userId, file.getOriginalFilename());

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(minioKey)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            log.info("Uploaded file to MinIO: {}", minioKey);
            return minioKey;

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to storage: " + e.getMessage(), e);
        }
    }

    public InputStream downloadFile(String minioKey) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(minioKey)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to download file from storage: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String minioKey) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(minioKey)
                            .build()
            );
            log.info("Deleted file from MinIO: {}", minioKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
        }
    }

    private String generateKey(Long userId, String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return String.format("users/%d/%s%s", userId, UUID.randomUUID(), extension);
    }
}