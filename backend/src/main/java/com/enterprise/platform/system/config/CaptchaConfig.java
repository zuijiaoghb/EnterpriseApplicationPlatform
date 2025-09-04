package com.enterprise.platform.system.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class CaptchaConfig {

    @Bean
    public DefaultKaptcha getDefaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        
        // 图片边框 - 增加对比度
        properties.setProperty("kaptcha.border", "yes");
        properties.setProperty("kaptcha.border.color", "0,0,0");
        properties.setProperty("kaptcha.border.thickness", "1");
        
        // 背景颜色 - 白色背景提高清晰度
        properties.setProperty("kaptcha.background.clear.from", "white");
        properties.setProperty("kaptcha.background.clear.to", "white");
        
        // 字体颜色 - 黑色字体提高对比度
        properties.setProperty("kaptcha.textproducer.font.color", "0,0,0");
        
        // 图片宽度和高度 - 增大尺寸提高清晰度
        properties.setProperty("kaptcha.image.width", "150");
        properties.setProperty("kaptcha.image.height", "50");
        
        // 字体大小 - 增大字体提高可读性
        properties.setProperty("kaptcha.textproducer.font.size", "36");
        
        // 验证码长度 - 保持4位
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        
        // 字体 - 使用更清晰易读的字体
        properties.setProperty("kaptcha.textproducer.font.names", "Arial,Helvetica,sans-serif");
        
        // 字符间距 - 增加字符间距避免拥挤
        properties.setProperty("kaptcha.textproducer.char.space", "8");
        
        // 干扰线 - 适度添加干扰线防止机器识别但保持可读性
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");
        properties.setProperty("kaptcha.noise.color", "200,200,200");
        
        // 渲染质量 - 提高图片质量
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple");
        
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        
        return defaultKaptcha;
    }
}