package com.koyomiji.asmine.stencil;

public class ConstParameter<T> extends AbstractParameter<T> {
  public T value;

  public ConstParameter(T value) {
    this.value = value;
  }

  @Override
  public boolean match(IParameterRegistry registry, T value) {
    return registry.compareParameters(this.value, value);
  }

  @Override
  public T instantiate(IParameterRegistry registry) throws ResolutionExeption {
    return value;
  }
}
