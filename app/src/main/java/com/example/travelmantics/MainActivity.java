 package com.example.travelmantics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase cFirebaseDatabase;
    private DatabaseReference cDatabaseReference;
    EditText textTitle;
    EditText textDescription;
    EditText textPrice;
    TravelDeal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUtil.openFbReference("Travel Deals");
        cFirebaseDatabase = FirebaseUtil.cFirebaseDatabase;
        cDatabaseReference = FirebaseUtil.cDatabaseReference;

        textTitle = (EditText) findViewById(R.id.textTitle);
        textDescription = (EditText) findViewById(R.id.textDescription);
        textPrice = (EditText) findViewById(R.id.textPrice);
        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if (deal==null) {
            deal = new TravelDeal();
        }
        this.deal = deal;
        textTitle.setText(deal.getTitle());
        textDescription.setText(deal.getDescription());
        textPrice.setText(deal.getPrice());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                saveDeal();
                Toast.makeText(this, "Deal Saved", Toast.LENGTH_LONG).show();
                clean();
                backToList();
                return true;
            case R.id.delete_menu:
                deleteDeal();
                Toast.makeText(this, "Deal Deleted", Toast.LENGTH_LONG).show();
                backToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        if (FirebaseUtil.isAdmin) {
            menu.findItem(R.id.delete_menu).setVisible(true);
            menu.findItem(R.id.save_menu).setVisible(true);
            enableEditTexts(true);
        }
        else {
            menu.findItem(R.id.delete_menu).setVisible(false);
            menu.findItem(R.id.save_menu).setVisible(false);
            enableEditTexts(false);
        }

        return true;
    }

    private void saveDeal() {
        deal.setTitle(textTitle.getText().toString());
        deal.setDescription(textDescription.getText().toString());
        deal.setPrice(textPrice.getText().toString());
        if(deal.getId()==null) {
            cDatabaseReference.push().setValue(deal);
        }
        else {
            cDatabaseReference.child(deal.getId()).setValue(deal);
        }
    }
    private void deleteDeal() {
        if (deal == null) {
            Toast.makeText(this, "Please save the deal before deleting", Toast.LENGTH_SHORT).show();
            return;
        }
        cDatabaseReference.child(deal.getId()).removeValue();


    }
    private void backToList() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    private void clean() {
        textTitle.setText("");
        textDescription.setText("");
        textPrice.setText("");
        textTitle.requestFocus();


    }
    private void enableEditTexts(boolean isEnabled) {
        textTitle.setEnabled(isEnabled);
        textDescription.setEnabled(isEnabled);
        textPrice.setEnabled(isEnabled);
    }

}
