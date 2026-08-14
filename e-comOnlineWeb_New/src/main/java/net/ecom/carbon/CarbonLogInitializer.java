package net.ecom.carbon;

import java.io.File;
import javax.servlet.ServletContext;
import net.ecom.common.Validation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * Applies master-config values as JVM system properties and reloads {@code log4j2.xml}
 * once after {@link net.ecom.controller.EcomCacheLoader} finishes (single init, no per-request cost).
 * <p>
 * Only {@link CarbonApiLogger#ECOM_CARBON_LOG_ROLLING_PERIOD} is configured; the roll interval is
 * derived automatically (MINUTE=5, HOUR=1, DAY=1, MONTH=30).
 */
public final class CarbonLogInitializer {

    /** Internal system property consumed by {@code log4j2.xml}; not a master-config key. */
    static final String ROLLING_INTERVAL_SYS_PROP = "ECOM_CARBON_LOG_ROLLING_INTERVAL";

    private static final Object INIT_LOCK = new Object();
    private static volatile boolean initialized;

    private static final String DEFAULT_PERIOD = "MINUTE";

    private CarbonLogInitializer() {
    }

    /**
     * Idempotent. Call from {@code EcomCacheLoader} after servlet context attributes are populated.
     */
    public static void initialize(ServletContext ctx) {
        if (ctx == null) {
            return;
        }
        synchronized (INIT_LOCK) {
            if (initialized) {
                return;
            }
            try {
                String baseDir = resolveBaseDir(ctx);
                if (Validation.checkNull(baseDir).isEmpty()) {
                    return;
                }
                String period = resolveConfigValue(ctx, CarbonApiLogger.ECOM_CARBON_LOG_ROLLING_PERIOD, DEFAULT_PERIOD);
                int interval = defaultIntervalForPeriod(period);

                System.setProperty(CarbonApiLogger.ECOM_LOG_BASE_PATH_KEY, baseDir);
                System.setProperty("ECOM_CARBON_LOG_FILE_PATTERN", filePatternForPeriod(period));
                System.setProperty(ROLLING_INTERVAL_SYS_PROP, String.valueOf(interval));

                LoggerContext loggerContext = (LoggerContext) LogManager.getContext(
                        CarbonLogInitializer.class.getClassLoader(), false);
                loggerContext.reconfigure();
                initialized = true;
            } catch (Exception ex) {
                System.err.println("[ECOM-WAR] CarbonLogInitializer failed: " + ex.getMessage());
            }
        }
    }

    static boolean isInitialized() {
        return initialized;
    }

    /**
     * Default roll interval for {@link org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy}.
     */
    static int defaultIntervalForPeriod(String periodRaw) {
        if (periodRaw == null) {
            return 5;
        }
        switch (periodRaw.trim().toUpperCase()) {
            case "HOUR":
                return 1;
            case "DAY":
                return 1;
            case "MONTH":
                return 30;
            case "MINUTE":
            default:
                return 5;
        }
    }

    private static String filePatternForPeriod(String periodRaw) {
        if (periodRaw == null) {
            return "%d{yyyy-MM-dd-HH-mm}";
        }
        switch (periodRaw.trim().toUpperCase()) {
            case "HOUR":
                return "%d{yyyy-MM-dd-HH}";
            case "DAY":
                return "%d{yyyy-MM-dd}";
            case "MONTH":
                return "%d{yyyy-MM}";
            case "MINUTE":
            default:
                return "%d{yyyy-MM-dd-HH-mm}";
        }
    }

    private static String resolveConfigValue(ServletContext ctx, String key, String defaultValue) {
        Object attr = ctx.getAttribute(key);
        if (attr instanceof String) {
            String trimmed = ((String) attr).trim();
            if (!Validation.checkNull(trimmed).isEmpty()) {
                return trimmed;
            }
        }
        String init = ctx.getInitParameter(key);
        if (init != null && init.trim().length() > 0) {
            return init.trim();
        }
        String fromJvm = firstNonEmpty(System.getProperty(key), System.getenv(key));
        if (!Validation.checkNull(fromJvm).isEmpty()) {
            return fromJvm;
        }
        return defaultValue;
    }

    private static String resolveBaseDir(ServletContext ctx) {
        String fromContext = resolveConfigValue(ctx, CarbonApiLogger.ECOM_LOG_BASE_PATH_KEY, null);
        if (!Validation.checkNull(fromContext).isEmpty()) {
            return trimTrailingSeparator(fromContext);
        }
        String fromJvm = firstNonEmpty(
                System.getProperty(CarbonApiLogger.ECOM_LOG_BASE_PATH_KEY),
                System.getProperty("jboss.server.log.dir"),
                System.getenv(CarbonApiLogger.ECOM_LOG_BASE_PATH_KEY));
        if (!Validation.checkNull(fromJvm).isEmpty()) {
            return trimTrailingSeparator(fromJvm.trim());
        }
        return trimTrailingSeparator(System.getProperty("java.io.tmpdir"));
    }

    private static String firstNonEmpty(String... values) {
        if (values == null) {
            return null;
        }
        for (String v : values) {
            if (v != null && v.trim().length() > 0) {
                return v.trim();
            }
        }
        return null;
    }

    private static String trimTrailingSeparator(String path) {
        if (path == null || path.length() == 0) {
            return "";
        }
        if (path.endsWith(File.separator)) {
            return path.substring(0, path.length() - File.separator.length());
        }
        return path;
    }
}
