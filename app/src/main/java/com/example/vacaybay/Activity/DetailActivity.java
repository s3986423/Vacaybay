package com.example.vacaybay.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.vacaybay.Activity.Domain.ItemDomain;
import com.example.vacaybay.R;
import com.example.vacaybay.databinding.ActivityDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;
    private ItemDomain object;

    private long startDateInMillis = 0;
    private long endDateInMillis = 0;

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
        setupDurationPicker();
        setupBedPicker();
        setupLocationClick();
    }

    private void setVariable() {
        if (object == null) return;

        // Basic info
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("$" + object.getPrice());
        binding.backBtn.setOnClickListener(v -> finish());

        // Display current data from object
        binding.bedTxt.setText(String.valueOf(object.getBed()));
        binding.durationTxt.setText(object.getDuration());
        binding.distanceTxt.setText(object.getDistance());
        binding.descriptionTxt.setText(object.getDescription());
        binding.addressTxt.setText(object.getAddress());
        binding.ratingTxt.setText(object.getScore() + " Rating");
        binding.ratingBar.setRating((float) object.getScore());

        // Load image
        Glide.with(this)
                .load(object.getPic())
                .into(binding.pic);

        // Button: add to cart
        binding.addToCartBtn.setOnClickListener(v -> {
            addToCart();
            Intent intent = new Intent(DetailActivity.this, TicketActivity.class);
            intent.putExtra("object", object);
            startActivity(intent);
        });

        // Optionally, call updatePrice() once if you want the UI to show the correct initial total.
        // updatePrice();
    }

    private void setupDurationPicker() {
        // When user taps imageView101, start date flow
        binding.imageView101.setOnClickListener(view -> pickStartDate());
    }

    private void pickStartDate() {
        Calendar c = Calendar.getInstance();
        int year  = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day   = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog startDialog = new DatePickerDialog(
                DetailActivity.this,
                (datePicker, selYear, selMonth, selDay) -> {
                    // Build the start date in millis
                    Calendar startCal = Calendar.getInstance();
                    startCal.set(selYear, selMonth, selDay, 0, 0, 0);
                    startDateInMillis = startCal.getTimeInMillis();

                    // Also set this as the "dateTour" in our object
                    String startDateString = selDay + "/" + (selMonth + 1) + "/" + selYear;
                    object.setDateTour(startDateString);

                    // Next: pick the end date
                    pickEndDate();
                },
                year, month, day
        );
        startDialog.show();
    }

    private void pickEndDate() {
        Calendar c = Calendar.getInstance();
        int year  = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day   = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog endDialog = new DatePickerDialog(
                DetailActivity.this,
                (datePicker, selYear, selMonth, selDay) -> {
                    Calendar endCal = Calendar.getInstance();
                    endCal.set(selYear, selMonth, selDay, 0, 0, 0);
                    endDateInMillis = endCal.getTimeInMillis();

                    if (endDateInMillis < startDateInMillis) {
                        // Handle error if user picks an end date earlier than start date
                        Toast.makeText(this, "End date is before start date!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Compute the difference
                    computeDuration();
                },
                year, month, day
        );
        endDialog.show();
    }

    private void computeDuration() {
        // e.g. 13/1/2025 to 15/1/2025 => 3 days, 2 nights
        long diffInMillis = endDateInMillis - startDateInMillis;
        // We add 1 so that if startDate == endDate, it’s 1 day
        long diffInDays = (diffInMillis / (24 * 60 * 60 * 1000)) + 1;
        long nights = diffInDays - 1;
        if (nights < 0) nights = 0;

        String durationString = diffInDays + "D/" + nights + "N";
        // Show on the screen
        binding.durationTxt.setText(durationString);

        // Also store in the object
        object.setDuration(durationString);

        // Update the price once the user has chosen a new duration
        updatePrice();
    }

    private void setupBedPicker() {
        binding.imageView103.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(DetailActivity.this, binding.imageView103);
            popup.getMenu().add("1");
            popup.getMenu().add("2");
            popup.getMenu().add("3");
            // You can add more bed options if you want

            popup.setOnMenuItemClickListener(item -> {
                String chosen = item.getTitle().toString();
                binding.bedTxt.setText(chosen);

                // Also store in the object
                try {
                    object.setBed(Integer.parseInt(chosen));
                } catch (NumberFormatException e) {
                    object.setBed(1); // fallback
                }

                // Update price after user picks bed count
                updatePrice();
                return true;
            });
            popup.show();
        });
    }

    private void setupLocationClick() {
        binding.imageView102.setOnClickListener(view -> {
            if (object != null && object.getLocation() != null && !object.getLocation().isEmpty()) {
                openLocationInMaps(object.getLocation());
            } else {
                Toast.makeText(this, "No location available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openLocationInMaps(String locationString) {
        try {
            // locationString is something like "11.920359,108.499469"
            String[] latLng = locationString.split(",");
            double latitude = Double.parseDouble(latLng[0].trim());
            double longitude = Double.parseDouble(latLng[1].trim());

            // Option A: Show a marker at (latitude, longitude)
            String geoUri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude;

            // Option B: Launch directly into navigation
            // String geoUri = "google.navigation:q=" + latitude + "," + longitude;

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            // Force the intent to open in Google Maps if it's installed
            intent.setPackage("com.google.android.apps.maps");

            // Check if an Activity is available to handle this intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                // If no Google Maps, try a generic map viewer
                intent.setPackage(null);
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to parse location", Toast.LENGTH_SHORT).show();
        }
    }

    private void getIntentExtra() {
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

    private void updatePrice() {
        if (object == null) return;

        // 1) Parse the day count from the duration string => e.g. "3D/2N" => 3 days
        int days = 1;
        try {
            String durationString = object.getDuration(); // e.g. "3D/2N"
            if (durationString != null && durationString.contains("D")) {
                String daysPart = durationString.substring(0, durationString.indexOf("D"));
                days = Integer.parseInt(daysPart.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
            days = 1; // fallback
        }

        // 2) Get the bed count
        int beds = object.getBed();

        // 3) Apply your pricing rules:
        //    - $150 per day
        //    - $50 per bed
        //    If you want the first bed to be free and only charge for additional beds, replace "beds * 50" with "(beds - 1) * 50".
        int totalPrice = (days * 150) + (beds * 50);

        // 4) Update the UI with new total
        binding.priceTxt.setText("$" + totalPrice);
    }
}
