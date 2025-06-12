package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.tuple.Pair;

import java.util.*;

public class CallFrame implements Cloneable {
  public int returnFP;
  public int returnPC;

  public CallFrame() {
    this(-1, -1);
  }

  public CallFrame(int returnFP, int returnPC) {
    this.returnFP = returnFP;
    this.returnPC = returnPC;
  }

  @Override
  public CallFrame clone() {
    try {
      CallFrame clone = (CallFrame) super.clone();
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CallFrame callFrame = (CallFrame) o;
    return returnFP == callFrame.returnFP && returnPC == callFrame.returnPC;
  }

  @Override
  public int hashCode() {
    return Objects.hash(returnFP, returnPC);
  }
}
