package com.koyomiji.asmine.regex.code;

import com.koyomiji.asmine.regex.*;
import com.koyomiji.asmine.stencil.IStencil;
import com.koyomiji.asmine.stencil.insn.AbstractInsnStencil;
import com.koyomiji.asmine.tree.AbstractInsnNodeHelper;
import org.objectweb.asm.tree.*;

import java.util.List;

public class CodeRegexProcessor extends RegexProcessor {
  public CodeRegexProcessor(RegexModule module, List<?> string) {
    super(module, string);
  }

  public CodeRegexProcessor(List<AbstractRegexInsn> instructions, List<? extends AbstractInsnNode> string) {
    super(instructions, string);
  }

  @Override
  protected RegexThread newThread(int id) {
    return new CodeRegexThread(id);
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

  public boolean compareCharToStencil(RegexThread thread, Object actual, IStencil<?> expected) {
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

  public boolean compareCurrentCharToStencil(RegexThread thread, IStencil<?> expected) {
    return compareCharToStencil(thread, getCurrentChar(), expected);
  }
}
