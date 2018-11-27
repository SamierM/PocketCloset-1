package com.example.michael.myapplication;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class HomePage extends AppCompatActivity implements View.OnClickListener{

    private CardView closetCard, clothesCard, outfitCard, searchCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        closetCard = (CardView) findViewById(R.id.closet_card);
        clothesCard = (CardView) findViewById(R.id.clothes_card);
        outfitCard = (CardView) findViewById(R.id.outfit_card);
        searchCard = (CardView) findViewById(R.id.search_card);

        closetCard.setOnClickListener(this);
        clothesCard.setOnClickListener(this);
        outfitCard.setOnClickListener(this);
        searchCard.setOnClickListener(this);

        Clothing clothing1 = new Clothing("Clothing1", Clothing.clothingType.other);
        Clothing clothing2 = new Clothing("Clothing2", 0);
//        SQLiteDatabase theDatabase = openOrCreatDatabase("");
        DBManager manager = new DBManager( this, "Database", null, 0);
        System.out.println("We got here");
        manager.addClothing(clothing1);
        manager.addClothing(clothing2);
        Clothing clothing3 = manager.getClothing("Clothing1");
        System.out.println("We got past the get from database");
        System.out.println("Item Name: " + clothing3.getClothingName());
        //clothing3 = manager.getClothing("Nothing");
        System.out.println("Item Name: " + clothing3.getClothingName());




    }


    @Override
    public void onClick(View v) {
        Intent i;

        switch(v.getId()){
            case R.id.closet_card :
                i = new Intent(this,AllClosetsActivity.class);
                startActivity(i);
                break;
            case R.id.clothes_card :
                i = new Intent(this,ClothingActivity.class);
                startActivity(i);
                break;
            case R.id.outfit_card :
                i = new Intent(this,OutfitActivity.class);
                startActivity(i);
                break;
            case R.id.search_card :
                i = new Intent(this,Search.class);
                startActivity(i);
                break;
            default:
                break;
        }


    }
}
