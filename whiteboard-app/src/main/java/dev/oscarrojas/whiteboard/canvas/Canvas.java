package dev.oscarrojas.whiteboard.canvas;

import dev.oscarrojas.whiteboard.exception.InvalidInputException;
import java.util.UUID;

public class Canvas {

  private final int DEFAULT_WIDTH = 1000;
  private final int DEFAULT_HEIGHT = 1000;

  private String id = UUID.randomUUID().toString();
  private int width;
  private int height;
  private byte[] data;

  public Canvas() {
    this.width = DEFAULT_WIDTH;
    this.height = DEFAULT_HEIGHT;
  }

  public Canvas(int width, int height) {
    this.width = width;
    this.height = height;
    this.data = new byte[width * height * 4];
  }

  public Canvas(int width, int height, byte[] data) {
    this.width = width;
    this.height = height;
    this.data = new byte[data.length];
  }

  String getId() {
    return id;
  }

  void setId(String id) {
    this.id = id;
  }

  void putData(byte[] array) throws InvalidInputException {
    if (array.length != width * height * 4) {
      throw new InvalidInputException("Input array does not match required size");
    }
    data = array;
  }

  void reset() {
    for (int i = 0; i < data.length; i++) {
      data[i] = 0;
    }
  }

  byte[] getData() {
    return data;
  }
}