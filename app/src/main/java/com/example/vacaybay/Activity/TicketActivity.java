package com.example.vacaybay.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.vacaybay.Activity.Domain.ItemDomain;
import com.example.vacaybay.R;
import com.example.vacaybay.databinding.ActivityTicketBinding;

public class TicketActivity extends BaseActivity {
    ActivityTicketBinding binding;
    private ItemDomain object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTicketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();

    }

    private void setVariable(){
        Glide.with(TicketActivity.this)
                .load(object.getPic())
                .into(binding.pic);

        Glide.with(TicketActivity.this)
                .load(object.getTourGuidePic())
                .into(binding.profile);

        // Return to previous screen
        binding.backBtn.setOnClickListener(v -> finish());

        // Title, duration, bed, etc.
        binding.titleTxt.setText(object.getTitle());

        // Duration in the Ticket
        binding.durationTxt.setText(object.getDuration());

        // The "start date" the user picked in the Detail page
        binding.tourGuideTxt.setText(object.getDateTour());

        // The bed count from the Detail page
        binding.BedTxt.setText(String.valueOf(object.getBed()));

        // If you have time and date for the Tour
        binding.timeTxt.setText(object.getTimeTour());
        binding.tourGuideNameTxt.setText(object.getTourGuideName());

        // For message/call
        binding.messageBtn.setOnClickListener(v -> {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:" + object.getTourGuidePhone()));
            sendIntent.putExtra("sms_body", "type your message");
            startActivity(sendIntent);
        });
        binding.callBtn.setOnClickListener(v -> {
            String phone = object.getTourGuidePhone();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone,null));
            startActivity(intent);
        });
    }
    private void getIntentExtra(){
        object = (ItemDomain) getIntent().getSerializableExtra("object");
    }
}