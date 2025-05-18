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

public class MainActivity extends ReactActivity {
    private UncaughtExceptionHandler defaultExceptionHandler;
    
    @Override
    protected String getMainComponentName() {
        return "EnterpriseApplicationPlatform";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置全局异常处理器
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e("MainActivity", "未捕获异常: " + ex.getMessage(), ex);
                defaultExceptionHandler.uncaughtException(thread, ex);
            }
        });
        
        if (BuildConfig.DEBUG) {
            Log.d("MainActivity", "初始化React Native环境");
            Log.d("MainActivity", "当前线程: " + Thread.currentThread().getName());
        }
        super.onCreate(savedInstanceState);
        
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