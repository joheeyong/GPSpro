package com.android.gpspro.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.gpspro.DB.Entity.Account;
import com.android.gpspro.R;

import java.text.DecimalFormat;
public class AccountAdapter extends ListAdapter<Account, AccountAdapter.NoteHolder> {

    private static final DiffUtil.ItemCallback<Account> DIFF_CALLBACK = new DiffUtil.ItemCallback<Account>() {
        @Override
        public boolean areItemsTheSame(@NonNull Account oldItem, @NonNull Account newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Account oldItem, @NonNull Account newItem) {
            return oldItem.getTime().equals(newItem.getTime()) &&
                    oldItem.getCategory().equals(newItem.getCategory()) &&
                    oldItem.getPrice() == newItem.getPrice();
        }
    };

    private OnItemClickListener listener;
    public AccountAdapter() {
        super(DIFF_CALLBACK);
    }
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_item, parent, false);
        return new NoteHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Account currentNote = getItem(position);

        holder.textViewTitle.setText (currentNote.getTime());
        holder.textViewDescription.setText(currentNote.getCategory ());
        long value = Long.parseLong(String.valueOf(currentNote.getPrice()));
        DecimalFormat format = new DecimalFormat("###,###");
        format.format(value);
        String resilt_int =format.format (value);
        holder.textViewPriority.setText(resilt_int+" 원");
        String a ="숙박";
        String b ="교통";
        String c ="식비";
        String d ="쇼핑";
        String e ="관광";
        String f ="기타";
        if (a.equals (currentNote.getCategory ())){
            holder.iv_pick.setBackgroundResource (R.drawable.ic_tnrqkrcol);
        } else if (b.equals (currentNote.getCategory ())){
            holder.iv_pick.setBackgroundResource (R.drawable.ic_trainnbla);
        } else if (c.equals (currentNote.getCategory ())){
            holder.iv_pick.setBackgroundResource (R.drawable.ic_pokeblack);
        } else if (d.equals (currentNote.getCategory ())){
            holder.iv_pick.setBackgroundResource (R.drawable.ic_shopbla);
        } else if (e.equals (currentNote.getCategory ())){
            holder.iv_pick.setBackgroundResource (R.drawable.ic_rndwjsbla);
        } else if (f.equals (currentNote.getCategory ())){
            holder.iv_pick.setBackgroundResource (R.drawable.ic_nulllbla);
        }

    }

    public Account getNoteAt(int position) {
        return getItem(position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(Account note);
    }
    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;
        private ImageView iv_pick;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            iv_pick = itemView.findViewById(R.id.iv_pick);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }
}
