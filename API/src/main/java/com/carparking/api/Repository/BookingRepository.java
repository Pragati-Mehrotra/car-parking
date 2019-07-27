package com.carparking.api.Repository;

import com.carparking.api.Entity.Booking;
import com.carparking.api.Entity.User;
import org.springframework.data.repository.Repository;

public interface BookingRepository extends Repository<Booking, Integer> {
    Booking findByBookingId(Integer bookingId);

    Booking findByParkingIdAndInOtp(Integer parkingId, Integer inOtp);

    Booking findByParkingIdAndOutOtp(Integer parkingId, Integer outOtp);
}
