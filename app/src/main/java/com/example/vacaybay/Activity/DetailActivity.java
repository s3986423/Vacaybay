package com.example.vacaybay.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.vacaybay.Activity.Domain.ItemDomain;
import com.example.vacaybay.R;
import com.example.vacaybay.databinding.ActivityDetailBinding;

import java.util.Calendar;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    private ItemDomain object;

    private long startDateInMillis = 0;
    private long endDateInMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1) Get the item passed in
        getIntentExtra();

        // 2) Populate the screen
        setVariable();

        // 3) Setup new pickers
        setupDurationPicker();
        setupBedPicker();

        // 4) Setup location click (imageView102 -> open maps)
        setupLocationClick();
    }

    // ----------------------------------------------------------------
    // Where you set the data from object into your views
    // ----------------------------------------------------------------
    private void setVariable(){
        if (object == null) return;

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

        Glide.with(this)
                .load(object.getPic())
                .into(binding.pic);

        binding.addToCartBtn.setOnClickListener(v -> {
            // When user clicks Add to Cart, we move to TicketActivity
            // We will pass the updated object that now includes the chosen start date,
            // the updated duration, and the chosen bed.
            Intent intent = new Intent(DetailActivity.this, TicketActivity.class);
            intent.putExtra("object", object);
            startActivity(intent);
        });
    }

    // ----------------------------------------------------------------
    // Let user pick start date + end date, then compute duration
    // ----------------------------------------------------------------
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

                    // Pick the end date next
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
    }

    // ----------------------------------------------------------------
    // Let user pick bed (1 to 3) from popup menu
    // ----------------------------------------------------------------
    private void setupBedPicker() {
        binding.imageView103.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(DetailActivity.this, binding.imageView103);
            popup.getMenu().add("1");
            popup.getMenu().add("2");
            popup.getMenu().add("3");

            popup.setOnMenuItemClickListener(item -> {
                String chosen = item.getTitle().toString();
                binding.bedTxt.setText(chosen);

                // Also store in the object
                try {
                    object.setBed(Integer.parseInt(chosen));
                } catch (NumberFormatException e) {
                    object.setBed(1); // fallback
                }
                return true;
            });
            popup.show();
        });
    }

    // ----------------------------------------------------------------
    // On imageView102 click: open Google Maps to the stored location
    // ----------------------------------------------------------------
    private void setupLocationClick() {
        binding.imageView102.setOnClickListener(view -> {
            if (object != null && object.getLocation() != null && !object.getLocation().isEmpty()) {
                openLocationInMaps(object.getLocation());
            } else {
                Toast.makeText(this, "No location available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper method to parse lat/lng and open Google Maps
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

    private void getIntentExtra(){
        object = (ItemDomain) getIntent().getSerializableExtra("object");
    }
}
