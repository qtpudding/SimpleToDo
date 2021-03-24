package com.example.simpletodo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import static org.apache.commons.io.FileUtils.readLines;
import static org.apache.commons.io.FileUtils.writeLines;


public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;
    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // informs us that our main activity has been created

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //sets our activity_main file as the content of our activity
        //this is how we are able to see our UI in the application

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        //etItem.setText("I'm doing this fro Java");

        //how to make recycler view
        //renders a list of items
        //create adapter class and define the view holder
        //each item in our to do list is going to have its own text view
        //view holder will hold onto that text view and populate data from a model into the view

        loadItems();
        // construct adapter

        ItemsAdapter.OnLongClickListener onLongClickListener =  new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongCLicked(int position) {
                //delete the item from the model
                items.remove(position);
                // notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_LONG).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Signle click at position "+ position);
                //create the new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                //pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                //display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
        itemsAdapter =  new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                //add item to the model
                items.add(todoItem);
                //notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                //clear the text once submitted
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_LONG).show();
                saveItems();
            }
        });
    }

    //handle the result of the edit activity
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            //retrieve the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //update the model at the right position with new item text
            items.set(position,itemText);
            //notify the adapter
            itemsAdapter.notifyItemChanged(position);
            //persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successful", Toast.LENGTH_LONG).show();
        }
        else{
            Log.w("MainActivity", "Unkown call to onActivityResult");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    //this function will load items by reading every line of th data file
    private void loadItems(){
        try {
            items = new ArrayList<>(readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch(IOException e){
            Log.e("MainActivity","Error reading items", e);
            items = new ArrayList<>();
        }
    }
    //this function saves items by writing them into the data file
    private void saveItems(){
        try{
            writeLines(getDataFile(), items);
        }
        catch(IOException e){
            Log.e("MainActivity","Error writing items", e);
        }


    }
}
