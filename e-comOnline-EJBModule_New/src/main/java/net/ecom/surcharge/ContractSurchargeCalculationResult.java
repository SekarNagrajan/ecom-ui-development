package net.ecom.surcharge;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ContractSurchargeCalculationResult implements Serializable {

    private Map<String, Map<String, ContractSurchargeResultVo>> chargesByPairId;
    private Map<String, Map<String, ContractSurchargeResultVo>> setupChargesByContract = new LinkedHashMap<>();
    private Map<String, Map<String, ContractSurchargeResultVo>> resultChargesByContract = new LinkedHashMap<>();
    private Set<String> missingFields = new LinkedHashSet<>();

    public Map<String, Map<String, ContractSurchargeResultVo>> getChargesByPairId() {
        return chargesByPairId;
    }

    public void setChargesByPairId(Map<String, Map<String, ContractSurchargeResultVo>> chargesByPairId) {
        this.chargesByPairId = chargesByPairId;
    }

    public Map<String, Map<String, ContractSurchargeResultVo>> getSetupChargesByContract() {
        return setupChargesByContract;
    }

    public void setSetupChargesByContract(Map<String, Map<String, ContractSurchargeResultVo>> setupChargesByContract) {
        this.setupChargesByContract = setupChargesByContract;
    }

    public Map<String, Map<String, ContractSurchargeResultVo>> getResultChargesByContract() {
        return resultChargesByContract;
    }

    public void setResultChargesByContract(Map<String, Map<String, ContractSurchargeResultVo>> resultChargesByContract) {
        this.resultChargesByContract = resultChargesByContract;
    }

    public Set<String> getMissingFields() {
        return missingFields;
    }

    public void setMissingFields(Set<String> missingFields) {
        this.missingFields = missingFields;
    }
}
