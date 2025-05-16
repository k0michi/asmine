package com.koyomiji.jasmine.regex.code;

import com.koyomiji.jasmine.regex.AbstractRegexInsn;
import com.koyomiji.jasmine.regex.MatchResult;
import com.koyomiji.jasmine.regex.RegexProcessor;
import com.koyomiji.jasmine.regex.RegexThread;
import com.koyomiji.jasmine.stencil.AbstractStencil;
import com.koyomiji.jasmine.stencil.insn.AbstractInsnStencil;
import com.koyomiji.jasmine.tree.AbstractInsnNodeHelper;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;

import java.util.List;

public class CodeRegexProcessor extends RegexProcessor {
  public CodeRegexProcessor(List<AbstractRegexInsn> instructions, List<? extends AbstractInsnNode> string) {
    super(instructions, string);
  }

  @Override
  protected RegexThread newThread(int programCounter) {
    return new CodeRegexThread(programCounter);
  }

  @Override
  protected MatchResult newMatchResult(RegexThread thread) {
    CodeRegexThread codeThread = (CodeRegexThread) thread;
    return new CodeMatchResult(codeThread);
  }

  @Override
  public CodeRegexThread execute() {
    return (CodeRegexThread) super.execute();
  }

  @Override
  public CodeRegexThread execute(int begin) {
    return (CodeRegexThread) super.execute(begin);
  }

  @Override
  protected boolean isTransitiveChar(Object character) {
    return character instanceof AbstractInsnNode && AbstractInsnNodeHelper.isPseudo((AbstractInsnNode) character);
  }

  @Override
  public boolean compareCharToLiteral(Object actual, Object expected) {
    return actual instanceof AbstractInsnNode && expected instanceof AbstractInsnNode
        && AbstractInsnNodeHelper.equals((AbstractInsnNode) actual, (AbstractInsnNode) expected);
  }

  public boolean compareCharToStencil(RegexThread thread, Object actual, AbstractStencil<?> expected) {
    if (!(thread instanceof CodeRegexThread)) {
      throw new IllegalArgumentException("thread must be an instance of CodeRegexThread");
    }

    CodeRegexThread codeThread = (CodeRegexThread) thread;

    if (actual instanceof AbstractInsnNode && expected instanceof AbstractInsnStencil) {
      AbstractInsnNode actualInsn = (AbstractInsnNode) actual;
      AbstractInsnStencil expectedInsn = (AbstractInsnStencil) expected;

      return expectedInsn.match(codeThread, actualInsn);
    }

    return false;
  }

  public boolean compareCurrentCharToStencil(RegexThread thread, AbstractStencil<?> expected) {
    return compareCharToStencil(thread, getCurrentChar(), expected);
  }

  private LabelNode getLabel(List<?> list, int insn) {
    for (int i = insn - 1; i >= 0; i--) {
      if (list.get(i) instanceof LabelNode) {
        return (LabelNode) list.get(i);
      }
    }

    return null;
  }

  public LabelNode getCurrentLabel() {
    return getLabel(getString(), getStringPointer());
  }

  private LineNumberNode getLineNumber(List<?> list, int insn) {
    for (int i = insn - 1; i >= 0; i--) {
      if (list.get(i) instanceof LineNumberNode) {
        return (LineNumberNode) list.get(i);
      }
    }

    return null;
  }

  public LineNumberNode getCurrentLineNumber() {
    return getLineNumber(getString(), getStringPointer());
  }

  private FrameNode getFrame(List<?> list, int insn) {
    for (int i = insn - 1; i >= 0; i--) {
      if (list.get(i) instanceof FrameNode) {
        return (FrameNode) list.get(i);
      }
    }

    return null;
  }

  public FrameNode getCurrentFrame() {
    return getFrame(getString(), getStringPointer());
  }
}
