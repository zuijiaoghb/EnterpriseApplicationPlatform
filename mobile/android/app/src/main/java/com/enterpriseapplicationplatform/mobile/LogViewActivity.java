package com.enterpriseapplicationplatform.mobile;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LogViewActivity extends AppCompatActivity {
    private TextView logTextView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_view);
        
        logTextView = findViewById(R.id.log_text_view);
        
        String logFilePath = getIntent().getStringExtra("LOG_FILE_PATH");
        if (logFilePath != null) {
            String logContent = readLogFile(logFilePath);
            logTextView.setText(logContent);
        }
    }
    
    private String readLogFile(String filePath) {
        StringBuilder logContent = new StringBuilder();
        File logFile = new File(filePath);
        
        if (logFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logContent.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return logContent.toString();
    }
}