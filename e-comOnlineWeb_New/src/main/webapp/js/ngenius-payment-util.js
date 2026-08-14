/**
 * N-Genius Hosted Payment Page Utility
 *
 * Mirrors stripe-payment-util.js structure.
 * Posts to /secureNGeniusPayment.do with the same JSON payload shape.
 * On success, redirects to the N-Genius hosted pay-page URL.
 *
 * Usage:
 *   NGeniusPaymentUtil.processPayment({
 *       paymentType:  'BKG',    // BKG | BL | INV
 *       customerType: 'SH',
 *       totalAmount:  500.00,
 *       references:   [{ refNo: 'BKG001', amount: 500.00 }]
 *   });
 */
var NGeniusPaymentUtil = (function () {
    'use strict';

    var TAG = '[NGeniusPaymentUtil]';

    /**
     * Main entry point — validates, checks CSRF token, shows loader, posts to backend.
     */
    function processPayment(paymentData) {
        try {
            console.log(TAG + ' Processing payment:', paymentData);

            var validationError = validatePaymentData(paymentData);
            if (validationError) {
                alert(validationError);
                return;
            }

            if (!window.ngeniusCsrfToken || window.ngeniusCsrfToken.trim() === '') {
                alert('Security error. Please refresh the page.');
                return;
            }

            showLoader();
            makeSecurePaymentRequest(paymentData);

        } catch (e) {
            console.error(TAG + ' Error processing payment:', e.message);
            alert('Error processing payment request. Please try again.');
            hideLoader();
        }
    }

    function validatePaymentData(data) {
        if (!data) return 'Payment data is required';

        if (!data.paymentType || ['BKG', 'BL', 'INV'].indexOf(data.paymentType) === -1) {
            return 'Invalid payment type';
        }

        if (!data.totalAmount || data.totalAmount <= 0) {
            return 'Invalid payment amount';
        }

        if (!data.references || !Array.isArray(data.references) || data.references.length === 0) {
            return 'Please select items to pay';
        }

        for (var i = 0; i < data.references.length; i++) {
            var ref = data.references[i];
            if (!ref.refNo || (typeof ref.refNo === 'string' && ref.refNo.trim() === '')) {
                return 'Invalid reference data: missing reference number';
            }
            var amt = parseFloat(ref.amount);
            ref.amount = isNaN(amt) ? 0 : amt;
        }

        return null;
    }

    function makeSecurePaymentRequest(paymentData) {
        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'secureNGeniusPayment.do', true);

        xhr.setRequestHeader('Content-Type',       'application/json');
        xhr.setRequestHeader('X-Requested-With',   'XMLHttpRequest');
        xhr.setRequestHeader('X-NGENIUS-CSRF-Token', window.ngeniusCsrfToken);

        xhr.timeout = 30000;

        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                hideLoader();

                if (xhr.status === 200) {
                    handleSuccessResponse(xhr.responseText);
                } else if (xhr.status === 403) {
                    alert('Session expired. Please refresh the page.');
                    window.location.reload();
                } else {
                    console.error(TAG + ' Payment request failed, HTTP=' + xhr.status);
                    alert('Payment request failed. Please try again.');
                }
            }
        };

        xhr.onerror = function () {
            hideLoader();
            console.error(TAG + ' Network error');
            alert('Network error. Please check your connection and try again.');
        };

        xhr.ontimeout = function () {
            hideLoader();
            console.error(TAG + ' Request timed out');
            alert('Request timed out. Please try again.');
        };

        xhr.send(JSON.stringify(paymentData));
        console.log(TAG + ' Request sent for type:', paymentData.paymentType);
    }

    function handleSuccessResponse(responseText) {
        try {
            var response = JSON.parse(responseText);

            if (response.success === true) {
                var url = response.ngeniusOrderUrl;
                // covers both live (paypage.ngenius-payments.com) and sandbox (paypage.sandbox.ngenius-payments.com)
                if (url && typeof url === 'string' && url.indexOf('https://paypage.') === 0) {
                    console.log(TAG + ' Redirecting to N-Genius pay page');
                    window.location.href = url;
                } else {
                    console.error(TAG + ' Invalid redirect URL received:', url);
                    alert('Payment setup error. Please try again.');
                }
            } else {
                alert(response.message || 'Payment setup failed. Please try again.');
            }

        } catch (e) {
            console.error(TAG + ' Error parsing response:', e.message);
            alert('Error processing payment response. Please try again.');
        }
    }

    function showLoader() {
        if (typeof openLoader === 'function') openLoader();
    }

    function hideLoader() {
        if (typeof closeLoader === 'function') closeLoader();
    }

    function initializeCSRFToken(token) {
        window.ngeniusCsrfToken = token;
        if (!token || token.trim() === '') {
            console.error(TAG + ' CSRF token not available — security error');
        } else {
            console.log(TAG + ' CSRF token initialized');
        }
    }

    return {
        processPayment:      processPayment,
        initializeCSRFToken: initializeCSRFToken
    };
})();
