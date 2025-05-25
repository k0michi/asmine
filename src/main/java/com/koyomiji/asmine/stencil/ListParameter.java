package com.koyomiji.asmine.stencil;

import com.koyomiji.asmine.common.ArrayListHelper;

import java.util.ArrayList;
import java.util.List;

public class ListParameter<T> extends AbstractParameter<List<T>> {
  public List<AbstractParameter<T>> parameters;

  public ListParameter(List<AbstractParameter<T>> parameters) {
    this.parameters = parameters;
  }

  public ListParameter(AbstractParameter<T>... parameters) {
    this.parameters = ArrayListHelper.of(parameters);
  }

  @Override
  public boolean match(IParameterRegistry registry, List<T> value) {
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
  public List<T> instantiate(IParameterRegistry registry) throws ResolutionExeption {
    ArrayList<T> values = new ArrayList<>(parameters.size());

    for (AbstractParameter<T> parameter : parameters) {
      values.add(parameter.instantiate(registry));
    }

    return values;
  }
}
