package com.carparking.api.Repository;

import com.carparking.api.Entity.Booking;
import org.springframework.data.repository.Repository;

public interface BookingRepository extends Repository<Booking, Integer> {
    Booking findByBookingId(Integer bookingId);
}
