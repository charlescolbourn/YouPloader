package at.becast.youploader.youtube.data;

import java.io.File;

public class Upload {
  public final String url;
  public final File file;
  public final String id;
  public final Video video;

  public Upload(String url, File file, String id, Video video) {
    this.url = url;
    this.file = file;
    this.id = id;
    this.video = video;
  }

  public Upload() {
    this(null, null, null, null);
  }
}
