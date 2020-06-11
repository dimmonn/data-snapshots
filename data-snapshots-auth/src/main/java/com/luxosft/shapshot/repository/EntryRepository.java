package com.luxosft.shapshot.repository;

import com.luxosft.shapshot.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Long> {

}
