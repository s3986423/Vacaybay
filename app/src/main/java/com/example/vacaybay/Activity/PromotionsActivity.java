package com.example.vacaybay.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;

import com.example.vacaybay.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PromotionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotions);

        Button details_button_1 = findViewById(R.id.see_details_button_1);

        details_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_promotions_details, null);

                TextView detail_title = dialogView.findViewById(R.id.detail_title);
                TextView detail_description = dialogView.findViewById(R.id.detail_description);
                Button dismiss_button = dialogView.findViewById(R.id.dismiss_button);

                detail_title.setText("10% Off for Four Seasons Villa");
                detail_description.setText("Stay at the luxurious Four Seasons Villa and enjoy 10% off. One time deal! Get it now!");

                AlertDialog dialog = new AlertDialog.Builder(PromotionsActivity.this).setView(dialogView).create();

                dismiss_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
}
