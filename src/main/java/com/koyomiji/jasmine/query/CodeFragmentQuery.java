package com.koyomiji.jasmine.query;

import com.koyomiji.jasmine.common.ArrayHelper;
import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.regex.RegexMatcher;
import com.koyomiji.jasmine.regex.code.CodeMatchResult;
import com.koyomiji.jasmine.stencil.ResolutionExeption;
import com.koyomiji.jasmine.stencil.insn.AbstractInsnStencil;
import com.koyomiji.jasmine.tuple.Pair;
import org.objectweb.asm.tree.InsnList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeFragmentQuery<T> extends AbstractQuery<T> {
  protected CodeManipulator codeManipulator;
  protected CodeMatchResult matchResult;
  protected Map<Object, List<Pair<Object, Object>>> stringBinds;

  public CodeFragmentQuery(T parent, CodeManipulator codeManipulator, CodeMatchResult matchResult) {
    super(parent);
    this.codeManipulator = codeManipulator;
    this.matchResult = matchResult;

    this.stringBinds = new HashMap<>();

    if (matchResult != null) {
      for (Map.Entry<Object, List<Pair<Integer, Integer>>> entry : matchResult.getBounds().entrySet()) {
        for (Pair<Integer, Integer> range : entry.getValue()) {
          if (!stringBinds.containsKey(entry.getKey())) {
            stringBinds.put(entry.getKey(), ArrayListHelper.of());
          }

          stringBinds.get(entry.getKey()).add(Pair.of(codeManipulator.getIndexSymbol(range.first), codeManipulator.getIndexSymbol(range.second)));
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

  public CodeFragmentQuery<T> insertBeforeBound(Object key, AbstractInsnStencil... insns) {
    return insertBeforeBound(key, ArrayHelper.toList(insns));
  }

  public CodeFragmentQuery<T> insertBeforeBound(Object key, List<AbstractInsnStencil> insns) {
    if (!stringBinds.containsKey(key)) {
      return this;
    }

    for(Pair<Object, Object> range : stringBinds.get(key)) {
      Pair<Integer, Integer> indices = codeManipulator.getIndicesForSymbols(range);

      if (indices == null) {
        continue;
      }

      try {
        codeManipulator.insertBefore(
                indices.first,
                instantiate(insns)
        );
      } catch (ResolutionExeption e) {
        throw new RuntimeException(e);
      }
    }

    return this;
  }

  public CodeFragmentQuery<T> insertBefore(AbstractInsnStencil... insns) {
    return insertBeforeBound(RegexMatcher.BOUNDARY_KEY, insns);
  }

  public CodeFragmentQuery<T> insertBefore(List<AbstractInsnStencil> insns) {
    return insertBeforeBound(RegexMatcher.BOUNDARY_KEY, insns);
  }

  public CodeFragmentQuery<T> insertAfterBound(Object key, AbstractInsnStencil... insns) {
    return insertAfterBound(key, ArrayHelper.toList(insns));
  }

  public CodeFragmentQuery<T> insertAfterBound(Object key, List<AbstractInsnStencil> insns) {
    if (!stringBinds.containsKey(key)) {
      return this;
    }

    for(Pair<Object, Object> range : stringBinds.get(key)) {
      Pair<Integer, Integer> indices = codeManipulator.getIndicesForSymbols(range);

      if (indices == null) {
        continue;
      }

      try {
        codeManipulator.insertAfter(
                indices.second - 1,
                instantiate(insns)
        );
      } catch (ResolutionExeption e) {
        throw new RuntimeException(e);
      }
    }

    return this;
  }

  public CodeFragmentQuery<T> insertAfter(AbstractInsnStencil... insns) {
    return insertAfterBound(RegexMatcher.BOUNDARY_KEY, insns);
  }

  public CodeFragmentQuery<T> insertAfter(List<AbstractInsnStencil> insns) {
    return insertAfterBound(RegexMatcher.BOUNDARY_KEY, insns);
  }

  public CodeFragmentQuery<T> addFirst(AbstractInsnStencil... insns) {
    return addFirst(ArrayHelper.toList(insns));
  }

  public CodeFragmentQuery<T> addFirst(List<AbstractInsnStencil> insns) {
    try {
      codeManipulator.addFirst(instantiate(insns));
    } catch (ResolutionExeption e) {
      throw new RuntimeException(e);
    }

    return this;
  }

  public CodeFragmentQuery<T> addLast(AbstractInsnStencil... insns) {
    return addLast(ArrayHelper.toList(insns));
  }

  public CodeFragmentQuery<T> addLast(List<AbstractInsnStencil> insns) {
    try {
      codeManipulator.insertBefore(codeManipulator.getMethodNode().instructions.size(), instantiate(insns));
    } catch (ResolutionExeption e) {
      throw new RuntimeException(e);
    }

    return this;
  }

  public CodeFragmentQuery<T> replaceBoundWith(Object key, AbstractInsnStencil insn) {
    return replaceBoundWith(key, ArrayListHelper.of(insn));
  }

  public CodeFragmentQuery<T> replaceBoundWith(Object key, AbstractInsnStencil... insns) {
    return replaceBoundWith(key, ArrayHelper.toList(insns));
  }

  public CodeFragmentQuery<T> replaceBoundWith(Object key, List<AbstractInsnStencil> insns) {
    if (!stringBinds.containsKey(key)) {
      return this;
    }

    for(Pair<Object, Object> range : stringBinds.get(key)) {
      Pair<Integer, Integer> indices = codeManipulator.getIndicesForSymbols(range);

      if (indices == null) {
        continue;
      }

      try {
        codeManipulator.replace(
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

  public CodeFragmentQuery<T> replaceWith(AbstractInsnStencil insn) {
    return replaceBoundWith(RegexMatcher.BOUNDARY_KEY, insn);
  }

  public CodeFragmentQuery<T> replaceWith(AbstractInsnStencil... insns) {
    return replaceBoundWith(RegexMatcher.BOUNDARY_KEY, insns);
  }

  public CodeFragmentQuery<T> replaceWith(List<AbstractInsnStencil> insns) {
    return replaceBoundWith(RegexMatcher.BOUNDARY_KEY, insns);
  }

  public CodeFragmentQuery<T> remove(Object key) {
    if (!stringBinds.containsKey(key)) {
      return this;
    }

    for(Pair<Object, Object> range : stringBinds.get(key)) {
      Pair<Integer, Integer> indices = codeManipulator.getIndicesForSymbols(range);

      if (indices == null) {
        continue;
      }

      codeManipulator.remove(
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
