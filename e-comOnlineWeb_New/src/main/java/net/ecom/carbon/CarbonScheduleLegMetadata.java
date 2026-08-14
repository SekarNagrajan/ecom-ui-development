package net.ecom.carbon;

public class CarbonScheduleLegMetadata {

    private final String loadport;
    private final String disport;
    private final String legType;
    private final String transportMode;
    private final String vesselName;
    private final String serviceName;
    private final String voyage;
    private final String vesselImo;
    private final String operator;

    public CarbonScheduleLegMetadata(String loadport, String disport, String legType, String transportMode,
            String vesselName, String serviceName, String voyage, String vesselImo, String operator) {
        this.loadport = loadport;
        this.disport = disport;
        this.legType = legType;
        this.transportMode = transportMode;
        this.vesselName = vesselName;
        this.serviceName = serviceName;
        this.voyage = voyage;
        this.vesselImo = vesselImo;
        this.operator = operator;
    }

    public String getLoadport() {
        return loadport;
    }

    public String getDisport() {
        return disport;
    }

    public String getLegType() {
        return legType;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public String getVesselName() {
        return vesselName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getVoyage() {
        return voyage;
    }

    public String getVesselImo() {
        return vesselImo;
    }

    public String getOperator() {
        return operator;
    }
}
