package com.carparking.api.Service;

import com.carparking.api.Entity.Booking;
import com.carparking.api.Repository.BookingCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService implements IBookingService {

    @Autowired
    BookingCrudRepository bookingCrudRepository;

    @Override
    public Booking saveBooking(Booking booking) {

        int randomPin   =(int)(Math.random()* 9000)+ 1000;
        System.out.println("------------" + randomPin);
        booking.setInOtp(randomPin);
        booking.setStatus("Booked");
        return bookingCrudRepository.save(booking);
    }
}
