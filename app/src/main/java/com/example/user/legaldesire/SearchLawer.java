package com.example.user.legaldesire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.View;
import android.widget.ImageButton;
import android.support.v4.app.Fragment;

import com.example.user.legaldesire.fragments.LawyerRecycler;


public class SearchLawer extends AppCompatActivity {
    ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lawer);
        imageButton=findViewById(R.id.filterBtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(SearchLawer.this,imageButton);
               popup.getMenuInflater().inflate(R.menu.filter,popup.getMenu());

               popup.show();

    }
});
        getSupportFragmentManager().beginTransaction().replace(R.id.recycler_container,new LawyerRecycler()).commit();

        Fragment selectFragment = new LawyerRecycler();
        getSupportFragmentManager().beginTransaction().replace(R.id.recycler_container,selectFragment).commit();

    }}