package net.ecom.common;

import javax.servlet.ServletContext;
import net.ecom.AppTierCntrlBean.EComCntrlBeanRemote;
import net.ecom.lookup.EJBLookUp;

/**
 * Payment email notifications:
 * <ul>
 *   <li>Customer success/failure — sent when the gateway plugin is enabled
 *       ({@link GeneralConf#Enable_Stripe_Payment} / {@link GeneralConf#Enable_Ngenius_Payment})
 *       and a customer email is available.</li>
 *   <li>{@link GeneralConf#Payment_Event_Notification_Email} — ops inbox for
 *       FAILED and ERROR events only.</li>
 * </ul>
 */
public class PaymentEventNotificationMail {

    private static final String TAG = "[PaymentEventNotification]";
    private static final String INITIATOR_ID = "ECOM-BKG";

    private PaymentEventNotificationMail() {
    }

    /**
     * Immediate receipt ping — no longer used for ops inbox.
     * {@link GeneralConf#Payment_Event_Notification_Email} receives FAILED/ERROR only.
     */
    public static void notifyReceived(ServletContext context, String subject, String body) {
        LogUtil.trace(TAG, "notifyReceived skipped — ops mail is FAILED/ERROR only");
    }

    /**
     * Ops notification to {@link GeneralConf#Payment_Event_Notification_Email}.
     * Sends only when status is FAILED or ERROR.
     */
    public static void notifyIfConfigured(ServletContext context,
                                          String gateway,
                                          String eventType,
                                          String status,
                                          String reference,
                                          String additionalInfo) {
        try {
            if (context == null) {
                return;
            }

            if (!isOpsFailureOrError(status)) {
                LogUtil.trace(TAG, "Ops mail skipped for status=" + status
                        + " (only FAILED/ERROR go to Payment_Event_Notification_Email)");
                return;
            }

            String toEmail = (String) context.getAttribute(GeneralConf.Payment_Event_Notification_Email);
            if (toEmail == null || toEmail.trim().isEmpty()) {
                LogUtil.trace(TAG, "No Payment_Event_Notification_Email configured — skipping notification");
                return;
            }
            toEmail = toEmail.trim();

            String fromMail = (String) context.getAttribute(GeneralConf.clientfrommail);
            String clientCode = (String) context.getAttribute("client_code");
            if (fromMail == null || fromMail.trim().isEmpty()) {
                LogUtil.logException(TAG + " clientfrommail not configured — cannot send notification");
                return;
            }

            String subject = "Payment Webhook [" + nullSafe(gateway) + "] - "
                    + nullSafe(status) + " / " + nullSafe(eventType);

            StringBuilder body = new StringBuilder();
            body.append("Payment webhook event notification\n\n");
            body.append("Gateway   : ").append(nullSafe(gateway)).append("\n");
            body.append("Event     : ").append(nullSafe(eventType)).append("\n");
            body.append("Status    : ").append(nullSafe(status)).append("\n");
            body.append("Reference : ").append(nullSafe(reference)).append("\n");
            if (additionalInfo != null && !additionalInfo.trim().isEmpty()) {
                body.append("Details   : ").append(additionalInfo.trim()).append("\n");
            }

            Mailer mail = new Mailer();
            mail.setTo(toEmail);
            mail.setSubject(subject);
            mail.setMessage(body.toString());

            EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
            ecomCntrlObj.sendMailFromEjb(mail, fromMail, INITIATOR_ID, clientCode);

            LogUtil.trace(TAG, "Ops notification sent -> gateway=" + gateway
                    + ", event=" + eventType + ", status=" + status + ", to=" + toEmail);

        } catch (Exception e) {
            LogUtil.logException(TAG + " Failed to send webhook notification", e);
        }
    }

    /**
     * Customer success email when the payment plugin is enabled.
     */
    public static void notifyCustomerPaymentSuccess(ServletContext context,
                                                    String customerEmail,
                                                    String gateway,
                                                    String reference,
                                                    String amountInfo) {
        notifyCustomer(context, customerEmail, gateway, reference, amountInfo, true);
    }

    /**
     * Customer failure/decline email when the payment plugin is enabled.
     */
    public static void notifyCustomerPaymentFailed(ServletContext context,
                                                   String customerEmail,
                                                   String gateway,
                                                   String reference,
                                                   String failureReason) {
        notifyCustomer(context, customerEmail, gateway, reference, failureReason, false);
    }

