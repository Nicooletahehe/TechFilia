package com.example.proiectdam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.proiectdam.database.model.Item;
import com.example.proiectdam.util.ChartView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartActivity extends AppCompatActivity {
    public static String CHART_KEY = "chart_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Item> items = (List<Item>) getIntent().getSerializableExtra(CHART_KEY);
        //conversie de la lista la mapa
        Map<String, Integer> source = getSource(items);
        setContentView(new ChartView(getApplicationContext(),source));
    }

    private Map<String, Integer> getSource(List<Item> items) {
        Log.i("chartactivity", String.valueOf(items));
        if(items == null || items.isEmpty()) {
            return null;
        }
        Map<String, Integer> source = new HashMap<>();
        for(Item item:items){
            if(source.containsKey(item.getItemType())) {
                Integer currentValue = source.get(item.getItemType());
                Integer value = (currentValue != null ? currentValue :0) +1;
                source.put(item.getItemType(), value);

            } else {
                source.put(item.getItemType(), 1);
            }
        }
        return source;
    }
}