package com.example.jaejudo.domain.analysis.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 파일 업로드
    public String upload(MultipartFile file, String dirName) throws IOException {
        String fileName = dirName + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));
        }

        return fileName; // S3 Key 반환
    }

    // 다운로드용 Presigned URL 생성 (1시간 유효)
    public String generatePresignedUrl(String s3Key) {
        if (s3Key == null) return null;
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1시간
        expiration.setTime(expTimeMillis);

        URL url = amazonS3.generatePresignedUrl(bucket, s3Key, expiration);
        return url.toString();
    }

    /**
     * S3 파일 삭제
     * @param s3Key 삭제할 파일의 S3 Key (경로 포함 파일명)
     */
    public void deleteFile(String s3Key) {
        if (s3Key == null || s3Key.isEmpty()) {
            return;
        }
        try {
            // 파일 존재 여부 확인 후 삭제 (선택 사항이지만 안전함)
            if (amazonS3.doesObjectExist(bucket, s3Key)) {
                amazonS3.deleteObject(bucket, s3Key);
                log.info("S3 file deleted: {}", s3Key);
            } else {
                log.warn("S3 file not found, skip delete: {}", s3Key);
            }
        } catch (AmazonServiceException e) {
            log.error("Failed to delete S3 file: {}", s3Key, e);
            // 필요한 경우 예외를 다시 던져서 스케줄러가 재시도하게 할 수도 있음
        }
    }
}