package com.example.proiectdam.util;

import com.example.proiectdam.database.model.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemJsonParse {

    public static List<Item> fromJson(String json) {
        if(json == null || json.isEmpty()){
            return new ArrayList<>();
        }
        try {
            JSONArray array = new JSONArray(json);
            return getItemsFromJson(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static List<Item> getItemsFromJson(JSONArray array) throws JSONException {
        List<Item> results = new ArrayList<>();
        for(int i=0;i<array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            Item Item = getItemFromJson(object);
            results.add(Item);
        }
        return results;
    }

    private static Item getItemFromJson(JSONObject object) throws JSONException {
        String itemName = object.getString("name");
        int itemPrice = object.getInt("price");
        int itemYear = object.getInt("year");
        String ceva = String.valueOf(object.getString("type"));
        String itemType = ceva.substring(0, 1).toUpperCase() + ceva.substring(1);
        String itemCountry = object.getString("country");
        return new Item(itemName,itemPrice,itemYear,itemType,itemCountry);
    }
}
