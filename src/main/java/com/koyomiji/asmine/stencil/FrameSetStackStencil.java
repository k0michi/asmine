package com.koyomiji.asmine.stencil;

import com.koyomiji.asmine.common.FrameHelper;
import com.koyomiji.asmine.common.OpcodesHelper;
import org.objectweb.asm.tree.FrameNode;

import java.util.Collections;
import java.util.List;

public class FrameSetStackStencil implements IStencil<FrameNode> {
  public IStencil<FrameNode> frame;
  public int begin;
  public List<Object> values;

  public FrameSetStackStencil(IStencil<FrameNode> frame, int begin, Object value) {
    this(frame, begin, Collections.singletonList(value));
  }

  public FrameSetStackStencil(IStencil<FrameNode> frame, int begin, List<Object> values) {
    this.frame = frame;
    this.begin = begin;
    this.values = values;
  }

  @Override
  public boolean match(IStencilRegistry registry, FrameNode value) {
    throw new UnsupportedOperationException("FrameSetStackStencil does not support match");
  }

  @Override
  public FrameNode evaluate(IStencilRegistry registry) throws StencilEvaluationException {
    FrameNode frameEvaluated = frame.evaluate(registry);
    List<Object> stack = FrameHelper.toPhysicalForm(frameEvaluated.stack);
    List<Object> valuesPhysical = FrameHelper.toPhysicalForm(values);

    while (stack.size() < begin) {
      stack.add(OpcodesHelper.AUTO);
    }

    for (int i = 0; i < valuesPhysical.size(); i++) {
      int index = begin + i;

      if (index < stack.size()) {
        stack.set(index, valuesPhysical.get(i));
      } else {
        stack.add(valuesPhysical.get(i));
      }
    }

    frameEvaluated.stack = FrameHelper.toLogicalForm(stack);
    return frameEvaluated;
  }
}
