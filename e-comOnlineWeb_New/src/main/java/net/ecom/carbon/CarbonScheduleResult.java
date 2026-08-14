package net.ecom.carbon;

public class CarbonScheduleResult {

    private final ClimatiqRequest climatiqRequest;
    private final ClimatiqResponse climatiqResponse;
    private final CarbonScheduleMetadata metadata;

    public CarbonScheduleResult(ClimatiqRequest climatiqRequest, ClimatiqResponse climatiqResponse,
            CarbonScheduleMetadata metadata) {
        this.climatiqRequest = climatiqRequest;
        this.climatiqResponse = climatiqResponse;
        this.metadata = metadata;
    }

    public ClimatiqRequest getClimatiqRequest() {
        return climatiqRequest;
    }

    public ClimatiqResponse getClimatiqResponse() {
        return climatiqResponse;
    }

    public CarbonScheduleMetadata getMetadata() {
        return metadata;
    }
}
