package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.tuple.Pair;

import java.util.*;

public class RegexThread implements Cloneable {
  protected int id;
  protected boolean terminated = false;
  protected int functionPointer = 0;
  protected int programCounter = 0;
  protected Stack<Object> stack = new Stack<>();
  protected Stack<CallFrame> callStack = new Stack<>();
  protected List<RegexThreadScope> scopes = new ArrayList<>();
  protected Stack<Integer> scopeStack = new Stack<>();
  protected List<Object> trace = new ArrayList<>();

  public RegexThread(int id) {
    this.id = id;
    callStack.push(new CallFrame());
    callStack.push(new CallFrame());
    beginScope(-1);
  }

  @Override
  public RegexThread clone() {
    try {
      RegexThread clone = (RegexThread) super.clone();
      clone.stack = (Stack<Object>) this.stack.clone();
      clone.scopes = new ArrayList<>();

      for (RegexThreadScope bind : this.scopes) {
        clone.scopes.add(bind.clone());
      }

      clone.scopeStack = (Stack<Integer>) this.scopeStack.clone();

      clone.callStack = new Stack<>();
      for (CallFrame callFrame : this.callStack) {
        clone.callStack.push(callFrame.clone());
      }
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  public RegexThread clone(int id) {
    RegexThread clone = (RegexThread) this.clone();
    clone.id = id;
    return clone;
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

  public void pushCall(CallFrame callFrame, int stringPointer) {
    callStack.push(callFrame);
    beginScope(stringPointer);
  }

  public CallFrame popCall(int stringPointer) {
    CallFrame popped = callStack.pop();
    endScope(stringPointer);
    return popped;
  }

  protected RegexThreadScope newRegexThreadScope(int parent) {
    return new RegexThreadScope(parent);
  }

  public RegexThreadScope beginScope(int stringPointer) {
    int index = scopes.size();
    int parent = -1;

    if (!scopeStack.isEmpty()) {
      parent = scopeStack.peek();
      RegexThreadScope parentScope = scopes.get(parent);
      parentScope.addChild(index);
    }

    RegexThreadScope scope = newRegexThreadScope(parent);
    scope.setBegin(stringPointer);
    scopes.add(scope);
    scopeStack.push(index);
    return scope;
  }

  public RegexThreadScope endScope(int stringPointer) {
    int index = scopeStack.pop();
    RegexThreadScope scope = scopes.get(index);
    scope.setEnd(stringPointer);
    return scope;
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

  public RegexThreadScope beginBind(Object key, int stringPointer) {
    RegexThreadScope scope = beginScope(stringPointer);
    scope.key = key;
    return scope;
  }

  public RegexThreadScope endBind(int stringPointer) {
    return endScope(stringPointer);
  }

  // Returns the bound range for the current scope and the given key
  public Pair<Integer, Integer> getScopedBound(Object key) {
    Pair<Integer, Integer> result = null;

    // Check ancestors
    for (Integer index : scopeStack) {
      RegexThreadScope scope = scopes.get(index);
      List<Integer> children = scope.children;

      for (Integer childIndex : children) {
        RegexThreadScope childScope = scopes.get(childIndex);

        if (childScope.isEnded() && Objects.equals(childScope.key, key)) {
          result = Pair.of(childScope.begin, childScope.end);
        }
      }
    }

    // Check siblings' children
    if (result == null) {
      for (RegexThreadScope scope : scopes) {
        if (scope.isEnded() && Objects.equals(scope.key, key)) {
          result = Pair.of(scope.begin, scope.end);
        }
      }
    }

    return result;
  }

  // FIXME: This is a temporary method.
  public List<Pair<Integer, Integer>> getBounds(Object index) {
    List<Pair<Integer, Integer>> stringBinds = new ArrayList<>();

    for (RegexThreadScope scope : scopes) {
      if (Objects.equals(scope.key, index)) {
        stringBinds.add(Pair.of(scope.begin, scope.end));
      }
    }

    return stringBinds;
  }

  // FIXME: This is a temporary method.
  public Map<Object, List<Pair<Integer, Integer>>> getBounds() {
    Map<Object, List<Pair<Integer, Integer>>> bounds = new HashMap<>();

    for (RegexThreadScope scope : scopes) {
      if (!bounds.containsKey(scope.key)) {
        bounds.put(scope.key, new ArrayList<>());
      }
      bounds.get(scope.key).add(Pair.of(scope.begin, scope.end));
    }

    return bounds;
  }

  public Pair<Integer, Integer> getBoundLast(Object index) {
    List<Pair<Integer, Integer>> bounds = getBounds(index);

    if (bounds.isEmpty()) {
      return null;
    }

    return bounds.get(bounds.size() - 1);
  }

  public int getID() {
    return id;
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
