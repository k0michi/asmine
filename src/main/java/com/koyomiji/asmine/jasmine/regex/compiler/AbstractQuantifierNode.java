package com.koyomiji.asmine.regex.compiler;

public abstract class AbstractQuantifierNode extends AbstractRegexNode {
  public AbstractRegexNode child;
  public QuantifierType type;

  public AbstractQuantifierNode(AbstractRegexNode child) {
    this.child = child;
    this.type = QuantifierType.GREEDY;
  }

  public AbstractQuantifierNode(AbstractRegexNode child, QuantifierType type) {
    this.child = child;
    this.type = type;
  }
}
