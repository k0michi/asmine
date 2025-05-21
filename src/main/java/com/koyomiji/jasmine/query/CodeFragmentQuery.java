package com.koyomiji.jasmine.query;

import com.koyomiji.jasmine.common.ArrayHelper;
import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.common.InsnListHelper;
import com.koyomiji.jasmine.regex.RegexMatcher;
import com.koyomiji.jasmine.regex.code.CodeMatchResult;
import com.koyomiji.jasmine.stencil.ResolutionExeption;
import com.koyomiji.jasmine.stencil.insn.AbstractInsnStencil;
import com.koyomiji.jasmine.tuple.Pair;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeFragmentQuery<T> extends AbstractQuery<T> {
  protected MethodManipulator methodManipulator;
  protected CodeMatchResult matchResult;
  protected Map<Object, List<Pair<Object, Object>>> stringBinds;

  public CodeFragmentQuery(T parent, MethodManipulator methodManipulator, CodeMatchResult matchResult) {
    super(parent);
    this.methodManipulator = methodManipulator;
    this.matchResult = matchResult;

    this.stringBinds = new HashMap<>();

    if (matchResult != null) {
      for (Map.Entry<Object, List<Pair<Integer, Integer>>> entry : matchResult.getBounds().entrySet()) {
        for (Pair<Integer, Integer> range : entry.getValue()) {
          if (!stringBinds.containsKey(entry.getKey())) {
            stringBinds.put(entry.getKey(), ArrayListHelper.of());
          }

          stringBinds.get(entry.getKey()).add(Pair.of(methodManipulator.getIndexSymbol(range.first), methodManipulator.getIndexSymbol(range.second)));
        }
      }
    }
  }

  private InsnList instantiate(List<AbstractInsnStencil> insns) throws ResolutionExeption {
    InsnList insnList = new InsnList();

    for (AbstractInsnStencil insn : insns) {
      insnList.add(insn.instantiate(matchResult));
    }

    return insnList;
  }

  public CodeFragmentQuery<T> replaceBoundWith(Object key, AbstractInsnStencil insn) {
    return replaceBoundWith(key, ArrayListHelper.of(insn));
  }

  public CodeFragmentQuery<T> replaceWith(AbstractInsnStencil insn) {
    return replaceBoundWith(RegexMatcher.BOUNDARY_KEY, insn);
  }

  public CodeFragmentQuery<T> replaceBoundWith(Object key, AbstractInsnStencil... insns) {
    return replaceBoundWith(key, ArrayHelper.toList(insns));
  }

  public CodeFragmentQuery<T> replaceWith(AbstractInsnStencil... insns) {
    return replaceBoundWith(RegexMatcher.BOUNDARY_KEY, insns);
  }

  public CodeFragmentQuery<T> replaceBoundWith(Object key, List<AbstractInsnStencil> insns) {
    if (!stringBinds.containsKey(key)) {
      return this;
    }

    for(Pair<Object, Object> range : stringBinds.get(key)) {
      Pair<Integer, Integer> indices = methodManipulator.getIndicesForSymbols(range);

      if (indices == null) {
        continue;
      }

      try {
        methodManipulator.replaceInsns(
                indices.first,
                indices.second,
                instantiate(insns)
        );
      } catch (ResolutionExeption e) {
        throw new RuntimeException(e);
      }
    }

    return this;
  }

  public CodeFragmentQuery<T> replaceWith(List<AbstractInsnStencil> insns) {
    return replaceBoundWith(RegexMatcher.BOUNDARY_KEY, insns);
  }

  public CodeFragmentQuery<T> remove(Object key) {
    if (!stringBinds.containsKey(key)) {
      return this;
    }

    for(Pair<Object, Object> range : stringBinds.get(key)) {
      Pair<Integer, Integer> indices = methodManipulator.getIndicesForSymbols(range);

      if (indices == null) {
        continue;
      }

      methodManipulator.removeInsns(
              indices.first,
              indices.second
      );
    }

    return this;
  }

  public CodeFragmentQuery<T> remove() {
    return remove(RegexMatcher.BOUNDARY_KEY);
  }

  public boolean isPresent() {
    return matchResult != null;
  }
}
