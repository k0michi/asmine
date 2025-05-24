package com.koyomiji.asmine.stencil;

import com.koyomiji.asmine.common.ArrayListHelper;

import java.util.ArrayList;
import java.util.List;

public class ListStencil<T> implements IStencil<List<T>> {
  public List<IStencil<T>> parameters;

  public ListStencil(List<IStencil<T>> parameters) {
    this.parameters = parameters;
  }

  public ListStencil(IStencil<T>... parameters) {
    this.parameters = ArrayListHelper.of(parameters);
  }

  @Override
  public boolean match(IStencilRegistry registry, List<T> value) {
    if (value.size() != parameters.size()) {
      return false;
    }

    for (int i = 0; i < parameters.size(); i++) {
      if (!parameters.get(i).match(registry, value.get(i))) {
        return false;
      }
    }

    return true;
  }

  @Override
  public List<T> evaluate(IStencilRegistry registry) throws EvaluationException {
    ArrayList<T> values = new ArrayList<>(parameters.size());

    for (IStencil<T> parameter : parameters) {
      values.add(parameter.evaluate(registry));
    }

    return values;
  }
}
