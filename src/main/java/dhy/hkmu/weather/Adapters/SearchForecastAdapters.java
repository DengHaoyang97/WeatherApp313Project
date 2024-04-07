package dhy.hkmu.weather.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import dhy.hkmu.weather.Domains.SearchForecast;
import dhy.hkmu.weather.R;

public class SearchForecastAdapters extends RecyclerView.Adapter<SearchForecastAdapters.viewHolder> {
    ArrayList<SearchForecast> items;
    Context context;


    public SearchForecastAdapters(ArrayList<SearchForecast> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public SearchForecastAdapters.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_search_forecast,parent,false);
        context= parent.getContext();
        return new viewHolder(inflate);
    }


    @Override
    public void onBindViewHolder(@NonNull SearchForecastAdapters.viewHolder holder, int position) {



        holder.dayOfWeek.setText(items.get(position).getDay());
        holder.temp.setText(items.get(position).getTemp());
        holder.description.setText(items.get(position).getForecastDescription());
        String picPath = items.get(position).getPicPath();

        int resourceId = holder.itemView.getResources().getIdentifier(picPath, "drawable", holder.itemView.getContext().getPackageName());
        holder.pic.setImageResource(resourceId);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView dayOfWeek,temp,description;
        ImageView pic;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfWeek=itemView.findViewById(R.id.search_day_of_week);
            temp=itemView.findViewById(R.id.search_forecast_temp);
            description=itemView.findViewById(R.id.search_forecast_description);
            pic=itemView.findViewById(R.id.search_forecast_icon);

        }
    }
}
