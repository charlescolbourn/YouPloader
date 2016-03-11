package at.becast.youploader.oauth.json;

public class Code {
  public final String device_code;
  public final String user_code;
  public final String verification_url;
  public final int expires_in;
  public final int interval;

  public Code(String device_code, String user_code, String verification_url, int expires_in, int interval) {
    this.device_code = device_code;
    this.user_code = user_code;
    this.verification_url = verification_url;
    this.expires_in = expires_in;
    this.interval = interval;
  }

  public Code() {
    this(null, null, null, 0, 0);
  }
}