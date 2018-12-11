package com.example.michael.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ClothingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Serializable {

    List<Clothing> clothingList; //switch to wearable later for clothing and outfits
    private final static int ROWS_WIDE = 3;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Dialog dialog;
    private DBManager manager;
    private File imageFile;
    private Spinner spinnerType, spinnerColor, spinnerCondition;
    private Clothing.clothingType type;
    private Clothing.clothingColor color;
    private Clothing.clothingCondition condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clothing_recyclerview);


        clothingList = new ArrayList<>();
        manager = new DBManager(this, null, null, 1);
        clothingList.addAll(manager.getAllClothes());

        RecyclerView my_recycler_view = (RecyclerView) findViewById(R.id.clothing_recyclerview_id);
        ClothingRecyclerViewAdapter myAdapter = new ClothingRecyclerViewAdapter(this, clothingList);
        my_recycler_view.setLayoutManager(new GridLayoutManager(this, ROWS_WIDE));
        my_recycler_view.setAdapter(myAdapter);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.allClothingtoolbar_id);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(name);

        drawerLayout = (DrawerLayout) findViewById(R.id.clothingDrawerLayoutId);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.clothingNavViewId);
        navigationView.setNavigationItemSelectedListener(this);

        dialog = new Dialog(this);


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent intent;
        switch (id) {
            case R.id.home:
                intent = new Intent(ClothingActivity.this, HomePage.class);
                startActivity(intent);
                break;
            case R.id.add:
                View view = menuItem.getActionView();
                showAddClothingPopup(view);
                break;
            case R.id.delete:
                //intent = new Intent(ClothingActivity.this,OutfitActivity.class);
                //startActivity(intent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.clothingDrawerLayoutId);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showAddClothingPopup(View v) {

        TextView txtclose;
        Button btnAdd;
        CardView camera;
        Clothing entry;
        dialog.setContentView(R.layout.add_clothing_popup);
        spinnerType = dialog.findViewById(R.id.type);
        spinnerColor = dialog.findViewById(R.id.color);
        spinnerCondition = dialog.findViewById(R.id.condition);

        spinnerType.setAdapter(new ArrayAdapter<Clothing.clothingType>(this, R.layout.support_simple_spinner_dropdown_item, Clothing.clothingType.values()));
        spinnerCondition.setAdapter(new ArrayAdapter<Clothing.clothingCondition>(this, R.layout.support_simple_spinner_dropdown_item, Clothing.clothingCondition.values()));
        spinnerColor.setAdapter(new ArrayAdapter<Clothing.clothingColor>(this, R.layout.support_simple_spinner_dropdown_item, Clothing.clothingColor.values()));

        final TextInputLayout textInputName = dialog.findViewById(R.id.text_input_ClothingName);
        final TextInputLayout textInputLocation = dialog.findViewById(R.id.text_input_location);
        btnAdd = (Button) dialog.findViewById(R.id.addButton);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = (Clothing.clothingType) spinnerType.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color = (Clothing.clothingColor) spinnerColor.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                condition = (Clothing.clothingCondition) spinnerCondition.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateName(textInputName)) {

                    return;
                }


                Clothing clothing = new Clothing(textInputName.getEditText().getText().toString().trim(), imageFile.getPath(), 0, type, color, condition, 0, 0);
                clothingList.add(clothing);
                manager.addClothing(clothing);


                dialog.dismiss();
            }
        });

        camera = (CardView) dialog.findViewById(R.id.cameraViewClothing);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Camera.class);
                startActivityForResult(i, 1);

            }
        });

        txtclose = (TextView) dialog.findViewById(R.id.txtclose);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                imageFile = (File) data.getSerializableExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //No picture was taken
            }
        }
    }


    private boolean validateName(TextInputLayout inputname) {

        String name = inputname.getEditText().getText().toString().trim();

        if (name.isEmpty()) {
            inputname.setError("Enter a name.");
            return false;
        } else {

            inputname.setError(null);
            return true;
        }
    }

    private boolean validateLocation(TextInputLayout inputLocation) {

        String description = inputLocation.getEditText().getText().toString().trim();

        if (description.isEmpty()) {
            inputLocation.setError("Enter a location.");
            return false;
        } else {

            inputLocation.setError(null);
            return true;
        }
    }

}
