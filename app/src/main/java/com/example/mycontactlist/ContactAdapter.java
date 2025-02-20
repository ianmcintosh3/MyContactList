package com.example.mycontactlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private ArrayList<Contact> contactData;
    private static View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewContact;
        public TextView textPhone;
        public Button deleteButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContact = itemView.findViewById(R.id.textContactName);
            textPhone = itemView.findViewById(R.id.textPhoneNumber);
            deleteButton = itemView.findViewById(R.id.buttonDeleteContact);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }

        public TextView getContactTextView() {
            return textViewContact;
        }

        public TextView getPhoneTextView() {
            return textPhone;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }
    }

    public ContactAdapter(ArrayList<Contact> contacts) {
        this.contactData = contacts;
    }
    public ContactAdapter(ArrayList<Contact> arrayList, Context context){
        this.contactData = arrayList;
        this.parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Contact currentContact = contactData.get(position);
        holder.getContactTextView().setText(currentContact.getContactName());
        holder.getPhoneTextView().setText(currentContact.getPhoneNumber());
        if(isDeleting) {
            holder.getDeleteButton().setVisibility(View.VISIBLE);
            holder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(position);
                }
            });
        }
        else{
            holder.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }
    public void setDelete(boolean b){
        isDeleting = b;

    }
    private void deleteItem(int position){
        Contact contact = contactData.get(position);
        ContactDataSource ds = new ContactDataSource(parentContext);
        try{
            ds.open();
            boolean didDelete = ds.deleteContact(contact.getContactID());
            ds.close();
            if(didDelete){
                contactData.remove(position);
                notifyDataSetChanged();
            }
            else{
                Toast.makeText(parentContext, "Delete Failed", Toast.LENGTH_LONG).show();

            }
        }
        catch(Exception e){
            Toast.makeText(parentContext, "Delete Failed", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public int getItemCount() {
        return contactData.size();
    }

}