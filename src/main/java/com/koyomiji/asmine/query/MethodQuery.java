package com.koyomiji.asmine.query;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.common.PrinterHelper;
import com.koyomiji.asmine.regex.code.CodeMatchResult;
import com.koyomiji.asmine.regex.code.CodeRegexMatcher;
import com.koyomiji.asmine.regex.compiler.AbstractRegexNode;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Printer;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MethodQuery<T> extends AbstractQuery<T> {
  protected MethodNode methodNode;
  protected CodeManipulator codeManipulator;

  public MethodQuery(T parent, MethodNode methodNode) {
    super(parent);
    this.methodNode = methodNode;

    if (methodNode != null) {
      this.codeManipulator = new CodeManipulator(methodNode);
    }
  }

  public static MethodQuery<MethodNode> of(MethodNode methodNode) {
    return new MethodQuery<>(methodNode, methodNode);
  }

  public static MethodQuery<MethodNode> ofNew() {
    return ofNew(0, null, null, null, null);
  }

  public static MethodQuery<MethodNode> ofNew(int access, String name, String descriptor, String signature, String[] exceptions) {
    return of(new MethodNode(access, name, descriptor, signature, exceptions));
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

  public int getAccess() {
    return methodNode.access;
  }

  public MethodQuery<T> setAccess(int access) {
    methodNode.access = access;
    return this;
  }

  public MethodQuery<T> addAccess(int access) {
    methodNode.access |= access;
    return this;
  }

  public MethodQuery<T> removeAccess(int access) {
    methodNode.access &= ~access;
    return this;
  }

  public String getName() {
    return methodNode.name;
  }

  public MethodQuery<T> setName(String name) {
    methodNode.name = name;
    return this;
  }

  public String getDescriptor() {
    return methodNode.desc;
  }

  public MethodQuery<T> setDescriptor(String descriptor) {
    methodNode.desc = descriptor;
    return this;
  }

  public String getSignature() {
    return methodNode.signature;
  }

  public MethodQuery<T> setSignature(String signature) {
    methodNode.signature = signature;
    return this;
  }

  public List<String> getExceptions() {
    return methodNode.exceptions;
  }

  public MethodQuery<T> setExceptions(List<String> exceptions) {
    methodNode.exceptions = exceptions;
    return this;
  }

  public MethodQuery<T> setExceptions(String... exceptions) {
    return setExceptions(ArrayListHelper.of(exceptions));
  }

  public MethodQuery<T> addException(String exception) {
    return addExceptions(Collections.singletonList(exception));
  }

  public MethodQuery<T> addExceptions(List<String> exceptions) {
    if (methodNode.exceptions == null) {
      methodNode.exceptions = new ArrayList<>();
    }

    methodNode.exceptions.addAll(exceptions);
    return this;
  }

  public MethodQuery<T> addExceptions(String... exceptions) {
    return addExceptions(Arrays.asList(exceptions));
  }

  public MethodQuery<T> removeException(String exception) {
    return removeExceptions(Collections.singletonList(exception));
  }

  public MethodQuery<T> removeExceptions(List<String> exceptions) {
    if (methodNode.exceptions != null) {
      methodNode.exceptions.removeAll(exceptions);
    }

    return this;
  }

  public MethodQuery<T> removeExceptions(String... exceptions) {
    return removeExceptions(Arrays.asList(exceptions));
  }

  // TODO: Parameters
  // TODO: Annotations
  // TODO: Attributes

  public Object getAnnotationDefault() {
    return methodNode.annotationDefault;
  }

  public MethodQuery<T> setAnnotationDefault(Object annotationDefault) {
    methodNode.annotationDefault = annotationDefault;
    return this;
  }

  public int getVisibleAnnotableParameterCount() {
    return methodNode.visibleAnnotableParameterCount;
  }

  public MethodQuery<T> setVisibleAnnotableParameterCount(int count) {
    methodNode.visibleAnnotableParameterCount = count;
    return this;
  }

  // TODO: Attributes

  public int getInvisibleAnnotableParameterCount() {
    return methodNode.invisibleAnnotableParameterCount;
  }

  public MethodQuery<T> setInvisibleAnnotableParameterCount(int count) {
    methodNode.invisibleAnnotableParameterCount = count;
    return this;
  }

  // TODO: Attributes

  public MethodQuery<T> addInsns(AbstractInsnNode... insns) {
    codeManipulator.addLast(insns);
    return this;
  }

  public MethodQuery<T> addInsnsFirst(AbstractInsnNode... insns) {
    codeManipulator.addFirst(insns);
    return this;
  }

  public MethodQuery<T> addInsnsLast(AbstractInsnNode... insns) {
    codeManipulator.addLast(insns);
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

  // TODO: TryCatch

  public int getMaxStack() {
    return methodNode.maxStack;
  }

  public MethodQuery<T> setMaxStack(int maxStack) {
    methodNode.maxStack = maxStack;
    return this;
  }

  public int getMaxLocals() {
    return methodNode.maxLocals;
  }

  public MethodQuery<T> setMaxLocals(int maxLocals) {
    methodNode.maxLocals = maxLocals;
    return this;
  }

  // TODO: LocalVariables
  // TODO: VariableAnnotations

  public MethodQuery<T> print(Printer printer) {
    return print(printer, new PrintWriter(System.out));
  }

  public MethodQuery<T> print(Printer printer, PrintWriter printWriter) {
    PrinterHelper.print(printer, methodNode, printWriter);
    return this;
  }
}
