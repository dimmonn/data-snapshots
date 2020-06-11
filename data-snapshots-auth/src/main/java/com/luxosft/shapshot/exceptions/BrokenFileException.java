package com.luxosft.shapshot.exceptions;

public class BrokenFileException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public BrokenFileException() {
    super("File is broken.");
  }
}
