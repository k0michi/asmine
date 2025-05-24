package com.koyomiji.asmine.stencil;

public class AnyStencil<T> implements IStencil<T> {
  @Override
  public boolean match(IStencilRegistry registry, T value) {
    return true;
  }

  @Override
  public T instantiate(IStencilRegistry registry) throws ResolutionExeption {
    throw new ResolutionExeption("ParameterAny cannot be instantiated");
  }
}
