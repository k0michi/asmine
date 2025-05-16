package com.koyomiji.jasmine.query;

import com.koyomiji.jasmine.common.ArrayHelper;
import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.regex.code.CodeMatchResult;
import com.koyomiji.jasmine.stencil.insn.AbstractInsnStencil;

import java.util.List;

public class CodeFragmentsQuery<T> extends AbstractQuery<T> {
  protected List<CodeFragmentQuery<T>> queries;

  public CodeFragmentsQuery(T parent, MethodManipulator methodManipulator, List<CodeMatchResult> matchResults) {
    super(parent);
    this.queries = ArrayListHelper.of();

    for (CodeMatchResult matchResult : matchResults) {
      queries.add(new CodeFragmentQuery<>(parent, methodManipulator, matchResult));
    }
  }

  public CodeFragmentsQuery<T> replaceWith(AbstractInsnStencil insn) {
    return replaceWith(ArrayListHelper.of(insn));
  }

  public CodeFragmentsQuery<T> replaceWith(AbstractInsnStencil... insns) {
    return replaceWith(ArrayHelper.toList(insns));
  }

  public CodeFragmentsQuery<T> replaceWith(List<AbstractInsnStencil> insns) {
    for (CodeFragmentQuery<T> query : queries) {
      query.replaceWith(insns);
    }

    return this;
  }

  public CodeFragmentsQuery<T> remove() {
    for (CodeFragmentQuery<T> query : queries) {
      query.remove();
    }

    return this;
  }

  public int getPresentCount() {
    return queries.size();
  }
}
