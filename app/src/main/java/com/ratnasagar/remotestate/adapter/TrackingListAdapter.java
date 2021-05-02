package com.ratnasagar.remotestate.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ratnasagar.remotestate.R;
import com.ratnasagar.remotestate.UserLocation;
import com.ratnasagar.remotestate.model.TrackingData;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TrackingListAdapter extends RecyclerView.Adapter<TrackingListAdapter.ViewHolder> {

    ArrayList<TrackingData> trackingData;
    Context context;
    public TrackingListAdapter(ArrayList<TrackingData> trackingData, Context context) {
        this.trackingData = trackingData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracking_item,parent,false);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvLatitude.setText(trackingData.get(position).getLatitude());
        holder.tvLongitude.setText(trackingData.get(position).getLongitude());
        holder.tvTrackingTime.setText(trackingData.get(position).getTrackingTime());
        holder.tvAddress.setText(trackingData.get(position).getAddress());

        holder.llFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserLocation.class);
                intent.putExtra("latitude",trackingData.get(position).getLatitude());
                intent.putExtra("longitude",trackingData.get(position).getLongitude());
                intent.putExtra("trackingTime",trackingData.get(position).getTrackingTime());
                intent.putExtra("address",trackingData.get(position).getAddress());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackingData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLatitude,tvLongitude,tvTrackingTime,tvAddress;
        LinearLayout llFull;
        public ViewHolder(View itemView) {
            super(itemView);
            tvLatitude=itemView.findViewById(R.id.tvLatitude);
            tvLongitude=itemView.findViewById(R.id.tvLongitude);
            tvTrackingTime=itemView.findViewById(R.id.tvTrackingTime);
            tvAddress=itemView.findViewById(R.id.tvAddress);
            llFull=itemView.findViewById(R.id.llFull);
        }
    }

}

