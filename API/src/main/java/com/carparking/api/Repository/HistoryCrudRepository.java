package com.carparking.api.Repository;

import com.carparking.api.Entity.History;
import org.springframework.data.repository.CrudRepository;

public interface HistoryCrudRepository extends CrudRepository<History, Integer> {
}
