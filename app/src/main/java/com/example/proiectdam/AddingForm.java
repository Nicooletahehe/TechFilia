package com.example.proiectdam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proiectdam.database.model.Item;
import com.google.android.material.textfield.TextInputEditText;

import static android.widget.ArrayAdapter.createFromResource;

public class AddingForm extends AppCompatActivity {

    public static final String NEW_ADD_ITEM_KEY = "new_add_item_key";

    private TextInputEditText tietName;
    private TextInputEditText tietPrice;
    private TextInputEditText tietYear;
    private Spinner addType;
    private Spinner spnCountry;
    private Button addBtn;
    private Button cancelBtn;

    private Intent intent;
    private Item item = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_form);
        initComponents();

        intent = getIntent();

        if(intent.hasExtra(NEW_ADD_ITEM_KEY)) {
            item = (Item) intent.getParcelableExtra(NEW_ADD_ITEM_KEY);
            buildViewsFromItem(item);
        }
    }

    private void buildViewsFromItem(Item item) {
        if(item == null) {
            return;
        }
        if(item.getItemName() != null){
            tietName.setText(String.valueOf(item.getItemName()));
        }

        tietPrice.setText(String.valueOf(item.getItemPrice()));
        tietYear.setText(String.valueOf(item.getItemYear()));
        selectCountry(item);
        selectType(item);
    }

    private void selectCountry(Item item) {
        ArrayAdapter adapter = (ArrayAdapter) spnCountry.getAdapter();
        for(int i=0; i<adapter.getCount();i++) {
            String spn = (String) adapter.getItem(i);
            if(spn != null && spn.equals(item.getItemCountry()) ) {
                spnCountry.setSelection(i);
                break;
            }
        }
    }

    private void selectType(Item item) {
        ArrayAdapter adapter = (ArrayAdapter) addType.getAdapter();
        for(int i=0; i<adapter.getCount();i++) {
            String spn = (String) adapter.getItem(i);
            if(spn != null && spn.equals(item.getItemCountry()) ) {
                addType.setSelection(i);
                break;
            }
        }
    }

    private void initComponents() {
        tietName = findViewById(R.id.form_tiet_add_name);
        tietPrice = findViewById(R.id.form_tiet_add_price);
        tietYear = findViewById(R.id.form_tiet_add_year);
        addType = findViewById(R.id.form_add_spinner_type);
        spnCountry = findViewById(R.id.form_add_spinner);
        addBtn = findViewById(R.id.form_add_btn);
        cancelBtn = findViewById(R.id.form_cancel_btn);
        populateSpnCountries();
        populateSpnTypes();
        addBtn.setOnClickListener(addSaveClickEvent());
        cancelBtn.setOnClickListener(cancelClickEvent());
    }

    //cancel => ma intorc pe o alta activitate
    private View.OnClickListener cancelClickEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
    }

    private View.OnClickListener addSaveClickEvent() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validate()) {
                    Log.i("AddingForm", String.valueOf(validate()));
                    createItemFromViews();
                    Log.i("AddingForm", String.valueOf(item));
                    intent.putExtra(NEW_ADD_ITEM_KEY, item);
                    Log.i("AddingForm", String.valueOf(intent));
                    setResult(RESULT_OK, intent);
                    Log.i("AddingForm", String.valueOf(intent));
                    finish();
                }
            }
        };
    }

    //introducere validari pentru formular
    private boolean validate(){
        //numele sa fie mai mare de 3 caractere
        if(tietName.getText() == null || tietName.getText().toString().trim().length() < 3) {
            Toast.makeText(getApplicationContext(),"Invalid Name. Please insert min 3 characters.", Toast.LENGTH_LONG).show();
            return false;
        }

        //pretul sa fie mai mare de 0
        if(tietPrice.getText() == null || tietPrice.getText().toString().trim().length() < 0) {
            Toast.makeText(getApplicationContext(),"Invalid Price. The price should be greater than 0.", Toast.LENGTH_LONG).show();
            return false;
        }

        //anul sa fie intre 1800 si 2021
        if(tietYear.getText() == null || tietYear.getText().toString().trim().length() != 4 || Integer.parseInt(tietYear.getText().toString().trim()) < 1800
                || Integer.parseInt(tietYear.getText().toString().trim()) > 2021) {
            Toast.makeText(getApplicationContext(),"Invalid Year. The year accepted is between 1800 and 2021.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void createItemFromViews() {
        String itemName = tietName.getText().toString();
        int itemPrice = Integer.parseInt(tietPrice.getText().toString());
        int itemYear = Integer.parseInt(tietYear.getText().toString());
        //spinner
        String itemCountry = spnCountry.getSelectedItem().toString();
        String itemType = addType.getSelectedItem().toString();
        if(item == null) {
            item = new Item(itemName, itemPrice, itemYear, itemType, itemCountry);
        } else {
            item.setItemName(itemName);
            item.setItemPrice(itemPrice);
            item.setItemCountry(itemCountry);
            item.setItemYear(itemYear);
            item.setItemType(itemType);
        }

    }

    private void populateSpnCountries() {
        //creare adapter care asigura convertirea unei colectii de string-uri
        // la o colectie de view (TextView)
        ArrayAdapter<CharSequence> adapter = createFromResource(getApplicationContext(),
                R.array.form_choose_country,
                android.R.layout.simple_spinner_dropdown_item);
        //atasam adapter catre spinner
        spnCountry.setAdapter(adapter);
    }

    private void populateSpnTypes() {
        ArrayAdapter<CharSequence> adapter = createFromResource(getApplicationContext(),
                R.array.form_choose_item,
                android.R.layout.simple_spinner_dropdown_item);
        //atasam adapter catre spinner
        addType.setAdapter(adapter);
    }
}