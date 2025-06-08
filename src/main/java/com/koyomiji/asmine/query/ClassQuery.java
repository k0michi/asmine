package com.koyomiji.asmine.query;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.common.ListHelper;
import com.koyomiji.asmine.common.PrinterHelper;
import com.koyomiji.asmine.tree.NormalizedMethodNode;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Printer;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

  public static ClassQuery ofNew() {
    return of(new ClassNode());
  }

  public ClassNode getNode() {
    return classNode;
  }

  public int getMinorVersion() {
    return classNode.version >> 16;
  }

  public ClassQuery setMinorVersion(int minorVersion) {
    classNode.version = (minorVersion << 16) | (classNode.version & 0xFFFF);
    return this;
  }

  public int getMajorVersion() {
    return classNode.version & 0xFFFF;
  }

  public ClassQuery setMajorVersion(int majorVersion) {
    classNode.version = (classNode.version & 0xFFFF0000) | (majorVersion & 0xFFFF);
    return this;
  }

  public int getAccess() {
    return classNode.access;
  }

  public ClassQuery setAccess(int access) {
    classNode.access = access;
    return this;
  }

  public ClassQuery addAccess(int access) {
    classNode.access |= access;
    return this;
  }

  public ClassQuery removeAccess(int access) {
    classNode.access &= ~access;
    return this;
  }

  public String getName() {
    return classNode.name;
  }

  public ClassQuery setName(String name) {
    classNode.name = name;
    return this;
  }

  public String getSignature() {
    return classNode.signature;
  }

  public ClassQuery setSignature(String signature) {
    classNode.signature = signature;
    return this;
  }

  public String getSuperName() {
    return classNode.superName;
  }

  public ClassQuery setSuperName(String superName) {
    classNode.superName = superName;
    return this;
  }

  public List<String> getInterfaces() {
    return classNode.interfaces;
  }

  public ClassQuery setInterfaces(List<String> interfaces) {
    classNode.interfaces = interfaces;
    return this;
  }

  public ClassQuery setInterfaces(String... interfaces) {
    return setInterfaces(ArrayListHelper.of(interfaces));
  }

  public ClassQuery addInterface(String interface_) {
    return addInterfaces(Collections.singletonList(interface_));
  }

  public ClassQuery addInterfaces(List<String> interfaces) {
    if (classNode.interfaces == null) {
      classNode.interfaces = new ArrayList<>();
    }

    classNode.interfaces.addAll(interfaces);
    return this;
  }

  public ClassQuery addInterfaces(String... interfaces) {
    return addInterfaces(Arrays.asList(interfaces));
  }

  public ClassQuery removeInterface(String interface_) {
    return removeInterfaces(Collections.singletonList(interface_));
  }

  public ClassQuery removeInterfaces(List<String> interfaces) {
    if (classNode.interfaces == null) {
      return this;
    }

    classNode.interfaces.removeAll(interfaces);
    return this;
  }

  public ClassQuery removeInterfaces(String... interfaces) {
    return removeInterfaces(Arrays.asList(interfaces));
  }

  public String getSourceFile() {
    return classNode.sourceFile;
  }

  public ClassQuery setSourceFile(String sourceFile) {
    classNode.sourceFile = sourceFile;
    return this;
  }

  public String getSourceDebug() {
    return classNode.sourceDebug;
  }

  public ClassQuery setSourceDebug(String sourceDebug) {
    classNode.sourceDebug = sourceDebug;
    return this;
  }

