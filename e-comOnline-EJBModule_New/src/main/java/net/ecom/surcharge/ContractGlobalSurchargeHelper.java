package net.ecom.surcharge;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.ecom.common.GeneralConf;
import net.ecom.common.Validation;

public final class ContractGlobalSurchargeHelper {

    /**
     * Commercial DB. Standalone GWFCMSAuroSurcharge uses
     * {@code datasource.JDBCServiceLocatorFactory}; inside the container the same
     * database is reached through the {@code jdbc/COMDB} resource
     * ({@code java:/COMDS}) already declared on EcomPhase2Bean.
     */
    private static final String SURCHARGE_DB = "COMDB";

    private static final String INSERT_CRA_TMP_SURCHARGE =
            "insert into cratmpSurcharge(sid,LoadPort,LoadTerminal,DischargePort,DischargeTerminal,"
            + "Tranship1,Tranship1Terminal,Tranship2,Tranship2Terminal,Tranship3,Tranship3Terminal,SurchargeDate,EqpType,CommodityCode,DGClass,service,custcode, "
            + "originport, deliveryport, EqpId, weight, pairid,isDg,receipt,delivery,Orgmode,Delmode,soc,nor,oog,ometrotype,ometrocode,dmetrotype,dmetrocode,containerFlag) "
            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private ContractGlobalSurchargeHelper() {
    }

    public static ContractSurchargeCalculationResult calculateSurcharges(List<ContractSurchargeInputVo> rows) {
        ContractSurchargeCalculationResult calculationResult = new ContractSurchargeCalculationResult();
        calculationResult.setChargesByPairId(new HashMap<String, Map<String, ContractSurchargeResultVo>>());
        calculationResult.setMissingFields(ContractSurchargeInputVo.aggregateMissingFields(rows));
        if (rows == null || rows.isEmpty()) {
            return calculationResult;
        }

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sid = generateSid();
        try {
            conn = new lrp.serverutils.refs.ServerObject(SURCHARGE_DB).getConnection();
            if (conn == null) {
                System.err.println("Database connection is null. Check the " + SURCHARGE_DB + " datasource settings.");
                return calculationResult;
            }
            pst = conn.prepareStatement(INSERT_CRA_TMP_SURCHARGE);
            for (ContractSurchargeInputVo row : rows) {
                bindCraTmpSurchargeRow(pst, sid, row);
                pst.addBatch();
            }
            pst.executeBatch();
            closeQuietly(null, pst, rs);
            insertCompanionTables(conn, sid);
            pst = conn.prepareStatement("exec usp_svcGlobalSurchargeMain " + sid);
            boolean hasResults = pst.execute();
            int rsIndex = 0;
            while (hasResults) {
                ResultSet current = pst.getResultSet();
                if (current != null) {
                    parseSurchargeResultSet(calculationResult, current, rsIndex);
                    rsIndex++;
                }
                hasResults = pst.getMoreResults();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(conn, pst, rs);
        }
        return calculationResult;
    }

    private static String generateSid() {
        Random generator = new Random();
        String sid = String.valueOf(generator.nextInt(10000) * generator.nextInt(10000));
        if (sid.length() > 19) {
            sid = sid.substring(0, 19);
        }
        return sid;
    }

    private static void bindCraTmpSurchargeRow(PreparedStatement pst, String sid, ContractSurchargeInputVo row) throws Exception {
        String receipt = Validation.checkNull(row.getReceipt());
        String delivery = Validation.checkNull(row.getDelivery());
        //
        pst.setString(1, sid);
        pst.setString(2, row.getLoadport());
        pst.setString(3, row.getLoadterminal());
        pst.setString(4, row.getDischport());
        pst.setString(5, row.getDischterminal());
        pst.setString(6, row.getTranship1());
        pst.setString(7, row.getTranship1Terminal());
        pst.setString(8, row.getTranship2());
        pst.setString(9, row.getTranship2Terminal());
        pst.setString(10, row.getTranship3());
        pst.setString(11, row.getTranship3Terminal());
        pst.setString(12, row.getSurchargeDate());
        pst.setString(13, row.getEqptype());
        pst.setString(14, row.getCommodityCode());
        pst.setString(15, row.getDgClass());
        pst.setString(16, row.getService());
        pst.setString(17, row.getCustCode());
        pst.setString(18, row.getOriginport());
        pst.setString(19, row.getDeliveryport());
        pst.setString(20, "1");
        pst.setString(21, row.getWeight());
        pst.setString(22, row.getPairId());
        pst.setString(23, row.getIsDg());
        pst.setString(24, receipt);
        pst.setString(25, delivery);
        pst.setString(26, row.getOrgMode());
        pst.setString(27, row.getDelMode());
        pst.setString(28, row.getSoc());
        pst.setString(29, row.getNor());
        pst.setString(30, row.getOog());
        pst.setString(31, row.getOmetrotype());
        pst.setString(32, row.getOmetrocode());
        pst.setString(33, row.getDmetrotype());
        pst.setString(34, row.getDmetrocode());
        pst.setString(35, row.getContainerFlag());
    }

    private static void insertCompanionTables(Connection conn, String sid) throws Exception {
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement("insert into cracomsureqp(pairid,eqptype, sid) select distinct pairid,eqptype, ? from cratmpSurcharge (nolock) where sid = ?");
            pst.setString(1, sid);
            pst.setString(2, sid);
            pst.execute();
            closeQuietly(null, pst, null);

            pst = conn.prepareStatement("insert into cracomsurcomd select sid, commodityCode, pairid, eqpType, eqpid, weight from cratmpSurcharge (nolock) where sid = ?");
            pst.setString(1, sid);
            pst.execute();
            closeQuietly(null, pst, null);

            pst = conn.prepareStatement("insert into cracomsureqp(pairid,eqptype, sid) select distinct pairid,'LP' as eqptype, ? from cratmpSurcharge (nolock) where sid = ?");
            pst.setString(1, sid);
            pst.setString(2, sid);
            pst.execute();
            closeQuietly(null, pst, null);

            pst = conn.prepareStatement("insert into cracomsureqp(pairid,eqptype, sid) select distinct pairid,'TEUS' as eqptype, ? from cratmpSurcharge (nolock) where sid = ?");
            pst.setString(1, sid);
            pst.setString(2, sid);
            pst.execute();
        } finally {
            closeQuietly(null, pst, null);
        }
    }

