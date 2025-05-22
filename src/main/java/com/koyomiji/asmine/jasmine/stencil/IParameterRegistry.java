package com.koyomiji.asmine.stencil;

public interface IParameterRegistry {
  Object resolveParameter(Object key) throws ResolutionExeption;

  <T> void bindParameter(Object key, T value);

  <T> boolean bindParameterIfAbsent(Object key, T value);

  <T> boolean compareParameters(T value1, T value2);

  <T> boolean compareParameterToBound(Object key, T value);
}
