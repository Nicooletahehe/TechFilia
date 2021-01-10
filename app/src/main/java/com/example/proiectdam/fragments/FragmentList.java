package com.example.proiectdam.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proiectdam.AddingForm;
import com.example.proiectdam.MainActivity;
import com.example.proiectdam.R;
import com.example.proiectdam.asyncTask.AsyncTaskRunner;
import com.example.proiectdam.asyncTask.Callback;
import com.example.proiectdam.database.service.ItemService;
import com.example.proiectdam.network.HttpManager;
import com.example.proiectdam.database.model.Item;
import com.example.proiectdam.util.ItemAdapter;
import com.example.proiectdam.util.ItemJsonParse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentList extends Fragment {
    public static final int UPDATE_REQUEST_CODE = 778;
    private static String LIST_ITEM_KEY = "list_item_key";
    private static String URL_ITEMS = "https://jsonkeeper.com/b/LSFQ";
    private ListView lvItem;
    private List<Item> items = new ArrayList<>();
    private AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();

    private ItemService itemService;

    public FragmentList() {
        // Required empty public constructor
    }

    public static FragmentList newInstance(ArrayList<Item> items) {
        FragmentList fragment = new FragmentList();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(FragmentList.LIST_ITEM_KEY, items);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initComponents(view);
        Log.i("createiview", String.valueOf(view));
        items.add(new Item("Nicoleta", 23,1890, "Bill", "Romania"));
        Log.i("createiview", String.valueOf(items));
        getItemsFromNEtwork();
        itemService = new ItemService(getContext().getApplicationContext());
        itemService.getAll(getAllFromDbCallback());
        return view;
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
                    notifyInternalAdapter();
                }
            }
        };
    }

    private void getItemsFromNEtwork() {
        Callable<String> asyncOperation = new HttpManager(URL_ITEMS);
        //ruleaza in activitate
        Callback<String> mainThreadOperation = getMainThreadOperationForItems();
        asyncTaskRunner.executeAsync(asyncOperation,mainThreadOperation);
    }

    private Callback<String> getMainThreadOperationForItems() {
        return new Callback<String>() {
            @Override
            public void runResultOnUiThread(String result) {
//                Toast.makeText(getContext().getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                items.addAll(ItemJsonParse.fromJson(result));
                notifyInternalAdapter();
            }
        };
    }

    private void initComponents(View view) {
        lvItem = view.findViewById(R.id.list_page_lv);
        if (getArguments() != null) {
            Log.i("FragmentHome", String.valueOf(items));
            items = getArguments().getParcelableArrayList(LIST_ITEM_KEY);
        }
        Log.i("FragmentHome2", String.valueOf(items));
        addItemAdapter();

        lvItem.setOnItemClickListener(updateEventListener());
        lvItem.setOnItemLongClickListener(deleteEventListener());
    }



    public AdapterView.OnItemClickListener updateEventListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("updatedevent", String.valueOf(items));
                Log.i("updatedevent", String.valueOf(items.get(position)));
                Intent intent = new Intent(getContext().getApplicationContext(), AddingForm.class);
                Log.i("updatedevent", String.valueOf(intent));
                intent.putExtra(AddingForm.NEW_ADD_ITEM_KEY, items.get(position));
                Log.i("updatedevent", String.valueOf(intent));
                startActivityForResult(intent, UPDATE_REQUEST_CODE);
//                notifyInternalAdapter();
            }
        };
    }

    private AdapterView.OnItemLongClickListener deleteEventListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("deletefucntion", String.valueOf(itemService));
                itemService.delete(deleteToDbCallback(position), items.get(position));
                return true;
            }
        };
    }

    private Callback<Integer> deleteToDbCallback(final int position) {
        return new Callback<Integer>() {
            @Override
            public void runResultOnUiThread(Integer result) {
                if (result != -1) {
                    items.remove(position);
                    notifyInternalAdapter();
                }
            }
        };
    }

//    private AdapterView.OnItemClickListener updateEventListener() {
//        return new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.i("updatedevent", String.valueOf(items));
//                Log.i("updatedevent", String.valueOf(items.get(position)));
//                Intent intent = new Intent(getContext().getApplicationContext(), AddingForm.class);
//                Log.i("updatedevent", String.valueOf(intent));
//                intent.putExtra(AddingForm.NEW_ADD_ITEM_KEY, items.get(position));
//                Log.i("updatedevent", String.valueOf(intent));
//                startActivityForResult(intent, UPDATE_REQUEST_CODE);
//                notifyInternalAdapter();
//            }
//        };
//    }

    private void addItemAdapter() {
        //adaugare adapter pentru listview
        ItemAdapter adapter = new ItemAdapter(getContext().getApplicationContext(),
                R.layout.lv_row_view_stamp, items, getLayoutInflater());
        Log.i("addItemAdapter2", String.valueOf(items));
        Log.i("addItemAdapter", String.valueOf(lvItem));
        Log.i("addItemAdapter3", String.valueOf(adapter));
        lvItem.setAdapter(adapter);
    }

    public void notifyInternalAdapter() {
        Log.i("fragmentlist-notify", String.valueOf(lvItem));
        ItemAdapter adapter = (ItemAdapter) lvItem.getAdapter();
        Log.i("fragmentlist-notify", String.valueOf(adapter));
        adapter.notifyDataSetChanged();
    }
}