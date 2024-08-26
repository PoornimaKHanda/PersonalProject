package com.example.tinyurl.controller;

import com.example.tinyurl.service.UrlMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UrlMappingController {

    @Autowired
    private UrlMappingService service;

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody String originalUrl) {
        String shortUrl = service.shortenUrl(originalUrl);
        return new ResponseEntity<>(shortUrl, HttpStatus.OK);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortUrl) {
        String originalUrl = service.getOriginalUrl(shortUrl);
        if (originalUrl != null) {
            return new ResponseEntity<>(originalUrl, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
