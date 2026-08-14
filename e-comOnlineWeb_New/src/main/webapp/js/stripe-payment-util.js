/**
 * Common Stripe Payment Utility
 * 
 * Features:
 * - Reusable across all payment types (Booking, BL, Invoice)
 * - Secure CSRF protection
 * - Clean error handling
 * - Easy maintenance
 * - Production-safe logging
 * 
 * Usage:
 * StripePaymentUtil.processPayment({
 *     paymentType: 'INV', // BKG, BL, INV
 *     customerType: 'SH',
 *     totalAmount: 1500.00,
 *     references: [{refNo: 'INV001', amount: 1500.00}]
 * });
 * 
 * @author E-Commerce System
 * @version 1.0
 */
var StripePaymentUtil = (function() {
    'use strict';
    
    var TAG = '[StripePaymentUtil]';
    
    /**
     * Process payment with Stripe
     * @param {Object} paymentData - Payment request data
     * @param {string} paymentData.paymentType - Payment type (BKG, BL, INV)
     * @param {string} paymentData.customerType - Customer type (SH, CN, etc.)
     * @param {number} paymentData.totalAmount - Total payment amount
     * @param {Array} paymentData.references - Array of {refNo, amount} objects
     */
    function processPayment(paymentData) {
        try {
            console.log(TAG + ' Processing payment with data:', paymentData);
            // 1. Validate input
            var validationError = validatePaymentData(paymentData);
            if (validationError) {
                alert(validationError);
                return;
            }
            
            // 2. Check CSRF token
            if (!window.csrfToken || window.csrfToken.trim() === '') {
                alert('Security error. Please refresh the page.');
                return;
            }
            
            // 3. Show loading
            showLoader();
            
            // 4. Make secure AJAX call
            makeSecurePaymentRequest(paymentData);
            
        } catch (e) {
            console.error(TAG + ' Error processing payment:', e.message);
            alert('Error processing payment request. Please try again.');
            hideLoader();
        }
    }
    
    /**
     * Validate payment data
     */
    function validatePaymentData(data) {
        if (!data) {
            return 'Payment data is required';
        }
        
        if (!data.paymentType || !['BKG', 'BL', 'INV'].includes(data.paymentType)) {
            return 'Invalid payment type';
        }
        
        if (!data.totalAmount || data.totalAmount <= 0) {
            return 'Invalid payment amount';
        }
        
        if (!data.references || !Array.isArray(data.references) || data.references.length === 0) {
            return 'Please select items to pay';
        }
        
        // Validate each reference
        console.log(TAG + ' Validating references:', JSON.stringify(data.references));
        for (var i = 0; i < data.references.length; i++) {
            var ref = data.references[i];
            if (!ref.refNo || (typeof ref.refNo === 'string' && ref.refNo.trim() === '')) {
                return 'Invalid reference data: missing reference number';
            }
            var amt = parseFloat(ref.amount);
            if (isNaN(amt)) amt = 0;
            ref.amount = amt;
        }
        
        return null; // Valid
    }
    
    /**
     * Make secure AJAX request to backend
     */
    function makeSecurePaymentRequest(paymentData) {
        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'secureStripePayment.do', true);
        
        // Security headers
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
        xhr.setRequestHeader('X-CSRF-Token', window.csrfToken);
        
        // Timeout protection
        xhr.timeout = 30000; // 30 seconds
        
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                hideLoader();
                
                if (xhr.status === 200) {
                    handleSuccessResponse(xhr.responseText);
                } else if (xhr.status === 403) {
                    alert('Session expired. Please refresh the page.');
                    window.location.reload();
                } else {
                    console.error(TAG + ' Payment request failed with status:', xhr.status);
                    alert('Payment request failed. Please try again.');
                }
            }
        };
        
        xhr.onerror = function() {
            hideLoader();
            console.error(TAG + ' Network error during payment request');
            alert('Network error. Please check your connection and try again.');
        };
        
        xhr.ontimeout = function() {
            hideLoader();
            console.error(TAG + ' Payment request timed out');
            alert('Request timed out. Please try again.');
        };
        
        // Send payment data
        xhr.send(JSON.stringify(paymentData));
        
        console.log(TAG + ' Payment request sent for type:', paymentData.paymentType);
    }
    
    /**
     * Handle successful response from backend
     */
    function handleSuccessResponse(responseText) {
        try {
            console.log(TAG + ' Payment response received');
            
            var response = JSON.parse(responseText);
            
            if (response.success === true) {
                // Validate Stripe URL for security
                if (response.stripeSessionUrl && 
                    typeof response.stripeSessionUrl === 'string' &&
                    response.stripeSessionUrl.startsWith('https://checkout.stripe.com/')) {
                    
                    console.log(TAG + ' Redirecting to Stripe Checkout');
                    window.location.href = response.stripeSessionUrl;
                } else {
                    console.error(TAG + ' Invalid redirect URL received');
                    alert('Payment setup error. Please try again.');
                }
            } else {
                alert(response.message || 'Payment setup failed. Please try again.');
            }
            
        } catch (e) {
            console.error(TAG + ' Error parsing payment response');
            alert('Error processing payment response. Please try again.');
        }
    }
    
    /**
     * Show loading indicator
     */
    function showLoader() {
        if (typeof openLoader === 'function') {
            openLoader();
        }
    }
    
    /**
     * Hide loading indicator
     */
    function hideLoader() {
        if (typeof closeLoader === 'function') {
            closeLoader();
        }
    }
    
    /**
     * Initialize CSRF token from page
     * Call this when page loads
     */
    function initializeCSRFToken(token) {
        window.csrfToken = token;
        
        if (!token || token.trim() === '') {
            console.error(TAG + ' CSRF token not available - security error');
            alert('Security error. Please refresh the page.');
        } else {
            console.log(TAG + ' CSRF token initialized');
        }
    }
    
    // Public API
    return {
        processPayment: processPayment,
        initializeCSRFToken: initializeCSRFToken
    };
})();

/**
 * Auto-initialize CSRF token if available
 */
if (typeof csrfToken !== 'undefined') {
    StripePaymentUtil.initializeCSRFToken(csrfToken);
}