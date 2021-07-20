package com.android.gpspro;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
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

//                    oldItem.getLat().equals(newItem.getLat()) &&
//                    oldItem.getLng().equals(newItem.getLng()) ;
        }
    };

    //private List<Note> notes = new ArrayList<>();
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
//        holder.textViewLat.setText(String.valueOf(currentNote.getLat()));
//        holder.textViewLng.setText(String.valueOf(currentNote.getLng()));


    }



    public Place getPlaceAt(int position) {
        return getItem(position);
    }

    public void setOnItemClickListener(PlaceAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Place place);
    }


    class PlaceHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewCount;
        private TextView textViewLat;
        private TextView textViewLng;
        private TextView textViewPriority;

        public PlaceHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
//              textViewPriority = itemView.findViewById(R.id.text_view_priority);

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
