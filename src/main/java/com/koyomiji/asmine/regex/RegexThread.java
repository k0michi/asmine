package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.tuple.Pair;

import java.util.*;

public class RegexThread implements Cloneable {
  protected boolean terminated = false;
  protected int functionPointer = 0;
  protected int programCounter = 0;
  protected Stack<Object> stack = new Stack<>();
  protected HashMap<Object, List<Pair<Integer, Integer>>> stringBinds = new HashMap<>();
  protected List<Object> trace = new ArrayList<>();

  public RegexThread() {}

  @Override
  protected Object clone() {
    try {
      RegexThread clone = (RegexThread) super.clone();
      clone.stack = (Stack<Object>) this.stack.clone();

      clone.stringBinds = new HashMap<>();
      for (Map.Entry<Object, List<Pair<Integer, Integer>>> entry : this.stringBinds.entrySet()) {
        List<Pair<Integer, Integer>> clonedList = new ArrayList<>(entry.getValue());
        clone.stringBinds.put(entry.getKey(), clonedList);
      }

      return clone;
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  public int advanceProgramCounter() {
    return advanceProgramCounter(1);
  }

  public int advanceProgramCounter(int offset) {
    return programCounter += offset;
  }

  public int getProgramCounter() {
    return programCounter;
  }

  public void setProgramCounter(int programCounter) {
    this.programCounter = programCounter;
  }

  public int getFunctionPointer() {
    return functionPointer;
  }

  public void setFunctionPointer(int functionPointer) {
    this.functionPointer = functionPointer;
  }

  public void push(Object c) {
    stack.push(c);
  }

  public Object pop() {
    return stack.pop();
  }

  public void terminate() {
    this.terminated = true;
  }

  public boolean isRunning() {
    return !this.terminated;
  }

  public boolean isTerminated() {
    return this.terminated;
  }

  public int stackSize() {
    return stack.size();
  }

  public void unbind(Object index) {
    stringBinds.remove(index);
  }

  public void bind(Object index, Pair<Integer, Integer> range) {
    if (!stringBinds.containsKey(index)) {
      stringBinds.put(index, new ArrayList<>());
    }

    this.stringBinds.get(index).add(range);
  }

  public Pair<Integer, Integer> getBoundLast(Object index) {
    return stringBinds.get(index).get(stringBinds.get(index).size() - 1);
  }

  public List<Pair<Integer, Integer>> getBounds(Object index) {
    return stringBinds.get(index);
  }

  public Map<Object, List<Pair<Integer, Integer>>> getBounds() {
    return stringBinds;
  }

  public Stack<Object> getStack() {
    return stack;
  }

  public void trace(Object obj) {
    this.trace.add(obj);
  }

  public List<Object> getTrace() {
    return trace;
  }
}
