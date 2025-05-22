package com.koyomiji.asmine.query;

import com.koyomiji.asmine.common.ArrayHelper;
import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.regex.RegexMatcher;
import com.koyomiji.asmine.regex.code.CodeMatchResult;
import com.koyomiji.asmine.stencil.ResolutionExeption;
import com.koyomiji.asmine.stencil.insn.AbstractInsnStencil;
import com.koyomiji.asmine.tuple.Pair;
import org.objectweb.asm.tree.InsnList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeFragmentQuery<T> extends AbstractQuery<T> {
  protected CodeManipulator codeManipulator;
  protected CodeMatchResult matchResult;
  protected Map<Object, List<Pair<Object, Object>>> stringBinds;
  protected List<Pair<Object, Object>> selected;

  public CodeFragmentQuery(T parent, CodeManipulator codeManipulator, CodeMatchResult matchResult) {
    super(parent);
    this.codeManipulator = codeManipulator;
    this.matchResult = matchResult;
    this.stringBinds = new HashMap<>();
    this.selected = new ArrayList<>();

    if (matchResult != null) {
      for (Map.Entry<Object, List<Pair<Integer, Integer>>> entry : matchResult.getBounds().entrySet()) {
        for (Pair<Integer, Integer> range : entry.getValue()) {
          if (!stringBinds.containsKey(entry.getKey())) {
            stringBinds.put(entry.getKey(), ArrayListHelper.of());
          }

          stringBinds.get(entry.getKey()).add(Pair.of(codeManipulator.getCursor(range.first), codeManipulator.getCursor(range.second)));
        }
      }

      this.selected = stringBinds.get(RegexMatcher.BOUNDARY_KEY);
    }
  }

  public CodeFragmentQuery(T parent, CodeManipulator codeManipulator, CodeMatchResult matchResult, Map<Object, List<Pair<Object, Object>>> stringBinds, List<Pair<Object, Object>> selected) {
    super(parent);
    this.codeManipulator = codeManipulator;
    this.matchResult = matchResult;
    this.stringBinds = stringBinds;
    this.selected = selected;
  }

  private InsnList instantiate(List<AbstractInsnStencil> insns) throws ResolutionExeption {
    InsnList insnList = new InsnList();

    for (AbstractInsnStencil insn : insns) {
      insnList.add(insn.instantiate(matchResult));
    }

    return insnList;
  }

  public CodeFragmentQuery<CodeFragmentQuery<T>> selectBound(Object key) {
    List<Pair<Object, Object>> newSelected = new ArrayList<>();

    for (Pair<Object, Object> range : stringBinds.get(key)) {
      for (Pair<Object, Object> selectedRange : selected) {
        int start = codeManipulator.getIndexForCursor(range.first);
        int end = codeManipulator.getLastIndexForCursor(range.second);
        int selectedStart = codeManipulator.getIndexForCursor(selectedRange.first);
        int selectedEnd = codeManipulator.getLastIndexForCursor(selectedRange.second);

        if (start >= selectedStart && end <= selectedEnd) {
          newSelected.add(Pair.of(range.first, range.second));
        }
      }
    }

    return new CodeFragmentQuery<>(this, codeManipulator, matchResult, stringBinds, newSelected);
  }

  public CodeFragmentQuery<T> insertBefore(AbstractInsnStencil... insns) {
    return insertBefore(ArrayListHelper.of(insns));
  }

  public CodeFragmentQuery<T> insertBefore(List<AbstractInsnStencil> insns) {
    for(Pair<Object, Object> range : selected) {
      Pair<Integer, Integer> indices = codeManipulator.getIndicesForCursors(range);

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

  public CodeFragmentQuery<T> insertAfter(AbstractInsnStencil... insns) {
    return insertAfter(ArrayListHelper.of(insns));
  }

  public CodeFragmentQuery<T> insertAfter(List<AbstractInsnStencil> insns) {
    for(Pair<Object, Object> range : selected) {
      Pair<Integer, Integer> indices = codeManipulator.getIndicesForCursors(range);

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

  public CodeFragmentQuery<T> replaceWith(AbstractInsnStencil... insns) {
    return replaceWith(ArrayHelper.toList(insns));
  }

  public CodeFragmentQuery<T> replaceWith(List<AbstractInsnStencil> insns) {
    for(Pair<Object, Object> range : selected) {
      Pair<Integer, Integer> indices = codeManipulator.getIndicesForCursors(range);

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

  public CodeFragmentQuery<T> remove() {
    for(Pair<Object, Object> range : selected) {
      Pair<Integer, Integer> indices = codeManipulator.getIndicesForCursors(range);

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

  public boolean isPresent() {
    return selected.size() > 0;
  }
}
