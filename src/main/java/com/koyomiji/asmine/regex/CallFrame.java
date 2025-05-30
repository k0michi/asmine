package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
