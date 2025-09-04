package com.enterprise.platform.system.controller;

import com.enterprise.platform.system.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/captcha")
    public ResponseEntity<Map<String, Object>> getCaptcha() {
        try {
            String captchaId = captchaService.generateCaptchaId();
            String text = captchaService.generateCaptcha(captchaId);
            BufferedImage image = captchaService.createImage(text);
            String base64Image = captchaService.encodeImageToBase64(image);

            Map<String, Object> response = new HashMap<>();
            response.put("captchaId", captchaId);
            response.put("image", base64Image);
            response.put("message", "验证码获取成功");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "验证码生成失败");
            error.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}