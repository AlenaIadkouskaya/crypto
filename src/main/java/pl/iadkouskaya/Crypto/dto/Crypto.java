package pl.iadkouskaya.Crypto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
//@AllArgsConstructor
public class Crypto {
    private String symbol;
    private String name;
    private Double current_price;

    public Crypto(String symbol, String name, Double current_price) {
        this.symbol = symbol;
        this.name = name;
        this.current_price = current_price;
    }

    public Crypto() {
    }

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
