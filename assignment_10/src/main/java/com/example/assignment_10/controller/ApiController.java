package com.example.assignment_10.controller;

import com.example.assignment_10.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
public class ApiController {

    @Autowired
    private DataService dataService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String result = dataService.uploadFile(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error uploading file: " + e.getMessage());
        }
    }

    @PostMapping("/query")
    public ResponseEntity<Object> queryData(@RequestBody String sql) {
        try {
            List<Map<String, Object>> result = dataService.queryData(sql);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error querying data: " + e.getMessage());
        }
    }
}
