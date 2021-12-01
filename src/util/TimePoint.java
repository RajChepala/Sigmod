package util;

public class TimePoint {


  public double value;
  public double constraint;
  public boolean isModified;
  public long time;

  public TimePoint(long time, double constraint) {
    this.time = time;
    this.constraint = constraint;
    this.value = constraint;
  }
}
