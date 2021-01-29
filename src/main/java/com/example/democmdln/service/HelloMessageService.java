package com.example.democmdln.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HelloMessageService {

    @Value("${messagename}")
    private String msgName;

    public String getMessage() {
        return getMessage(msgName);
    }

    public String getMessage(String value) {
        return "Hello " + value;
    }

}
