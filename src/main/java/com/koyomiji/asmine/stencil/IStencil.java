package com.koyomiji.asmine.stencil;

public interface IStencil<T> {
  boolean match(IStencilRegistry registry, T value);

  T instantiate(IStencilRegistry registry) throws ResolutionException;
}
