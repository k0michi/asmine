package com.koyomiji.asmine.stencil;

public class BindStencil<T> implements IStencil<T> {
  public Object key;
  public IStencil<T> child;

  public BindStencil(Object key) {
    this(key, new AnyStencil<>());
  }


  public BindStencil(Object key, IStencil<T> child) {
    this.key = key;
    this.child = child;
  }

  @Override
  public boolean match(IStencilRegistry registry, T value) {
    if (child.match(registry, value)) {
      registry.bindStencil(key, value);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public T evaluate(IStencilRegistry registry) throws StencilEvaluationException {
    throw new StencilEvaluationException("BindParameter cannot be instantiated");
  }
}
