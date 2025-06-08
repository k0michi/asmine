package com.koyomiji.asmine.regex.code;

import com.koyomiji.asmine.regex.MatchResult;
import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.StencilEvaluationException;

import java.util.Map;

public class CodeMatchResult extends MatchResult implements IStencilRegistry {
  private final CodeRegexThread thread;

  public CodeMatchResult(CodeRegexThread thread) {
    super(thread);
    this.thread = thread;
  }

  @Override
  public Object resolveStencil(Object key) throws StencilEvaluationException {
    return thread.resolveStencil(key);
  }

  @Override
  public <T> void bindStencil(Object key, T value) {
    thread.bindStencil(key, value);
  }

  @Override
  public <T> boolean bindStencilIfAbsent(Object key, T value) {
    return thread.bindStencilIfAbsent(key, value);
  }

  @Override
  public <T> boolean compareValues(T value1, T value2) {
    return thread.compareValues(value1, value2);
  }

  @Override
  public <T> boolean compareBoundToValue(Object key, T value) {
    return thread.compareBoundToValue(key, value);
  }

  public Map<Object, Object> getParameterBinds() {
    return thread.getStencilBounds();
  }
}
