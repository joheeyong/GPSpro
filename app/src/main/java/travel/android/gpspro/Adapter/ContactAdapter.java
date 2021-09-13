package travel.android.gpspro.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.gpspro.R;

import java.util.List;

import travel.android.gpspro.DB.Entity.Contact;
import travel.android.gpspro.Fragment.FragmentPage4;
import travel.android.gpspro.Other.ImageUpload;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private static final String TAG = "ContactsAdapter";
    private FragmentPage4 fragment4;
    private List<Contact> contacts;
    private ContactAdapter.OnItemLongClickListener listenerr;
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
                ivProfile.setImageResource(R.drawable.logopic);
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
        final Contact contact = contacts.get(position);
        holder.setItem(contact.getName(), String.valueOf (contact.getIdd()), contact.getProfileURL());

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
