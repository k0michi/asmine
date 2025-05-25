package com.koyomiji.asmine.stencil;

public abstract class AbstractParameter<T> {
  public abstract boolean match(IParameterRegistry registry, T value);

  public abstract T instantiate(IParameterRegistry registry) throws ResolutionExeption;
}