    /**
     * Routes each result set from usp_svcGlobalSurchargeMain. This SP returns all
     * applicable charge rows in the first result set (index 0); any further result
     * sets are empty. Those index-0 charges are the "result" charges (RS3) used for
     * the all-in calculation, and they also feed chargesByPairId so the existing
     * ApplicableCharges output is preserved. The "setup" charges (RS2) come from
     * usp_ecomContract and are wired in RateContractBean, not here.
     */
    private static void parseSurchargeResultSet(ContractSurchargeCalculationResult calculationResult,
            ResultSet rs, int index) throws Exception {
        Map<String, Map<String, ContractSurchargeResultVo>> byContract = null;
        boolean applyTeus = false;
        if (index == 0) {
            byContract = calculationResult.getResultChargesByContract();
            applyTeus = true;
        }
        parseChargeRows(rs, calculationResult.getChargesByPairId(), byContract, applyTeus, index);
    }

    private static void parseChargeRows(ResultSet rs,
            Map<String, Map<String, ContractSurchargeResultVo>> byPairId,
            Map<String, Map<String, ContractSurchargeResultVo>> byContract,
            boolean applyTeus, int index) throws Exception {
        if (rs == null) {
            return;
        }
        Set<String> cols = columnLabels(rs);
        String previousPairId = null;
        Set<String> seenCharges = new HashSet<String>();
        boolean logEnabled = GeneralConf.Enable_Sout_Log;
        StringBuilder jsonLog = logEnabled ? new StringBuilder("[") : null;
        boolean firstJsonRow = true;
        while (rs.next()) {
            String chargeCode = col(rs, cols, "chargecode");
            if (chargeCode.length() == 0) {
                continue;
            }
            String pairId = col(rs, cols, "pairid");
            String vatos = resolveVatos(rs, cols);
            String amtType = col(rs, cols, "amtType");
            String eqpType = col(rs, cols, "eqptype");
            double amount = readAmount(rs, cols, applyTeus, eqpType);

            if (logEnabled) {
                if (!firstJsonRow) {
                    jsonLog.append(",");
                }
                jsonLog.append("{\"pairId\":\"").append(pairId)
                       .append("\",\"chargeCode\":\"").append(chargeCode)
                       .append("\",\"amount\":\"").append(formatAmount(amount))
                       .append("\",\"vatos\":\"").append(vatos)
                       .append("\",\"amtType\":\"").append(amtType)
                       .append("\",\"eqpType\":\"").append(eqpType)
                       .append("\"}");
                firstJsonRow = false;
            }

            if (byPairId != null) {
                if (previousPairId == null || !previousPairId.equalsIgnoreCase(pairId)) {
                    seenCharges.clear();
                    previousPairId = pairId;
                }
                if (!seenCharges.contains(chargeCode)) {
                    seenCharges.add(chargeCode);
                    putCharge(byPairId, pairId, buildVo(chargeCode, amount, vatos, amtType, eqpType));
                }
            }

            if (byContract != null) {
                putCharge(byContract, pairId, buildVo(chargeCode, amount, vatos, amtType, eqpType));
            }
        }
        if (logEnabled) {
            jsonLog.append("]");
            System.out.println("[ECOM-SP-RS" + index + "] usp_svcGlobalSurchargeMain result: " + jsonLog);
        }
    }

