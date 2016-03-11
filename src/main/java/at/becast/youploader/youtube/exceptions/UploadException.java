package at.becast.youploader.youtube.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class UploadException extends Exception {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public final Error1 error;


  public static class Error1 {
    public final Error2[] errors;
    public final int code;
    public final String message;

    public Error1() {
      this.errors = null;
      this.code = -1;
      this.message = null;
    }
  }

  public static class Error2 {
    public final String domain;
    public final String reason;
    public final String message;
    public final String locationType;
    public final String location;

    public Error2() {
      this.domain = null;
      this.reason = null;
      this.message = null;
      this.locationType = null;
      this.location = null;
    }
  }


  public UploadException() {
    this.error = null;
  }

  public static UploadException construct(InputStream stream) throws IOException {
    return new ObjectMapper().readValue(stream, UploadException.class);
  }
}
