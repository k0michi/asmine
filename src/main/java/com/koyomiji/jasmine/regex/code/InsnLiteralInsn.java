package com.koyomiji.jasmine.regex.code;

import com.koyomiji.jasmine.common.ArrayListHelper;
import com.koyomiji.jasmine.regex.AbstractRegexInsn;
import com.koyomiji.jasmine.regex.RegexProcessor;
import com.koyomiji.jasmine.regex.RegexThread;
import com.koyomiji.jasmine.tree.AbstractInsnNodeHelper;
import com.koyomiji.jasmine.tuple.Pair;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;

import java.util.List;

public class InsnLiteralInsn extends AbstractRegexInsn {
  public AbstractInsnNode literal;

  public InsnLiteralInsn(AbstractInsnNode literal) {
    this.literal = literal;
  }

  @Override
  public Pair<Boolean, List<RegexThread>> execute(RegexProcessor processor, RegexThread thread) {
    CodeRegexProcessor codeProcessor = (CodeRegexProcessor) processor;

    if (AbstractInsnNodeHelper.isPseudo(literal)) {
      if (literal instanceof LabelNode) {
        LabelNode label = codeProcessor.getCurrentLabel();

        if (codeProcessor.compareCharToLiteral(label, literal)) {
          thread.advanceProgramCounter();
          return Pair.of(true, ArrayListHelper.of(thread));
        } else {
          return Pair.of(false, ArrayListHelper.of());
        }
      } else if (literal instanceof LineNumberNode) {
        LineNumberNode lineNumber = codeProcessor.getCurrentLineNumber();

        if (codeProcessor.compareCharToLiteral(lineNumber, literal)) {
          thread.advanceProgramCounter();
          return Pair.of(true, ArrayListHelper.of(thread));
        } else {
          return Pair.of(false, ArrayListHelper.of());
        }
      } else if (literal instanceof FrameNode) {
        FrameNode frame = codeProcessor.getCurrentFrame();

        if (codeProcessor.compareCharToLiteral(frame, literal)) {
          thread.advanceProgramCounter();
          return Pair.of(true, ArrayListHelper.of(thread));
        } else {
          return Pair.of(false, ArrayListHelper.of());
        }
      }
    }

    if (!codeProcessor.compareCurrentCharToLiteral(literal)) {
      return Pair.of(false, ArrayListHelper.of());
    }

    thread.advanceProgramCounter();
    return Pair.of(true, ArrayListHelper.of(thread));
  }

  @Override
  public boolean isTransitive() {
    return AbstractInsnNodeHelper.isPseudo(literal);
  }
}
