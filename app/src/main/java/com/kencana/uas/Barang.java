package com.kencana.uas;

public class Barang {
    private String ItemId, ItemName, ItemDenomination, ItemQuantity, ItemPrice, Description;
    private byte[] ItemImage;


    public Barang(String ItemId, String ItemName,String ItemDenomination,String ItemQuantity,String ItemPrice,byte[] ItemImage, String Description){
        this.ItemId = ItemId;
        this.ItemName = ItemName;
        this.ItemDenomination = ItemDenomination;
        this.ItemQuantity = ItemQuantity;
        this.ItemPrice = ItemPrice;
        this.ItemImage = ItemImage;
        this.Description = Description;
    }

    public String getItemId() {
        return ItemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public String getItemDenomination() {
        return ItemDenomination;
    }

    public String getItemQuantity() {
        return ItemQuantity;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public String getDescription() {
        return Description;
    }

    public byte[] getItemImage() {
        return ItemImage;
    }
}
