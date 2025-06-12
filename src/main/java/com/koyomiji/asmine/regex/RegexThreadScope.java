package com.koyomiji.asmine.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RegexThreadScope that = (RegexThreadScope) o;
    return parent == that.parent && begin == that.begin && end == that.end && Objects.equals(key, that.key) && Objects.equals(children, that.children);
  }

  @Override
  public int hashCode() {
    return Objects.hash(parent, key, begin, end, children);
  }
}
