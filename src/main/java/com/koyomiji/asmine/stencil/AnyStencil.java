package com.koyomiji.asmine.stencil;

public class AnyStencil<T> implements IStencil<T> {
  @Override
  public boolean match(IStencilRegistry registry, T value) {
    return true;
  }

  @Override
  public T evaluate(IStencilRegistry registry) throws EvaluationException {
    throw new EvaluationException("ParameterAny cannot be instantiated");
  }
}