    private static void notifyCustomer(ServletContext context,
                                       String customerEmail,
                                       String gateway,
                                       String reference,
                                       String detail,
                                       boolean success) {
        try {
            if (context == null) {
                return;
            }
            if (!isPaymentPluginEnabled(context, gateway)) {
                LogUtil.trace(TAG, "Customer mail skipped — plugin not enabled for gateway=" + gateway);
                return;
            }
            if (customerEmail == null || customerEmail.trim().isEmpty()) {
                LogUtil.trace(TAG, "No customer email — skipping customer "
                        + (success ? "success" : "failure") + " notification");
                return;
            }
            customerEmail = customerEmail.trim();

            String fromMail = (String) context.getAttribute(GeneralConf.clientfrommail);
            String clientCode = (String) context.getAttribute("client_code");
            if (fromMail == null || fromMail.trim().isEmpty()) {
                LogUtil.logException(TAG + " clientfrommail not configured — cannot send customer mail");
                return;
            }

            String subject;
            StringBuilder body = new StringBuilder();
            if (success) {
                subject = "Payment successful [" + nullSafe(gateway) + "]";
                body.append("Your online payment was completed successfully.\n\n");
                body.append("Gateway   : ").append(nullSafe(gateway)).append("\n");
                body.append("Reference : ").append(nullSafe(reference)).append("\n");
                if (detail != null && !detail.trim().isEmpty()) {
                    body.append("Details   : ").append(detail.trim()).append("\n");
                }
                body.append("\nThank you for your payment.\n");
            } else {
                subject = "Payment unsuccessful [" + nullSafe(gateway) + "]";
                body.append("Your online payment could not be completed.\n\n");
                body.append("Gateway   : ").append(nullSafe(gateway)).append("\n");
                body.append("Reference : ").append(nullSafe(reference)).append("\n");
                body.append("Reason    : ").append(nullSafe(detail)).append("\n\n");
                body.append("Please try again with a different payment method, or contact support if the problem continues.\n");
            }

            Mailer mail = new Mailer();
            mail.setTo(customerEmail);
            mail.setSubject(subject);
            mail.setMessage(body.toString());

            EComCntrlBeanRemote ecomCntrlObj = EJBLookUp.getEcomCntrlBeanLookUp();
            ecomCntrlObj.sendMailFromEjb(mail, fromMail, INITIATOR_ID, clientCode);

            LogUtil.trace(TAG, "Customer " + (success ? "success" : "failure")
                    + " mail sent -> gateway=" + gateway
                    + ", reference=" + reference + ", to=" + customerEmail);

        } catch (Exception e) {
            LogUtil.logException(TAG + " Failed to send customer "
                    + (success ? "success" : "failure") + " notification", e);
        }
    }

    /** Ops inbox only for FAILED / ERROR. */
    public static boolean isOpsFailureOrError(String status) {
        if (status == null) {
            return false;
        }
        String s = status.trim().toUpperCase();
        return "FAILED".equals(s) || "ERROR".equals(s);
    }

    /**
     * Customer emails require the corresponding payment plugin to be enabled (Y).
     */
    public static boolean isPaymentPluginEnabled(ServletContext context, String gateway) {
        if (context == null || gateway == null) {
            return false;
        }
        String g = gateway.trim();
        String flag = null;
        if ("Stripe".equalsIgnoreCase(g)) {
            flag = (String) context.getAttribute(GeneralConf.Enable_Stripe_Payment);
        } else if ("NGenius".equalsIgnoreCase(g) || "N-Genius".equalsIgnoreCase(g)) {
            flag = (String) context.getAttribute(GeneralConf.Enable_Ngenius_Payment);
        } else {
            // Unknown gateway — allow if either payment plugin is on
            String stripe = (String) context.getAttribute(GeneralConf.Enable_Stripe_Payment);
            String ngenius = (String) context.getAttribute(GeneralConf.Enable_Ngenius_Payment);
            return "Y".equalsIgnoreCase(stripe) || "Y".equalsIgnoreCase(ngenius);
        }
        return "Y".equalsIgnoreCase(flag);
    }

    private static String nullSafe(String value) {
        return value == null || value.trim().isEmpty() ? "-" : value.trim();
    }
}
