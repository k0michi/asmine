package com.koyomiji.jasmine.query;

import com.koyomiji.jasmine.common.ArrayHelper;
import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.common.InsnListHelper;
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
  protected Pair<Object, Object> range;

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

      this.range = Pair.of(methodManipulator.getIndexSymbol(matchResult.getRange().first), methodManipulator.getIndexSymbol(matchResult.getRange().second));
    }
  }

  private InsnList instantiate(List<AbstractInsnStencil> insns) throws ResolutionExeption {
    InsnList insnList = new InsnList();

    for (AbstractInsnStencil insn : insns) {
      insnList.add(insn.instantiate(matchResult));
    }

    return insnList;
  }

  public CodeFragmentQuery<T> replaceWith(AbstractInsnStencil insn) {
    return replaceWith(ArrayListHelper.of(insn));
  }

  public CodeFragmentQuery<T> replaceWith(AbstractInsnStencil... insns) {
    return replaceWith(ArrayHelper.toList(insns));
  }

  public CodeFragmentQuery<T> replaceWith(List<AbstractInsnStencil> insns) {
    try {
      replaceWith(
              instantiate(insns)
      );
    } catch (ResolutionExeption e) {
      return this;
    }

    return this;
  }

  public CodeFragmentQuery<T> replaceWith(AbstractInsnNode insn) {
    return replaceWith(InsnListHelper.of(insn));
  }

  public CodeFragmentQuery<T> replaceWith(AbstractInsnNode... insns) {
    return replaceWith(InsnListHelper.of(insns));
  }

  public CodeFragmentQuery<T> replaceWith(InsnList insns) {
    if (matchResult == null) {
      return this;
    }

    methodManipulator.replaceInsns(
            methodManipulator.getIndexForSymbol(range.first),
            methodManipulator.getIndexForSymbol(range.second),
            insns
    );

    return this;
  }

  public CodeFragmentQuery<T> remove() {
    if (matchResult == null) {
      return this;
    }

    methodManipulator.removeInsns(
            methodManipulator.getIndexForSymbol(range.first),
            methodManipulator.getIndexForSymbol(range.second)
    );

    return this;
  }

  public boolean isPresent() {
    return matchResult != null;
  }
}
