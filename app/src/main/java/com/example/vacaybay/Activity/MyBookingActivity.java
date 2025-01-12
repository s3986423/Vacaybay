package com.example.vacaybay.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacaybay.Activity.Adapter.BookingAdapter;
import com.example.vacaybay.Activity.Models.Booking;
import com.example.vacaybay.R;
import com.example.vacaybay.databinding.ActivityMyBookingBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class MyBookingActivity extends AppCompatActivity {
    private RecyclerView bookingsRecyclerView;
    private BookingAdapter bookingAdapter;
    private List<Booking> bookingList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    ActivityMyBookingBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        // Set up RecyclerView
        bookingsRecyclerView = findViewById(R.id.bookingRecyclerView);
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingAdapter = new BookingAdapter(this, bookingList);
        bookingsRecyclerView.setAdapter(bookingAdapter);

        // Fetch bookings from Firestore
        fetchBookings();

        setupBottomNavigation();
    }

    private void fetchBookings() {
        // Get the current user's ID
        String userID = mAuth.getCurrentUser().getUid();

        // Query Firestore for bookings linked to the current user
        firestore.collection("bookings")
                .whereEqualTo("userID", userID)
                .get()
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
                        bookingAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MyBookingActivity.this, "No bookings found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MyBookingActivity.this, "Failed to load bookings.", Toast.LENGTH_SHORT).show();
                });
    }
    private void setupBottomNavigation() {
        binding.chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                if (id == R.id.explorer) {
                    startActivity(new Intent(MyBookingActivity.this, MainActivity.class));
                    Log.d("ChipNavigationBar", "Item selected: " + id);
                } else if (id == R.id.favorites) {
                    // Navigate to FavoritesActivity (create if needed)
//                        startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
                } else if (id == R.id.cart) {
//                    startActivity(new Intent(MyBookingActivity.this, MyBookingActivity.class));
//                    finish();
                } else if (id == R.id.promotion) {
                    // Navigate to PromotionsActivity
                    startActivity(new Intent(MyBookingActivity.this, PromotionsActivity.class));
                } else if (id == R.id.profile) {
                    // Navigate to ProfileActivity (create if needed)
//                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }
            }
        });
    }
}
