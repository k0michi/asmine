package com.koyomiji.asmine.regex;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.common.ListHelper;
import com.koyomiji.asmine.tuple.Pair;

import java.util.*;

public class RegexProcessor {
  protected RegexModule module;
  protected List<?> string;
  protected List<RegexThread> threads;
  protected int stringPointer = 0;
  protected int nextThreadID = 0;

  public RegexProcessor(RegexModule module, List<?> string) {
    this.module = module;
    this.string = string;
  }

  public RegexProcessor(List<AbstractRegexInsn> insns, List<?> string) {
    this(new RegexModule(ArrayListHelper.of(new RegexFunction(0, insns))), string);
  }

  protected RegexThread newThread(int id) {
    return new RegexThread(id);
  }

  protected MatchResult newMatchResult(RegexThread thread) {
    return new MatchResult(thread);
  }

  public RegexThread cloneThread(RegexThread thread) {
    return thread.clone(nextThreadID++);
  }

  protected AbstractRegexInsn getInstruction(RegexThread thread) {
    if (!module.hasFunction(thread.functionPointer)) {
      throw new RegexProcessorException("Function not found: " + thread.functionPointer);
    }

    return this.module.getFunction(thread.functionPointer).insns.get(thread.getProgramCounter());
  }

  protected List<RegexThread> step(RegexThread thread) {
    return getInstruction(thread).execute(this, thread);
  }

  private List<RegexThread> skipTransitives(RegexThread thread) {
    Stack<RegexThread> stack = new Stack<>();
    stack.push(thread);

    LinkedList<RegexThread> intransitives = new LinkedList<>();

    while (!stack.isEmpty()) {
      RegexThread t = stack.pop();

      if (t.isRunning() && getInstruction(t).getExecutionType() == AbstractRegexInsn.TRANSITIVE) {
        List<RegexThread> children = step(t);

        for (int i = children.size() - 1; i >= 0; i--) {
          RegexThread child = children.get(i);
          stack.push(child);
        }
      } else {
        intransitives.add(t);
      }
    }

    return intransitives;
  }

  protected void visitChar(Object character) {
  }

  public RegexThread execute() {
    return execute(0);
  }

  private void addThread(List<RegexThread> threads, Set<RegexThread> memo, RegexThread thread) {
    if (memo.add(thread)) {
      threads.add(thread);
    }
  }

  public RegexThread execute(int begin) {
    this.threads = new ArrayList<>();
    this.threads.add(newThread(nextThreadID++));
    RegexThread terminated = null;

    for (stringPointer = begin; stringPointer <= string.size(); stringPointer++) {
      visitChar(getCurrentChar());

      List<RegexThread> next = new ArrayList<>();
      Set<RegexThread> nextMemo = new HashSet<>();

      match:
      for (RegexThread t : threads) {
        if (t.isTerminated()) {
          terminated = t;
          break match;
        }

        if (isTransitiveChar(getCurrentChar())) {
          // Consume the current char. This is prioritized over skipping
          for (RegexThread u : skipTransitives(t.clone())) {
            if (u.isTerminated()) {
              terminated = u;
              break match;
            }

            for (RegexThread v : step(u)) {
              addThread(next, nextMemo, v);
            }
          }

          // In case of skipping the current char
          addThread(next, nextMemo, t);
        } else {
          for (RegexThread u : skipTransitives(t)) {
            if (u.isTerminated()) {
              terminated = u;
              break match;
            }

            for (RegexThread v : step(u)) {
              addThread(next, nextMemo, v);
            }
          }
        }
      }

      threads = next;
    }

    return terminated;
  }

  /*
   * Compare
   */

  public boolean compareCharToLiteral(Object actual, Object expected) {
    return Objects.equals(actual, expected);
  }

  public boolean compareCurrentCharToLiteral(Object expected) {
    if (stringPointer < 0 || stringPointer >= string.size()) {
      return false;
    }

    return compareCharToLiteral(getCurrentChar(), expected);
  }

  public boolean compareSubstrings(Pair<Integer, Integer> range1, Pair<Integer, Integer> range2) {
    if (range1.second - range1.first != range2.second - range2.first) {
      return false;
    }

    for (int i = 0; i < range1.second - range1.first; i++) {
      if (!compareCharToLiteral(string.get(range1.first + i), string.get(range2.first + i))) {
        return false;
      }
    }

    return true;
  }

  protected boolean isTransitiveChar(Object character) {
    return false;
  }

  /*
   * Getters
   */

  public RegexModule getModule() {
    return module;
  }

  public List<?> getString() {
    return string;
  }

  public List<RegexThread> getThreads() {
    return threads;
  }

  public int getStringPointer() {
    return stringPointer;
  }

  public int getStringLength() {
    return string.size();
  }

  public Object getCurrentChar() {
    return ListHelper.getOrNull(this.string, stringPointer);
  }
}
