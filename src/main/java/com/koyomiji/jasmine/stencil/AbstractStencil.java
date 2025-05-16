package com.koyomiji.jasmine.stencil;

public abstract class AbstractStencil<T> {
  public abstract T instantiate(IParameterRegistry registry) throws ResolutionExeption;
}
