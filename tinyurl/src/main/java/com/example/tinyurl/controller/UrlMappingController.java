package com.example.tinyurl.controller;

import com.example.tinyurl.service.UrlMappingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class UrlMappingController {

    private static final Logger logger = LoggerFactory.getLogger(UrlMappingController.class);

    @Autowired
    private UrlMappingService service;

    // ObjectMapper for parsing JSON
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody String originalUrl) {
        String shortUrl = service.shortenUrl(originalUrl);
        return new ResponseEntity<>(shortUrl, HttpStatus.OK);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<String> handleRedirect(
            @PathVariable String shortUrl,
            HttpServletRequest request) {

        // Fetch the original URL JSON string from the service
        String originalUrlJson = service.getOriginalUrl(shortUrl);
        System.out.println(originalUrlJson);

        // Extract the actual URL from the JSON string
        String originalUrl = extractUrlFromJson(originalUrlJson);
        System.out.println(originalUrl);

        if (originalUrl != null) {
            // Check if the request comes from a browser by looking at the User-Agent
            String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
            if (userAgent != null && isBrowserRequest(userAgent)) {
                // Create headers and set the redirection location
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(URI.create(originalUrl));
                // Return a 302 Found response with the location header
                return new ResponseEntity<>(headers, HttpStatus.FOUND);
            } else {
                // Return the original URL for non-browser requests as plain text
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "text/plain").body(originalUrl);
            }
        } else {
            // Return 404 Not Found if the short URL doesn't map to any original URL
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // Helper method to extract the URL from the JSON string
    private String extractUrlFromJson(String json) {
        try {
            // Parse the JSON string to extract the URL
            JsonNode node = objectMapper.readTree(json);
            return node.get("inputValue").asText(); // Extract the value of "inputValue"
        } catch (Exception e) {
            // Log the error or handle it appropriately
            logger.error("Failed to parse JSON", e);
            return null;
        }
    }

    // Helper method to check if the request is from a browser
    private boolean isBrowserRequest(String userAgent) {
        // Check for a broader range of browser identifiers or make it more generic
        return userAgent.toLowerCase().matches(".*(mozilla|chrome|safari|edge|opera).*");
    }

}
