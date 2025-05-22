package com.koyomiji.asmine.sexpr.reader;

import org.objectweb.asm.Label;
import org.objectweb.asm.TypeReference;
import org.objectweb.asm.tree.LabelNode;

class TypeReferenceExtended extends TypeReference {
  public LabelNode offset;
  public LabelNode[] start;
  public LabelNode[] end;
  public int[] index;

  public TypeReferenceExtended(int typeRef) {
    super(typeRef);
  }

  public TypeReferenceExtended(int typeRef, LabelNode offset, LabelNode[] start, LabelNode[] end, int[] index) {
    super(typeRef);
    this.offset = offset;
    this.start = start;
    this.end = end;
    this.index = index;
  }

  public LabelNode getOffset() {
    return offset;
  }

  public LabelNode[] getStart() {
    return start;
  }

  public LabelNode[] getEnd() {
    return end;
  }
}
