package com.example.proiectdam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proiectdam.database.model.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.widget.ArrayAdapter.createFromResource;

public class UserForm extends AppCompatActivity {
    public static final String NEW_ADD_USER_KEY = "new_add_user_key";

    private TextInputEditText tietName;
    private TextInputEditText tietEmail;
    private TextInputEditText tietPassword;
    private Spinner spnGender;
    private Button addBtn;
    private Button cancelBtn;

    private Intent intent;
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
        initComponents();

        intent = getIntent();

        if(intent.hasExtra(NEW_ADD_USER_KEY)) {
            user = (User) intent.getParcelableExtra(NEW_ADD_USER_KEY);
            buildViewsFromUser(user);
        }
    }

    private void buildViewsFromUser(User user) {
        if(user == null) {
            return;
        }
        if(user.getName() != null){
            tietName.setText(String.valueOf(user.getName()));
        }

        tietEmail.setText(String.valueOf(user.getEmail()));
        tietPassword.setText(String.valueOf(user.getPassword()));
        selectGender(this.user);
    }

    private void selectGender(User user) {
        ArrayAdapter adapter = (ArrayAdapter) spnGender.getAdapter();
        for(int i=0; i<adapter.getCount();i++) {
            String spn = (String) adapter.getItem(i);
            if(spn != null && spn.equals(user.getGender()) ) {
                spnGender.setSelection(i);
                break;
            }
        }
    }

    private void initComponents() {
        tietName = findViewById(R.id.form_user_tiet_add_name);
        tietEmail = findViewById(R.id.form_user_tiet_add_email);
        tietPassword = findViewById(R.id.form_user_tiet_add_password);
        spnGender = findViewById(R.id.form_user_add_spinner);
        addBtn = findViewById(R.id.form_add_btn);
        cancelBtn = findViewById(R.id.form_cancel_btn);
        populateSpnGender();
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
                    createUserFromViews();
                    Log.i("AddingForm", String.valueOf(user));
                    intent.putExtra(NEW_ADD_USER_KEY, user);
                    Log.i("AddingForm", String.valueOf(intent));
                    setResult(RESULT_OK, intent);
                    Log.i("AddingForm", String.valueOf(intent));
                    finish();
                }
            }
        };
    }

    private void createUserFromViews() {
        String userNAme = tietName.getText().toString();
        String userEmail = tietEmail.getText().toString();
        String userPassword = tietPassword.getText().toString();
        //spinner
        String userGender = spnGender.getSelectedItem().toString();
        if(user == null) {
            user = new User(userNAme, userEmail, userPassword, userGender);
        } else {
            user.setName(userNAme);
            user.setEmail(userEmail);
            user.setPassword(userPassword);
            user.setGender(userGender);
        }

    }

    private boolean validate(){
        //numele sa fie mai mare de 3 caractere
        if(tietName.getText() == null || tietName.getText().toString().trim().length() < 3) {
            Toast.makeText(getApplicationContext(),"Invalid Name. Please insert min 3 characters.", Toast.LENGTH_LONG).show();
            return false;
        }

        //adresa de email valida
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(tietEmail.getText() == null || String.valueOf(tietEmail).matches(emailPattern)) {
            Toast.makeText(getApplicationContext(),"Invalid Email Address.", Toast.LENGTH_LONG).show();
            return false;
        }

        //parola de min 8
        if(tietPassword.getText().toString().length()<8 && !isValidPassword(String.valueOf(tietPassword))) {
            Toast.makeText(getApplicationContext(),"Invalid Password. Min 8 characters", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean isValidPassword(String valueOf) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(valueOf);

        return matcher.matches();
    }

    private void populateSpnGender() {
        ArrayAdapter<CharSequence> adapter = createFromResource(getApplicationContext(),
                R.array.form_choose_gender,
                android.R.layout.simple_spinner_dropdown_item);
        //atasam adapter catre spinner
        spnGender.setAdapter(adapter);
    }
}