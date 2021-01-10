package com.example.proiectdam;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.proiectdam.asyncTask.Callback;
import com.example.proiectdam.database.service.ItemService;
import com.example.proiectdam.fragments.FragmentAbout;
import com.example.proiectdam.fragments.FragmentHome;
import com.example.proiectdam.fragments.FragmentList;
import com.example.proiectdam.database.model.Item;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int NEW_ADD_REQUEST_CODE = 777;
    public static final int UPDATE_REQUEST_MAIN_CODE = 778;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Fragment currentFragment;

    private ItemService itemService;

    private ArrayList<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configNavigation();
        initComponents();
        openDefaultFragment(savedInstanceState);

        itemService = new ItemService(getApplicationContext());
        itemService.getAll(getAllFromDbCallback());
    }

    private void initComponents() {
//        Log.i("listview", String.valueOf(lt));
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(addNavigationMenuItemSelectedEvent());
    }

    private void configNavigation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        //burger menu
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                //unde se afla toolbarul
                drawerLayout,
                toolbar,
                //texte pentru fallback in cazul in care nu functioneaza meniul
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        //deschidere/inchidere
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //rasucirea toggle-ului
        actionBarDrawerToggle.syncState();
    }

    private NavigationView.OnNavigationItemSelectedListener addNavigationMenuItemSelectedEvent() {
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.main_nav_home) {
                    currentFragment = new FragmentHome();
                } else if (item.getItemId() == R.id.main_nav_contact) {
                    currentFragment = new FragmentAbout();
                } else if(item.getItemId() == R.id.main_nav_add) {
                    Intent intent = new Intent(getApplicationContext(), AddingForm.class);
                    startActivityForResult(intent, NEW_ADD_REQUEST_CODE);
                } else if(item.getItemId() == R.id.main_nav_list){
                    currentFragment = FragmentList.newInstance(items);
                } else if(item.getItemId() == R.id.main_nav_charts){
                    Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
                    List<Item> itemPr = items;
                    Log.i("itempr",String.valueOf(itemPr));
                    intent.putExtra(ChartActivity.CHART_KEY, (Serializable) itemPr);
                    startActivityForResult(intent, NEW_ADD_REQUEST_CODE);
                }
                else if(item.getItemId() == R.id.main_nav_users){
                    Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                    startActivityForResult(intent, NEW_ADD_REQUEST_CODE);
                }
//                Toast.makeText(getApplicationContext(),
//                        getString(R.string.show_option, item.getTitle()),
//                        Toast.LENGTH_SHORT)
//                        .show();
                //incarcam pe ecran fragmentul corespunzator optiunii selectate
                openFragment();
                //inchidem meniul lateral
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null) {
            Item item = (Item) data.getParcelableExtra(AddingForm.NEW_ADD_ITEM_KEY);
            if (requestCode == NEW_ADD_REQUEST_CODE) {
                Log.i("insertintodb", String.valueOf(item));
                itemService.insert(insertIntoDbCallback(), item);
            } else if (requestCode == UPDATE_REQUEST_MAIN_CODE) {
                itemService.update(updateIntoDbCallback(), item);
            }
        }
    }

    private Callback<List<Item>> getAllFromDbCallback() {
        return new Callback<List<Item>>() {
            @Override
            public void runResultOnUiThread(List<Item> result) {
                if(result != null){
                    items.clear();
                    items.addAll(result);
                    Log.i("getallmain", String.valueOf(result));
                    Log.i("getallmain2", String.valueOf(items));
                    if (currentFragment instanceof FragmentList) {
                        Log.i("getallmain3", "notify");
                        ((FragmentList) currentFragment).notifyInternalAdapter();
                    }
                }
            }
        };
    }

//    private Callback<List<Item>> getAllByCountryFromDbCallback() {
//        return new Callback<List<Item>>() {
//            @Override
//            public void runResultOnUiThread(List<Item> result) {
//                if(result != null){
//                    items.clear();
//                    items.addAll(result);
//                    if (currentFragment instanceof FragmentList) {
//                        ((FragmentList) currentFragment).notifyInternalAdapter();
//                    }
//                }
//            }
//        };
//    }

    private Callback<Item> insertIntoDbCallback(){
        Log.i("insertmain", "ceva");
        return new Callback<Item>() {
            @Override
            public void runResultOnUiThread(Item result) {
                Log.i("insertmain", String.valueOf(result));
                Log.i("insertmain", String.valueOf(items));
                if(result != null) {
                    items.add(result);
                    Log.i("insertmain", String.valueOf(items));
                    if (currentFragment instanceof FragmentList) {
                        Log.i("currentinstancenotify", String.valueOf(items));
                        ((FragmentList) currentFragment).notifyInternalAdapter();
                    }
                }
            }
        };
    }

    private Callback<Item> updateIntoDbCallback(){
        return new Callback<Item>() {
            @Override
            public void runResultOnUiThread(Item result) {
                if(result != null){
                    for(Item item:items) {
                        if(item.getId() == result.getId()){
                            item.setItemName(result.getItemName());
                            item.setItemType(result.getItemType());
                            item.setItemYear(result.getItemYear());
                            item.setItemPrice(result.getItemPrice());
                            item.setItemCountry(result.getItemCountry());
                            break;
                        }
                    }
                    if (currentFragment instanceof FragmentList) {
                        ((FragmentList) currentFragment).notifyInternalAdapter();
                    }
                }
            }
        };
    }
//
//    public AdapterView.OnItemClickListener updateEventListener() {
//        return new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.i("updatedevent", String.valueOf(items));
//                Log.i("updatedevent", String.valueOf(items.get(position)));
//                Intent intent = new Intent(getApplicationContext(), AddingForm.class);
//                Log.i("updatedevent", String.valueOf(intent));
//                intent.putExtra(AddingForm.NEW_ADD_ITEM_KEY, items.get(position));
//                Log.i("updatedevent", String.valueOf(intent));
//                startActivityForResult(intent, UPDATE_REQUEST_MAIN_CODE);
////                notifyInternalAdapter();
//            }
//        };
//    }

    private void openDefaultFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            currentFragment = new FragmentHome();
            openFragment();
            navigationView.setCheckedItem(R.id.main_nav_home);
        }
    }

    private void openFragment() {
        getSupportFragmentManager()
                .beginTransaction()//incepe tranzactia pentru adaugarea fragmentului
                .replace(R.id.main_frame_container, currentFragment)//se inlocuieste FrameLayout din content_main.xml cu fisierul xml a fragmentul initializat
                .commit();//se confirma schimbarea
    }
}