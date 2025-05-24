package com.koyomiji.asmine.stencil;

public class ConstStencil<T> implements IStencil<T> {
  public T value;

  public ConstStencil(T value) {
    this.value = value;
  }

  @Override
  public boolean match(IStencilRegistry registry, T value) {
    return registry.compareParameters(this.value, value);
  }

  @Override
  public T evaluate(IStencilRegistry registry) throws StencilEvaluationException {
    return value;
  }
}
