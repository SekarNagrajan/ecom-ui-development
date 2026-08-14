package net.ecom.carbon;

import java.util.Map;

public class ClimatiqResponse {

    private final int httpStatus;
    private final Map<String, Object> body;

    public ClimatiqResponse(int httpStatus, Map<String, Object> body) {
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public Map<String, Object> getBody() {
        return body;
    }
}
