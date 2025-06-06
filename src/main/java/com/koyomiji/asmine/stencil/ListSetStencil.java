package com.koyomiji.asmine.stencil;

import java.util.List;

public class ListSetStencil<T> implements IStencil<List<T>> {
  public IStencil<List<T>> list;
  public int index;
  public T value;
  public T defaultValue;

  public ListSetStencil(IStencil<List<T>> list, int index, T value) {
    this(list, index, value, null);
  }

  public ListSetStencil(IStencil<List<T>> list, int index, T value, T defaultValue) {
    this.list = list;
    this.index = index;
    this.value = value;
    this.defaultValue = defaultValue;
  }

  @Override
  public boolean match(IStencilRegistry registry, List<T> value) {
    throw new UnsupportedOperationException("ListSetStencil does not support match");
  }

  @Override
  public List<T> evaluate(IStencilRegistry registry) throws StencilEvaluationException {
    List<T> listEvaluated = list.evaluate(registry);

    while (listEvaluated.size() <= index) {
      listEvaluated.add(defaultValue);
    }

    listEvaluated.set(index, value);
    return listEvaluated;
  }
}
