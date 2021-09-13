package travel.android.gpspro.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.gpspro.R;

import travel.android.gpspro.DB.Entity.Travel;

public class TravelAdapter extends ListAdapter<Travel, TravelAdapter.TravelHolder> {

    private static final DiffUtil.ItemCallback<Travel> DIFF_CALLBACK = new DiffUtil.ItemCallback<Travel>() {
        @Override
        public boolean areItemsTheSame(@NonNull Travel oldItem, @NonNull Travel newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Travel oldItem, @NonNull Travel newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription());
        }
    };
    private OnItemClickListener listener;
    private OnItemLongClickListener listenerr;

    public TravelAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public TravelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new TravelHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelHolder holder, int position) {
        Travel currentTravel = getItem(position);
        holder.textViewTitle.setText(currentTravel.getTitle());
        holder.textViewDescription.setText(currentTravel.getDescription());
        holder.textViewDate.setText(currentTravel.getDate ());
        holder.textViewStar.setText(String.valueOf(currentTravel.getStar ()));
        Double a = Double.valueOf ((String.valueOf(currentTravel.getStar ())));
        if (a <=1.5){
            holder.textViewComent.setText ("별로예요...");
        }else if (a <=2.5){
            holder.textViewComent.setText ("아쉬워요..");
        }else if (a <=3.5){
            holder.textViewComent.setText ("보통이예요");
        }else if (a <=4.5){
            holder.textViewComent.setText ("좋았어요");
        }else {
            holder.textViewComent.setText ("최고예요!");
        }
    }




    public Travel getTravelAt(int position) {
        return getItem(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listenerr) {
        this.listenerr = listenerr;
    }

    public interface OnItemLongClickListener {

        void onItemLongClick(Travel note);
    }
    public interface OnItemClickListener {
        void onItemClick(Travel note);
    }

    class TravelHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewDate;
        private TextView textViewStar;
        private TextView textViewComent;

        public TravelHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewStar = itemView.findViewById(R.id.text_view_star);
            textViewComent =  itemView.findViewById(R.id.coment);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
            itemView.setOnLongClickListener (new View.OnLongClickListener () {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (listenerr != null && position != RecyclerView.NO_POSITION) {
                        listenerr.onItemLongClick(getItem(position));
                    }
                    return false;
                }
            });
        }
    }

}
