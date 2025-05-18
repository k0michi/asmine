package com.koyomiji.jasmine.query;

import com.koyomiji.jasmine.common.InsnListListAdapter;
import com.koyomiji.jasmine.common.PrinterHelper;
import com.koyomiji.jasmine.regex.AbstractRegexInsn;
import com.koyomiji.jasmine.regex.MatchResult;
import com.koyomiji.jasmine.regex.RegexModule;
import com.koyomiji.jasmine.regex.code.CodeMatchResult;
import com.koyomiji.jasmine.regex.code.CodeRegexProcessor;
import com.koyomiji.jasmine.regex.compiler.AbstractRegexNode;
import com.koyomiji.jasmine.regex.compiler.RegexCompiler;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Printer;

import java.io.PrintWriter;
import java.util.*;

public class MethodQuery<T> extends AbstractQuery<T> {
  protected MethodNode methodNode;
  protected MethodManipulator methodManipulator;

  public MethodQuery(T parent, MethodNode methodNode) {
    super(parent);
    this.methodNode = methodNode;
    this.methodManipulator = new MethodManipulator(methodNode);
  }

  public static MethodQuery<MethodNode> of(MethodNode methodNode) {
    return new MethodQuery<>(methodNode, methodNode);
  }

  public static MethodQuery<MethodNode> ofNew() {
    return of(new MethodNode());
  }

  public MethodQuery<T> addInsns(AbstractInsnNode... insns) {
    methodManipulator.addInsnsLast(insns);
    return this;
  }

  public MethodQuery<T> addInsnsFirst(AbstractInsnNode... insns) {
    methodManipulator.addInsnsFirst(insns);
    return this;
  }

  public MethodQuery<T> addInsnsLast(AbstractInsnNode... insns) {
    methodManipulator.addInsnsLast(insns);
    return this;
  }

  public MethodNode getNode() {
    return methodNode;
  }

  public MethodQuery<T> require() {
    if (methodNode == null) {
      throw new IllegalStateException("Method not found");
    }

    return this;
  }

  public CodeFragmentQuery<MethodQuery<T>> selectCodeFragment(AbstractRegexNode regex) {
    RegexCompiler compiler = new RegexCompiler();
    RegexModule module = compiler.compile(regex);
    CodeRegexProcessor processor = new CodeRegexProcessor(module, new InsnListListAdapter(methodNode.instructions));
    CodeMatchResult matchResult = (CodeMatchResult) processor.match();
    return new CodeFragmentQuery<>(this, methodManipulator, matchResult);
  }

  public CodeFragmentsQuery<MethodQuery<T>> selectCodeFragments(AbstractRegexNode regex) {
    RegexCompiler compiler = new RegexCompiler();
    RegexModule module = compiler.compile(regex);
    CodeRegexProcessor processor = new CodeRegexProcessor(module, new InsnListListAdapter(methodNode.instructions));
    List<MatchResult> matchResults = processor.matchAll();
    return new CodeFragmentsQuery<>(this, methodManipulator, (List<CodeMatchResult>) (Object) matchResults);
  }

  public MethodQuery<T> print(Printer printer) {
    return print(printer, new PrintWriter(System.out));
  }

  public MethodQuery<T> print(Printer printer, PrintWriter printWriter) {
    PrinterHelper.print(printer, methodNode, printWriter);
    return this;
  }
}
