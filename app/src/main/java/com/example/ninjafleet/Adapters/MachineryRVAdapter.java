package com.example.ninjafleet.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ninjafleet.R;
import com.example.ninjafleet.models.MachineryModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class MachineryRVAdapter extends RecyclerView.Adapter<MachineryRVAdapter.ViewHolder> {

    private List<MachineryModel.Machinery> machineryList;
    private Context context;

    public MachineryRVAdapter(Context context ,List<MachineryModel.Machinery> machineryList) {
        this.context = context;
        this.machineryList = machineryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.machinery_rv_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MachineryModel.Machinery machinery = machineryList.get(position);
        holder.machineryName.setText(machinery.getMachineryName());
        holder.machineryCategory.setText(machinery.getCategory());
        holder.pricing.setText( "\u20B9"+ machinery.getPricing()+" /Hour");
        Picasso.get().load("https://ninjafleet-gweyhfetapb5eugk.centralindia-01.azurewebsites.net/"+machinery.getImages().get(0)).into(holder.machineryImage);

        holder.bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) machinery);
                Navigation.findNavController(holder.itemView).navigate(R.id.machineryDetailsFragment,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return machineryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView machineryName , machineryCategory, pricing;
        Button bookBtn;
        ImageView machineryImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            machineryName = itemView.findViewById(R.id.machineryName);
            machineryCategory = itemView.findViewById(R.id.categoryName);
            pricing = itemView.findViewById(R.id.pricing);
            bookBtn = itemView.findViewById(R.id.bookBtn);
            machineryImage = itemView.findViewById(R.id.machineryImage);
        }
    }
}
