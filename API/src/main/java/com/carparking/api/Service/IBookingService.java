package com.carparking.api.Service;

import com.carparking.api.Entity.Booking;

import java.util.List;

public interface IBookingService {

    //public List<Booking> getActiveBookings(int userId);

    public Booking saveBooking(Booking booking);

    //public Booking cancelBooking(int bookingId);

    //public Booking checkout(int bookingId);

}
