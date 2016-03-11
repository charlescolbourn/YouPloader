package at.becast.youploader.youtube.data;

import java.io.File;

public class Upload {
  public final String url;
  public final File file;

  public Upload(String url, File file) {
    this.url = url;
    this.file = file;
  }

  public Upload() {
    this(null, null);
  }
}
