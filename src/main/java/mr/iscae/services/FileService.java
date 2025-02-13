package mr.iscae.services;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    private static final String FILE_BASE_URL = "https://s3.api.hostflare.cloud/product/";

    public String uploadFile(MultipartFile file) {
        try {
            // Validate file
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("File cannot be empty");
            }

            String uuid = UUID.randomUUID().toString();
            String originalFilename = file.getOriginalFilename();
            String fileName = uuid + "/" + originalFilename;  // Format: uuid/filename

            // Upload file
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            // Return complete URL
            return FILE_BASE_URL + fileName;

        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage(), e);
        }
    }

    public String getFileUrl(String fileName) {
        return FILE_BASE_URL + fileName;
    }

    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file: " + e.getMessage(), e);
        }
    }
    private static final List<String> IMAGE_CONTENT_TYPES = Arrays.asList(
            "image/png", "image/jpeg", "image/jpg",
            "image/svg+xml",
            "image/bmp", "image/tiff", "image/webp"
    );

    public static boolean isValidImage(MultipartFile file) {
        return isValidContentType(file);
    }
    private static boolean isValidContentType(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        return FileService.IMAGE_CONTENT_TYPES.contains(contentType);
    }
}