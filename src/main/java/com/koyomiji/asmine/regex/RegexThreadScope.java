package com.koyomiji.asmine.regex;

import java.util.ArrayList;
import java.util.List;

public class RegexThreadScope implements Cloneable {
  public int parent;
  public Object key;
  public int begin = -1;
  public int end = -1;
  public List<Integer> children = new ArrayList<>();

  public RegexThreadScope(int parent) {
    this.parent = parent;
  }

  @Override
  public RegexThreadScope clone() {
    try {
      RegexThreadScope clone = (RegexThreadScope) super.clone();
      clone.children = new ArrayList<>(this.children);
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  public void setBegin(int begin) {
    this.begin = begin;
  }

  public void setEnd(int end) {
    this.end = end;
  }

  public void addChild(int child) {
    this.children.add(child);
  }

  public boolean isEnded() {
    return end != -1;
  }
}
