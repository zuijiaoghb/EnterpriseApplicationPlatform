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
## ios jsbundle文件
PS D:\EnterpriseApplicationPlatform\mobileend> npx react-native bundle --platform ios --dev false --entry-file index.js --bundle-output ios/main.jsbundle --assets-dest ios
或者 PS D:\EnterpriseApplicationPlatform\mobileend> npm run bundle-ios 
## 打包ipa
PS D:\EnterpriseApplicationPlatform\mobileend> npx react-native run-ios
ios 使用 Mac 上的 Xcode 完成打包