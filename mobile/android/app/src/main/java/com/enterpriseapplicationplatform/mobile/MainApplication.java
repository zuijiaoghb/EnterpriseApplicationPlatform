package com.enterpriseapplicationplatform.mobile;

import android.app.Application;
import android.os.Build;
import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.soloader.SoLoader;
import java.util.Arrays;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {
    private static final String TAG = "MainApplication";
    private static final String LOG_FILE_NAME = "crash_log.txt";
    private static final Object FILE_LOCK = new Object();
    
    public void writeToLogFile(String message) {
        synchronized (FILE_LOCK) {
            try {
                File logFile = new File(getExternalFilesDir(null), LOG_FILE_NAME);
                FileWriter writer = new FileWriter(logFile, true);
                writer.append(new Date().toString()).append(": ").append(message).append("\n");
                writer.close();
            } catch (IOException e) {
                Log.e(TAG, "写入日志文件失败", e);
            }
        }
    }
    public static final int LOG_LEVEL_VERBOSE = 0;
    public static final int LOG_LEVEL_DEBUG = 1;
    public static final int LOG_LEVEL_INFO = 2;
    public static final int LOG_LEVEL_WARN = 3;
    public static final int LOG_LEVEL_ERROR = 4;
    
    private boolean shouldLog(int level) {        
        return BuildConfig.DEBUG || level >= LOG_LEVEL_WARN;
    }
    
    public void handleException(Exception e) {
        if (e != null && e.getMessage() != null) {
            if (shouldLog(LOG_LEVEL_ERROR)) {
                // 添加密钥服务异常处理
                if (e instanceof java.security.KeyStoreException) {
                    writeToLogFile("密钥库异常: " + e.getMessage());
                    Log.e(TAG, "密钥库异常", e);
                } else if (e instanceof java.security.UnrecoverableKeyException) {
                    writeToLogFile("密钥不可恢复异常: " + e.getMessage());
                    Log.e(TAG, "密钥不可恢复异常", e);
                } else if (e instanceof java.security.NoSuchAlgorithmException) {
                    writeToLogFile("算法不支持异常: " + e.getMessage());
                    Log.e(TAG, "算法不支持异常", e);
                }
                android.util.Log.e(TAG, "异常: " + e.getMessage(), e);
                String stackTrace = Log.getStackTraceString(e);
                writeToLogFile("异常: " + e.getMessage() + "\n" + stackTrace);
                if (BuildConfig.DEBUG) {
                    android.util.Log.w(TAG, "异常堆栈:", e);
                }
            }
            
            // 根据异常类型分类处理
            if (e instanceof NullPointerException) {
                Log.w(TAG, "空指针异常，建议检查对象初始化");
            } else if (e instanceof IllegalStateException) {
                Log.w(TAG, "非法状态异常，建议检查组件生命周期");
            } else if (e instanceof SecurityException) {
                Log.w(TAG, "权限异常，建议检查权限请求流程");                
            }
        }
    }
    
    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        
        @Override
        public boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                new MainReactPackage()
            );
        }

        @Override
        protected String getJSMainModuleName() {
            return "index";
        }
    };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            if (shouldLog(LOG_LEVEL_DEBUG)) {
                android.util.Log.d(TAG, "开始初始化SoLoader");
            }
            
            SoLoader.init(this, false);
            
            if (shouldLog(LOG_LEVEL_DEBUG)) {
                android.util.Log.d(TAG, "SoLoader初始化成功");
            }
            
            // 初始化React Native环境
            if (shouldLog(LOG_LEVEL_DEBUG)) {
                android.util.Log.d(TAG, "开始初始化React Native环境");
            }
            
            ReactNativeHost reactNativeHost = getReactNativeHost();
            ReactInstanceManager reactInstanceManager = reactNativeHost.getReactInstanceManager();
            
            if (shouldLog(LOG_LEVEL_DEBUG)) {
                android.util.Log.d(TAG, "React Native环境初始化完成");
                android.util.Log.d(TAG, "开发者模式: " + reactNativeHost.getUseDeveloperSupport());
            }
            
            // 添加资源清理回调
            reactInstanceManager.addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
                @Override
                public void onReactContextInitialized(ReactContext context) {
                    context.addLifecycleEventListener(new LifecycleEventListener() {
                        @Override
                        public void onHostResume() {
                            if (shouldLog(LOG_LEVEL_DEBUG)) {
                                Log.d(TAG, "应用回到前台");
                            }
                        }

                        @Override
                        public void onHostPause() {
                            if (shouldLog(LOG_LEVEL_DEBUG)) {
                                Log.d(TAG, "应用进入后台");
                            }
                        }

                        @Override
                        public void onHostDestroy() {
                            if (shouldLog(LOG_LEVEL_INFO)) {
                                Log.i(TAG, "清理React Native资源");
                            }
                            // 执行资源清理
                            reactInstanceManager.destroy();
                        }
                    });
                }
            });
                       
        } catch (Exception e) {
            handleException(e);
            throw new RuntimeException("React Native初始化失败", e);
        }
    }

}