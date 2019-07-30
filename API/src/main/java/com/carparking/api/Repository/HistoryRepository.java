package com.carparking.api.Repository;

import com.carparking.api.Entity.Booking;
import com.carparking.api.Entity.History;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface HistoryRepository extends Repository<History, Integer> {
    
    List<History> findHistoryByUserId(Integer userId);
}
