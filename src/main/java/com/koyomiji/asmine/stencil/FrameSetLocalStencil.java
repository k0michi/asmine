package com.koyomiji.asmine.stencil;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.common.FrameHelper;
import com.koyomiji.asmine.common.OpcodesHelper;
import org.objectweb.asm.tree.FrameNode;

import java.util.Collections;
import java.util.List;

public class FrameSetLocalStencil implements IStencil<FrameNode> {
  public IStencil<FrameNode> frame;
  public int begin;
  public List<Object> values;

  public FrameSetLocalStencil(IStencil<FrameNode> frame, int begin, Object value) {
    this(frame, begin, Collections.singletonList(value));
  }

  public FrameSetLocalStencil(IStencil<FrameNode> frame, int begin, List<Object> values) {
    this.frame = frame;
    this.begin = begin;
    this.values = values;
  }

  @Override
  public boolean match(IStencilRegistry registry, FrameNode value) {
    throw new UnsupportedOperationException("FrameSetLocalStencil does not support match");
  }

  @Override
  public FrameNode evaluate(IStencilRegistry registry) throws StencilEvaluationException {
    FrameNode frameEvaluated = frame.evaluate(registry);
    List<Object> local = FrameHelper.toPhysicalForm(frameEvaluated.local);
    List<Object> valuesPhysical = FrameHelper.toPhysicalForm(values);

    while (local.size() < begin) {
      local.add(OpcodesHelper.AUTO);
    }

    for (int i = 0; i < valuesPhysical.size(); i++) {
      int index = begin + i;

      if (index < local.size()) {
        local.set(index, valuesPhysical.get(i));
      } else {
        local.add(valuesPhysical.get(i));
      }
    }

    frameEvaluated.local = FrameHelper.toLogicalForm(local);
    return frameEvaluated;
  }
}
