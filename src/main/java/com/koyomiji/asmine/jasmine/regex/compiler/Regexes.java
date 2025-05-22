package com.koyomiji.asmine.regex.compiler;

import com.koyomiji.asmine.regex.AnchorBeginInsn;

public class Regexes {
  public static AlternateNode alternate(AbstractRegexNode... options) {
    return new AlternateNode(options);
  }

  public static AnchorBeginNode anchorBegin() {
    return new AnchorBeginNode();
  }

  public static AnchorEndNode anchorEnd() {
    return new AnchorEndNode();
  }

  public static AnyNode any() {
    return new AnyNode();
  }

  public static BindNode bind(Object key, AbstractRegexNode node) {
    return new BindNode(key, node);
  }

  public static BoundNode bound(Object key) {
    return new BoundNode(key);
  }

  public static ConcatenateNode concatenate(AbstractRegexNode... nodes) {
    return new ConcatenateNode(nodes);
  }

  public static PlusNode plus(AbstractRegexNode child) {
    return new PlusNode(child);
  }

  public static PlusNode plus(AbstractRegexNode child, QuantifierType type) {
    return new PlusNode(child, type);
  }

  public static QuestionNode question(AbstractRegexNode child) {
    return new QuestionNode(child);
  }

  public static QuestionNode question(AbstractRegexNode child, QuantifierType type) {
    return new QuestionNode(child, type);
  }

  public static StarNode star(AbstractRegexNode child) {
    return new StarNode(child);
  }

  public static StarNode star(AbstractRegexNode child, QuantifierType type) {
    return new StarNode(child, type);
  }
}
