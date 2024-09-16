package com.example.ninjafleet.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ninjafleet.R;
import com.example.ninjafleet.models.OnboardingModel;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder> {

    private List<OnboardingModel> onboardingModelList;

    public OnboardingAdapter(List<OnboardingModel> onboardingModelList) {
        this.onboardingModelList = onboardingModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.onboarding_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setOnboardingData(onboardingModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return onboardingModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textTitle;
        private TextView textDescription;
        private ImageView imageOnboarding;

        public ViewHolder(View inflate) {
            super(inflate);

            textTitle = inflate.findViewById(R.id.textTitle);
            textDescription = inflate.findViewById(R.id.textDescription);
            imageOnboarding = inflate.findViewById(R.id.imageOnboarding);
        }

        void  setOnboardingData(OnboardingModel onboardingData){
            textTitle.setText(onboardingData.getTitle());
            textDescription.setText(onboardingData.getDescription());
            imageOnboarding.setImageResource(onboardingData.getImagge());
        }
    }
}
