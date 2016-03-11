package at.becast.youploader.youtube;

import at.becast.youploader.youtube.io.UploadEvent;

public class DefaultUploadEvent implements UploadEvent {
  private long step;

  public DefaultUploadEvent() {
    this.step = 0;
  }

  @Override
  public void onInit() {
    System.out.print("   0.00%");
  }

  @Override
  public void onSpeedLimitSet(long limit) {
  }

  @Override
  public void onRead(long length, long position, long size) {
    if (this.step < System.currentTimeMillis() - 900) {
      System.out.printf("\b\b\b\b\b\b\b%6.2f%%", (float) position / size * 100);
      this.step = System.currentTimeMillis();
    }
  }

  @Override
  public void onClose() {
  }
}
