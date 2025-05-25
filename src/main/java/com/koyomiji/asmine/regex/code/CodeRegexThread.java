package com.koyomiji.asmine.regex.code;

import com.koyomiji.asmine.stencil.IParameterRegistry;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import com.koyomiji.asmine.regex.RegexThread;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CodeRegexThread extends RegexThread implements IParameterRegistry {
  protected HashMap<Object, Object> parameterBinds = new HashMap<>();

  public CodeRegexThread() {
    super();
  }

  @Override
  protected Object clone() {
    CodeRegexThread clone = (CodeRegexThread) super.clone();
    clone.parameterBinds = (HashMap<Object, Object>) this.parameterBinds.clone();
    return clone;
  }

  @Override
  public Object resolveParameter(Object key) throws ResolutionExeption {
    if (!parameterBinds.containsKey(key)) {
      throw new ResolutionExeption("Parameter not found: " + key);
    }

    return parameterBinds.get(key);
  }

  @Override
  public <T> void bindParameter(Object key, T value) {
    parameterBinds.put(key, value);
  }

  @Override
  public <T> boolean bindParameterIfAbsent(Object key, T value) {
    if (parameterBinds.containsKey(key)) {
      return false;
    }

    parameterBinds.put(key, value);
    return true;
  }

  @Override
  public <T> boolean compareParameters(T value1, T value2) {
    return Objects.equals(value1, value2);
  }

  @Override
  public <T> boolean compareParameterToBound(Object key, T value) {
    if (!parameterBinds.containsKey(key)) {
      return false;
    }

    return compareParameters(value, parameterBinds.get(key));
  }

  public Map<Object, Object> getParameterBinds() {
    return parameterBinds;
  }
}
