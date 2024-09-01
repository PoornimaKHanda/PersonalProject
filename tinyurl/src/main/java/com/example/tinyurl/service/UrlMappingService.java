package com.example.tinyurl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UrlMappingService {

    private static final Logger logger = LoggerFactory.getLogger(UrlMappingService.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${url.shortening.length:7}")  // Use application properties for configurable values
    private int urlShorteningLength;

    public String shortenUrl(String originalUrl) {
        String shortUrl = generateShortUrl(originalUrl, urlShorteningLength);
        // Create a new URL mapping and save it in Redis
        redisTemplate.opsForValue().set(shortUrl, originalUrl);
        return shortUrl;
    }

    public String getOriginalUrl(String shortUrl) {
        // Retrieve the original URL from Redis and handle the case where it might be null
        return Optional.ofNullable(redisTemplate.opsForValue().get(shortUrl))
                       .orElseThrow(() -> new RuntimeException("URL not found"));
    }

    private String generateShortUrl(String url, int length) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(url.getBytes("UTF-8"));
            String hash = String.format("%032x", new BigInteger(1, bytes));
            return hash.substring(0, length);
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            logger.error("Error generating short URL", e);
            throw new RuntimeException("Error generating short URL", e);
        }
    }
}
