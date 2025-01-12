package com.example.vacaybay.Activity.adminActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vacaybay.Activity.Models.Booking;
import com.example.vacaybay.R;

import java.util.Arrays;
import java.util.List;


public class AdminBooking extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking);

//        // Find the container LinearLayout
//        LinearLayout bookingListContainer = findViewById(R.id.booking_list_container);
//
//        // Example list of booking data
//        List<Booking> bookings = Arrays.asList(
//                new Booking("23 Jan 2023", "x392", "2 Beds - 5 Nights", "Cash/Banking", "23 Mar 2023", "25 Mar 2023"),
//                new Booking("24 Jan 2023", "x393", "1 Bed - 2 Nights", "Card Payment", "24 Mar 2023", "26 Mar 2023")
//        );
//
//        // Inflate booking_list_card for each booking
//        for (Booking booking : bookings) {
//            View bookingCard = LayoutInflater.from(this).inflate(R.layout.booking_list_card, bookingListContainer, false);
//
//            // Populate the card with data
//            TextView bookingDate = bookingCard.findViewById(R.id.booking_date);
//            TextView bookingId = bookingCard.findViewById(R.id.booking_id);
//            TextView bookingDetails = bookingCard.findViewById(R.id.booking_details);
//            TextView payment = bookingCard.findViewById(R.id.payment);
//            TextView checkinDate = bookingCard.findViewById(R.id.checkin_date);
//            TextView checkoutDate = bookingCard.findViewById(R.id.checkout_date);
//
//            bookingDate.setText("Booking on - " + booking.getDate());
//            bookingId.setText("Booking ID - " + booking.getId());
//            bookingDetails.setText(booking.getDetails());
//            payment.setText(booking.getPayment());
//            checkinDate.setText(booking.getCheckinDate());
//            checkoutDate.setText(booking.getCheckoutDate());
//
//            // Add the card to the container
//            bookingListContainer.addView(bookingCard);
//        }


    }
}
