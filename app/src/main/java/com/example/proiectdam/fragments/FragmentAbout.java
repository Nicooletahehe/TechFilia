package com.example.proiectdam.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;

import com.example.proiectdam.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentAbout extends Fragment {
    public static final String RATING_SHARED_PREF = "ratingSharedPref";
    public static final String RATING_NUMBER = "ratingNumber";
    private RatingBar ratingBar;
    private Button ratingBarBtn;

    private SharedPreferences preferences;

    public FragmentAbout() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        ratingBar = view.findViewById(R.id.contact_ratingBar);
        //construim fisierul de preferinte, daca nu-l gaseste il creaza
        //de tip data binary => hashtable
        preferences = getActivity().getSharedPreferences(RATING_SHARED_PREF, Context.MODE_PRIVATE);
        getSharedPReferenceEventListener();
        loadFromSharedPreferences();
    }

    //citire din shared preferences
    private void loadFromSharedPreferences() {
        float rtgNb = preferences.getFloat(RATING_NUMBER, 0);
        //incarcare valori din fisier in componente vizuale
        ratingBar.setRating(rtgNb);
    }

    private void getSharedPReferenceEventListener() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
               saveSharedPreferences();
            }
        });
    }

    private void saveSharedPreferences() {
        Log.i("RatingBar",String.valueOf(ratingBar.getRating()));
        float ratingNb = ratingBar.getRating();

        //salvare in preferences - memoria java
        //deschide fisierul
        SharedPreferences.Editor editor = preferences.edit();
        //punem valori
        editor.putFloat(RATING_NUMBER, ratingNb);

        //memoria disk, altfel informatiile se pierd
        editor.apply();
    }
}