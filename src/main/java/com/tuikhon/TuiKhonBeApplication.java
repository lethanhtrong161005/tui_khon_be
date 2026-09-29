package com.tuikhon;

import com.tuikhon.constant.AppConstant;
import com.tuikhon.util.DotenvUtils;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Tui Khon backend application.
 * Automatically sets JVM default timezone to UTC and loads local .env files if present.
 */
@SpringBootApplication
public class TuiKhonBeApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(AppConstant.DEFAULT_TIMEZONE));
        DotenvUtils.loadEnvFile();
        SpringApplication.run(TuiKhonBeApplication.class, args);
    }
}
