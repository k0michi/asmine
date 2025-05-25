package com.koyomiji.asmine.sexpr.reader;

import com.koyomiji.asmine.common.AccessFlagTarget;
import com.koyomiji.asmine.common.DoubleHelper;
import com.koyomiji.asmine.common.LongHelper;
import com.koyomiji.asmine.common.ModifiedUTF8;
import com.koyomiji.asmine.sexpr.*;
import com.koyomiji.asmine.sexpr.map.AccessFlagMap;
import com.koyomiji.asmine.sexpr.map.ReferenceKindMap;
import com.koyomiji.asmine.sexpr.tree.*;
import com.koyomiji.asmine.tuple.Pair;
import com.koyomiji.asmine.tuple.Quartet;
import com.koyomiji.asmine.tuple.Triplet;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.LabelNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SExprClassTraverser {
  private final SExprListNode listNode;
  private int index = 0;

  public SExprClassTraverser(SExprListNode node) {
    this.listNode = node;
  }

  public void skip() {
    index++;
  }

  public void skipSymbol(String symbol) {
    if (compareSymbol(listNode.children.get(index), symbol)) {
      index++;
    } else {
      throw new IllegalArgumentException("Invalid S-Expression node");
    }
  }

  public boolean isNextList() {
    return hasNext() && listNode.children.get(index) instanceof SExprListNode;
  }

  public boolean isNextList(String symbol) {
    return hasNext() && listNode.children.get(index) instanceof SExprListNode &&
            compareSymbol(((SExprListNode) listNode.children.get(index)).children.get(0), symbol);
  }

  public boolean isNextString() {
    return hasNext() && listNode.children.get(index) instanceof SExprStringNode;
  }

  public boolean isNextDataString() {
    return hasNext() && listNode.children.get(index) instanceof SExprDataStringNode;
  }

  public boolean isNextSymbol() {
    return hasNext() && listNode.children.get(index) instanceof SExprSymbolNode;
  }

  public boolean isNextInteger() {
    return hasNext() && listNode.children.get(index) instanceof SExprIntegerNode;
  }

  public boolean isNextFloatingPoint() {
    return hasNext() && listNode.children.get(index) instanceof SExprFloatingPointNode;
  }

  public String nextString() {
    return nextString(null);
  }

  public String nextString(String name) {
    if (isNextString()) {
      return ((SExprStringNode) nextNode()).value;
    } else if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      String value = next.nextString(null);
      next.expectEnd();
      return value;
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public String nextStringOptional(String name) {
    if (isNextString()) {
      return ((SExprStringNode) nextNode()).value;
    } else if (name != null && isNextList(name)) {
      // Throws if name is matched and the structure is wrong
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      String value = next.nextString(null);
      next.expectEnd();
      return value;
    }

    return null;
  }

  public byte[] nextDataString(String name) {
    if (isNextDataString()) {
      return ((SExprDataStringNode) nextNode()).value;
    } else if (isNextString()) {
      return ModifiedUTF8.encode(nextString(null));
    } else if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      byte[] value = next.nextDataString(null);
      next.expectEnd();
      return value;
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public byte[] nextDataStringOptional(String name) {
    if (isNextDataString()) {
      return ((SExprDataStringNode) nextNode()).value;
    } else if (isNextString()) {
      return ModifiedUTF8.encode(nextString(null));
    } else if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      byte[] value = next.nextDataString(null);
      next.expectEnd();
      return value;
    }

    return null;
  }

  public String nextSymbol() {
    if (isNextSymbol()) {
      return ((SExprSymbolNode) nextNode()).value;
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public long nextInteger(String name) {
    if (isNextInteger()) {
      return ((SExprIntegerNode) nextNode()).value;
    } else if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      long value = next.nextInteger(null);
      next.expectEnd();
      return value;
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public double nextFloatingPoint(String name) {
    if (isNextFloatingPoint()) {
      return ((SExprFloatingPointNode) nextNode()).value;
    } else if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      double value = next.nextFloatingPoint(null);
      next.expectEnd();
      return value;
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public SExprClassTraverser peekListTraverser() {
    return new SExprClassTraverser(peekListNode());
  }

  public SExprClassTraverser nextListTraverser() {
    return new SExprClassTraverser(nextListNode());
  }

  public AbstractSExprNode peekNode() {
    return listNode.children.get(index);
  }

  public AbstractSExprNode nextNode() {
    return listNode.children.get(index++);
  }

  public void next() {
    index++;
  }

  public SExprListNode peekListNode() {
    AbstractSExprNode next = peekNode();

    if (next instanceof SExprListNode) {
      return (SExprListNode) next;
    } else {
      throw new IllegalArgumentException("Invalid S-Expression node");
    }
  }

  public SExprListNode nextListNode() {
    AbstractSExprNode next = nextNode();

    if (next instanceof SExprListNode) {
      return (SExprListNode) next;
    } else {
      throw new IllegalArgumentException("Invalid S-Expression node");
    }
  }

  public boolean hasNext() {
    return index < listNode.children.size();
  }

  public void expectEnd() {
    if (hasNext()) {
      throw new SyntaxException("Expected end of list, but found: " + listNode.children.get(index));
    }
  }

  /*
   * JVM Class
   */

  public int nextFlags(String name, AccessFlagTarget target) {
    int result = 0;

    while (isNextList(name)) {
      SExprClassTraverser child = peekListTraverser();
      child.skip();

      while (child.hasNext()) {
        if (child.isNextSymbol()) {
          Optional<Integer> value = AccessFlagMap.toInt(child.nextSymbol(), target);

          if (value.isPresent()) {
            result |= value.get();
          } else {
            throw new IllegalArgumentException("Invalid S-Expression node");
          }
        } else {
          throw new IllegalArgumentException("Invalid S-Expression node");
        }
      }

      child.expectEnd();
      next();
    }

    return result;
  }

  public int nextReferenceKind(String name) {
    if (isNextSymbol()) {
      return ReferenceKindMap.toInt(nextSymbol());
    } else if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      int value = next.nextReferenceKind(null);
      next.expectEnd();
      return value;
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public String nextLabelSymbol(String name) {
    return nextSymbol();
  }

  public LabelNode nextLabel(String name, LabelMap labelResolver) {
    if (isNextInteger()) {
      return labelResolver.getOffsetLabel(nextInteger(null));
    } else if (isNextSymbol()) {
      return labelResolver.getSymbolLabel(nextLabelSymbol(null));
    } else if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      LabelNode value = next.nextLabel(null, labelResolver);
      next.expectEnd();
      return value;
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public Object nextLoadableConstant(String name) {
    if (name != null) {
      if (isNextList(name)) {
        SExprClassTraverser child = nextListTraverser();
        Object value = child.nextLoadableConstant(null);
        child.expectEnd();
        return value;
      }
    } else {
      if (isNextList("integer")) {
        return nextConstantInteger(null);
      } else if (isNextList("float")) {
        return nextConstantFloat(null);
      } else if (isNextList("long")) {
        return nextConstantLong(null);
      } else if (isNextList("double")) {
        return nextConstantDouble(null);
      } else if (isNextList("class")) {
        return Type.getObjectType(nextConstantClass(null));
      } else if (isNextList("string")) {
        return nextConstantString(null);
      } else if (isNextList("method_handle")) {
        return nextConstantMethodHandle(null);
      } else if (isNextList("method_type")) {
        return nextConstantMethodType(null);
//      } else if (isNextList("dynamic")) {
//        return nextConstantDynamic(null);
      }
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public int nextConstantInteger(String name) {
    if (isNextInteger()) {
      Optional<Integer> value = LongHelper.toInt(nextInteger(null));

      if (value.isPresent()) {
        return value.get();
      }
    } else if (name == null && isNextList("integer")) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      Optional<Integer> value = LongHelper.toInt(next.nextInteger(null));
      next.expectEnd();

      if (value.isPresent()) {
        return value.get();
      }
    } else if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      int value = next.nextConstantInteger(null);
      next.expectEnd();
      return value;
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public float nextConstantFloat(String name) {
    if (isNextFloatingPoint()) {
      Optional<Float> value = DoubleHelper.toFloat(nextFloatingPoint(null));

      if (value.isPresent()) {
        return value.get();
      }

      throw new IllegalArgumentException("Invalid S-Expression node");
    } else if (name == null && isNextList("float")) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      Optional<Float> value = DoubleHelper.toFloat(next.nextFloatingPoint(null));
      next.expectEnd();

      if (value.isPresent()) {
        return value.get();
      }
    } else if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      float value = next.nextConstantFloat(null);
      next.expectEnd();
      return value;
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public long nextConstantLong(String name) {
    if (isNextInteger()) {
      return nextInteger(null);
    } else if (name == null && isNextList("long")) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      long value = next.nextInteger(null);
      next.expectEnd();
      return value;
    } else if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      long value = next.nextConstantLong(null);
      next.expectEnd();
      return value;
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public double nextConstantDouble(String name) {
    if (isNextFloatingPoint()) {
      return nextFloatingPoint(null);
    } else if (name == null && isNextList("double")) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      double value = next.nextFloatingPoint(null);
      next.expectEnd();
      return value;
    } else if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      double value = next.nextConstantDouble(null);
      next.expectEnd();
      return value;
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public String nextConstantClass() {
    return nextConstantClass(null);
  }

  public String nextConstantClass(String name) {
    if (isNextString()) {
      return nextString(null);
    } else if (name == null && isNextList("class")) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      String value = next.nextString();
      next.expectEnd();
      return value;
    } else if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      String value = next.nextConstantClass(null);
      next.expectEnd();
      return value;
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public String nextConstantClassOptional(String name) {
    if (isNextString()) {
      return nextString(null);
    } else if (name == null && isNextList("class")) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      String value = next.nextString();
      next.expectEnd();
      return value;
    } else if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      String value = next.nextConstantClass(null);
      next.expectEnd();
      return value;
    }

    return null;
  }

  public List<String> nextConstantClasses(String name) {
    List<String> result = new ArrayList<>();

    while (isNextList(name)) {
      SExprClassTraverser child = peekListTraverser();
      child.skip();

      while (child.hasNext()) {
        result.add(child.nextConstantClass(null));
      }

      next();
    }

    return result;
  }

  public String nextConstantString(String name) {
    if (isNextString()) {
      return nextString(null);
    } else if (name == null && isNextList("string")) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      String value = next.nextString();
      next.expectEnd();
      return value;
    } else if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      String value = next.nextConstantString(null);
      next.expectEnd();
      return value;
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public Triplet<String, String, String> nextConstantFieldref(String name) {
    if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();

      if (next.isNextList("fieldref")) {
        Triplet<String, String, String> value = next.nextConstantFieldref(null);
        next.expectEnd();
        return value;
      }
    } else if (name == null && isNextList("fieldref")) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      String clazz = next.nextConstantClass("class");
      Pair<String, String> nameAndType = next.nextConstantNameAndType("name_and_type");
      next.expectEnd();
      return Triplet.of(clazz, nameAndType.first, nameAndType.second);
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public Quartet<String, String, String, Boolean> nextConstantMethodref(String name) {
    if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();

      if (next.isNextList("methodref") || next.isNextList("interface_methodref")) {
        Quartet<String, String, String, Boolean> value = next.nextConstantMethodref(null);
        next.expectEnd();
        return value;
      }
    } else if (name == null && (isNextList("methodref") || isNextList("interface_methodref"))) {
      boolean isInterface = isNextList("interface_methodref");

      SExprClassTraverser next = nextListTraverser();
      next.skip();
      String clazz = next.nextConstantClass("class");
      Pair<String, String> nameAndType = next.nextConstantNameAndType("name_and_type");
      next.expectEnd();
      return Quartet.of(clazz, nameAndType.first, nameAndType.second, isInterface);
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public Pair<String, String> nextConstantNameAndType(String name) {
    if (name == null && isNextList("name_and_type") || name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();

      if (name != null && next.isNextList("name_and_type")) {
        return next.nextConstantNameAndType(null);
      }

      String nameString = next.nextString("name");
      String descriptor = next.nextString("descriptor");
      next.expectEnd();
      return Pair.of(nameString, descriptor);
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public Pair<String, String> nextConstantNameAndTypeOptional(String name) {
    if (name == null && isNextList("name_and_type") || name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();

      if (name != null && next.isNextList("name_and_type")) {
        return next.nextConstantNameAndType(null);
      }

      String nameString = next.nextString("name");
      String descriptor = next.nextString("descriptor");
      next.expectEnd();
      return Pair.of(nameString, descriptor);
    }

    return null;
  }

  public Handle nextConstantMethodHandle(String name) {
    if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();

      if (next.isNextList("method_handle")) {
        Handle value = next.nextConstantMethodHandle(null);
        next.expectEnd();
        return value;
      }
    } else if (name == null && isNextList("method_handle")) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();

      int referenceKind = next.nextReferenceKind("reference_kind");
      Handle handle = null;

      switch (referenceKind) {
        case Opcodes.H_GETFIELD:
        case Opcodes.H_GETSTATIC:
        case Opcodes.H_PUTFIELD:
        case Opcodes.H_PUTSTATIC:
          Triplet<String, String, String> fieldref = nextConstantFieldref("reference");
          handle = new Handle(referenceKind, fieldref.first, fieldref.second, fieldref.third);
          break;
        case Opcodes.H_INVOKEVIRTUAL:
        case Opcodes.H_INVOKESTATIC:
        case Opcodes.H_INVOKESPECIAL:
        case Opcodes.H_NEWINVOKESPECIAL:
        case Opcodes.H_INVOKEINTERFACE:
          Quartet<String, String, String, Boolean> methodref = nextConstantMethodref("reference");
          handle = new Handle(referenceKind, methodref.first, methodref.second, methodref.third);
          break;
      }

      next.expectEnd();
      return handle;
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public Type nextConstantMethodType(String name) {
    if (isNextString()) {
      return Type.getMethodType(nextString(null));
    } else if (name == null && isNextList("method_type")) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      Type value = Type.getMethodType(next.nextString());
      next.expectEnd();
      return value;
    } else if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      Type value = next.nextConstantMethodType(null);
      next.expectEnd();
      return value;
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

//  public ConstantDynamic nextConstantDynamic(String name) {
//    if (name != null && isNextList(name)) {
//      SExprClassTraverser next = nextListTraverser();
//      next.skip();
//      ConstantDynamic value = next.nextConstantDynamic(null);
//      next.expectEnd();
//      return value;
//    } else if (name == null && isNextList("dynamic")) {
//      SExprClassTraverser next = nextListTraverser();
//      next.skip();
//
//      Pair<Handle, Object[]> bootstrapMethod = next.nextBootstrapMethod("bootstrap_method");
//      Pair<String, String> nameAndType = next.nextConstantNameAndType("name_and_type");
//
//      next.expectEnd();
//      return new ConstantDynamic(nameAndType.first, nameAndType.second, bootstrapMethod.first, bootstrapMethod.second);
//    }
//
//    throw new IllegalArgumentException("Invalid S-Expression node");
//  }

  public Quartet<Handle, Object[], String, String> nextConstantInvokeDynamic(String name) {
    if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      Quartet<Handle, Object[], String, String> value = next.nextConstantInvokeDynamic(null);
      next.expectEnd();
      return value;
    } else if (name == null && isNextList("invoke_dynamic")) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();

      Pair<Handle, Object[]> bootstrapMethod = next.nextBootstrapMethod("bootstrap_method");
      Pair<String, String> nameAndType = next.nextConstantNameAndType("name_and_type");

      next.expectEnd();
      return Quartet.of(bootstrapMethod.first, bootstrapMethod.second, nameAndType.first, nameAndType.second);
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public Pair<Handle, Object[]> nextBootstrapMethod(String name) {
    if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();

      Handle methodRef = nextConstantMethodHandle("method_ref");

      List<Object> arguments = new ArrayList<>();

      while (next.hasNext()) {
        Object argument = next.nextLoadableConstant(null);
        arguments.add(argument);
      }

      next.expectEnd();
      return Pair.of(methodRef, arguments.toArray(new Object[0]));
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public String nextConstantModule(String name) {
    if (isNextString()) {
      return nextString(null);
    } else if (name == null && isNextList("module")) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      String value = next.nextString();
      next.expectEnd();
      return value;
    } else if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      String value = next.nextConstantModule(null);
      next.expectEnd();
      return value;
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  public List<String> nextConstantModules(String name) {
    List<String> result = new ArrayList<>();

    while (isNextList(name)) {
      SExprClassTraverser child = peekListTraverser();
      child.skip();

      while (child.hasNext()) {
        result.add(child.nextConstantModule(null));
      }

      next();
    }

    return result;
  }

  public String nextConstantPackage() {
    return nextConstantPackage(null);
  }

  public String nextConstantPackage(String name) {
    if (isNextString()) {
      return nextString(null);
    } else if (name == null && isNextList("package")) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      String value = next.nextString();
      next.expectEnd();
      return value;
    } else if (name != null && isNextList(name)) {
      SExprClassTraverser next = nextListTraverser();
      next.skip();
      String value = next.nextConstantPackage(null);
      next.expectEnd();
      return value;
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  /*
   * Internals
   */

  static boolean compareSymbol(AbstractSExprNode node, String symbol) {
    return node instanceof SExprSymbolNode && ((SExprSymbolNode) node).value.equals(symbol);
  }
}
