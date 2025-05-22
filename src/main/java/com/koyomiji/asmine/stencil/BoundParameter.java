package com.koyomiji.asmine.stencil;

public class BoundParameter<T> extends AbstractParameter<T> {
  public Object key;

  public BoundParameter(Object key) {
    this.key = key;
  }

  @Override
  public boolean match(IParameterRegistry registry, T value) {
    return registry.compareParameterToBound(key, value);
  }

  @Override
  public T instantiate(IParameterRegistry registry) throws ResolutionExeption {
    // There is no way to check the type is T
    return (T) registry.resolveParameter(key);
  }
}
