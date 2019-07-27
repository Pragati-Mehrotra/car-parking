package com.carparking.api.Service;

import com.carparking.api.Entity.Booking;
import com.carparking.api.Repository.BookingCrudRepository;
import com.carparking.api.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService implements IBookingService {

    @Autowired
    BookingCrudRepository bookingCrudRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Override
    public Booking saveBooking(Booking booking) {

        int randomPin = (int)(Math.random()* 9000)+ 1000;
        System.out.println("------------" + randomPin);
        booking.setInOtp(randomPin);
        booking.setStatus("Booked");
        return bookingCrudRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Integer bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);
        return booking;
    }

    @Override
    public Booking checkout(int bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);
        int randomPin = (int)(Math.random()* 9000)+ 1000;
        System.out.println("------------" + randomPin);
        booking.setInOtp(randomPin);
        booking.setStatus("CheckedOut");
        booking.setOutOpt(randomPin);
        Booking updatedBooking = bookingCrudRepository.save(booking);
        return updatedBooking;
    }
}
