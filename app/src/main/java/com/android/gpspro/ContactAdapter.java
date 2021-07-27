package com.android.gpspro;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.gpspro.Fragment.FragmentPage4;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private static final String TAG = "ContactsAdapter";
    private FragmentPage4 fragment4;
    private List<Contact> contacts;
    private NoteAdapter.OnItemLongClickListener listenerr;
    public void addItems(List<Contact> contacts){
        this.contacts = contacts;
    }

    public void addItem(Contact contact){
        contacts.add(contact);
    }

    public ContactAdapter(FragmentPage4 fragment4, List<Contact> contacts) {
        this.fragment4 = fragment4;
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

            if(profileURL == null || profileURL.equals("") ){ // 사진 업로드를 하지 않으면 기본 값 null
                ivProfile.setImageResource(R.drawable.ic_person);
            }else{
                ImageUpload.setImage(profileURL, ivProfile);
            }
        }
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, final int position) {
        // 컬렉션 증가 변화에만 반응함.
        final Contact contact = contacts.get(position);
        holder.setItem(contact.getName(), contact.getEmail(), contact.getProfileURL());

        // 데이터 바인딩할 때 이벤트 달기
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment4.editContactDialog(contact);
            }
        });

        holder.itemView.setOnLongClickListener (new View.OnLongClickListener () {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


    public class OnItemLongClickListener {
    }
}
