package com.example.mycontactlist;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
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

    private ArrayList<Contact> contacts;
    private ContactAdapter contactAdapter;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int contactId = contacts.get(position).getContactID(); // Retrieve the contact ID

            //contactID to main
            Intent intent = new Intent(ContactListActivity.this, MainActivity.class);
            intent.putExtra("contactID", contactId);
            //add Email here?
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initListButton();
        initMapButton();
        initSettingsButton();
        initAddContactButton();
        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                double levelScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                int batteryPercent = (int) Math.floor(batteryLevel / levelScale * 100);
                TextView textBatteryState = (TextView)findViewById(R.id.textBatteryLevel);
                //TextView textMainBatteryState = (TextView)findViewById(R.id.textMainBatteryLevel);
                textBatteryState.setText(batteryPercent + "%");
                //textMainBatteryState.setText(batteryPercent + "%");

            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);


        String sortBy = getSharedPreferences("MyContactListPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "contactname");
        String sortOrder = getSharedPreferences("MyContactListPreferences",
                Context.MODE_PRIVATE).getString("sortorder", "ASC");

        // contacts from base
        ContactDataSource ds = new ContactDataSource(this);

        try {
            ds.open();
            contacts = ds.getContacts(sortBy, sortOrder); //contact objects
            ds.close();

            //Recycler
            RecyclerView contactList = findViewById(R.id.rvContacts);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            contactList.setLayoutManager(layoutManager);


            contactAdapter = new ContactAdapter(contacts, this);
            contactAdapter.setOnItemClickListener(onItemClickListener);
            contactList.setAdapter(contactAdapter);

        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
        }

        initDeleteSwitch();
        //initDeleteContactButton();
    }
//    public void onResume(){
//        super.onResume();
//        String sortBy = getSharedPreferences("MyCont")
//    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setEnabled(false); //disable
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
    private void initAddContactButton(){
        Button newContact = findViewById(R.id.buttonAddContact);
        newContact.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ContactListActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initDeleteSwitch(){
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Boolean status = compoundButton.isChecked();
                contactAdapter.setDelete(status);
                contactAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
            String sortBy = getSharedPreferences("MyContactListPreferences",
                    Context.MODE_PRIVATE).getString("sortfield", "contactname");
            String sortOrder = getSharedPreferences("MyContactListPreferences",
                    Context.MODE_PRIVATE).getString("sortorder", "ASC");

            // contacts from base
            ContactDataSource ds = new ContactDataSource(this);

            try {
                ds.open();
                contacts = ds.getContacts(sortBy, sortOrder); //contact objects
                ds.close();
                if(contacts.size() > 0){

                //Recycler
                RecyclerView contactList = findViewById(R.id.rvContacts);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                contactList.setLayoutManager(layoutManager);


                contactAdapter = new ContactAdapter(contacts, this);
                contactAdapter.setOnItemClickListener(onItemClickListener);
                contactList.setAdapter(contactAdapter);

            }
            else{
                    Intent intent = new Intent(ContactListActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            } catch (Exception e) {
                Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
            }
        }

}