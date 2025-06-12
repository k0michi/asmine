package com.koyomiji.asmine.regex.code;

import com.koyomiji.asmine.stencil.IStencilRegistry;
import com.koyomiji.asmine.stencil.StencilEvaluationException;
import com.koyomiji.asmine.regex.RegexThread;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CodeRegexThread extends RegexThread implements IStencilRegistry {
  protected HashMap<Object, Object> stencilBinds = new HashMap<>();

  public CodeRegexThread(int id) {
    super(id);
  }

  @Override
  public CodeRegexThread clone() {
    CodeRegexThread clone = (CodeRegexThread) super.clone();
    clone.stencilBinds = (HashMap<Object, Object>) this.stencilBinds.clone();
    return clone;
  }

  @Override
  public Object resolveStencil(Object key) throws StencilEvaluationException {
    if (!stencilBinds.containsKey(key)) {
      throw new StencilEvaluationException("Stencil not found: " + key);
    }

    return stencilBinds.get(key);
  }

  @Override
  public <T> void bindStencil(Object key, T value) {
    stencilBinds.put(key, value);
  }

  @Override
  public <T> boolean bindStencilIfAbsent(Object key, T value) {
    if (stencilBinds.containsKey(key)) {
      return false;
    }

    stencilBinds.put(key, value);
    return true;
  }

  @Override
  public <T> boolean compareValues(T value1, T value2) {
    return Objects.equals(value1, value2);
  }

  @Override
  public <T> boolean compareBoundToValue(Object key, T value) {
    if (!stencilBinds.containsKey(key)) {
      return false;
    }

    return compareValues(value, stencilBinds.get(key));
  }

  public Map<Object, Object> getStencilBounds() {
    return stencilBinds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    CodeRegexThread that = (CodeRegexThread) o;
    return Objects.equals(stencilBinds, that.stencilBinds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), stencilBinds);
  }
}
