package com.luxosft.shapshot.auth.repository;

import com.luxosft.shapshot.auth.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Long> {

}
