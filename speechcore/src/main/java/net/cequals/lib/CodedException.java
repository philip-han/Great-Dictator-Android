package net.cequals.lib;

public class CodedException extends Exception {

  private int code;

  public CodedException(int code) {
    this.code = code;
  }

  public CodedException(int code, String message) {
    super(message);
    this.code = code;
  }

  public int getCode() {
    return code;
  }

}