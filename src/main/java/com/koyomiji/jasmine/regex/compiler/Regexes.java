package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.regex.AnchorBeginInsn;

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

  public static QuestionNode question(AbstractRegexNode child) {
    return new QuestionNode(child);
  }

  public static StarNode star(AbstractRegexNode child) {
    return new StarNode(child);
  }
}
