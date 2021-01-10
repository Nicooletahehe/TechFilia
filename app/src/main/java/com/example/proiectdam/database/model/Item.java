package com.example.proiectdam.database.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

//@Entity(tableName="items", foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId", onDelete = CASCADE))

@Entity(tableName="items")
public class Item implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name ="name")
    private String itemName;
    @ColumnInfo(name ="price")
    private int itemPrice;
    @ColumnInfo(name ="year")
    private int itemYear;
    @ColumnInfo(name ="type")
    private String itemType;
    @ColumnInfo(name ="country")
    private String itemCountry;
//    private int userId;

    //constructor Room cu toti parametrii
    public Item(long id, String itemName, int itemPrice, int itemYear, String itemType, String itemCountry) {
        this.id = id;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemYear = itemYear;
        this.itemType = itemType;
        this.itemCountry = itemCountry;
    }

    //Room va sti ca nu are nevoie de constructorul acesta
//    @Ignore
//    public Item(long id, String itemName, int itemPrice, int itemYear, String itemType, String itemCountry, final int userId) {
//        this.id = id;
//        this.itemName = itemName;
//        this.itemPrice = itemPrice;
//        this.itemYear = itemYear;
//        this.itemType = itemType;
//        this.itemCountry = itemCountry;
////        this.userId = userId;
//    }

    @Ignore
    public Item(String itemName, int itemPrice, int itemYear, String itemType, String itemCountry) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemYear = itemYear;
        this.itemType = itemType;
        this.itemCountry = itemCountry;
    }

//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemYear() {
        return itemYear;
    }

    public void setItemYear(int itemYear) {
        this.itemYear = itemYear;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemCountry() {
        return itemCountry;
    }

    public void setItemCountry(String itemCountry) {
        this.itemCountry = itemCountry;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemYear=" + itemYear +
                ", itemType='" + itemType + '\'' +
                ", itemCountry='" + itemCountry + '\'' +
                '}';
    }

    private Item(Parcel source) {
        id = source.readLong();
        itemName = source.readString();
        itemPrice = source.readInt();
        itemYear = source.readInt();
        itemCountry = source.readString();
        itemType = source.readString();
        Log.i("Item-Parcel", String.valueOf(itemType));
    }

    public static Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(itemName);
        dest.writeInt(itemPrice);
        dest.writeInt(itemYear);
        dest.writeString(itemCountry);
        dest.writeString(itemType);
    }
}
