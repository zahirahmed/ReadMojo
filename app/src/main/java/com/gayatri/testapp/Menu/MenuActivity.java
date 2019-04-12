package com.gayatri.testapp.Menu;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gayatri.testapp.AllBooksListActivity;
import com.gayatri.testapp.AudioActivity;
import com.gayatri.testapp.CreateAudioBook;
import com.gayatri.testapp.CreateBooksActivity;
import com.gayatri.testapp.CreateImageBook;
import com.gayatri.testapp.R;

import com.gayatri.testapp.TestActivity;
import com.gayatri.testapp.TextActivity;
import com.gayatri.testapp.Utils.MyTextView;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class MenuActivity extends AppCompatActivity {

    public Button audiobook_ll,textbook_ll,testbook_ll,imagebook_ll,allbook_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        testbook_ll = (Button)findViewById(R.id.testbook_ll);
        audiobook_ll = (Button)findViewById(R.id.audiobook_ll);
        imagebook_ll = (Button)findViewById(R.id.imagebook_ll);
        textbook_ll = (Button)findViewById(R.id.textbook_ll);
        allbook_ll = (Button)findViewById(R.id.allbook_ll);


        textbook_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MenuActivity.this, TextActivity.class);
                startActivity(i);
            }
        });

        audiobook_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MenuActivity.this,CreateBooksActivity.class);
                startActivity(i);
            }
        });
        allbook_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MenuActivity.this,AllBooksListActivity.class);
                startActivity(i);
            }
        });

        testbook_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MenuActivity.this, TestActivity.class);
                startActivity(i);
            }
        });

        imagebook_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MenuActivity.this, CreateImageBook.class);
                startActivity(i);
            }
        });
        // Create an icon
        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.float_main);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        // repeat many times:
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageResource(R.drawable.share_profile);

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.drawable.report_icon);

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.drawable.floatbt2);

        SubActionButton button1 = itemBuilder.setContentView(itemIcon1).build();
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();

        //attach the sub buttons to the main button
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .attachTo(actionButton)
                .build();


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
