package com.android.gpspro.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.gpspro.DB.Entity.Place;
import com.android.gpspro.R;

import org.w3c.dom.Text;

public class PlaceAdapter extends ListAdapter<Place, PlaceAdapter.PlaceHolder> {
    private static final int RESULT_OK = 1;
    private final int GET_GALLERY_IMAGE = 200;
    private static final DiffUtil.ItemCallback<Place> DIFF_CALLBACK = new DiffUtil.ItemCallback<Place>() {


        @Override
        public boolean areItemsTheSame(@NonNull Place oldItem, @NonNull Place newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Place oldItem, @NonNull Place newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription());
        }
    };

    private PlaceAdapter.OnItemClickListener listener;
    public PlaceAdapter() {
        super(DIFF_CALLBACK);
    }
    @NonNull
    @Override
    public PlaceAdapter.PlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_item, parent, false);
        return new PlaceAdapter.PlaceHolder (itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.PlaceHolder holder, int position) {
        Place currentNote = getItem(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription ());
        String lat= String.valueOf (currentNote.getLat ());
        holder.tv_latlist.setText (lat);
        String lng= String.valueOf (currentNote.getLng());
        holder.tv_lnglist.setText (lng);
    }



//    public Place getPlaceAt(int position) {
//        return getItem(position);
//    }

    public void setOnItemClickListener(PlaceAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

//    public void setOnItemLongClickListener(PlaceAdapter.OnItemLongClickListener listener) {
//        this.listener = (OnItemClickListener) listener;
//    }

    public interface OnItemClickListener {
        void onItemClick(Place place);
    }


    class PlaceHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView tv_latlist;
        private TextView tv_lnglist;

        public PlaceHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            tv_latlist = itemView.findViewById (R.id.tv_latlist);
            tv_lnglist = itemView.findViewById (R.id.tv_lnglist);

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
//
//    public interface OnItemLongClickListener {
//        void OnItemLongClick(Place place);
//    }
}
