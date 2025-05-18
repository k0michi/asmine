package com.koyomiji.jasmine.regex.compiler;

import com.koyomiji.jasmine.regex.AbstractRegexInsn;
import com.koyomiji.jasmine.regex.ForkInsn;
import com.koyomiji.jasmine.regex.JumpInsn;
import com.koyomiji.jasmine.regex.TerminalInsn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegexCompiler {
  public static final Object BOUNDARY_KEY = new Object();

  public List<AbstractRegexInsn> compile(AbstractRegexNode node) {
    node = new ConcatenateNode(
            new StarNode(new AnyNode(), QuantifierType.LAZY),
            new BindNode(BOUNDARY_KEY, node)
    );

    List<AbstractRegexInsn> compiled = node.compile(new RegexCompilerContext());
    compiled.add(new TerminalInsn());
    compiled = postprocess(compiled);
    return compiled;
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
