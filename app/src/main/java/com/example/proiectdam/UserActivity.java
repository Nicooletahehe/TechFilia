package com.example.proiectdam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.proiectdam.asyncTask.Callback;
import com.example.proiectdam.database.model.Item;
import com.example.proiectdam.database.model.User;
import com.example.proiectdam.database.service.UserService;
import com.example.proiectdam.util.UserAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    private static final int USER_ADD_REQUEST_CODE = 555;
    private static final int UPDATE_USER_REQUEST_CODE = 556;

    private ListView lvUsers;
    private FloatingActionButton addUserBtn;
    private FloatingActionButton userChart;

    private List<User> users = new ArrayList<>();
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initComponents();
        users.add(new User("Nicoleta", "nicoleta@email.com", "ceva1234", "Female"));
        userService = new UserService(getApplicationContext());
        userService.getAll(getAllFromDbCallback());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            User user = (User) data.getParcelableExtra(UserForm.NEW_ADD_USER_KEY);
            if (requestCode == USER_ADD_REQUEST_CODE) {
                userService.insert(insertIntoDbCallback(), user);
            } else if (requestCode == UPDATE_USER_REQUEST_CODE) {
                userService.update(updateToDbCallback(), user);
            }
        }
    }

    private Callback<List<User>> getAllFromDbCallback() {
        return new Callback<List<User>>() {
            @Override
            public void runResultOnUiThread(List<User> result) {
                if (result != null) {
                    users.clear();
                    users.addAll(result);
                    notifyAdapter();
                }
            }
        };
    }

    private Callback<User> insertIntoDbCallback() {
        Log.i("insertmain", "ceva");
        return new Callback<User>() {
            @Override
            public void runResultOnUiThread(User result) {
                Log.i("insertmain", String.valueOf(result));
                Log.i("insertmain", String.valueOf(users));
                if (result != null) {
                    users.add(result);
                    Log.i("insertmain", String.valueOf(users));
                    notifyAdapter();
                }
            }
        };
    }

    private Callback<User> updateToDbCallback() {
        return new Callback<User>() {
            @Override
            public void runResultOnUiThread(User result) {
                if (result != null) {
                    for (User user : users) {
                        if (user.getId() == result.getId()) {
                            user.setName(result.getName());
                            user.setEmail(result.getEmail());
                            user.setPassword(result.getPassword());
                            user.setGender(result.getGender());
                            break;
                        }
                    }
                    notifyAdapter();
                }
            }
        };
    }

    private Callback<Integer> deleteToDbCallback(final int position) {
        return new Callback<Integer>() {
            @Override
            public void runResultOnUiThread(Integer result) {
                if (result != -1) {
                    users.remove(position);
                    notifyAdapter();
                }
            }
        };
    }

    private void initComponents() {
        lvUsers = findViewById(R.id.lv_user);
        addUserBtn = findViewById(R.id.user_add);
        addUserBtn.setOnClickListener(addUserEvent());
        userChart = findViewById(R.id.fab_user_chart);
        addAdapter();
        lvUsers.setOnItemClickListener(updateEventListener());
        lvUsers.setOnItemLongClickListener(deleteEventListener());
        userChart.setOnClickListener(drawChart());
    }

    private View.OnClickListener drawChart() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserChartActivity.class);
                List<User> itemPr = users;
                intent.putExtra(UserChartActivity.USERS, (Serializable) itemPr);
                startActivity(intent);
            }
        };
    }

    private AdapterView.OnItemClickListener updateEventListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), UserForm.class);
                startActivityForResult(intent, UPDATE_USER_REQUEST_CODE);
            }
        };
    }

    private AdapterView.OnItemLongClickListener deleteEventListener() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                userService.delete(deleteToDbCallback(position), users.get(position));
                return true;
            }
        };
    }

    private View.OnClickListener addUserEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserForm.class);
                startActivityForResult(intent, USER_ADD_REQUEST_CODE);
            }
        };
    }

    private void addAdapter() {
        UserAdapter adapter = new UserAdapter(getApplicationContext(), R.layout.lv_row_view_user, users, getLayoutInflater());
        Log.i("addItemAdapter2", String.valueOf(users));
        Log.i("addItemAdapter", String.valueOf(lvUsers));
        Log.i("addItemAdapter3", String.valueOf(adapter));
        lvUsers.setAdapter(adapter);
    }

    private void notifyAdapter() {
        UserAdapter adapter = (UserAdapter) lvUsers.getAdapter();
        adapter.notifyDataSetChanged();
    }
}