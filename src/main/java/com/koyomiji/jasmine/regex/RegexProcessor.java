package com.koyomiji.jasmine.regex;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.common.ListHelper;
import com.koyomiji.jasmine.tuple.Pair;

import java.util.*;

public class RegexProcessor {
  protected RegexModule module;
  protected List<?> string;
  protected List<RegexThread> threads;
  protected int stringPointer = 0;

  public RegexProcessor(RegexModule module, List<?> string) {
    this.module = module;
    this.string = string;
  }

  public RegexProcessor(List<AbstractRegexInsn> insns, List<?> string) {
    this(new RegexModule(ArrayListHelper.of(new RegexFunction(0, insns))), string);
  }

  protected RegexThread newThread() {
    return new RegexThread();
  }

  protected MatchResult newMatchResult(RegexThread thread) {
    return new MatchResult(thread);
  }

  protected AbstractRegexInsn getInstruction(RegexThread thread) {
    return this.module.getFunction(thread.functionPointer).insns.get(thread.getProgramCounter());
  }

  protected Pair<Boolean, List<RegexThread>> step(RegexThread thread) {
    return getInstruction(thread).execute(this, thread);
  }

  private List<RegexThread> skipTransitive(RegexThread thread) {
    LinkedList<RegexThread> intransitives = new LinkedList<>();

    if (getInstruction(thread).isTransitive()) {
      Pair<Boolean, List<RegexThread>> result = step(thread);

      if (result.first) {
        for (RegexThread t : result.second) {
          intransitives.addAll(skipTransitive(t));
        }
      }
    } else {
      intransitives.add(thread);
    }

    return intransitives;
  }

  public MatchResult match() {
    return match(0);
  }

  public MatchResult match(int begin) {
    RegexThread terminated = execute(begin);

    if (terminated == null) {
      return null;
    }

    return newMatchResult(terminated);
  }

  public List<MatchResult> matchAll() {
    List<MatchResult> results = new ArrayList<>();
    int begin = 0;

    while (true) {
      MatchResult terminated = match(begin);

      if (terminated == null) {
        break;
      }

      results.add(terminated);
      begin = terminated.getRange().second;
    }

    return results;
  }

  public RegexThread execute() {
    return execute(0);
  }

  public RegexThread execute(int begin) {
    this.threads = new ArrayList<>();
    this.threads.add(newThread());
    RegexThread terminated = null;

    for (stringPointer = begin; stringPointer <= string.size(); stringPointer++) {
      if (isTransitiveChar(getCurrentChar())) {
        continue;
      }

      List<RegexThread> next = new ArrayList<>();

      match:
      for (int j = 0; j < threads.size(); j++) {
        RegexThread thread = threads.get(j);
        List<RegexThread> intransitives = skipTransitive(thread);

        for (int k = 0; k < intransitives.size(); k++) {
          RegexThread t = intransitives.get(k);
          Pair<Boolean, List<RegexThread>> result = step(t);

          if (result.first) {
            if (result.second.size() == 0) {
              terminated = t;
              break match;
            } else {
              next.addAll(result.second);
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
