/*
 * Common utility for config-based System.out logging.
 *
 * Three levels:
 *   trace()        - ALWAYS prints. Key flow milestones (entry/exit, status changes, results).
 *   log()          - Prints only when GeneralConf.Enable_Sout_Log is true. Detailed/debug data.
 *   logException() - ALWAYS prints. Use in catch blocks for errors.
 */
package net.ecom.common;

public final class LogUtil {

    private LogUtil() {
    }

    // ---- trace: always on, essential flow points ----

    public static void trace(String message) {
        System.out.println("[ECOM] " + message);
    }

    public static void trace(String prefix, String message) {
        System.out.println("[ECOM] " + prefix + " " + message);
    }

    // ---- log: config-gated detailed/debug logging ----

    public static void log(String message) {
        if (GeneralConf.Enable_Sout_Log) {
            System.out.println("[ECOM] " + message);
        }
    }

    public static void log(String prefix, String message) {
        if (GeneralConf.Enable_Sout_Log) {
            System.out.println("[ECOM] " + prefix + " " + message);
        }
    }

    // ---- logException: always on, error/catch blocks ----

    public static void logException(String message) {
        System.err.println("[ECOM-ERR] " + message);
    }

    public static void logException(String prefix, Throwable t) {
        System.err.println("[ECOM-ERR] " + prefix + ": " + (t != null ? t.getMessage() : "null"));
        if (t != null) {
            t.printStackTrace();
        }
    }
}
