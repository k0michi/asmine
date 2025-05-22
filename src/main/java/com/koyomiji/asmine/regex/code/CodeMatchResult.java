package com.koyomiji.asmine.regex.code;

import com.koyomiji.asmine.regex.MatchResult;
import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;

import java.util.Map;

public class CodeMatchResult extends MatchResult implements IParameterRegistry {
  private final CodeRegexThread thread;

  public CodeMatchResult(CodeRegexThread thread) {
    super(thread);
    this.thread = thread;
  }

  @Override
  public Object resolveParameter(Object key) throws ResolutionExeption {
    return thread.resolveParameter(key);
  }

  @Override
  public <T> void bindParameter(Object key, T value) {
    thread.bindParameter(key, value);
  }

  @Override
  public <T> boolean bindParameterIfAbsent(Object key, T value) {
    return thread.bindParameterIfAbsent(key, value);
  }

  @Override
  public <T> boolean compareParameters(T value1, T value2) {
    return thread.compareParameters(value1, value2);
  }

  @Override
  public <T> boolean compareParameterToBound(Object key, T value) {
    return thread.compareParameterToBound(key, value);
  }

  public Map<Object, Object> getParameterBinds() {
    return thread.getParameterBinds();
  }
}
