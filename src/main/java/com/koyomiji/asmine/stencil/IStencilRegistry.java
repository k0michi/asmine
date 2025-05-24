package com.koyomiji.asmine.stencil;

public interface IStencilRegistry {
  Object resolveParameter(Object key) throws EvaluationException;

  <T> void bindParameter(Object key, T value);

  <T> boolean bindParameterIfAbsent(Object key, T value);

  <T> boolean compareParameters(T value1, T value2);

  <T> boolean compareParameterToBound(Object key, T value);
}
