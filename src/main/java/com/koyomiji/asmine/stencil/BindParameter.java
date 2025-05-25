package com.koyomiji.asmine.stencil;

public class BindParameter<T> extends AbstractParameter<T> {
  public Object key;
  public AbstractParameter<T> child;

  public BindParameter(Object key) {
    this(key, new AnyParameter<>());
  }


  public BindParameter(Object key, AbstractParameter<T> child) {
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
    Object resolved = registry.resolveParameter(key);

    // Due to the type erasure, we cannot assure that the resolved value is of type T
    // So we perform the match again
    if (!child.match(registry, (T) resolved)) {
      throw new ResolutionExeption("Resolved value does not match the child");
    }

    return (T) resolved;
  }
}
