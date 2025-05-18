package com.koyomiji.jasmine.regex.code;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.regex.AbstractRegexInsn;
import com.koyomiji.jasmine.regex.RegexProcessor;
import com.koyomiji.jasmine.regex.RegexThread;
import com.koyomiji.jasmine.stencil.insn.AbstractInsnStencil;
import com.koyomiji.jasmine.stencil.insn.FrameStencil;
import com.koyomiji.jasmine.stencil.insn.LabelStencil;
import com.koyomiji.jasmine.stencil.insn.LineNumberStencil;
import com.koyomiji.jasmine.tuple.Pair;
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

    if (stencil.isPseudo()) {
      if (stencil instanceof LabelStencil) {
        LabelNode label = codeProcessor.getCurrentLabel();

        if (codeProcessor.compareCharToStencil(thread, label, stencil)) {
          thread.advanceProgramCounter();
          return ArrayListHelper.of(thread);
        } else {
          return ArrayListHelper.of();
        }
      } else if (stencil instanceof LineNumberStencil) {
        LineNumberNode lineNumber = codeProcessor.getCurrentLineNumber();

        if (codeProcessor.compareCharToStencil(thread, lineNumber, stencil)) {
          thread.advanceProgramCounter();
          return ArrayListHelper.of(thread);
        } else {
          return ArrayListHelper.of();
        }
      } else if (stencil instanceof FrameStencil) {
        FrameNode frame = codeProcessor.getCurrentFrame();

        if (codeProcessor.compareCharToStencil(thread, frame, stencil)) {
          thread.advanceProgramCounter();
          return ArrayListHelper.of(thread);
        } else {
          return ArrayListHelper.of();
        }
      }
    }

    if (!codeProcessor.compareCurrentCharToStencil(thread, stencil)) {
      return ArrayListHelper.of();
    }

    thread.advanceProgramCounter();
    return ArrayListHelper.of(thread);
  }

  @Override
  public boolean isTransitive() {
    return stencil.isPseudo();
  }
}
