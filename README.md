# Enterprise Application Platform

## 技术栈
- 前端: React + Ant Design + React-native(android+ios)
- 后端: Spring Boot 3.x
- 数据库: MySQL
- 构建工具: Gradle  .\gradlew.bat clean build    .\gradlew.bat bootRun

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