    private static ContractSurchargeResultVo buildVo(String chargeCode, double amount, String vatos,
            String amtType, String eqpType) {
        ContractSurchargeResultVo vo = new ContractSurchargeResultVo();
        vo.setChargeCode(chargeCode);
        vo.setAmtType(amtType);
        vo.setEqpType(eqpType);
        vo.setVatos(vatos);
        vo.setAmountUsd(formatAmount(amount));
        return vo;
    }

    private static void putCharge(Map<String, Map<String, ContractSurchargeResultVo>> target,
            String key, ContractSurchargeResultVo vo) {
        Map<String, ContractSurchargeResultVo> chargeMap = target.get(key);
        if (chargeMap == null) {
            chargeMap = new LinkedHashMap<String, ContractSurchargeResultVo>();
            target.put(key, chargeMap);
        }
        chargeMap.put(vo.getChargeCode(), vo);
    }

    private static Set<String> columnLabels(ResultSet rs) throws Exception {
        Set<String> labels = new HashSet<String>();
        ResultSetMetaData meta = rs.getMetaData();
        int count = meta.getColumnCount();
        for (int i = 1; i <= count; i++) {
            String label = meta.getColumnLabel(i);
            if (label != null && label.trim().length() > 0) {
                labels.add(label.trim().toLowerCase());
            }
            String name = meta.getColumnName(i);
            if (name != null && name.trim().length() > 0) {
                labels.add(name.trim().toLowerCase());
            }
        }
        return labels;
    }

    private static String col(ResultSet rs, Set<String> cols, String... names) throws Exception {
        for (String name : names) {
            if (name != null && cols.contains(name.toLowerCase())) {
                return Validation.checkNull(rs.getString(name));
            }
        }
        return "";
    }

    private static String resolveVatos(ResultSet rs, Set<String> cols) throws Exception {
        String flag = col(rs, cols, "Flag");
        if ("xxx".equalsIgnoreCase(flag)) {
            return "Y";
        }
        String vatos = col(rs, cols, "vatos");
        return vatos.length() == 0 ? "N" : vatos;
    }

    private static double readAmount(ResultSet rs, Set<String> cols, boolean applyTeus, String eqpType)
            throws Exception {
        double amount = parseDouble(col(rs, cols, "AmountInUsd"));
        if (applyTeus && "TEUS".equalsIgnoreCase(eqpType)) {
            String contType = col(rs, cols, "conttype");
            if (contType.length() > 0 && Character.isDigit(contType.charAt(0))) {
                int teus = contType.charAt(0) - '0';
                amount = amount * (teus / 2f);
            }
        }
        return amount;
    }

    private static double parseDouble(String value) {
        String text = Validation.checkNull(value).replace(",", "");
        if (text.length() == 0) {
            return 0d;
        }
        try {
            return Double.parseDouble(text);
        } catch (Exception e) {
            return 0d;
        }
    }

    private static String formatAmount(double amount) {
        if (Math.abs(amount - Math.round(amount)) < 0.0001d) {
            return String.valueOf(Math.round(amount));
        }
        return String.valueOf(amount);
    }

    private static void closeQuietly(Connection conn, PreparedStatement pst, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
        }
        try {
            if (pst != null) {
                pst.close();
            }
        } catch (Exception e) {
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
        }
    }
}
