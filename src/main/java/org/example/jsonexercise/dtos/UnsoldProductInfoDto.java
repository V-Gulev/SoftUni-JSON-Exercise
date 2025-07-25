package org.example.jsonexercise.dtos;

import org.example.jsonexercise.entities.Product;

public class UnsoldProductInfoDto {
    private String name;
    private double price;
    private String seller;

    public UnsoldProductInfoDto() {
    }
    public UnsoldProductInfoDto(Product product) {
        this.name = product.getName();
        this.price = product.getPrice().doubleValue();
        this.seller = product.getSeller() == null ? "No Seller" : product.getSeller().getFirstName() + " " + product.getSeller().getLastName();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getSeller() {
        return seller;
    }
    public void setSeller(String seller) {
        this.seller = seller;
    }
}
