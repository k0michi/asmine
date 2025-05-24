package com.koyomiji.asmine.stencil;

import org.objectweb.asm.tree.AbstractInsnNode;

public interface IStencil<T> {
  boolean match(IParameterRegistry registry, T value);

  T instantiate(IParameterRegistry registry) throws ResolutionExeption;
}
