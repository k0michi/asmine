package com.koyomiji.asmine.regex.compiler;

import java.util.List;

public class PseudoJumpInsn extends AbstractPseudoInsn {
  public PseudoLabelInsn label;

  public PseudoJumpInsn(PseudoLabelInsn label) {
    this.label = label;
  }
}
