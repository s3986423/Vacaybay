package com.example.vacaybay.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.vacaybay.Activity.Domain.ItemDomain;
import com.example.vacaybay.Activity.Models.Booking;
import com.example.vacaybay.R;
import com.example.vacaybay.databinding.ActivityDetailBinding;
import com.example.vacaybay.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    private ItemDomain object;
    private Context context;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        getIntentExtra();
        setVariable();
    }

    private void setVariable(){
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("$"+object.getPrice());
        binding.backBtn.setOnClickListener(v -> finish());
        binding.bedTxt.setText(""+object.getBed());
        binding.durationTxt.setText(object.getDuration());
        binding.distanceTxt.setText(object.getDistance());
        binding.descriptionTxt.setText(object.getDescription());
        binding.addressTxt.setText(object.getAddress());
        binding.ratingTxt.setText(object.getScore()+" Rating");
        binding.ratingBar.setRating((float) object.getScore());

        Glide.with(DetailActivity.this)
                .load(object.getPic())
                .into(binding.pic);
        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
                Intent intent = new Intent(DetailActivity.this, TicketActivity.class);
                intent.putExtra("object", object);
                startActivity(intent);

            }
        });
    }
    private void getIntentExtra(){
        object = (ItemDomain) getIntent().getSerializableExtra("object");
    }

    private void addToCart() {
        // Check if the user is logged in
        if (mAuth.getCurrentUser() != null) {
            String userID = mAuth.getCurrentUser().getUid(); // Get current user's ID

            // Create a HashMap to store the booking details
            HashMap<String, Object> bookingMap = new HashMap<>();
            bookingMap.put("title", object.getTitle());
            bookingMap.put("address", object.getAddress());
            bookingMap.put("bed", object.getBed());
            bookingMap.put("date", object.getDateTour());
            bookingMap.put("duration", object.getDuration());
            bookingMap.put("tourGuideName", object.getTourGuideName()); // If applicable
            bookingMap.put("userID", userID); // Link to the current user

            // Store the booking in Firestore
            firestore.collection("bookings")
                    .add(bookingMap)
                    .addOnSuccessListener(documentReference -> {
                        // Optionally notify the user
                        Toast.makeText(DetailActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Handle error
                        Toast.makeText(DetailActivity.this, "Failed to add to Cart", Toast.LENGTH_SHORT).show();
                    });
        } else {    
            Toast.makeText(DetailActivity.this, "No current user detected", Toast.LENGTH_SHORT).show();
        }
    }

}