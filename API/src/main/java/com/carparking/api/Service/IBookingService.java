package com.carparking.api.Service;

import com.carparking.api.Entity.Booking;
import com.carparking.api.Entity.History;

import java.util.List;

public interface IBookingService {

    //public List<Booking> getActiveBookings(int userId);

    public Booking saveBooking(Booking booking);

    public History cancelBooking(Integer bookingId);

    public Booking checkout(Integer bookingId);

    public Booking getBookingById(Integer bookingId);

    public List<Booking> getActiveBookings(Integer userId);
}
