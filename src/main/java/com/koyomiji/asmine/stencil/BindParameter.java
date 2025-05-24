package com.koyomiji.asmine.stencil;

public class BindParameter<T> implements IStencil<T> {
  public Object key;
  public IStencil<T> child;

  public BindParameter(Object key) {
    this(key, new AnyParameter<>());
  }


  public BindParameter(Object key, IStencil<T> child) {
    this.key = key;
    this.child = child;
  }

  @Override
  public boolean match(IParameterRegistry registry, T value) {
    if (child.match(registry, value)) {
      registry.bindParameter(key, value);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public T instantiate(IParameterRegistry registry) throws ResolutionExeption {
    throw new ResolutionExeption("BindParameter cannot be instantiated");
  }
}
