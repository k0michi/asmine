package com.koyomiji.asmine.query;

public class Cursor {
  // Physical indices
  private int firstIndex;
  private int lastIndex;

  public Cursor() {
    this(-2);
  }

  public Cursor(int index) {
    this.firstIndex = index;
    this.lastIndex = index;
  }

  public int getFirstIndex() {
    return firstIndex;
  }

  public void setFirstIndex(int firstIndex) {
    this.firstIndex = firstIndex;
  }

  public int getLastIndex() {
    return lastIndex;
  }

  public void setLastIndex(int lastIndex) {
    this.lastIndex = lastIndex;
  }

  public void setIndex(int index) {
    if (firstIndex == -2) {
      setFirstIndex(index);
    }

    setLastIndex(index);
  }

  public void reset() {
    this.firstIndex = this.lastIndex = -2;
  }
}
