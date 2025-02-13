package pl.iadkouskaya.Crypto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Crypto {
    private String symbol;
    private String name;
    private Double current_price;

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public Double getCurrent_price() {
        return current_price;
    }
}
