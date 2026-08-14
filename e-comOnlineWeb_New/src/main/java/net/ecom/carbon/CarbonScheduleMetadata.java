package net.ecom.carbon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarbonScheduleMetadata {

    private final boolean selectedRouteDefault;
    private final int scheduleOptionsCount;
    private final List<CarbonScheduleLegMetadata> legs;

    public CarbonScheduleMetadata(boolean selectedRouteDefault, int scheduleOptionsCount,
            List<CarbonScheduleLegMetadata> legs) {
        this.selectedRouteDefault = selectedRouteDefault;
        this.scheduleOptionsCount = scheduleOptionsCount;
        this.legs = legs == null
                ? Collections.<CarbonScheduleLegMetadata>emptyList()
                : new ArrayList<CarbonScheduleLegMetadata>(legs);
    }

    public boolean isSelectedRouteDefault() {
        return selectedRouteDefault;
    }

    public int getScheduleOptionsCount() {
        return scheduleOptionsCount;
    }

    public List<CarbonScheduleLegMetadata> getLegs() {
        return Collections.unmodifiableList(legs);
    }
}
