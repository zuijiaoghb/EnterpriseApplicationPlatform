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

## mobile项目
## 打包生成index.android.bundle bundle文件
PS D:\EnterpriseApplicationPlatform\mobileend> npx react-native bundle --platform android --dev false --entry-file index.js --bundle-output android/app/src/main/assets/index.android.bundle --assets-dest android/app/src/main/res
## 打包apk
PS D:\EnterpriseApplicationPlatform\mobileend> npx react-native run-android
## 构建发布apk
.\gradlew.bat assembleRelease