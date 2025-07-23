package org.example.jsonexercise.dtos;

import java.math.BigDecimal;

public class ImportProductDto {
    private String name;
    private BigDecimal price;

    public ImportProductDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
