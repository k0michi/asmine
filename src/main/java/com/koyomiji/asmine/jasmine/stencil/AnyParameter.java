package com.koyomiji.asmine.stencil;

public class AnyParameter<T> extends AbstractParameter<T> {
  @Override
  public boolean match(IParameterRegistry registry, T value) {
    return true;
  }

  @Override
  public T instantiate(IParameterRegistry registry) throws ResolutionExeption {
    throw new ResolutionExeption("ParameterAny cannot be instantiated");
  }
}