//  public ClassQuery setModule(String moduleName, int access, String version) {
//    classNode.module = new ModuleNode(moduleName, access, version);
//    return this;
//  }
//
//  public ClassQuery removeModule() {
//    classNode.module = null;
//    return this;
//  }

  // TODO: selectModule

  public String getOuterClass() {
    return classNode.outerClass;
  }

  public ClassQuery setOuterClass(String outerClass) {
    classNode.outerClass = outerClass;
    return this;
  }

  public String getOuterMethod() {
    return classNode.outerMethod;
  }

  public ClassQuery setOuterMethod(String outerMethod) {
    classNode.outerMethod = outerMethod;
    return this;
  }

  public String getOuterMethodDesc() {
    return classNode.outerMethodDesc;
  }

  public ClassQuery setOuterMethodDesc(String outerMethodDesc) {
    classNode.outerMethodDesc = outerMethodDesc;
    return this;
  }

  public ClassQuery addVisibleAnnotation(String descriptor) {
    if (classNode.visibleAnnotations == null) {
      classNode.visibleAnnotations = new ArrayList<>();
    }

    classNode.visibleAnnotations.add(new AnnotationNode(descriptor));
    return this;
  }

  // TODO: selectVisibleAnnotation

  public ClassQuery addInvisibleAnnotation(String descriptor) {
    if (classNode.invisibleAnnotations == null) {
      classNode.invisibleAnnotations = new ArrayList<>();
    }

    classNode.invisibleAnnotations.add(new AnnotationNode(descriptor));
    return this;
  }

  // TODO: selectInvisibleAnnotation

  public ClassQuery addVisibleTypeAnnotation(int typeRef, TypePath typePath, String descriptor) {
    if (classNode.visibleTypeAnnotations == null) {
      classNode.visibleTypeAnnotations = new ArrayList<>();
    }

    classNode.visibleTypeAnnotations.add(new TypeAnnotationNode(typeRef, typePath, descriptor));
    return this;
  }

  // TODO: selectVisibleTypeAnnotation

  public ClassQuery addInvisibleTypeAnnotation(int typeRef, TypePath typePath, String descriptor) {
    if (classNode.invisibleTypeAnnotations == null) {
      classNode.invisibleTypeAnnotations = new ArrayList<>();
    }

    classNode.invisibleTypeAnnotations.add(new TypeAnnotationNode(typeRef, typePath, descriptor));
    return this;
  }

  public ClassQuery addAttribute(Attribute attribute) {
    if (classNode.attrs == null) {
      classNode.attrs = new ArrayList<>();
    }

    classNode.attrs.add(attribute);
    return this;
  }

  // TODO: selectAttribute

  public ClassQuery addInnerClass(String name, String outerName, String innerName, int access) {
    if (classNode.innerClasses == null) {
      classNode.innerClasses = new ArrayList<>();
    }

    classNode.innerClasses.add(new InnerClassNode(name, outerName, innerName, access));
    return this;
  }

  // TODO: selectInnerClass

