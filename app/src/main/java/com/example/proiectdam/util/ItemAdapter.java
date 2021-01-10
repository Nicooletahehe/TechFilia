package com.example.proiectdam.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proiectdam.R;
import com.example.proiectdam.database.model.Item;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {
    private Context context;
    private List<Item> items;
    //variabila java in care vom injecta view-urile
    //layout inflater pentru activitatea este in layout
    private LayoutInflater inflater;
    private int resource;


    public ItemAdapter(@NonNull Context context, int resource, @NonNull List<Item> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.items = objects;
        this.inflater = inflater;
        this.resource = resource;
    }

    //    suprascriem o metoda de-a lui array adapter
    //    position=indexul item-ului din lista
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //va contine tot fisierul xml
        View view = inflater.inflate(resource, parent,false);
        Item item = items.get(position);
        Log.i("ITemAdapter1", String.valueOf(item));
        if(item != null) {
            addItemName(view, item.getItemName());
            addItemPrice(view, item.getItemPrice());
            addItemYear(view, item.getItemYear());
            addItemCountry(view, item.getItemCountry());
//            Log.i("ItemAdapterType",String.valueOf(item.getItemType()));
            addItemType(view, item.getItemType());
        }
        Log.i("ItemAdapter2", String.valueOf(view));
        return view;
    }

    private void addItemName(View view, String itemName) {
        TextView textView = view.findViewById(R.id.view_name);
        populateListViewContent(itemName, textView);
    }

    private void addItemPrice(View view, int itemPrice) {
        TextView textView = view.findViewById(R.id.view_price);
        populateListViewContent(String.valueOf(itemPrice), textView);
    }

    private void addItemYear(View view, int itemYear) {
        TextView textView = view.findViewById(R.id.view_year);
        populateListViewContent(String.valueOf(itemYear), textView);
    }

    private void addItemCountry(View view, String itemCountry) {
        TextView textView = view.findViewById(R.id.view_country);
        populateListViewContent(String.valueOf(itemCountry), textView);
    }

    private void addItemType(View view, String itemType) {
        TextView textView = view.findViewById(R.id.view_type);
        populateListViewContent(itemType, textView);
    }

    private void populateListViewContent(String value, TextView textView) {
        if (value != null && !value.isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText(R.string.lv_default_value);
        }
    }
}
