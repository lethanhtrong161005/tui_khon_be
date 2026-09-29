package com.tuikhon.util;

import com.tuikhon.constant.AppConstant;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for loading environment variables from local .env files into JVM System properties.
 */
public final class DotenvUtils {

    private static final Logger logger = LoggerFactory.getLogger(DotenvUtils.class);

    private DotenvUtils() {
        // Utility class
    }

    /**
     * Automatically loads environment variables from .env.local, .env.dev, or .env files
     * into JVM System properties if not already defined in system environment or System properties.
     */
    public static void loadEnvFile() {
        String profile = System.getenv(AppConstant.APP_PROFILE_KEY);
        if (Objects.isNull(profile) || profile.isBlank()) {
            profile = System.getProperty(AppConstant.APP_PROFILE_KEY, AppConstant.PROFILE_LOCAL);
        }

        Path envPath = Paths.get(".env." + profile);
        if (!Files.exists(envPath)) {
            envPath = Paths.get(".env." + AppConstant.PROFILE_LOCAL);
        }
        if (!Files.exists(envPath)) {
            envPath = Paths.get(".env");
        }

        if (Files.exists(envPath)) {
            logger.info("Loading environment variables from: {}", envPath.toAbsolutePath());
            try {
                List<String> lines = Files.readAllLines(envPath);
                for (String line : lines) {
                    String trimmedLine = line.trim();
                    if (trimmedLine.isEmpty() || trimmedLine.startsWith("#") || !trimmedLine.contains("=")) {
                        continue;
                    }
                    int eqIndex = trimmedLine.indexOf("=");
                    String key = trimmedLine.substring(0, eqIndex).trim();
                    String value = trimmedLine.substring(eqIndex + 1).trim();

                    if (Objects.isNull(System.getenv(key)) && Objects.isNull(System.getProperty(key))) {
                        System.setProperty(key, value);
                    }
                }
            } catch (Exception e) {
                logger.warn("Failed to load environment file {}: {}", envPath, e.getMessage());
            }
        }
    }
}
