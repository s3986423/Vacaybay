package com.example.vacaybay.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacaybay.Activity.Adapter.BookingAdminAdapter;
import com.example.vacaybay.Activity.Models.Booking;
import com.example.vacaybay.R;
import com.example.vacaybay.databinding.ActivityAdminBookingBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminBooking extends AppCompatActivity {
    private RecyclerView bookingsRecyclerView;
    private BookingAdminAdapter bookingAdminAdapter;
    private List<Booking> bookingList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private FirebaseFirestore firestore;
    ActivityAdminBookingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Realtime Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        bookingsRecyclerView = findViewById(R.id.recyclerViewBookings);
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingAdminAdapter = new BookingAdminAdapter(this, bookingList);
        bookingsRecyclerView.setAdapter(bookingAdminAdapter);

        // Fetch bookings from Firebase
        fetchBookings();

    }

    private void fetchBookings() {
        // Query Firestore for all bookings (no userID filter)
        firestore.collection("bookings")
                .get()  // Get all documents in the "bookings" collection
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Clear the current list
                        bookingList.clear();

                        // Add bookings to the list
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Booking booking = document.toObject(Booking.class);
                            bookingList.add(booking);
                        }

                        // Notify the adapter about the data change
                        bookingAdminAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AdminBooking.this, "No bookings found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminBooking.this, "Failed to load bookings.", Toast.LENGTH_SHORT).show();
                });
    }



}
