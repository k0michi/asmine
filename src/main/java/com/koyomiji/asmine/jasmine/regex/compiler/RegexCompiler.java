package com.koyomiji.asmine.regex.compiler;

import com.koyomiji.asmine.regex.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegexCompiler {
  public RegexModule compile(AbstractRegexNode node) {
    RegexCompilerContext context = new RegexCompilerContext();
    node.compile(context);
    context.emit(new ReturnInsn());

    List<RegexFunction> functions = context.getFunctions();

    for (RegexFunction function : functions) {
      function.insns = postprocess(function.insns);
    }

    return new RegexModule(functions);
  }

  private List<AbstractRegexInsn> postprocess(List<AbstractRegexInsn> insns) {
    Map<PseudoLabelInsn, Integer> labels = new HashMap<>();
    List<AbstractRegexInsn> result = new ArrayList<>();

    int offset = 0;
    for (int i = 0; i < insns.size(); i++) {
      while (i < insns.size() && insns.get(i) instanceof PseudoLabelInsn) {
        PseudoLabelInsn label = (PseudoLabelInsn) insns.get(i);
        labels.put(label, offset);
        i++;
      }

      offset++;
    }

    for (int i = 0; i < insns.size(); i++) {
      AbstractRegexInsn insn = insns.get(i);

      if (insn instanceof PseudoJumpInsn) {
        PseudoJumpInsn jumpInsn = (PseudoJumpInsn) insn;
        Integer jumpOffset = labels.get(jumpInsn.label);

        if (jumpOffset != null) {
          result.add(new JumpInsn(jumpOffset -  result.size()));
        } else {
          throw new RegexCompilerException("Label not found: " + jumpInsn.label);
        }
      } else if (insn instanceof PseudoForkInsn) {
        PseudoForkInsn forkInsn = (PseudoForkInsn) insn;
        List<Integer> offsets = new ArrayList<>();

        for (PseudoLabelInsn label : forkInsn.labels) {
          Integer jumpOffset = labels.get(label);

          if (jumpOffset != null) {
            offsets.add(jumpOffset - result.size());
          } else {
            throw new RegexCompilerException("Label not found: " + label);
          }
        }

        result.add(new ForkInsn(offsets));
      } else if (insn instanceof PseudoLabelInsn) {
      } else {
        result.add(insn);
      }
    }

    return result;
  }
}
