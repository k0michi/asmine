package com.koyomiji.asmine.stencil;

public class AnyParameter<T> implements IStencil<T> {
  @Override
  public boolean match(IParameterRegistry registry, T value) {
    return true;
  }

  @Override
  public T instantiate(IParameterRegistry registry) throws ResolutionExeption {
    throw new ResolutionExeption("ParameterAny cannot be instantiated");
  }
}
