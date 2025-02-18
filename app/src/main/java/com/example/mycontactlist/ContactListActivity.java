package com.example.mycontactlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {

    private ArrayList<Contact> contacts; // Store contact objects instead of names

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int contactId = contacts.get(position).getContactID(); // Retrieve the contact ID

            // Pass contact ID to MainActivity
            Intent intent = new Intent(ContactListActivity.this, MainActivity.class);
            intent.putExtra("contactID", contactId);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        // Initialize navigation buttons
        initListButton();
        initMapButton();
        initSettingsButton();

        // Retrieve contacts from the database
        ContactDataSource ds = new ContactDataSource(this);

        try {
            ds.open();
            contacts = ds.getContacts(); // Retrieve full contact objects
            ds.close();

            // Set up RecyclerView
            RecyclerView contactList = findViewById(R.id.rvContacts);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            contactList.setLayoutManager(layoutManager);

            // Initialize Adapter with contacts and set click listener
            ContactAdapter contactAdapter = new ContactAdapter(contacts);
            contactAdapter.setOnItemClickListener(onItemClickListener);
            contactList.setAdapter(contactAdapter);

        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
        }
    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setEnabled(false); // Disable List button in this activity
        ibList.setOnClickListener(v -> {
            Intent intent = new Intent(ContactListActivity.this, ContactListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void initMapButton() {
        ImageButton ibMap = findViewById(R.id.imageButtonMap);
        ibMap.setOnClickListener(v -> {
            Intent intent = new Intent(ContactListActivity.this, ContactMapActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setOnClickListener(v -> {
            Intent intent = new Intent(ContactListActivity.this, ContactSettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}