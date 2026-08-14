package net.ecom.AppTierCntrlBean;

import java.util.List;
import java.util.UUID;
import javax.ejb.Remote;
import net.ecom.vo.OnlinePaymentVO;
import net.ecom.vo.OnlinePaymentReferenceVO;
import net.ecom.vo.OnlinePaymentStripeEventVO;

/**
 * Remote interface for Online Payment EJB operations
 * Handles database operations for Stripe payment processing
 * 
 * @author system
 */
@Remote
public interface EcomOnlinePaymentBeanRemote {
    
    // ========== Online Payment Operations ==========
    
    /**
     * Create a new online payment record
     * @param paymentVO Payment details
     * @return Generated payment ID
     */
    public long createPayment(OnlinePaymentVO paymentVO);
    

    /**
     * Update payment status
     * @param requestId Payment request ID
     * @param status New status
     * @return Number of rows affected
     */
    public int updatePaymentStatus(UUID requestId, String status);
    
    /**
     * Update payment with Stripe session information
     * @param requestId Payment request ID
     * @param stripeSessionId Stripe session ID
     * @param status Payment status
     * @return Number of rows affected
     */
    public int updatePaymentWithStripeSession(UUID requestId, String stripeSessionId, String status);
    
    /**
     * Update payment with Stripe payment completion details
     * @param requestId Payment request ID
     * @param stripePaymentIntentId Stripe payment intent ID
     * @param stripeChargeId Stripe charge ID
     * @param stripeTransactionId Stripe transaction ID
     * @param stripeReceiptUrl Stripe receipt URL
     * @param status Payment status
     * @return Number of rows affected
     */
    public int updatePaymentWithStripeCompletion(UUID requestId, String stripePaymentIntentId, 
                                                String stripeChargeId, String stripeTransactionId, 
                                                String stripeReceiptUrl, String status);
    
    /**
     * Update payment with error information
     * @param requestId Payment request ID
     * @param errorMessage Error message
     * @param status Payment status (usually 'FAILED')
     * @return Number of rows affected
     */
    public int updatePaymentWithError(UUID requestId, String errorMessage, String status);

    /**
     * Update payment charge details by Stripe PaymentIntent ID.
     * Used as a fallback when payment_intent.succeeded arrives after (or instead of)
     * checkout.session.completed.
     * Only fills columns that are currently NULL to avoid overwriting data.
     */
    public int updatePaymentDetailsByPaymentIntent(String stripePaymentIntentId,
                                                   String stripeChargeId,
                                                   String stripeTransactionId,
                                                   String stripeReceiptUrl);

    /**
     * Update payment error/status by Stripe PaymentIntent ID (for failed/canceled intents).
     */
    public int updatePaymentErrorByPaymentIntent(String stripePaymentIntentId,
                                                 String errorMessage, String status);

    /**
     * Get payment by request ID
     * @param requestId Payment request ID
     * @return Payment details or null if not found
     */
    public OnlinePaymentVO getPaymentByRequestId(UUID requestId);
    
    
    // ========== Payment Reference Operations ==========
    
    /**
     * Add multiple payment references
     * @param references List of reference details
     * @return Number of references added
     */
    public int addPaymentReferences(List<OnlinePaymentReferenceVO> references);
    

    /**
     * Insert Stripe webhook event
     * @param eventVO Stripe event details
     * @return Generated event ID
     */
    public long insertStripeEvent(OnlinePaymentStripeEventVO eventVO);

    /**
     * Backfill requestId and stripeSessionId on all StripeEvent rows that share
     * the same stripePaymentIntentId but currently have NULL requestId.
     * Called from checkout.session.completed handler, which is the only event
     * that carries both requestId (metadata) and stripePaymentIntentId.
     *
     * @return Number of rows updated
     */
    public int backfillStripeEventsByPaymentIntent(String stripePaymentIntentId,
                                                   UUID requestId,
                                                   String stripeSessionId);
    
    // ========== LRP Payment Gateway ==========

    /**
     * Insert completed payment details into LRP PaymentGatewaymaster and PaymentGatewaydetail tables.
     * Checks if transID already exists before inserting.
     * @param requestId The payment request ID
     * @return true if inserted (or already exists), false on error
     */
    public boolean insertPaymentGateway(UUID requestId);

    // ========== Payment History ==========

    public java.util.List<net.ecom.vo.OnlinePaymentVO> getPaymentHistoryByCustomer(String customerCode, String fromDate, String toDate);

    public java.util.List<net.ecom.vo.OnlinePaymentReferenceVO> getReferencesByCustomerPayments(String customerCode, String fromDate, String toDate);

    public java.util.List<net.ecom.vo.OnlinePaymentReferenceVO> getReferencesByRequestId(java.util.UUID requestId);


    // ========== Utility Operations ==========
    
    /**
     * Generate new unique request ID
     * @return New UUID for payment request
     */
    public UUID generateRequestId();
    
    /**
     * Convert USD amount to cents
     * @param amountUsd Amount in USD
     * @return Amount in cents
     */
    public long convertToCents(java.math.BigDecimal amountUsd);
    
}