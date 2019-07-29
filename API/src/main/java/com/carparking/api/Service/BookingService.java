package com.carparking.api.Service;

import com.carparking.api.Entity.Booking;
import com.carparking.api.Entity.History;
import com.carparking.api.Entity.Parking;
import com.carparking.api.Repository.BookingCrudRepository;
import com.carparking.api.Repository.ParkingCrudRepository;
import com.carparking.api.Repository.BookingRepository;
import com.carparking.api.Repository.ParkingRepository;
import com.carparking.api.Repository.HistoryCrudRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService implements IBookingService {

    @Autowired
    BookingCrudRepository bookingCrudRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    HistoryCrudRepository historyCrudRepository;

    @Autowired
    ParkingRepository parkingRepository;

    @Autowired
    ParkingCrudRepository parkingCrudRepository;

    @Override
    public Booking saveBooking(Booking booking) {

        do {
            int randomPin = (int)(Math.random()* 9000)+ 1000;
            Booking existingBooking = bookingRepository.findByParkingIdAndInOtp(booking.getParkingId(), randomPin);
            if(existingBooking == null) {
                booking.setInOtp(randomPin);
                break;
            }
        }
        while(booking.getInOtp() == null);

        booking.setStatus("Booked");
        Double bill = (double)(booking.getSlotDuration() * 10);
        booking.setBill(bill);
        Integer parkingId = booking.getParkingId();
        Booking savedBooking = bookingCrudRepository.save(booking);
        if(savedBooking != null) {
            Parking parking = parkingRepository.findByParkingId(parkingId);
            Integer availableSlots = parking.getAvailableSlots() - 1;
            parking.setAvailableSlots(availableSlots);
            Parking savedParking = parkingCrudRepository.save(parking);
        }
        return savedBooking;
    }

    @Override
    public Object getBookingById(Integer bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);
        if(booking != null) {
            return booking;
        }
        else {
            JSONObject message = new JSONObject();
            message.put("error","Booking Id does not exist.Please enter a valid booking Id.");
            return message;
        }
    }

    @Override
    public Booking checkout(Integer bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);
        booking.setStatus("CheckedOut");

        do {
            int randomPin = (int)(Math.random()* 9000)+ 1000;
            Booking existingBooking = bookingRepository.findByParkingIdAndOutOtp(booking.getParkingId(), randomPin);
            if(existingBooking == null) {
                booking.setOutOtp(randomPin);
                break;
            }
        }
        while(booking.getOutOtp() == null);
        Booking updatedBooking = bookingCrudRepository.save(booking);
        return updatedBooking;
    }

    @Override
    public History cancelBooking(Integer bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId);
        Parking parking = parkingRepository.findByParkingId(booking.getParkingId());
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
        Integer availableSlots = parking.getAvailableSlots() + 1;
        parking.setAvailableSlots(availableSlots);
        Parking savedParking = parkingCrudRepository.save(parking);
        String parkingName = parking.getParkingName();
        savedHistory.setParkingName(parkingName);
        return savedHistory;
    }

    @Override
    public Object getActiveBookings(Integer userId) {
        List <Booking> bookingList = bookingRepository.findByUserId(userId);
        return bookingList;
    }
}
