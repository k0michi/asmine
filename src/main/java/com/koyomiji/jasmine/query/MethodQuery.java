package com.koyomiji.jasmine.query;

import com.koyomiji.jasmine.common.PrinterHelper;
import com.koyomiji.jasmine.regex.code.CodeMatchResult;
import com.koyomiji.jasmine.regex.code.CodeRegexMatcher;
import com.koyomiji.jasmine.regex.compiler.AbstractRegexNode;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Printer;

import java.io.PrintWriter;

public class MethodQuery<T> extends AbstractQuery<T> {
  protected MethodNode methodNode;
  protected CodeManipulator codeManipulator;

  public MethodQuery(T parent, MethodNode methodNode) {
    super(parent);
    this.methodNode = methodNode;
    this.codeManipulator = new CodeManipulator(methodNode);
  }

  public static MethodQuery<MethodNode> of(MethodNode methodNode) {
    return new MethodQuery<>(methodNode, methodNode);
  }

  public static MethodQuery<MethodNode> ofNew() {
    return of(new MethodNode());
  }

  public MethodQuery<T> addInsns(AbstractInsnNode... insns) {
    codeManipulator.addInsnsLast(insns);
    return this;
  }

  public MethodQuery<T> addInsnsFirst(AbstractInsnNode... insns) {
    codeManipulator.addInsnsFirst(insns);
    return this;
  }

  public MethodQuery<T> addInsnsLast(AbstractInsnNode... insns) {
    codeManipulator.addInsnsLast(insns);
    return this;
  }

  public MethodNode getNode() {
    return methodNode;
  }

  public MethodQuery<T> require() {
    if (methodNode == null) {
      throw new QueryException("Method not found");
    }

    return this;
  }

  public CodeFragmentQuery<MethodQuery<T>> selectCodeFragment(AbstractRegexNode regex) {
    CodeRegexMatcher matcher = new CodeRegexMatcher(regex);
    CodeMatchResult matchResult = matcher.match(methodNode.instructions, 0);
    return new CodeFragmentQuery<>(this, codeManipulator, matchResult);
  }

  public CodeFragmentQuery<MethodQuery<T>> selectCodeFragments(AbstractRegexNode regex) {
    CodeRegexMatcher matcher = new CodeRegexMatcher(regex);
    CodeMatchResult matchResult = matcher.matchAll(methodNode.instructions, 0);
    return new CodeFragmentQuery<>(this, codeManipulator, matchResult);
  }

  public MethodQuery<T> print(Printer printer) {
    return print(printer, new PrintWriter(System.out));
  }

  public MethodQuery<T> print(Printer printer, PrintWriter printWriter) {
    PrinterHelper.print(printer, methodNode, printWriter);
    return this;
  }
}
