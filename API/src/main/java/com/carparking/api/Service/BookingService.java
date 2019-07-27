package com.carparking.api.Service;

import com.carparking.api.Entity.Booking;
import com.carparking.api.Entity.History;
import com.carparking.api.Repository.BookingCrudRepository;
import com.carparking.api.Repository.BookingRepository;
import com.carparking.api.Repository.HistoryCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService implements IBookingService {

    @Autowired
    BookingCrudRepository bookingCrudRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    HistoryCrudRepository historyCrudRepository;

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
    public Booking checkout(Integer bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);
        int randomPin = (int)(Math.random()* 9000)+ 1000;
        System.out.println("------------" + randomPin);
        booking.setStatus("CheckedOut");
        booking.setOutOpt(randomPin);
        Booking updatedBooking = bookingCrudRepository.save(booking);
        return updatedBooking;
    }

    @Override
    public History cancelBooking(Integer bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);
        History history = new History();
        history.setBookingId(booking.getBookingId());
        history.setParkingId(booking.getParkingId());
        history.setUserId(booking.getUserId());
        history.setBill(booking.getBill());
        history.setStatus("Cancelled");
        history.setInTime(booking.getInTime());
        history.setOutTime(booking.getOutTime());
        history.setSlotDuration(booking.getSlotDuration());
        History savedHistory = historyCrudRepository.save(history);
        bookingCrudRepository.deleteById(bookingId);
        return savedHistory;
    }

}
