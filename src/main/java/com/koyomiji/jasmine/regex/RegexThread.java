package com.koyomiji.jasmine.regex;

import com.koyomiji.jasmine.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class RegexThread implements Cloneable {
  protected int step = 0;
  protected int programCounter = 0;
  protected Stack<Object> stack = new Stack<>();
  protected HashMap<Object, Pair<Integer, Integer>> stringBinds = new HashMap<>();

  public RegexThread() {}

  public RegexThread(int programCounter) {
    this.programCounter = programCounter;
  }

  @Override
  protected Object clone() {
    try {
      RegexThread clone = (RegexThread) super.clone();
      clone.stack = (Stack<Object>) this.stack.clone();
      clone.stringBinds = (HashMap<Object, Pair<Integer, Integer>>) this.stringBinds.clone();
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  public int getStep() {
    return step;
  }

  public int advanceProgramCounter() {
    return advanceProgramCounter(1);
  }

  public int advanceProgramCounter(int offset) {
    step++;
    return programCounter += offset;
  }

  public int getProgramCounter() {
    return programCounter;
  }

  public void push(Object c) {
    stack.push(c);
  }

  public Object pop() {
    return stack.pop();
  }

  public int stackSize() {
    return stack.size();
  }

  public void unbind(Object index) {
    stringBinds.remove(index);
  }

  public void bind(Object index, Pair<Integer, Integer> range) {
    this.stringBinds.put(index, range);
  }

  public Pair<Integer, Integer> getBoundRange(Object index) {
    return stringBinds.get(index);
  }

  public Stack<Object> getStack() {
    return stack;
  }

  public Map<Object, Pair<Integer, Integer>> getStringBinds() {
    return stringBinds;
  }
}