//  public String getNestHostClass() {
//    return classNode.nestHostClass;
//  }
//
//  public ClassQuery setNestHostClass(String nestHostClass) {
//    classNode.nestHostClass = nestHostClass;
//    return this;
//  }
//
//  public List<String> getNestMembers() {
//    return classNode.nestMembers;
//  }
//
//  public ClassQuery setNestMembers(List<String> nestMembers) {
//    classNode.nestMembers = nestMembers;
//    return this;
//  }
//
//  public ClassQuery setNestMembers(String... nestMembers) {
//    return setNestMembers(ArrayListHelper.of(nestMembers));
//  }
//
//  public ClassQuery addNestMember(String nestMember) {
//    return addNestMembers(Collections.singletonList(nestMember));
//  }
//
//  public ClassQuery addNestMembers(List<String> nestMembers) {
//    if (classNode.nestMembers == null) {
//      classNode.nestMembers = new ArrayList<>();
//    }
//
//    classNode.nestMembers.addAll(nestMembers);
//    return this;
//  }
//
//  public ClassQuery addNestMembers(String... nestMembers) {
//    return addNestMembers(Arrays.asList(nestMembers));
//  }
//
//  public ClassQuery removeNestMember(String nestMember) {
//    return removeInterfaces(Collections.singletonList(nestMember));
//  }
//
//  public ClassQuery removeNestMembers(List<String> nestMembers) {
//    if (classNode.nestMembers != null) {
//      classNode.nestMembers.removeAll(nestMembers);
//    }
//
//    return this;
//  }
//
//  public ClassQuery removeNestMembers(String... nestMembers) {
//    return removeNestMembers(Arrays.asList(nestMembers));
//  }
//
//  public List<String> getPermittedSubclasses() {
//    return classNode.permittedSubclasses;
//  }
//
//  public ClassQuery setPermittedSubclasses(List<String> permittedSubclasses) {
//    classNode.permittedSubclasses = permittedSubclasses;
//    return this;
//  }
//
//  public ClassQuery setPermittedSubclasses(String... permittedSubclasses) {
//    return setPermittedSubclasses(ArrayListHelper.of(permittedSubclasses));
//  }
//
//  public ClassQuery addPermittedSubclass(String permittedSubclass) {
//    return addPermittedSubclasses(Collections.singletonList(permittedSubclass));
//  }
//
//  public ClassQuery addPermittedSubclasses(List<String> permittedSubclasses) {
//    if (classNode.permittedSubclasses == null) {
//      classNode.permittedSubclasses = new ArrayList<>();
//    }
//
//    classNode.permittedSubclasses.addAll(permittedSubclasses);
//    return this;
//  }
//
//  public ClassQuery addPermittedSubclasses(String... permittedSubclasses) {
//    return addPermittedSubclasses(Arrays.asList(permittedSubclasses));
//  }
//
//  public ClassQuery removePermittedSubclass(String permittedSubclass) {
//    return removePermittedSubclasses(Collections.singletonList(permittedSubclass));
//  }
//
//  public ClassQuery removePermittedSubclasses(List<String> permittedSubclasses) {
//    if (classNode.permittedSubclasses != null) {
//      classNode.permittedSubclasses.removeAll(permittedSubclasses);
//    }
//
//    return this;
//  }
//
//  public ClassQuery removePermittedSubclasses(String... permittedSubclasses) {
//    return removePermittedSubclasses(Arrays.asList(permittedSubclasses));
//  }
//
//  public ClassQuery addRecordComponent(String name, String desc, String signature) {
//    if (classNode.recordComponents == null) {
//      classNode.recordComponents = new ArrayList<>();
//    }
//
//    classNode.recordComponents.add(new RecordComponentNode(name, desc, signature));
//    return this;
//  }

  // TODO: selectRecordComponent

  public ClassQuery addMethod(int access, String name, String desc, String signature, String[] exceptions) {
    MethodNode methodNode = new MethodNode(access, name, desc, signature, exceptions);
    classNode.methods.add(methodNode);
    return this;
  }

  public MethodQuery<ClassQuery> selectMethod(String name, String desc) {
    int found = ListHelper.findIndex(classNode.methods, m -> m.name.equals(name) && m.desc.equals(desc));

    if (found != -1) {
      NormalizedMethodNode methodNode = getMethodNode(found);
      return new MethodQuery<>(this, methodNode);
    }

    return new MethodQuery<>(this, null);
  }

  public ClassQuery addField(int access, String name, String desc, String signature, Object value) {
    FieldNode fieldNode = new FieldNode(access, name, desc, signature, value);
    classNode.fields.add(fieldNode);
    return this;
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

  /*
   * Internal
   */

  private NormalizedMethodNode normalizeMethod(MethodNode methodNode) {
    NormalizedMethodNode normalized = new NormalizedMethodNode(
            classNode.name,
            methodNode.access,
            methodNode.name,
            methodNode.desc,
            methodNode.signature,
            methodNode.exceptions != null ? methodNode.exceptions.toArray(new String[0]) : null
    );
    methodNode.accept(normalized);
    return normalized;
  }

  private NormalizedMethodNode getMethodNode(int index) {
    MethodNode methodNode = classNode.methods.get(index);

    if (!(methodNode instanceof NormalizedMethodNode)) {
      classNode.methods.set(index, normalizeMethod(methodNode));
    }

    return (NormalizedMethodNode) classNode.methods.get(index);
  }
}
