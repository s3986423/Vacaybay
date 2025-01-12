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

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        bookingsRecyclerView = findViewById(R.id.bookingRecyclerView);
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingAdapter = new BookingAdapter(this, bookingList);
        bookingsRecyclerView.setAdapter(bookingAdapter);

        // Fetch bookings from Firestore
        fetchBookings();

        setupBottomNavigation();
    }

    private void fetchBookings() {
        String userID = mAuth.getCurrentUser().getUid();

        firestore.collection("bookings")
                .whereEqualTo("userID", userID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        bookingList.clear();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Booking booking = document.toObject(Booking.class);
                            bookingList.add(booking);
                        }

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
//                        startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
                } else if (id == R.id.cart) {
//                    startActivity(new Intent(MyBookingActivity.this, MyBookingActivity.class));
//                    finish();
                } else if (id == R.id.promotion) {
                    startActivity(new Intent(MyBookingActivity.this, PromotionsActivity.class));
                } else if (id == R.id.profile) {
//                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }
            }
        });
    }
}
