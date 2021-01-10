package com.example.proiectdam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.proiectdam.database.model.User;
import com.example.proiectdam.util.ChartView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserChartActivity extends AppCompatActivity {
    public static final String USERS = "users_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<User> coaches = (List<User>) getIntent().getSerializableExtra(USERS);

        setContentView(new ChartView(getApplicationContext(), getSource(coaches)));
    }

    private Map<String, Integer> getSource(List<User> coaches) {
        if (coaches == null || coaches.isEmpty()) {
            return null;
        }
        Map<String, Integer> source = new HashMap<>();
        for (User user : coaches) {
            if (source.containsKey(user.getGender())) {
                Integer currentValue = source.get(user.getGender());
                Integer newValue = (currentValue != null ? currentValue : 0) + 1;
                source.put(user.getGender(), newValue);
            } else {
                source.put(user.getGender(), 1);
            }
        }
        return source;
    }
}