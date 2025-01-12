package com.example.vacaybay.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vacaybay.Activity.Domain.Category;
import com.example.vacaybay.Activity.Models.Booking;
import com.example.vacaybay.R;
import com.example.vacaybay.databinding.ViewholderCategoryBinding;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private Context context;
    private List<Booking> bookingList;

    public BookingAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.titleTextView.setText(booking.getTitle());
        holder.addressTextView.setText(booking.getAddress());
        holder.priceTextView.setText("$" + booking.getPrice());
        holder.dateTextView.setText(booking.getDate());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, addressTextView, priceTextView, dateTextView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}

