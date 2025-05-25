package com.koyomiji.asmine.common;

public class SourceLocation {
  private final int line;
  private final int column;
  private final int offset;

  public SourceLocation(int line, int column, int offset) {
    this.line = line;
    this.column = column;
    this.offset = offset;
  }

  @Override
  public String toString() {
    return "SourceLocation{" +
            "line=" + line +
            ", column=" + column +
            ", offset=" + offset +
            '}';
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  public int getOffset() {
    return offset;
  }
}
