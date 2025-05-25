package com.koyomiji.asmine.query;

import com.koyomiji.asmine.common.ListHelper;
import com.koyomiji.asmine.common.PrinterHelper;
import com.koyomiji.asmine.tree.ExternalizedMethodNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;

import java.io.PrintWriter;

public class ClassQuery extends AbstractQuery<ClassNode> {
  protected ClassNode classNode;
  protected QueryContext context;

  private ClassQuery(ClassNode classNode) {
    super(classNode);
    this.classNode = classNode;
    this.context = new QueryContext();
  }

  public static ClassQuery of(ClassNode classNode) {
    return new ClassQuery(classNode);
  }

  public ClassNode getNode() {
    return classNode;
  }

  private ExternalizedMethodNode externalizeMethod(MethodNode methodNode) {
    ExternalizedMethodNode externalizedMethodNode = new ExternalizedMethodNode(
            methodNode.access,
            methodNode.name,
            methodNode.desc,
            methodNode.signature,
            methodNode.exceptions != null ? methodNode.exceptions.toArray(new String[0]) : null
    );
    methodNode.accept(externalizedMethodNode);
    return externalizedMethodNode;
  }

  private ExternalizedMethodNode getMethodNode(int index) {
    MethodNode methodNode = classNode.methods.get(index);

    if (!(methodNode instanceof ExternalizedMethodNode)) {
      classNode.methods.set(index, externalizeMethod(methodNode));
    }

    return (ExternalizedMethodNode) classNode.methods.get(index);
  }

  public MethodQuery<ClassQuery> selectMethod(String name, String desc) {
    int found = ListHelper.findIndex(classNode.methods, m -> m.name.equals(name) && m.desc.equals(desc));

    if (found != -1) {
      ExternalizedMethodNode methodNode = getMethodNode(found);
      return new MethodQuery<>(this, methodNode);
    }

    return new MethodQuery<>(this, null);
  }

  public FieldQuery<ClassQuery> selectField(String name, String desc) {
    FieldNode found = ListHelper.findOrNull(classNode.fields, f -> f.name.equals(name) && f.desc.equals(desc));
    return new FieldQuery<>(this, found);
  }

  public ClassQuery print(Printer printer) {
    return print(printer, new PrintWriter(System.out));
  }

  public ClassQuery print(Printer printer, PrintWriter printWriter) {
    PrinterHelper.print(printer, classNode, printWriter);
    return this;
  }
}
