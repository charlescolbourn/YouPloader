package at.becast.youploader.youtube.io;

public interface UploadEvent {
  public void onInit();

  public void onSpeedLimitSet(long limit);

  public void onRead(long length, long position, long size);

  public void onClose();
}
