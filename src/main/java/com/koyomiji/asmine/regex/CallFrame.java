package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallFrame implements Cloneable {
  public int returnFP;
  public int returnPC;
  public HashMap<Object, Pair<Integer, Integer>> stringBinds = new HashMap<>();

  public CallFrame() {
    this(-1, -1);
  }

  public CallFrame(int returnFP, int returnPC) {
    this.returnFP = returnFP;
    this.returnPC = returnPC;
  }

  public void bind(Object key, Pair<Integer, Integer> range) {
    this.stringBinds.put(key, range);
  }

  public void merge(CallFrame popped) {
    for (Map.Entry<Object, Pair<Integer, Integer>> entry : popped.stringBinds.entrySet()) {
      this.stringBinds.putIfAbsent(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public CallFrame clone() {
    try {
      CallFrame clone = (CallFrame) super.clone();
      clone.stringBinds = (HashMap<Object, Pair<Integer, Integer>>) this.stringBinds.clone();
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }
}
