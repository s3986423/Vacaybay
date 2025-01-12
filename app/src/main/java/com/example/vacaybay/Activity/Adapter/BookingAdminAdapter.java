package com.example.vacaybay.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacaybay.Activity.Models.Booking;
import com.example.vacaybay.R;

import java.util.List;

public class BookingAdminAdapter extends RecyclerView.Adapter<BookingAdminAdapter.BookingAdminViewHolder> {
    private Context context;
    private List<Booking> bookingList;

    public BookingAdminAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.booking_list_card, parent, false);
        return new BookingAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdminViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        // Set the data for each view based on the booking object
        holder.titleTextView.setText(booking.getTitle());
        holder.customerEmailTextView.setText(booking.getUserID());
        holder.bedsTextView.setText(booking.getBed() + " Beds");
        holder.durationTextView.setText(booking.getDuration() + " days");
        holder.nameTextView.setText(booking.getTourGuideName());
        holder.priceTextView.setText("$" + booking.getPrice());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingAdminViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, customerEmailTextView, bedsTextView, durationTextView;
        TextView nameTextView, priceTextView;

        public BookingAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            customerEmailTextView = itemView.findViewById(R.id.cusomter_email);
            bedsTextView = itemView.findViewById(R.id.booking_beds);
            durationTextView = itemView.findViewById(R.id.duration);
            nameTextView = itemView.findViewById(R.id.name);
            priceTextView = itemView.findViewById(R.id.price);
        }
    }
}
