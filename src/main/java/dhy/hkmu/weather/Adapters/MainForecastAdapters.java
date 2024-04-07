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

import dhy.hkmu.weather.Domains.MainForecast;
import dhy.hkmu.weather.R;

public class MainForecastAdapters extends RecyclerView.Adapter<MainForecastAdapters.viewHolder> {
    ArrayList<MainForecast> items;
    Context context;


    public MainForecastAdapters(ArrayList<MainForecast> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MainForecastAdapters.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_main_forecast,parent,false);
        context= parent.getContext();
        return new viewHolder(inflate);
    }


    @Override
    public void onBindViewHolder(@NonNull MainForecastAdapters.viewHolder holder, int position) {
        holder.dayTxt.setText(items.get(position).getDay());
        holder.tempTxt.setText(items.get(position).getTemp());
        String picPath = items.get(position).getPicPath();

        int resourceId = holder.itemView.getResources().getIdentifier(picPath, "drawable", holder.itemView.getContext().getPackageName());
        holder.pic.setImageResource(resourceId);




    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView dayTxt,tempTxt;
        ImageView pic;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            dayTxt=itemView.findViewById(R.id.dayTxt);
            tempTxt=itemView.findViewById(R.id.tempTxt);
            pic=itemView.findViewById(R.id.pic);
        }
    }
}
