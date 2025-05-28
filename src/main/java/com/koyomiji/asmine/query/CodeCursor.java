package com.koyomiji.asmine.query;

public class CodeCursor {
  private CodeManipulator parent;
  private int firstIndex;
  private int lastIndex;

  public CodeCursor(CodeManipulator parent) {
    this.parent = parent;
    reset();
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

  public CodeCursor before() {
    return parent.getCursor(firstIndex - 1);
  }

  public CodeCursor after() {
    return parent.getCursor(lastIndex + 1);
  }

  public CodeManipulator getManipulator() {
    return parent;
  }
}
