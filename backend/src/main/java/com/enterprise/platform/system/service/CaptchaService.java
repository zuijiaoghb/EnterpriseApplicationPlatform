package com.enterprise.platform.system.service;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class CaptchaService {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Autowired(required = false)
    private RedisTemplate<String, String> redisTemplate;
    
    @Autowired
    private InMemoryCaptchaService inMemoryCaptchaService;

    private static final long CAPTCHA_EXPIRATION = 5; // 5分钟过期
    
    private boolean isRedisAvailable() {
        try {
            return redisTemplate != null && redisTemplate.getConnectionFactory() != null && 
                   redisTemplate.getConnectionFactory().getConnection() != null;
        } catch (Exception e) {
            return false;
        }
    }

    public String generateCaptchaId() {
        return UUID.randomUUID().toString();
    }

    public String generateCaptcha(String captchaId) {
        String text = defaultKaptcha.createText();
        
        // 优先使用Redis，如果不可用则使用内存存储
        if (isRedisAvailable()) {
            try {
                redisTemplate.opsForValue().set("captcha:" + captchaId, text, CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
            } catch (Exception e) {
                // Redis操作失败，回退到内存存储
                inMemoryCaptchaService.storeCaptcha(captchaId, text);
            }
        } else {
            inMemoryCaptchaService.storeCaptcha(captchaId, text);
        }
        
        return text;
    }

    public BufferedImage createImage(String text) {
        return defaultKaptcha.createImage(text);
    }

    public String encodeImageToBase64(BufferedImage image) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode captcha image", e);
        }
    }

    public boolean validateCaptcha(String captchaId, String userInput) {
        if (captchaId == null || userInput == null) {
            return false;
        }

        // 优先使用Redis，如果不可用则使用内存存储
        if (isRedisAvailable()) {
            try {
                String key = "captcha:" + captchaId;
                String storedCaptcha = redisTemplate.opsForValue().get(key);
                
                if (storedCaptcha == null) {
                    return false;
                }

                boolean valid = storedCaptcha.equalsIgnoreCase(userInput);
                
                // 验证后删除验证码，防止重复使用
                redisTemplate.delete(key);
                
                return valid;
            } catch (Exception e) {
                // Redis操作失败，回退到内存验证
                return inMemoryCaptchaService.validateCaptcha(captchaId, userInput);
            }
        } else {
            return inMemoryCaptchaService.validateCaptcha(captchaId, userInput);
        }
    }

    public void clearCaptcha(String captchaId) {
        if (captchaId != null) {
            if (isRedisAvailable()) {
                try {
                    redisTemplate.delete("captcha:" + captchaId);
                } catch (Exception e) {
                    // Redis操作失败，回退到内存清除
                    inMemoryCaptchaService.clearCaptcha(captchaId);
                }
            } else {
                inMemoryCaptchaService.clearCaptcha(captchaId);
            }
        }
    }
}