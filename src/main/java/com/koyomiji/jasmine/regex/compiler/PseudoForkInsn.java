package com.koyomiji.jasmine.regex.compiler;


import com.koyomiji.jasmine.common.ArrayListHelper;

import java.util.List;

public class PseudoForkInsn extends AbstractPseudoInsn {
  public List<PseudoLabelInsn> labels;

  public PseudoForkInsn(PseudoLabelInsn label0, PseudoLabelInsn label1) {
    this.labels = ArrayListHelper.of(label0, label1);
  }

  public PseudoForkInsn(List<PseudoLabelInsn> labels) {
    this.labels = labels;
  }
}
