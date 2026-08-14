package net.ecom.surcharge;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Pure allinAmt calculation for the "O" and "A" variants.
 *
 * <p>Matching between the setup (RS2) and result (RS3) charges is keyed by
 * charge code within a single contract (contractno + itemno), so the caller is
 * expected to pass maps that are already scoped to one contract.</p>
 */
public final class AllinAmtCalculator {

    public static final char ALLIN_ORIGINAL = 'O';
    public static final char ALLIN_AMENDED = 'A';

    /**
     * A single surcharge line used by the calculation.
     */
    public static final class ChargeLine {

        private final String chargeCode;
        private final double amount;
        private final String vatos;
        private final String chargeBasis;

        public ChargeLine(String chargeCode, double amount, String vatos) {
            this(chargeCode, amount, vatos, "");
        }

        public ChargeLine(String chargeCode, double amount, String vatos, String chargeBasis) {
            this.chargeCode = chargeCode;
            this.amount = amount;
            this.vatos = vatos;
            this.chargeBasis = chargeBasis;
        }

        public String getChargeCode() {
            return chargeCode;
        }

        public double getAmount() {
            return amount;
        }

        public String getVatos() {
            return vatos;
        }

        public String getChargeBasis() {
            return chargeBasis;
        }
    }

    private AllinAmtCalculator() {
    }

    /**
     * Computes the all-in amount for a single contract.
     *
     * <ul>
     *   <li>{@code O}: baseAmt + sum of every RS3 amount (vatos ignored).</li>
     *   <li>{@code A}: baseAmt + sum of RS2 amounts where vatos='N' + sum of RS3
     *       amounts whose charge code is NOT flagged vatos='N' in RS2 (RS3-only
     *       charges are included).</li>
     * </ul>
     *
     * @param allin         {@code 'O'} or {@code 'A'} (case-insensitive)
     * @param baseAmt       the contract base rate
     * @param setupByCode   RS2 charges for the contract keyed by charge code
     * @param resultByCode  RS3 charges for the contract keyed by charge code
     * @return the computed all-in amount
     */
    public static double computeAllinAmt(char allin, double baseAmt,
            Map<String, ChargeLine> setupByCode, Map<String, ChargeLine> resultByCode) {
        double total = baseAmt;
        if (Character.toUpperCase(allin) == ALLIN_ORIGINAL) {
            if (resultByCode != null) {
                for (ChargeLine line : resultByCode.values()) {
                    if (line != null) {
                        total += line.getAmount();
                    }
                }
            }
            return total;
        }

        Set<String> vatosNCodes = new HashSet<String>();
        if (setupByCode != null) {
            for (Map.Entry<String, ChargeLine> entry : setupByCode.entrySet()) {
                ChargeLine line = entry.getValue();
                if (line != null && "N".equalsIgnoreCase(line.getVatos())) {
                    total += line.getAmount();
                    vatosNCodes.add(entry.getKey());
                }
            }
        }
        if (resultByCode != null) {
            for (Map.Entry<String, ChargeLine> entry : resultByCode.entrySet()) {
                if (vatosNCodes.contains(entry.getKey())) {
                    continue;
                }
                ChargeLine line = entry.getValue();
                if (line != null) {
                    total += line.getAmount();
                }
            }
        }
        return total;
    }
}
