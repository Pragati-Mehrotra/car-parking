package com.carparking.api.Repository;


import com.carparking.api.Entity.History;
import org.springframework.data.repository.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoryRepository extends Repository<History, Integer> {

    @Query(value = "SELECT history.booking_id,history.in_time,history.out_time,history.slot_duration,history.status,history.bill,history.user_id,history.parking_id,parking.parking_name FROM history INNER JOIN parking ON history.parking_id = parking.parking_id", nativeQuery = true)
    List<History> findHistoryByUserId(Integer userId);
}
