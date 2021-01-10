package com.example.proiectdam.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proiectdam.R;
import com.example.proiectdam.database.model.User;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    private Context context;
    private List<User> users;
    private LayoutInflater inflater;
    private int resource;

    public UserAdapter(@NonNull Context context, int resource, @NonNull List<User> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.users = objects;
        this.inflater = inflater;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //va contine tot fisierul xml
        View view = inflater.inflate(resource, parent,false);
        User user = users.get(position);
        if(user != null) {
            addUserName(view, user.getName());
            addUserEmail(view, user.getEmail());
//            addUserPassword(view, user.getPassword());
//            addUserGender(view, user.getGender());
        }
        return view;
    }

    private void addUserName(View view, String userName) {
        TextView textView = view.findViewById(R.id.view_name);
        populateListViewContent(userName, textView);
    }

    private void addUserEmail(View view, String userEmail) {
        TextView textView = view.findViewById(R.id.view_email);
        populateListViewContent(String.valueOf(userEmail), textView);
    }

//    private void addUserPassword(View view, String userPassword) {
//        TextView textView = view.findViewById(R.id.view_year);
//        populateListViewContent(String.valueOf(userPassword), textView);
//    }
    
//    private void addUserGender(View view, String userGender) {
//        TextView textView = view.findViewById(R.id.view_gender);
//        populateListViewContent(String.valueOf(userGender), textView);
//    }

    private void populateListViewContent(String value, TextView textView) {
        if (value != null && !value.isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText(R.string.lv_default_value);
        }
    }

}
