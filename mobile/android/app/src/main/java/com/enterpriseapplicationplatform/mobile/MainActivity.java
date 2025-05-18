package com.enterpriseapplicationplatform.mobile;

import android.os.Bundle;
import android.util.Log;
import com.facebook.react.ReactActivity;
import com.facebook.soloader.SoLoader;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import java.lang.Thread.UncaughtExceptionHandler;
import java.io.File;
import android.content.Intent;

public class MainActivity extends ReactActivity {
    private UncaughtExceptionHandler defaultExceptionHandler;
    
    @Override
    protected String getMainComponentName() {
        return "EnterpriseApplicationPlatform";
    }
    
    private void showLogView() {
        File logFile = new File(getExternalFilesDir(null), "crash_log.txt");
        if (logFile.exists()) {
            Intent intent = new Intent(this, LogViewActivity.class);
            intent.putExtra("LOG_FILE_PATH", logFile.getAbsolutePath());
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置全局异常处理器
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e("MainActivity", "未捕获异常: " + ex.getMessage(), ex);
                String stackTrace = Log.getStackTraceString(ex);
                ((MainApplication)getApplication()).writeToLogFile("未捕获异常: " + ex.getMessage() + "\n" + stackTrace);
                defaultExceptionHandler.uncaughtException(thread, ex);
            }
        });
        
        try {
            // 初始化Hermes引擎
            try {
                SoLoader.init(this, false);
                if (!SoLoader.isInitialized()) {
                    throw new RuntimeException("Hermes引擎初始化失败");
                }
                
                if (BuildConfig.DEBUG) {
                    Log.d("MainActivity", "初始化React Native环境");
                    Log.d("MainActivity", "当前线程: " + Thread.currentThread().getName());
                    Log.d("MainActivity", "Hermes引擎状态: " + (SoLoader.isInitialized() ? "已初始化" : "未初始化"));
                }
            } catch (Exception e) {
                Log.e("MainActivity", "Hermes引擎初始化异常: " + e.getMessage(), e);
                ((MainApplication)getApplication()).writeToLogFile("Hermes引擎初始化异常: " + e.getMessage() + "\n" + Log.getStackTraceString(e));
                showLogView();
                finish();
                return;
            }
            
            // 健壮的空值检查
            try {
                if (getReactNativeHost() == null || getReactNativeHost().getReactInstanceManager() == null) {
                    throw new IllegalStateException("ReactInstanceManager未正确初始化");
                }
            } catch (Exception e) {
                Log.e("MainActivity", "React Native初始化检查异常: " + e.getMessage(), e);
                ((MainApplication)getApplication()).writeToLogFile("React Native初始化检查异常: " + e.getMessage() + "\n" + Log.getStackTraceString(e));
                showLogView();
                finish();
                return;
            }
            
            super.onCreate(savedInstanceState);
            
        } catch (Exception e) {
            Log.e("MainActivity", "初始化失败: " + e.getMessage(), e);
            ((MainApplication)getApplication()).writeToLogFile("初始化失败: " + e.getMessage() + "\n" + Log.getStackTraceString(e));
            showLogView();
            finish();
        }
        
        // 确保ReactInstanceManager已初始化
        ReactInstanceManager reactInstanceManager = null;
        try {
            reactInstanceManager = getReactNativeHost().getReactInstanceManager();
            if (reactInstanceManager == null) {
                Log.e("MainActivity", "ReactInstanceManager未初始化");
                return;
            }
        } catch (NullPointerException e) {
            Log.e("MainActivity", "获取ReactInstanceManager时发生空指针异常", e);
            return;
        }
        if (!reactInstanceManager.hasStartedCreatingInitialContext()) {
            reactInstanceManager.createReactContextInBackground();
        } // 在onCreate方法中添加以下检查
        if (reactInstanceManager.getCurrentReactContext() == null) {
            Log.e("MainActivity", "ReactContext未初始化");
            reactInstanceManager.recreateReactContextInBackground();
        }
        
        // 添加ReactInstanceManager错误监听
        reactInstanceManager.addReactInstanceEventListener(new ReactInstanceManager.ReactInstanceEventListener() {
            @Override
            public void onReactContextInitialized(ReactContext context) {
                if (!context.hasActiveCatalystInstance()) {
                    Log.e("MainActivity", "JS Bundle加载失败");
                }
            }
        });
        
        // 使用最新的React Native初始化方式
        reactInstanceManager.addReactInstanceEventListener(
            new ReactInstanceManager.ReactInstanceEventListener() {
                @Override
                public void onReactContextInitialized(ReactContext context) {
                    Log.d("MainActivity", "React Native初始化完成");
                    Log.d("MainActivity", "ReactContext: " + context.toString());
                    Log.d("MainActivity", "JS Bundle加载状态: " + context.hasActiveCatalystInstance());
                    Log.d("MainActivity", "Native模块数量: " + context.getNativeModules().size());
                    
                    // 确保UI在主线程更新
                    runOnUiThread(() -> {
                        ReactRootView rootView = new ReactRootView(MainActivity.this);
                        rootView.startReactApplication(
                            getReactNativeHost().getReactInstanceManager(),
                            getMainComponentName()
                        );
                        setContentView(rootView);
                    });
                }
            }
        );
    }
}