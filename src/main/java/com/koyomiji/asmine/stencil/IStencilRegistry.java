package com.koyomiji.asmine.stencil;

public interface IStencilRegistry {
  Object resolveStencil(Object key) throws StencilEvaluationException;

  <T> void bindStencil(Object key, T value);

  <T> boolean bindStencilIfAbsent(Object key, T value);

  <T> boolean compareValues(T value1, T value2);

  <T> boolean compareBoundToValue(Object key, T value);
}
