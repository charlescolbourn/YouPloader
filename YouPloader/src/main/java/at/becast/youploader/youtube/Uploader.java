package at.becast.youploader.youtube;

import com.fasterxml.jackson.databind.ObjectMapper;
import at.becast.youploader.oauth.OAuth2;
import at.becast.youploader.youtube.data.Upload;
import at.becast.youploader.youtube.data.Video;
import at.becast.youploader.youtube.exceptions.UploadException;
import at.becast.youploader.youtube.io.SimpleHTTP;
import at.becast.youploader.youtube.io.UploadEvent;
import at.becast.youploader.youtube.io.UploadStream;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Uploader {
  private OAuth2 oAuth2;

  public Uploader(OAuth2 oAuth2) {
    this.oAuth2 = oAuth2;
  }

  public Upload prepareUpload(File file, Video video) throws IOException, UploadException {
    SimpleHTTP http = new SimpleHTTP();

    if (video.snippet.title == null) {
      video.snippet.title = file.getName();
    }

    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", this.oAuth2.getHeader());
    headers.put("Content-Type", "application/json; charset=UTF-8");
    headers.put("X-Upload-Content-Length", String.valueOf(file.length()));
    headers.put("X-Upload-Content-Type", "video/*");

    Upload url = new Upload(
      http.post(
        "https://www.googleapis.com//upload/youtube/v3/videos?uploadType=resumable&part=snippet,status",
        headers,
        new ObjectMapper().writeValueAsString(video)
      ),
      file
    );

    http.close();
    return url;
  }

  public void upload(Upload upload, UploadEvent event, long limit) throws IOException {
    SimpleHTTP http = new SimpleHTTP();

    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", this.oAuth2.getHeader());
    headers.put("Content-Type", "video/*");

    UploadStream stream = new UploadStream(upload.file, event);
    stream.setSpeedLimit(limit);
    http.put(upload.url, headers, stream);

    stream.close();
    http.close();
  }

  public void resumeUpload(Upload upload, UploadEvent event, long limit) throws IOException {
    SimpleHTTP http = new SimpleHTTP();

    long uploaded;
    {
      Map<String, String> headers = new HashMap<>();
      headers.put("Authorization", this.oAuth2.getHeader());
      headers.put("Content-Range", "bytes */" + String.valueOf(upload.file.length()));

      uploaded = http.put(upload.url, headers);
    }

    if (uploaded == 0) {
      this.upload(upload, event, limit);
    }
    else {
      long length = upload.file.length();

      Map<String, String> headers = new HashMap<>();
      headers.put("Authorization", this.oAuth2.getHeader());
      headers.put("Content-Range", String.format("bytes %d-%d/%d", uploaded, length - 1, length));

      UploadStream stream = new UploadStream(upload.file, event, uploaded);
      stream.setSpeedLimit(limit);
      http.put(upload.url, headers, stream);
      stream.close();
    }

    http.close();
  }
}
