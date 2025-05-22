package com.koyomiji.asmine.regex.compiler;

import com.koyomiji.asmine.regex.AbstractRegexInsn;
import com.koyomiji.asmine.regex.RegexFunction;
import com.koyomiji.asmine.tuple.Pair;

import java.util.*;

public class RegexCompilerContext {
  private Map<Object, BindNode> bindMap = new HashMap<>();
  private Map<Pair<ConcatenateNode, Boolean>, Integer> concatFunctionMap = new HashMap<>();
  private int insideBound = 0;
  private int function = 0;
  private List<RegexFunction> functions = new ArrayList<>();

  public RegexCompilerContext() {
    function = newFunction();
  }

  public void emit(AbstractRegexInsn insn) {
    functions.get(function).insns.add(insn);
  }

  public void pushBound() {
    insideBound++;
  }

  public void popBound() {
    insideBound--;
  }

  public boolean isInsideBound() {
    return insideBound > 0;
  }

  public void setBindNode(Object key, BindNode node) {
    if (bindMap.containsKey(key)) {
      throw new RegexCompilerException("Duplicate key: " + key);
    }

    bindMap.put(key, node);
  }

  public BindNode getBindNode(Object key) {
    if (!bindMap.containsKey(key)) {
      throw new RegexCompilerException("Key not found: " + key);
    }

    return bindMap.get(key);
  }

  public boolean hasConcatFunction(ConcatenateNode node) {
    return concatFunctionMap.containsKey(Pair.of(node, isInsideBound()));
  }

  public int getConcatFunction(ConcatenateNode node) {
    if (!concatFunctionMap.containsKey(Pair.of(node, isInsideBound()))) {
      throw new RegexCompilerException("Concat function not found: " + node);
    }

    return concatFunctionMap.get(Pair.of(node, isInsideBound()));
  }

  public int getFunction() {
    return function;
  }

  public List<RegexFunction> getFunctions() {
    return functions;
  }

  private int newFunction() {
    int function = functions.size();
    functions.add(new RegexFunction(function, new ArrayList<>()));
    return function;
  }

  private int newFunctionForConcat(ConcatenateNode node) {
    int function = newFunction();
    concatFunctionMap.put(Pair.of(node, isInsideBound()), function);
    return function;
  }

  public RegexCompilerContext newContext() {
    RegexCompilerContext newContext = new RegexCompilerContext();
    newContext.bindMap = this.bindMap;
    newContext.concatFunctionMap = this.concatFunctionMap;
    newContext.insideBound = this.insideBound;
    newContext.function = this.function;
    newContext.functions = this.functions;
    return newContext;
  }

  public RegexCompilerContext newConcatFunctionContext(ConcatenateNode node) {
    RegexCompilerContext newContext = newContext();
    newContext.function = newFunctionForConcat(node);
    return newContext;
  }
}
