package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.tuple.Pair;

import java.util.*;

public class RegexThread implements Cloneable {
  protected boolean terminated = false;
  protected int functionPointer = 0;
  protected int programCounter = 0;
  protected Stack<Object> stack = new Stack<>();
  protected Stack<CallFrame> callStack = new Stack<>();
  protected HashMap<Object, List<Pair<Integer, Integer>>> stringBinds = new HashMap<>();
  protected List<Object> trace = new ArrayList<>();

  public RegexThread() {
    callStack.push(new CallFrame());
    callStack.push(new CallFrame());
  }

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

      clone.callStack = new Stack<>();
      for (CallFrame callFrame : this.callStack) {
        clone.callStack.push(callFrame.clone());
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

  public void pushCall(CallFrame callFrame) {
    callStack.push(callFrame);
  }

  public CallFrame popCall() {
    CallFrame popped = callStack.pop();
    CallFrame top = callStack.peek();
    top.merge(popped);
    return popped;
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

  public int callStackSize() {
    return callStack.size();
  }

  public void bind(Object key, Pair<Integer, Integer> range) {
    if (!stringBinds.containsKey(key)) {
      stringBinds.put(key, new ArrayList<>());
    }

    this.stringBinds.get(key).add(range);
    this.callStack.peek().bind(key, range);
  }

  public Pair<Integer, Integer> getScopedBound(Object key) {
    Pair<Integer, Integer> result = null;

    for (CallFrame frame : callStack) {
      if (frame.stringBinds.containsKey(key)) {
        result = frame.stringBinds.get(key);
      }
    }

    return result;
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
