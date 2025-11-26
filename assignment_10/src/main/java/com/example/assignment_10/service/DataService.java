package com.example.assignment_10.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataService {

    @Autowired
    private MinioClient minioClient;

    private static final String BUCKET_NAME = "datalake";

    public String uploadFile(MultipartFile file) throws Exception {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
        }

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(file.getOriginalFilename())
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
        }
        return "File uploaded successfully: " + file.getOriginalFilename();
    }

    public List<Map<String, Object>> queryData(String sql) throws Exception {
        // Load DuckDB JDBC driver
        Class.forName("org.duckdb.DuckDBDriver");

        // Connect to in-memory DuckDB
        try (Connection conn = DriverManager.getConnection("jdbc:duckdb:");
                Statement stmt = conn.createStatement()) {

            // Install and load httpfs extension to access S3/MinIO
            stmt.execute("INSTALL httpfs;");
            stmt.execute("LOAD httpfs;");

            // Configure S3 access (MinIO)
            stmt.execute("SET s3_endpoint='localhost:9000';");
            stmt.execute("SET s3_use_ssl=false;");
            stmt.execute("SET s3_url_style='path';");
            stmt.execute("SET s3_access_key_id='minioadmin';");
            stmt.execute("SET s3_secret_access_key='minioadmin';");

            // Execute the query
            try (ResultSet rs = stmt.executeQuery(sql)) {
                List<Map<String, Object>> results = new ArrayList<>();
                int columnCount = rs.getMetaData().getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                    }
                    results.add(row);
                }
                return results;
            }
        }
    }
}
