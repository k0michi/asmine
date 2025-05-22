package com.koyomiji.asmine.stencil;

public abstract class AbstractStencil<T> {
  public abstract T instantiate(IParameterRegistry registry) throws ResolutionExeption;
}
