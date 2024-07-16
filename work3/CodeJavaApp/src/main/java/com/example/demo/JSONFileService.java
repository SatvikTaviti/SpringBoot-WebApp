package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class JSONFileService {

    @Autowired
    private ObjectMapper objectMapper;

    public void createJSONFile(User user, File outputFile) throws IOException {
        objectMapper.writeValue(outputFile, user);
    }
}
