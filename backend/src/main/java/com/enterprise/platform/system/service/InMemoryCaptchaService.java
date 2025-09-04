package com.enterprise.platform.system.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class InMemoryCaptchaService {
    
    private final Map<String, CaptchaData> captchaStorage = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    public InMemoryCaptchaService() {
        // 每5分钟清理过期的验证码
        scheduler.scheduleAtFixedRate(this::cleanupExpiredCaptchas, 5, 5, TimeUnit.MINUTES);
    }
    
    public String generateCaptchaId() {
        return "captcha_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    public void storeCaptcha(String captchaId, String captchaText) {
        captchaStorage.put(captchaId, new CaptchaData(captchaText, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)));
    }
    
    public boolean validateCaptcha(String captchaId, String userInput) {
        if (captchaId == null || userInput == null) {
            return false;
        }
        
        CaptchaData data = captchaStorage.get(captchaId);
        if (data == null || System.currentTimeMillis() > data.getExpireTime()) {
            return false;
        }
        
        boolean valid = data.getText().equalsIgnoreCase(userInput);
        if (valid) {
            captchaStorage.remove(captchaId);
        }
        
        return valid;
    }
    
    public void clearCaptcha(String captchaId) {
        if (captchaId != null) {
            captchaStorage.remove(captchaId);
        }
    }
    
    private void cleanupExpiredCaptchas() {
        long now = System.currentTimeMillis();
        captchaStorage.entrySet().removeIf(entry -> now > entry.getValue().getExpireTime());
    }
    
    private static class CaptchaData {
        private final String text;
        private final long expireTime;
        
        public CaptchaData(String text, long expireTime) {
            this.text = text;
            this.expireTime = expireTime;
        }
        
        public String getText() {
            return text;
        }
        
        public long getExpireTime() {
            return expireTime;
        }
    }
    
    public void shutdown() {
        scheduler.shutdown();
    }
}