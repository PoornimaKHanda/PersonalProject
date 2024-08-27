package com.example.tinyurl.service;

import com.example.tinyurl.model.UrlMapping;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UrlMappingService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;     

    public String shortenUrl(String originalUrl) {
        String shortUrl = generateShortUrl(originalUrl, 7);
        UrlMapping mapping = new UrlMapping();
        mapping.setId(shortUrl);
        mapping.setOriginalUrl(originalUrl);

        redisTemplate.opsForValue().set(shortUrl, originalUrl);
        // redisTemplate.opsForValue().set(originalUrl, shortUrl);

        return shortUrl;
    }

    public String getOriginalUrl(String shortUrl) {
        return (String) redisTemplate.opsForValue().get(shortUrl);
    }

    private String generateShortUrl(String url, int length) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(url.getBytes("UTF-8"));
            String hash = String.format("%32x", new BigInteger(1, bytes));
            return hash.substring(0, length);

        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;

    }
}
