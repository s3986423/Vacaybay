package com.example.vacaybay.Activity.Models;

public class Booking {
    private String date;
    private String id;
    private String details;
    private String payment;
    private String checkinDate;
    private String checkoutDate;

    public Booking(String date, String id, String details, String payment, String checkinDate, String checkoutDate) {
        this.date = date;
        this.id = id;
        this.details = details;
        this.payment = payment;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getDetails() {
        return details;
    }

    public String getPayment() {
        return payment;
    }

    public String getCheckinDate() {
        return checkinDate;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }
}

