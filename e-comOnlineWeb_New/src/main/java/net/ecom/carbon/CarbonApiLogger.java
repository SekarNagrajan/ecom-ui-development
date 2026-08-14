package net.ecom.carbon;

import javax.servlet.ServletContext;
import org.apache.logging.log4j.LogManager;

/**
 * Writes Carbon / Climatiq API diagnostics to {@code {log base}/carbonAPI/carbon-api.log}
 * using Log4j2 rolling configuration from {@code log4j2.xml} (not server stdout).
 * <p>
 * Rolling is configured once at cache startup via {@link CarbonLogInitializer}.
 * Set {@value #ECOM_CARBON_LOG_ROLLING_PERIOD} only ({@code MINUTE}, {@code HOUR}, {@code DAY},
 * {@code MONTH}); the roll interval is assigned automatically from that period.
 */
public final class CarbonApiLogger {

    private static final String LOGGER_NAME = "net.ecom.carbon.CarbonApi";

    /** Master-config / context / JVM / env key for the parent log directory (must match {@code EcomCacheLoader}). */
    public static final String ECOM_LOG_BASE_PATH_KEY = "ECOM_LOG_BASE_PATH";

    /**
     * Rolling granularity: {@code MINUTE}, {@code HOUR}, {@code DAY}, or {@code MONTH}.
     * Default intervals: MINUTE=5, HOUR=1, DAY=1, MONTH=30 (see {@link CarbonLogInitializer}).
     */
    public static final String ECOM_CARBON_LOG_ROLLING_PERIOD = "ECOM_CARBON_LOG_ROLLING_PERIOD";

    private CarbonApiLogger() {
    }

    /**
     * @param ctx  servlet context (used only if cache init did not run; normally a no-op)
     * @param line log line; use the same {@code [ECOM-WAR] ...} prefix as other WAR diagnostics for consistency
     */
    public static void log(ServletContext ctx, String line) {
        if (!CarbonLogInitializer.isInitialized() && ctx != null) {
            CarbonLogInitializer.initialize(ctx);
        }
        LogManager.getLogger(LOGGER_NAME).info(line == null ? "" : line);
    }
}
