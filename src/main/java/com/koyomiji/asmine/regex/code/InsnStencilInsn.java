package com.koyomiji.asmine.regex.code;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.regex.AbstractRegexInsn;
import com.koyomiji.asmine.regex.RegexProcessor;
import com.koyomiji.asmine.regex.RegexThread;
import com.koyomiji.asmine.stencil.insn.AbstractInsnStencil;
import com.koyomiji.asmine.stencil.insn.FrameStencil;
import com.koyomiji.asmine.stencil.insn.LabelStencil;
import com.koyomiji.asmine.stencil.insn.LineNumberStencil;
import com.koyomiji.asmine.tree.AbstractInsnNodeHelper;
import com.koyomiji.asmine.tuple.Pair;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;

import java.util.List;

public class InsnStencilInsn extends AbstractRegexInsn {
  public AbstractInsnStencil stencil;

  public InsnStencilInsn(AbstractInsnStencil stencil) {
    this.stencil = stencil;
  }

  @Override
  public List<RegexThread> execute(RegexProcessor processor, RegexThread thread) {
    CodeRegexProcessor codeProcessor = (CodeRegexProcessor) processor;

    if (!codeProcessor.compareCurrentCharToStencil(thread, stencil)) {
      return ArrayListHelper.of();
    }

    thread.advanceProgramCounter();
    return ArrayListHelper.of(thread);
  }

  @Override
  public int getExecutionType() {
    return CONSUMING;
  }
}
