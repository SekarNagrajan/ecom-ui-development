package net.ecom.AppTierCntrlBean;

import javax.ejb.Stateless;
import javax.ejb.Remote;
import javax.annotation.Resources;
import javax.annotation.Resource;
import javax.annotation.PostConstruct;
import javax.ejb.SessionContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.ecom.vo.OnlinePaymentVO;
import net.ecom.vo.OnlinePaymentReferenceVO;
import net.ecom.vo.OnlinePaymentStripeEventVO;

/**
 * EJB Bean for Online Payment database operations
 * Handles Stripe payment processing data persistence
 * Uses ECOMDB datasource
 * 
 * @author system
 */
@Stateless(name = "EcomOnlinePaymentBean")
@Remote(EcomOnlinePaymentBeanRemote.class)
@Resources({
    @Resource(name = "jdbc/ECOMDB", mappedName = "java:/ECOMTXDS"),
    @Resource(name = "jdbc/ACCDB", mappedName = "java:/ACCTXDS")
})
public class EcomOnlinePaymentBean implements EcomOnlinePaymentBeanRemote {
    
    @Resource
    private SessionContext context;

    @PostConstruct
    public void init() {
        System.out.println("EcomOnlinePaymentBean initialized");
    }

    // ========== Online Payment Operations ==========

    @Override
    public long createPayment(OnlinePaymentVO paymentVO) {
        System.out.println("EcomOnlinePaymentBean.createPayment() -> " + paymentVO.getRequestId());
        
        final String sql = "INSERT INTO Ecom_OnlinePayment " +
                          "(requestId, customerCode, loginName, paymentType, currency, amountUsd, amountCents, " +
                          "status, description, agencyCode, createdAt, updatedAt) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";
        
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, paymentVO.getRequestId().toString());
            ps.setString(2, paymentVO.getCustomerCode());
            ps.setString(3, paymentVO.getLoginName());
            ps.setString(4, paymentVO.getPaymentType());
            ps.setString(5, paymentVO.getCurrency());
            ps.setBigDecimal(6, paymentVO.getAmountUsd());
            ps.setLong(7, paymentVO.getAmountCents());
            ps.setString(8, paymentVO.getStatus());
            ps.setString(9, paymentVO.getDescription());
            if (paymentVO.getAgencyCode() != null) {
                ps.setString(10, paymentVO.getAgencyCode());
            } else {
                ps.setNull(10, java.sql.Types.VARCHAR);
            }
            
