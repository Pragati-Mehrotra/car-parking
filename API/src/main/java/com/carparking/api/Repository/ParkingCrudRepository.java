package com.carparking.api.Repository;

import com.carparking.api.Entity.Parking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingCrudRepository extends CrudRepository<Parking, Integer> {
}
