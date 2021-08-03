package com.android.gpspro.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.gpspro.DB.Entity.Contact;
import com.android.gpspro.Activity.PhotoViewActivity;
import com.android.gpspro.Other.ImageUpload;
import com.android.gpspro.R;

import java.util.List;

public class ContactAdapterPhoto extends RecyclerView.Adapter<ContactAdapterPhoto.ViewHolder> {

    private static final String TAG = "ContactsAdapterPhoto";
    private PhotoViewActivity photoViewActivity;
    private List<Contact> contacts;




//    public void addItems(List<Contact> contacts){
//        this.contacts = contacts;
//    }
//
//    public void addItem(Contact contact){
//        contacts.add(contact);
//    }

    public ContactAdapterPhoto(PhotoViewActivity photoViewActivity, List<Contact> contacts) {

        this.photoViewActivity = photoViewActivity;
        this.contacts = contacts;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        TextView tvEmail;
        ImageView ivProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder: ");
            tvName = itemView.findViewById(R.id.tv_name);
            tvEmail = itemView.findViewById(R.id.tv_email);
            ivProfile = itemView.findViewById(R.id.iv_profile);
        }

        void setItem(String name, String email, String profileURL){
            Log.d(TAG, "setItem: ");
            tvName.setText(name);
            tvEmail.setText(email);

            if(profileURL == null || profileURL.equals("") ){
                ivProfile.setImageResource(R.drawable.logopic);
            }else{
                ImageUpload.setImage(profileURL, ivProfile);
            }
        }
    }

    @NonNull
    @Override
    public ContactAdapterPhoto.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_two, parent, false);
        return new ContactAdapterPhoto.ViewHolder (itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapterPhoto.ViewHolder holder, final int position) {
        final Contact contact = contacts.get(position);
        holder.setItem(contact.getName(), String.valueOf (contact.getIdd()), contact.getProfileURL());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
