package com.carparking.api.Repository;

import com.carparking.api.Entity.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingCrudRepository extends CrudRepository<Booking, Integer> {
}
