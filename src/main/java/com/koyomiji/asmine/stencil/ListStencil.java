package com.koyomiji.asmine.stencil;

import com.koyomiji.asmine.common.ArrayListHelper;

import java.util.ArrayList;
import java.util.List;

public class ListStencil<T> implements IStencil<List<T>> {
  public List<IStencil<T>> stencils;

  public ListStencil(List<IStencil<T>> stencils) {
    this.stencils = stencils;
  }

  @SafeVarargs
  public ListStencil(IStencil<T>... stencils) {
    this.stencils = ArrayListHelper.of(stencils);
  }

  @Override
  public boolean match(IStencilRegistry registry, List<T> value) {
    if (value.size() != stencils.size()) {
      return false;
    }

    for (int i = 0; i < stencils.size(); i++) {
      if (!stencils.get(i).match(registry, value.get(i))) {
        return false;
      }
    }

    return true;
  }

  @Override
  public List<T> evaluate(IStencilRegistry registry) throws StencilEvaluationException {
    ArrayList<T> values = new ArrayList<>(stencils.size());

    for (IStencil<T> stencil : stencils) {
      values.add(stencil.evaluate(registry));
    }

    return values;
  }
}
