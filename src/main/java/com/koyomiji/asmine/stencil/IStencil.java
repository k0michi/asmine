package com.koyomiji.asmine.stencil;

public interface IStencil<T> {
  boolean match(IStencilRegistry registry, T value);

  T evaluate(IStencilRegistry registry) throws StencilEvaluationException;
}
