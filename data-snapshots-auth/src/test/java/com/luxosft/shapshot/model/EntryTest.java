package com.luxosft.shapshot.model;

import static org.junit.Assert.*;

import java.util.Date;
import org.junit.Test;

public class EntryTest {

  @Test
  public void testEquals() {
    Entry entryTest1 = new Entry(1, "test1", "test2", new Date());
    Entry entryTest2 = new Entry(1, "test3", "test4", null);
    assertEquals(entryTest1, entryTest2);
    assertEquals(entryTest1.hashCode(),entryTest2.hashCode());
  }

}