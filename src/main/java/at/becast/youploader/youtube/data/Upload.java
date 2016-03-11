package at.becast.youploader.youtube.data;

import java.io.File;

public class Upload {
  public final String url;
  public final File file;
  public final String id;

  public Upload(String url, File file, String id) {
    this.url = url;
    this.file = file;
    this.id = id;
  }

  public Upload(String result, File file2) {
    this(null, null, null);
  }
}
