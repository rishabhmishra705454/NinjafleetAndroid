package com.example.ninjafleet.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ninjafleet.R;
import com.example.ninjafleet.models.GetBookingResponse;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<GetBookingResponse.Datum> bookingList;

    public BookingAdapter(List<GetBookingResponse.Datum> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        GetBookingResponse.Datum booking = bookingList.get(position);
        holder.tvBookingId.setText("Booking ID: " + booking.getId());
        holder.tvMachineryId.setText("Machinery ID: " + booking.getMachineryId());
        holder.tvProviderId.setText("Provider ID: " + booking.getProviderId());
        holder.tvLandArea.setText("Land Area: " + booking.getLandArea() + " acres");
        holder.tvDates.setText("Dates: " + booking.getStartDate() + " to " + booking.getEndDate());
        holder.tvStatus.setText("Status: " + booking.getStatus());
        holder.tvTotalAmount.setText("Total Amount: ₹" + booking.getTotalAmount());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookingId, tvMachineryId, tvProviderId, tvLandArea, tvDates, tvStatus, tvTotalAmount;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingId = itemView.findViewById(R.id.tv_booking_id);
            tvMachineryId = itemView.findViewById(R.id.tv_machinery_id);
            tvProviderId = itemView.findViewById(R.id.tv_provider_id);
            tvLandArea = itemView.findViewById(R.id.tv_land_area);
            tvDates = itemView.findViewById(R.id.tv_dates);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvTotalAmount = itemView.findViewById(R.id.tv_total_amount);
        }
    }
}
