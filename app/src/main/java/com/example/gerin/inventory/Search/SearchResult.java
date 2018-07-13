package com.example.gerin.inventory.Search;

public class SearchResult {

    public int id;
    public String name;
    public Double quantity, price;

    public SearchResult(int id, String name, Double quanity, Double price){
        this.id = id;
        this.name = name;
        this.quantity = quanity;
        this.price = price;
    }

    public SearchResult(){

    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Double getQuantity(){
        return quantity;
    }

    public void setQuantity(Double quantity){
        this.quantity = quantity;
    }

    public Double getPrice(){
        return price;
    }

    public void setPrice(Double price){
        this.price = price;
    }

}
