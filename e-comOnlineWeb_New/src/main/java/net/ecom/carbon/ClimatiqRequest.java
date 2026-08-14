package net.ecom.carbon;

import com.google.gson.JsonArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ClimatiqRequest {

    private final List<String> locations;
    private final JsonArray legs;
    private final double weightKg;
    private final int quantity;
    private final List<Map<String, String>> routingDetails;

    public ClimatiqRequest(List<String> locations, JsonArray legs, double weightKg, int quantity) {
        this(locations, legs, weightKg, quantity, Collections.<Map<String, String>>emptyList());
    }

    public ClimatiqRequest(List<String> locations, JsonArray legs, double weightKg, int quantity,
            List<Map<String, String>> routingDetails) {
        this.locations = locations == null ? Collections.<String>emptyList() : new ArrayList<String>(locations);
        this.legs = legs;
        this.weightKg = weightKg;
        this.quantity = quantity;
        this.routingDetails = routingDetails == null
                ? Collections.<Map<String, String>>emptyList()
                : new ArrayList<Map<String, String>>(routingDetails);
    }

    public List<String> getLocations() {
        return locations;
    }

    public JsonArray getLegs() {
        return legs;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<Map<String, String>> getRoutingDetails() {
        return routingDetails;
    }
}