            int count = ps.executeUpdate();
            if (count > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getLong(1);
                    }
                }
            }
            return 0;
            
        } catch (Exception e) {
            System.err.println("Error creating payment: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updatePaymentStatus(UUID requestId, String status) {
        System.out.println("EcomOnlinePaymentBean.updatePaymentStatus() -> " + requestId + ", " + status);
        
        final String sql = "UPDATE Ecom_OnlinePayment SET status = ?, updatedAt = GETDATE() WHERE requestId = ? AND status != 'COMPLETED'";
        
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setString(2, requestId.toString());
            
            return ps.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("Error updating payment status: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updatePaymentWithStripeSession(UUID requestId, String stripeSessionId, String status) {
        System.out.println("EcomOnlinePaymentBean.updatePaymentWithStripeSession() -> " + requestId);
        
        final String sql = "UPDATE Ecom_OnlinePayment SET stripeSessionId = ?, status = ?, updatedAt = GETDATE() WHERE requestId = ? AND status != 'COMPLETED'";
        
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, stripeSessionId);
            ps.setString(2, status);
            ps.setString(3, requestId.toString());
            
            return ps.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("Error updating payment with Stripe session: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updatePaymentWithStripeCompletion(UUID requestId, String stripePaymentIntentId, 
                                                String stripeChargeId, String stripeTransactionId, 
                                                String stripeReceiptUrl, String status) {
        System.out.println("EcomOnlinePaymentBean.updatePaymentWithStripeCompletion() -> " + requestId);
        
        final String sql = "UPDATE Ecom_OnlinePayment SET " +
                          "stripePaymentIntentId = ?, stripeChargeId = ?, stripeTransactionId = ?, " +
                          "stripeReceiptUrl = ?, status = ?, updatedAt = GETDATE() " +
                          "WHERE requestId = ? AND status != 'COMPLETED'";
        
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            if (stripePaymentIntentId != null) {
                ps.setString(1, stripePaymentIntentId);
            } else {
                ps.setNull(1, Types.VARCHAR);
            }
            if (stripeChargeId != null) {
                ps.setString(2, stripeChargeId);
            } else {
                ps.setNull(2, Types.VARCHAR);
            }
            if (stripeTransactionId != null) {
                ps.setString(3, stripeTransactionId);
            } else {
                ps.setNull(3, Types.VARCHAR);
            }
            if (stripeReceiptUrl != null) {
                ps.setString(4, stripeReceiptUrl);
            } else {
                ps.setNull(4, Types.VARCHAR);
            }
            ps.setString(5, status);
            ps.setString(6, requestId.toString());
            
            int rows = ps.executeUpdate();
            System.out.println("EcomOnlinePaymentBean.updatePaymentWithStripeCompletion() -> rows updated: " + rows);
            return rows;
            
        } catch (Exception e) {
            System.err.println("Error updating payment with Stripe completion: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updatePaymentWithError(UUID requestId, String errorMessage, String status) {
        System.out.println("EcomOnlinePaymentBean.updatePaymentWithError() -> " + requestId);
        
        final String sql = "UPDATE Ecom_OnlinePayment SET errorMessage = ?, status = ?, updatedAt = GETDATE() WHERE requestId = ? AND status != 'COMPLETED'";
        
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, errorMessage);
            ps.setString(2, status);
            ps.setString(3, requestId.toString());
            
            return ps.executeUpdate();
            
        } catch (Exception e) {
            System.err.println("Error updating payment with error: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updatePaymentDetailsByPaymentIntent(String stripePaymentIntentId,
                                                   String stripeChargeId,
                                                   String stripeTransactionId,
                                                   String stripeReceiptUrl) {
        System.out.println("EcomOnlinePaymentBean.updatePaymentDetailsByPaymentIntent() -> PI: " + stripePaymentIntentId);

        final String sql = "UPDATE Ecom_OnlinePayment SET " +
                          "stripeChargeId = COALESCE(stripeChargeId, ?), " +
                          "stripeTransactionId = COALESCE(stripeTransactionId, ?), " +
                          "stripeReceiptUrl = COALESCE(stripeReceiptUrl, ?), " +
                          "updatedAt = GETDATE() " +
                          "WHERE stripePaymentIntentId = ?";

        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (stripeChargeId != null) {
                ps.setString(1, stripeChargeId);
            } else {
                ps.setNull(1, Types.VARCHAR);
            }
            if (stripeTransactionId != null) {
                ps.setString(2, stripeTransactionId);
            } else {
                ps.setNull(2, Types.VARCHAR);
            }
            if (stripeReceiptUrl != null) {
                ps.setString(3, stripeReceiptUrl);
            } else {
                ps.setNull(3, Types.VARCHAR);
            }
            ps.setString(4, stripePaymentIntentId);

            int rows = ps.executeUpdate();
            System.out.println("EcomOnlinePaymentBean.updatePaymentDetailsByPaymentIntent() -> rows updated: " + rows);
            return rows;

        } catch (Exception e) {
            System.err.println("Error updating payment details by PI: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updatePaymentErrorByPaymentIntent(String stripePaymentIntentId,
                                                 String errorMessage, String status) {
        System.out.println("EcomOnlinePaymentBean.updatePaymentErrorByPaymentIntent() -> PI: " + stripePaymentIntentId);

        final String sql = "UPDATE Ecom_OnlinePayment SET " +
                          "errorMessage = ?, status = ?, updatedAt = GETDATE() " +
                          "WHERE stripePaymentIntentId = ? AND status != 'COMPLETED'";

        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, errorMessage);
            ps.setString(2, status);
            ps.setString(3, stripePaymentIntentId);

            int rows = ps.executeUpdate();
            System.out.println("EcomOnlinePaymentBean.updatePaymentErrorByPaymentIntent() -> rows updated: " + rows);
            return rows;

        } catch (Exception e) {
            System.err.println("Error updating payment error by PI: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public OnlinePaymentVO getPaymentByRequestId(UUID requestId) {
        System.out.println("EcomOnlinePaymentBean.getPaymentByRequestId() -> " + requestId);
        
        final String sql = "SELECT id, requestId, customerCode, loginName, paymentType, currency, " +
                          "amountUsd, amountCents, status, stripeSessionId, stripePaymentIntentId, " +
                          "stripeChargeId, stripeTransactionId, stripeReceiptUrl, description, " +
                          "errorMessage, createdAt, updatedAt FROM Ecom_OnlinePayment WHERE requestId = ?";
        
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, requestId.toString());
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPaymentVO(rs);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error getting payment by request ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }




    // ========== Payment Reference Operations ==========


    @Override
    public int addPaymentReferences(List<OnlinePaymentReferenceVO> references) {
        System.out.println("EcomOnlinePaymentBean.addPaymentReferences() -> " + references.size() + " references");
        
        final String sql = "INSERT INTO Ecom_OnlinePayment_Reference " +
                          "(requestId, refType, refNo, custType, amountUsd, amountCents, remarks, createdAt) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE())";
        
        int totalAdded = 0;
        
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            for (OnlinePaymentReferenceVO ref : references) {
                ps.setString(1, ref.getRequestId().toString());
                ps.setString(2, ref.getRefType());
                ps.setString(3, ref.getRefNo());
                ps.setString(4, ref.getCustType());
                ps.setBigDecimal(5, ref.getAmountUsd());
                ps.setLong(6, ref.getAmountCents());
                ps.setString(7, ref.getRemarks());
                ps.addBatch();
            }
            
            int[] results = ps.executeBatch();
            for (int result : results) {
                if (result > 0 || result == Statement.SUCCESS_NO_INFO) totalAdded++;
            }
            
        } catch (Exception e) {
            System.err.println("Error adding payment references: " + e.getMessage());
            e.printStackTrace();
        }
        
        return totalAdded;
    }

    @Override
    public List<OnlinePaymentReferenceVO> getReferencesByRequestId(UUID requestId) {
        System.out.println("EcomOnlinePaymentBean.getReferencesByRequestId() -> " + requestId);

        List<OnlinePaymentReferenceVO> list = new ArrayList<>();
        final String sql = "SELECT id, requestId, refType, refNo, custType, amountUsd, amountCents, " +
                          "remarks, createdAt FROM Ecom_OnlinePayment_Reference WHERE requestId = ? ORDER BY createdAt";

        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, requestId.toString());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToReferenceVO(rs));
                }
            }

        } catch (Exception e) {
            System.err.println("Error getting references by requestId: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    // ========== Stripe Event Operations ==========

    @Override
    public long insertStripeEvent(OnlinePaymentStripeEventVO eventVO) {
        System.out.println("EcomOnlinePaymentBean.insertStripeEvent() -> " + eventVO.getEventId());
        
        final String sql = "INSERT INTO Ecom_OnlinePayment_StripeEvent " +
                          "(eventId, eventType, requestId, stripeSessionId, stripePaymentIntentId, " +
                          "failureCode, declineCode, failureMessage, failureType, paymentStatus, receivedAt) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
        
        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, eventVO.getEventId());
            ps.setString(2, eventVO.getEventType());
            
            if (eventVO.getRequestId() != null) {
                ps.setString(3, eventVO.getRequestId().toString());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }

            if (eventVO.getStripeSessionId() != null) {
                ps.setString(4, eventVO.getStripeSessionId());
            } else {
                ps.setNull(4, Types.VARCHAR);
            }

            if (eventVO.getStripePaymentIntentId() != null) {
                ps.setString(5, eventVO.getStripePaymentIntentId());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }

            if (eventVO.getFailureCode() != null) {
                ps.setString(6, eventVO.getFailureCode());
            } else {
                ps.setNull(6, Types.VARCHAR);
            }

            if (eventVO.getDeclineCode() != null) {
                ps.setString(7, eventVO.getDeclineCode());
            } else {
                ps.setNull(7, Types.VARCHAR);
            }

            if (eventVO.getFailureMessage() != null) {
                ps.setString(8, eventVO.getFailureMessage());
            } else {
                ps.setNull(8, Types.NVARCHAR);
            }

            if (eventVO.getFailureType() != null) {
                ps.setString(9, eventVO.getFailureType());
            } else {
                ps.setNull(9, Types.VARCHAR);
            }

            if (eventVO.getPaymentStatus() != null) {
                ps.setString(10, eventVO.getPaymentStatus());
            } else {
                ps.setNull(10, Types.VARCHAR);
            }
            
            int count = ps.executeUpdate();
            if (count > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getLong(1);
                    }
                }
            }
            return 0;
            
        } catch (Exception e) {
            System.err.println("Error inserting Stripe event: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int backfillStripeEventsByPaymentIntent(String stripePaymentIntentId,
                                                   UUID requestId,
                                                   String stripeSessionId) {
        System.out.println("EcomOnlinePaymentBean.backfillStripeEventsByPaymentIntent() -> PI: "
                + stripePaymentIntentId + ", requestId: " + requestId);

        final String sql = "UPDATE Ecom_OnlinePayment_StripeEvent SET " +
                          "requestId = ?, stripeSessionId = ? " +
                          "WHERE stripePaymentIntentId = ? AND requestId IS NULL";

        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, requestId.toString());

            if (stripeSessionId != null) {
                ps.setString(2, stripeSessionId);
            } else {
                ps.setNull(2, Types.VARCHAR);
            }

            ps.setString(3, stripePaymentIntentId);

            int rows = ps.executeUpdate();
            System.out.println("EcomOnlinePaymentBean.backfillStripeEventsByPaymentIntent() -> rows updated: " + rows);
            return rows;

        } catch (Exception e) {
            System.err.println("Error backfilling stripe events: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // ========== Utility Operations ==========

    @Override
    public UUID generateRequestId() {
        return UUID.randomUUID();
    }

    @Override
    public long convertToCents(BigDecimal amountUsd) {
        if (amountUsd == null) return 0;
        return amountUsd.multiply(new BigDecimal("100"))
                .setScale(0, java.math.RoundingMode.HALF_UP)
                .longValueExact();
    }


    // ========== Payment History ==========

    @Override
    public List<OnlinePaymentVO> getPaymentHistoryByCustomer(String customerCode, String fromDate, String toDate) {
        System.out.println("EcomOnlinePaymentBean.getPaymentHistoryByCustomer() -> " + customerCode
                + " from=" + fromDate + " to=" + toDate);

        List<OnlinePaymentVO> list = new ArrayList<>();
        final String sql = "SELECT p.id, p.requestId, p.customerCode, p.loginName, p.paymentType, p.currency, " +
                          "p.amountUsd, p.amountCents, p.status, p.stripeSessionId, p.stripePaymentIntentId, " +
                          "p.stripeChargeId, p.stripeTransactionId, p.stripeReceiptUrl, p.description, " +
                          "p.errorMessage, p.createdAt, p.updatedAt " +
                          "FROM Ecom_OnlinePayment p " +
                          "WHERE p.customerCode = ? AND p.createdAt >= ? AND p.createdAt < DATEADD(DAY, 1, ?) " +
                          "ORDER BY COALESCE(p.updatedAt, p.createdAt) DESC, p.createdAt DESC";

        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, customerCode);
            ps.setString(2, fromDate);
            ps.setString(3, toDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToPaymentVO(rs));
                }
            }

        } catch (Exception e) {
            System.err.println("Error getting payment history: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("EcomOnlinePaymentBean.getPaymentHistoryByCustomer() -> found " + list.size() + " records");
        return list;
    }

    @Override
    public List<OnlinePaymentReferenceVO> getReferencesByCustomerPayments(String customerCode, String fromDate, String toDate) {
        System.out.println("EcomOnlinePaymentBean.getReferencesByCustomerPayments() -> " + customerCode);

        List<OnlinePaymentReferenceVO> list = new ArrayList<>();
        final String sql = "SELECT r.id, r.requestId, r.refType, r.refNo, r.custType, " +
                          "r.amountUsd, r.amountCents, r.remarks, r.createdAt " +
                          "FROM Ecom_OnlinePayment_Reference r " +
                          "INNER JOIN Ecom_OnlinePayment p ON r.requestId = p.requestId " +
                          "WHERE p.customerCode = ? AND p.createdAt >= ? AND p.createdAt < DATEADD(DAY, 1, ?) " +
                          "ORDER BY r.createdAt DESC";

        try (Connection con = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, customerCode);
            ps.setString(2, fromDate);
            ps.setString(3, toDate);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToReferenceVO(rs));
                }
            }

        } catch (Exception e) {
            System.err.println("Error getting references by customer: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    // ========== LRP Payment Gateway ==========

    @Override
    public boolean insertPaymentGateway(UUID requestId) {
        System.out.println("EcomOnlinePaymentBean.insertPaymentGateway() -> requestId=" + requestId);

        Connection ecomCon = null;
        Connection lrpCon = null;
        try {
            ecomCon = new lrp.serverutils.refs.ServerObject("ECOMDB").getConnection();

            OnlinePaymentVO payment = null;
            final String paymentSql = "SELECT id, requestId, customerCode, loginName, paymentType, currency, " +
                    "amountUsd, amountCents, status, stripeSessionId, stripePaymentIntentId, " +
                    "stripeChargeId, stripeTransactionId, stripeReceiptUrl, description, " +
                    "errorMessage, agencyCode, createdAt, updatedAt " +
                    "FROM Ecom_OnlinePayment(NOLOCK) WHERE requestId = ? AND status = 'COMPLETED'";
            try (PreparedStatement ps = ecomCon.prepareStatement(paymentSql)) {
                ps.setString(1, requestId.toString());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        payment = new OnlinePaymentVO();
                        payment.setRequestId(UUID.fromString(rs.getString("requestId")));
                        payment.setCustomerCode(rs.getString("customerCode"));
                        payment.setLoginName(rs.getString("loginName"));
                        payment.setPaymentType(rs.getString("paymentType"));
                        payment.setCurrency(rs.getString("currency"));
                        payment.setAmountUsd(rs.getBigDecimal("amountUsd"));
                        payment.setStripeTransactionId(rs.getString("stripeTransactionId"));
                        payment.setStripePaymentIntentId(rs.getString("stripePaymentIntentId"));
                        payment.setDescription(rs.getString("description"));
                        payment.setAgencyCode(rs.getString("agencyCode"));
                        Timestamp crTs = rs.getTimestamp("createdAt");
                        if (crTs != null) payment.setCreatedAt(new java.util.Date(crTs.getTime()));
                        Timestamp upTs = rs.getTimestamp("updatedAt");
                        if (upTs != null) payment.setUpdatedAt(new java.util.Date(upTs.getTime()));
                    }
                }
            }

            if (payment == null) {
                System.err.println("EcomOnlinePaymentBean.insertPaymentGateway() -> No COMPLETED payment found for requestId=" + requestId);
                return false;
            }

            // ---------------------------------------------------------------
            // Determine which field to use as transID in PaymentGatewaymaster.
            //
            // Config key  : Payment_TransID_Source  (globalconfigproduct)
            // Value PAYMENT_REF → use stripePaymentIntentId (N-Genius payment
            //                     reference UUID — unique in BOTH sandbox and
            //                     production, avoids duplicate key failures
            //                     during sandbox testing).
            // Value RRN (default) → use stripeTransactionId (bank RRN — correct
            //                     for production reconciliation).
            // ---------------------------------------------------------------
            String transIDSource = readNGeniusConfig(ecomCon, "Payment_TransID_Source");
            boolean usePaymentRef = "PAYMENT_REF".equalsIgnoreCase(transIDSource);

            String transID;
            if (usePaymentRef) {
                transID = payment.getStripePaymentIntentId();
                System.out.println("EcomOnlinePaymentBean.insertPaymentGateway() -> using PAYMENT_REF as transID for requestId=" + requestId);
            } else {
                transID = payment.getStripeTransactionId();
            }

            if (transID == null || transID.trim().isEmpty()) {
                System.err.println("EcomOnlinePaymentBean.insertPaymentGateway() -> No transID available for requestId=" + requestId);
                return false;
            }

            final String checkSql = "SELECT COUNT(1) FROM PaymentGatewaymaster(NOLOCK) WHERE transID = ?";
            try (Connection lrpCheck = new lrp.serverutils.refs.ServerObject("ACCDB").getConnection();
                 PreparedStatement checkPs = lrpCheck.prepareStatement(checkSql)) {
                checkPs.setString(1, transID);
                try (ResultSet checkRs = checkPs.executeQuery()) {
                    if (checkRs.next() && checkRs.getInt(1) > 0) {
                        System.out.println("EcomOnlinePaymentBean.insertPaymentGateway() -> transID already exists, skipping");
                        return true;
                    }
                }
            }

            List<OnlinePaymentReferenceVO> refs = new ArrayList<>();
            final String refSql = "SELECT id, requestId, refType, refNo, custType, amountUsd, amountCents, " +
                    "remarks, createdAt FROM Ecom_OnlinePayment_Reference(NOLOCK) WHERE requestId = ?";
            try (PreparedStatement ps = ecomCon.prepareStatement(refSql)) {
                ps.setString(1, requestId.toString());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        refs.add(mapResultSetToReferenceVO(rs));
                    }
                }
            }

            lrpCon = new lrp.serverutils.refs.ServerObject("ACCDB").getConnection();

            final String masterSql = "INSERT INTO PaymentGatewaymaster " +
                    "(transID, transdate, transstatus, custCode, agencycode, currency, transamt, amountBalance, " +
                    "payMode, paymentRefNo, loginname, remarks, crusr, crdate) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
            long pkid = 0;
            try (PreparedStatement ps = lrpCon.prepareStatement(masterSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, transID);
                ps.setTimestamp(2, payment.getUpdatedAt() != null
                        ? new Timestamp(payment.getUpdatedAt().getTime()) : new Timestamp(System.currentTimeMillis()));
                ps.setString(3, "A");
                ps.setString(4, payment.getCustomerCode());
                if (payment.getAgencyCode() != null) {
                    ps.setString(5, payment.getAgencyCode());
                } else {
                    ps.setNull(5, Types.VARCHAR);
                }
                ps.setString(6, payment.getCurrency() != null ? payment.getCurrency() : "USD");
                ps.setBigDecimal(7, payment.getAmountUsd());
                ps.setBigDecimal(8, payment.getAmountUsd());
                // payMode: determined by which gateway is enabled in globalconfigproduct
                String ngeniusEnabled = readNGeniusConfig(ecomCon, "Enable_Ngenius_Payment");
                String payMode = "Y".equalsIgnoreCase(ngeniusEnabled) ? "NGENIUS" : "STRIPE";
                ps.setString(9, payMode);
                ps.setString(10, transID.length() > 25 ? transID.substring(0, 25) : transID);
                ps.setString(11, payment.getLoginName());
                ps.setString(12, payment.getDescription() != null
                        ? (payment.getDescription().length() > 300 ? payment.getDescription().substring(0, 300) : payment.getDescription())
                        : null);
                ps.setString(13, "ECOM");

                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        pkid = keys.getLong(1);
                    }
                }
            }

            if (pkid > 0 && !refs.isEmpty()) {
                final String detailSql = "INSERT INTO PaymentGatewaydetail (fkid, refType, refNo) VALUES (?, ?, ?)";
                try (PreparedStatement ps = lrpCon.prepareStatement(detailSql)) {
                    for (OnlinePaymentReferenceVO ref : refs) {
                        ps.setLong(1, pkid);
                        ps.setString(2, ref.getRefType());
                        ps.setString(3, ref.getRefNo());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            System.out.println("EcomOnlinePaymentBean.insertPaymentGateway() -> SUCCESS, pkid=" + pkid
                    + ", refs=" + refs.size());

            try {
                createAutoReceipt(transID);
            } catch (Exception re) {
                System.err.println("EcomOnlinePaymentBean.insertPaymentGateway() -> Auto receipt creation failed for transID=" + transID + ": " + re.getMessage());
                re.printStackTrace();
            }

            return true;

        } catch (Exception e) {
            System.err.println("Error inserting PaymentGateway for requestId=" + requestId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (ecomCon != null) {
                try { ecomCon.close(); } catch (Exception ex) { /* ignore */ }
            }
            if (lrpCon != null) {
                try { lrpCon.close(); } catch (Exception ex) { /* ignore */ }
            }
        }
    }

    // ========== N-Genius Config Helper ==========

    /**
     * Reads a single value from globalconfigproduct for N-Genius configuration.
     * Returns null (not empty string) when the key does not exist, so callers
     * can distinguish "not configured" from "configured but blank".
     *
     * @param con  existing ECOMDB connection (not closed here)
     * @param key  the globalconfigproduct key name
     * @return config value, or null if not found
     */
    private String readNGeniusConfig(Connection con, String key) {
        try {
            final String sql = "SELECT pathvalue FROM globalconfigproduct(NOLOCK) WHERE pathname = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, key);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String val = rs.getString(1);
                        return val != null ? val.trim() : null;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("EcomOnlinePaymentBean.readNGeniusConfig() -> error reading key=" + key + ": " + e.getMessage());
        }
        return null;
    }

    // ========== Auto Receipt Creation ==========
    public void createAutoReceipt(String transactionNo) {
        System.out.println("EcomOnlinePaymentBean.createAutoReceipt() -> transactionNo=" + transactionNo);

        final String sql = "{call USP_RECEIPTCREATIONEDI(?)}";

        try (Connection con = new lrp.serverutils.refs.ServerObject("ACCDB").getConnection();
             java.sql.CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, transactionNo);
            cs.execute();

            System.out.println("EcomOnlinePaymentBean.createAutoReceipt() -> Receipt created successfully for transactionNo=" + transactionNo);

        } catch (Exception e) {
            System.err.println("Error creating auto receipt for transactionNo=" + transactionNo + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ========== Private Helper Methods ==========

    private OnlinePaymentVO mapResultSetToPaymentVO(ResultSet rs) throws Exception {
        OnlinePaymentVO vo = new OnlinePaymentVO();
        
        vo.setId(rs.getLong("id"));
        vo.setRequestId(UUID.fromString(rs.getString("requestId")));
        vo.setCustomerCode(rs.getString("customerCode"));
        vo.setLoginName(rs.getString("loginName"));
        vo.setPaymentType(rs.getString("paymentType"));
        vo.setCurrency(rs.getString("currency"));
        vo.setAmountUsd(rs.getBigDecimal("amountUsd"));
        vo.setAmountCents(rs.getLong("amountCents"));
        vo.setStatus(rs.getString("status"));
        vo.setStripeSessionId(rs.getString("stripeSessionId"));
        vo.setStripePaymentIntentId(rs.getString("stripePaymentIntentId"));
        vo.setStripeChargeId(rs.getString("stripeChargeId"));
        vo.setStripeTransactionId(rs.getString("stripeTransactionId"));
        vo.setStripeReceiptUrl(rs.getString("stripeReceiptUrl"));
        vo.setDescription(rs.getString("description"));
        vo.setErrorMessage(rs.getString("errorMessage"));
        
        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) vo.setCreatedAt(new java.util.Date(createdAt.getTime()));
        
        Timestamp updatedAt = rs.getTimestamp("updatedAt");
        if (updatedAt != null) vo.setUpdatedAt(new java.util.Date(updatedAt.getTime()));
        
        return vo;
    }

    private OnlinePaymentReferenceVO mapResultSetToReferenceVO(ResultSet rs) throws Exception {
        OnlinePaymentReferenceVO vo = new OnlinePaymentReferenceVO();
        
        vo.setId(rs.getLong("id"));
        vo.setRequestId(UUID.fromString(rs.getString("requestId")));
        vo.setRefType(rs.getString("refType"));
        vo.setRefNo(rs.getString("refNo"));
        vo.setCustType(rs.getString("custType"));
        vo.setAmountUsd(rs.getBigDecimal("amountUsd"));
        vo.setAmountCents(rs.getLong("amountCents"));
        vo.setRemarks(rs.getString("remarks"));
        
        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) vo.setCreatedAt(new java.util.Date(createdAt.getTime()));
        
        return vo;
    }

}