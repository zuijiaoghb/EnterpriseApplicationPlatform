# Enterprise Application Platform

## 技术栈
- 前端: React + Ant Design + React-native(android+ios)
- 后端: Spring Boot 3.x
- 数据库: MySQL
- 构建工具: Gradle  .\gradlew.bat clean build    .\gradlew.bat bootRun
- 运行：java -jar enterprise-platform-backend-0.0.1-SNAPSHOT.jar
- centos8部署并创建自启服务：
1. 创建服务配置文件：
```
sudo vi /etc/systemd/system/enterpriseapplicationplatform.service
```
2. 添加以下内容（根据实际情况修改路径和参数）：
```
[Unit]
Description=EnterpriseApplicationPlatform
After=network.target

[Service]
User=root
WorkingDirectory=/app
ExecStart=/usr/bin/java -jar /app/enterprise-platform-backend-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
```
### 三、配置服务自启动
1. 重新加载systemd配置：
```
sudo systemctl daemon-reload
```
2. 设置开机自启：
```
sudo systemctl enable enterpriseapplicationplatform.service
```
3. 启动服务并验证状态：
```
sudo systemctl start enterpriseapplicationplatform.service
sudo systemctl status enterpriseapplicationplatform.service
```
### 四、验证自动启动功能
1. 测试重启服务：   
   ```
   sudo systemctl restart enterpriseapplicationplatform.service
   ```
2. 验证重启系统后是否自动启动：  
   ```
   sudo reboot
   # 重启后重新连接服务器
   systemctl status enterpriseapplicationplatform.service
   ```

## react项目
## 安装
npm install
## 运行
npm start
# 运行时可指定端口
PORT=3001 npm start
## 打包
npm run build
## 打包后，将build录下的文件上传到服务器即可
## centos8环境下使用 Nginx 部署frontend项目
## 安装nginx
yum install nginx
## 启动nginx
systemctl start nginx
## 停止nginx
systemctl stop nginx
## 重启nginx
systemctl restart nginx
## 配置nginx
vim /etc/nginx/nginx.conf
## 配置文件内容
server {
    listen       3001 default_server;
    listen       [::]:3001 default_server;
    server_name  localhost;
    root         /app/frontend/build;
    index        index.html index.htm;

    # Load configuration files for the default server block.
    include /etc/nginx/default.d/*.conf;

    location / {
        try_files $uri $uri/ /index.html;
    }

    error_page 404 /404.html;
        location = /40x.html {
    }

    error_page 500 502 503 504 /50x.html;
        location = /50x.html {
    }
}
## 重启nginx
systemctl restart nginx
## 查看nginx状态
systemctl status nginx
## 查看nginx日志
tail -f /var/log/nginx/access.log
tail -f /var/log/nginx/error.log
## 查看nginx配置文件
vim /etc/nginx/nginx.conf
## 查看nginx版本
nginx -v   

## mobile项目
## 打包生成index.android.bundle bundle文件
PS D:\EnterpriseApplicationPlatform\mobileend> npx react-native bundle --platform android --dev false --entry-file index.js --bundle-output android/app/src/main/assets/index.android.bundle --assets-dest android/app/src/main/res
## 打包apk
PS D:\EnterpriseApplicationPlatform\mobileend> npx react-native run-android
## ios jsbundle文件
PS D:\EnterpriseApplicationPlatform\mobileend> npx react-native bundle --platform ios --dev false --entry-file index.js --bundle-output ios/main.jsbundle --assets-dest ios
或者 PS D:\EnterpriseApplicationPlatform\mobileend> npm run bundle-ios 
## 打包ipa
PS D:\EnterpriseApplicationPlatform\mobileend> npx react-native run-ios
ios 使用 Mac 上的 Xcode 完成打包