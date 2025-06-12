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
  protected HashMap<Object, Pair<Integer, Integer>> stringBinds = new HashMap<>();
  protected ArrayList<Object> trace = new ArrayList<>();

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
      clone.stringBinds = (HashMap<Object, Pair<Integer, Integer>>) this.stringBinds.clone();

      clone.callStack = new Stack<>();
      for (CallFrame callFrame : this.callStack) {
        clone.callStack.push(callFrame.clone());
      }

      clone.trace = (ArrayList<Object>) this.trace.clone();
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

    if (scope.key != null) {
      stringBinds.put(scope.key, Pair.of(scope.begin, scope.end));
    }

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
    return stringBinds.get(key);
  }

  // FIXME: This is a temporary method.
  public List<Pair<Integer, Integer>> getBounds(Object key) {
    List<Pair<Integer, Integer>> stringBinds = new ArrayList<>();

    for (RegexThreadScope scope : scopes) {
      if (Objects.equals(scope.key, key)) {
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

  public Pair<Integer, Integer> getBoundLast(Object key) {
    List<Pair<Integer, Integer>> bounds = getBounds(key);

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RegexThread that = (RegexThread) o;
    return terminated == that.terminated && functionPointer == that.functionPointer && programCounter == that.programCounter && Objects.equals(stack, that.stack) && Objects.equals(callStack, that.callStack) && Objects.equals(scopes, that.scopes) && Objects.equals(scopeStack, that.scopeStack) && Objects.equals(stringBinds, that.stringBinds) && Objects.equals(trace, that.trace);
  }

  @Override
  public int hashCode() {
    return Objects.hash(terminated, functionPointer, programCounter, stack, callStack, scopes, scopeStack, stringBinds, trace);
  }
}
