package com.koyomiji.asmine.sexpr;

import com.koyomiji.asmine.common.AccessFlagTarget;
import com.koyomiji.asmine.common.AttributeHelper;
import com.koyomiji.asmine.common.BooleanHelper;
import com.koyomiji.asmine.common.UnknownAttribute;
import com.koyomiji.asmine.compat.ConstantDynamicCompat;
import com.koyomiji.asmine.compat.HandleCompat;
import com.koyomiji.asmine.compat.OpcodesCompat;
import com.koyomiji.asmine.sexpr.map.*;
import com.koyomiji.asmine.sexpr.tree.AbstractSExprNode;
import com.koyomiji.asmine.sexpr.tree.SExprFormattableListNode;
import com.koyomiji.asmine.sexpr.tree.SExprLineBreakNode;
import com.koyomiji.asmine.sexpr.tree.SExprSymbolNode;
import com.koyomiji.asmine.tuple.Pair;
import org.objectweb.asm.*;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

public class SExprClassPrinter extends Printer {
  private HashMap<Label, String> labelNames = new HashMap<>();
  private int tryCatchBlock = -1;
  private Stack<SExprFormattableListNode> nodeStack = new Stack<>();
  private AbstractSExprNode completed;
  private boolean preferAccessFlags = false;
  private List<SExprFormattableListNode> visibleParameterAnnotations = new ArrayList<>();
  private List<SExprFormattableListNode> invisibleParameterAnnotations = new ArrayList<>();

  /*
   * insn_n-1
   *         ^insnHead
   * insn_n
   *       ^insnTail
   * insn_n+1
   */
  private Pair<SExprFormattableListNode, AbstractSExprNode> insnHead;
  private Pair<SExprFormattableListNode, AbstractSExprNode> insnTail;
  private Label currentLabel;
  private Label prevLabel;

  private String moduleMainClass;
  private List<String> modulePackages = new ArrayList<>();

  public SExprClassPrinter() {
    super(OpcodesCompat.ASM_LATEST);
  }

  private SExprClassPrinter(SExprClassPrinter parent) {
    this();
    labelNames = parent.labelNames;
    tryCatchBlock = parent.tryCatchBlock;

    nodeStack = (Stack<SExprFormattableListNode>) parent.nodeStack.clone();
  }

  /*
   * Entry points
   */

  private static final String USAGE =
          "Prints an S-expression view of the given class.\n"
                  + "Usage: SExprClassPrinter [-nodebug] <fully qualified class name or class file name>";

  public static void main(final String[] args){
    main(args, new PrintWriter(System.out, true), new PrintWriter(System.err, true));
  }

  private static void main(final String[] args, final PrintWriter output, final PrintWriter logger) {
    try {
      main(args, USAGE, new SExprClassPrinter(), output, logger);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static void main(
          final String[] args,
          final String usage,
          final Printer printer,
          final PrintWriter output,
          final PrintWriter logger)
          throws IOException {
    TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, printer, output);

    String className = null;
    int parsingOptions = 0;

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-nodebug")) {
        parsingOptions |= ClassReader.SKIP_DEBUG;
      } else {
        className = args[i];
      }
    }

    if (className == null) {
      logger.println(usage);
      return;
    }

    if (className.endsWith(".class") || className.indexOf('\\') != -1  || className.indexOf('/') != -1) {
      try (InputStream inputStream = new FileInputStream(className)) {
        new ClassReader(inputStream).accept(traceClassVisitor, parsingOptions);
      }
    } else {
      new ClassReader(className).accept(traceClassVisitor, parsingOptions);
    }
  }

  /*
   * Accessors
   */

  public boolean doesPreferAccessFlags() {
    return preferAccessFlags;
  }

  public void setPreferAccessFlags(boolean preferAccessFlags) {
    this.preferAccessFlags = preferAccessFlags;
  }

  /*
   * Internals
   */

  private String getLabelName(Label label) {
    String labelName = labelNames.get(label);

    if (labelName == null) {
      labelName = "$l" + labelNames.size();
      labelNames.put(label, labelName);
    }

    return labelName;
  }

  protected SExprClassPrinter newPrinter() {
    return new SExprClassPrinter(this);
  }

  private SExprClassPrinter begin(String symbol) {
    if (nodeStack.size() > 0) {
      nodeStack.push((SExprFormattableListNode) nodeStack.peek().visitList());
    } else {
      nodeStack.push(new SExprFormattableListNode());
    }

    appendSymbol(symbol);
    return this;
  }

  private SExprClassPrinter breakLine() {
    if (nodeStack.size() > 0) {
      nodeStack.peek().visitLineBreak();
    }

    return this;
  }

  private SExprClassPrinter push(SExprFormattableListNode node) {
    nodeStack.push(node);
    return this;
  }

  private SExprClassPrinter pop() {
    SExprFormattableListNode popped = nodeStack.pop();

    if (nodeStack.size() == 0) {
      completed = popped;
    }

    return this;
  }

  private SExprClassPrinter end() {
    SExprFormattableListNode popped = nodeStack.pop();
    popped.visitEnd();

    if (nodeStack.size() == 0) {
      completed = popped;
    }

    return this;
  }

  @Override
  public void print(PrintWriter printWriter) {
    SExprWriter writer = new SExprWriter();
    completed.accept(writer);
    text.add(writer.getText());
    text.add("\n");

    super.print(printWriter);
  }

  private SExprClassPrinter appendSymbol(String string) {
    nodeStack.peek().visitSymbol(string);
    return this;
  }

  private SExprClassPrinter appendString(String string) {
    nodeStack.peek().visitString(string);
    return this;
  }

  private SExprClassPrinter appendDataString(byte[] bytes) {
    nodeStack.peek().visitDataString(bytes);
    return this;
  }

  private SExprClassPrinter appendInteger(long integer) {
    nodeStack.peek().visitInteger(integer);
    return this;
  }

  private SExprClassPrinter appendFloatingPoint(double value) {
    nodeStack.peek().visitFloatingPoint(value);
    return this;
  }

  private SExprClassPrinter appendStrings(String[] array) {
    for (String s : array) {
      appendString(s);
    }

    return this;
  }

  private SExprClassPrinter appendAll(Iterable<String> iterable) {
    for (String s : iterable) {
      appendSymbol(s);
    }

    return this;
  }

  private SExprClassPrinter appendAll(String[] array) {
    for (String s : array) {
      appendSymbol(s);
    }

    return this;
  }

  private SExprClassPrinter appendClasses(String[] array) {
    for (String s : array) {
      appendConstantClass(s);
    }

    return this;
  }

  private Pair<SExprFormattableListNode, AbstractSExprNode> mark() {
    return Pair.of(nodeStack.peek(), nodeStack.peek().children.get(nodeStack.peek().children.size() - 1));
  }

  /*
   * ClassVisitor
   */

  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
    begin("class");
    begin("minor_version").appendInteger(version >> 16).end();
    begin("major_version").appendInteger(version & 0xFF).end();

    boolean syntheticAttribute = false;

    if (!preferAccessFlags) {
      syntheticAttribute = (access & Opcodes.ACC_SYNTHETIC) != 0;
      access &= ~Opcodes.ACC_SYNTHETIC;
    }

    appendAccess(access, AccessFlagTarget.CLASS);
    appendString(name);

    if (superName != null) {
      begin("super_class").appendString(superName).end();
    }

    if (interfaces != null && interfaces.length > 0) {
      begin("interface").appendStrings(interfaces).end();
    }

    if ((access & Opcodes.ACC_DEPRECATED) != 0) {
      breakLine().begin("deprecated").end();
    }

    if (syntheticAttribute) {
      breakLine().begin("synthetic").end();
    }

    if (signature != null) {
      breakLine().begin("signature").appendString(signature).end();
    }
  }

  private SExprClassPrinter appendAccess(int access, AccessFlagTarget target) {
    List<String> flags = AccessFlagMap.toNames(access, target);

    if (flags.size() > 0) {
      if (target == AccessFlagTarget.MODULE || target == AccessFlagTarget.MODULE_EXPORTS || target == AccessFlagTarget.MODULE_OPENS || target == AccessFlagTarget.MODULE_REQUIRES) {
        begin("flag");
      } else {
        begin("access_flag");
      }

      for (String flag : flags) {
        appendSymbol(flag);
      }

      end();
    }
    return this;
  }

  public void visitSource(String source, String debug) {
    if (source != null) {
      breakLine().begin("source_file").appendString(source).end();
    }

    if (debug != null) {
      breakLine().begin("source_debug_extension").appendString(debug).end();
    }
  }

  public SExprClassPrinter visitModule(String name, int access, String version) {
    breakLine();
    begin("module").appendString(name);
    appendAccess(access, AccessFlagTarget.MODULE);

    if (version != null) {
      begin("version").appendString(version).end();
    }

    SExprClassPrinter printer = newPrinter();
    pop();

    return printer;
  }

  public void visitNestHost(String nestHost) {
    breakLine();
    begin("nest_host").appendString(nestHost).end();
  }

  public void visitOuterClass(String owner, String name, String descriptor) {
    breakLine();
    begin("enclosing_method");
    begin("class").appendString(owner).end();

    if (name != null) {
      appendConstantNameAndType("method", name, descriptor);
    }

    end();
  }

  public SExprClassPrinter visitClassAnnotation(String descriptor, boolean visible) {
    return appendAnnotation(descriptor, visible);
  }

  public SExprClassPrinter visitClassTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
    return appendTypeAnnotation(typeRef, typePath, descriptor, visible);
  }

  public void visitClassAttribute(Attribute attribute) {
    appendAttribute(attribute);
  }

  public void visitNestMember(String nestMember) {
    breakLine();
    begin("nest_member").appendString(nestMember).end();
  }

  public void visitPermittedSubclass(String permittedSubclass) {
    breakLine();
    begin("permitted_subclass").appendString(permittedSubclass).end();
  }

  public void visitInnerClass(String name, String outerName, String innerName, int access) {
    breakLine();
    begin("inner_class");

    begin("inner_class").appendString(name).end();

    if (outerName != null) {
      begin("outer_class").appendString(outerName).end();
    }

    if (innerName != null) {
      begin("inner_name").appendString(innerName).end();
    }

    appendAccess(access, AccessFlagTarget.INNER_CLASS);

    end();
  }

  public SExprClassPrinter visitRecordComponent(String name, String descriptor, String signature) {
    breakLine();
    begin("record_component").appendString(name).appendString(descriptor);

    if (signature != null) {
      breakLine().begin("signature").appendString(signature).end();
    }

    SExprClassPrinter printer = newPrinter();

    end();

    return printer;
  }

  public SExprClassPrinter visitField(int access, String name, String descriptor, String signature, Object value) {
    breakLine();
    begin("field");

    boolean syntheticAttribute = false;

    if (!preferAccessFlags) {
      syntheticAttribute = (access & Opcodes.ACC_SYNTHETIC) != 0;
      access &= ~Opcodes.ACC_SYNTHETIC;
    }

    appendAccess(access, AccessFlagTarget.FIELD);

    appendString(name).appendString(descriptor);

    if ((access & Opcodes.ACC_DEPRECATED) != 0) {
      breakLine().begin("deprecated").end();
    }

    if (syntheticAttribute) {
      breakLine().begin("synthetic").end();
    }

    if (signature != null) {
      breakLine().begin("signature").appendString(signature).end();
    }

    if (value != null) {
      breakLine().begin("constant_value").appendObject(value).end();
    }

    SExprClassPrinter printer = newPrinter();
    pop();

    return printer;
  }

  public SExprClassPrinter visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
    labelNames = new HashMap<>();

    breakLine();
    begin("method");

    boolean syntheticAttribute = false;

    if (!preferAccessFlags) {
      syntheticAttribute = (access & Opcodes.ACC_SYNTHETIC) != 0;
      access &= ~Opcodes.ACC_SYNTHETIC;
    }

    appendAccess(access, AccessFlagTarget.METHOD);

    appendString(name).appendString(descriptor);

    if ((access & Opcodes.ACC_DEPRECATED) != 0) {
      breakLine().begin("deprecated").end();
    }

    if (syntheticAttribute) {
      breakLine().begin("synthetic").end();
    }

    if (signature != null) {
      breakLine().begin("signature").appendString(signature).end();
    }

    if (exceptions != null && exceptions.length > 0) {
      breakLine().begin("exception").appendStrings(exceptions).end();
    }

    SExprClassPrinter printer = newPrinter();
    pop();

    return printer;
  }

  public void visitClassEnd() {
    end().breakLine();
  }

  /*
   * ModuleVisitor
   */

  // ModuleMainClass
  public void visitMainClass(String mainClass) {
    moduleMainClass = mainClass;
  }

  // ModulePackages
  public void visitPackage(String packaze) {
    modulePackages.add(packaze);
  }

  public void visitRequire(String module, int access, String version) {
    breakLine();
    begin("requires").appendString(module);

    appendAccess(access, AccessFlagTarget.MODULE_REQUIRES);

    if (version != null) {
      begin("version").appendString(version).end();
    }

    end();
  }

  public void visitExport(String packaze, int access, String... modules) {
    breakLine();
    begin("exports").appendString(packaze);
    appendAccess(access, AccessFlagTarget.MODULE_EXPORTS);

    begin("to");

    for (String module : modules) {
      appendString(module);
    }

    end();
    end();
  }

  public void visitOpen(String packaze, int access, String... modules) {
    breakLine();
    begin("opens").appendString(packaze);
    appendAccess(access, AccessFlagTarget.MODULE_OPENS);

    begin("to");

    for (String module : modules) {
      appendString(module);
    }

    end();
    end();
  }

  public void visitUse(String service) {
    breakLine();
    begin("uses").appendString(service).end();
  }

  public void visitProvide(String service, String... providers) {
    breakLine();
    begin("provides").appendString(service);

    begin("with");

    for (String provider : providers) {
      appendString(provider);
    }

    end();
    end();
  }

  public void visitModuleEnd() {
    end();

    if (moduleMainClass != null) {
      breakLine().begin("module_main_class").appendString(moduleMainClass).end();
    }

    if (modulePackages.size() > 0) {
      for (String packaze : modulePackages) {
        breakLine().begin("module_package").appendString(packaze).end();
      }
    }
  }

  /*
   * AnnotationVisitor
   */

  public void visit(String name, Object value) {
    if (value instanceof byte[]) {
      byte[] byteArray = (byte[]) value;
      SExprClassPrinter printer = visitArray(name);
      for (int i = 0; i < byteArray.length; i++) {
        printer.visit(null, byteArray[i]);
      }
    } else if (value instanceof boolean[]) {
      boolean[] booleanArray = (boolean[]) value;
      SExprClassPrinter printer = visitArray(name);
      for (int i = 0; i < booleanArray.length; i++) {
        printer.visit(null, booleanArray[i]);
      }
    } else if (value instanceof short[]) {
      short[] shortArray = (short[]) value;
      SExprClassPrinter printer = visitArray(name);
      for (int i = 0; i < shortArray.length; i++) {
        printer.visit(null, shortArray[i]);
      }
    } else if (value instanceof char[]) {
      char[] charArray = (char[]) value;
      SExprClassPrinter printer = visitArray(name);
      for (int i = 0; i < charArray.length; i++) {
        printer.visit(null, charArray[i]);
      }
    } else if (value instanceof int[]) {
      int[] intArray = (int[]) value;
      SExprClassPrinter printer = visitArray(name);
      for (int i = 0; i < intArray.length; i++) {
        printer.visit(null, intArray[i]);
      }
    } else if (value instanceof long[]) {
      long[] longArray = (long[]) value;
      SExprClassPrinter printer = visitArray(name);
      for (int i = 0; i < longArray.length; i++) {
        printer.visit(null, longArray[i]);
      }
    } else if (value instanceof float[]) {
      float[] floatArray = (float[]) value;
      SExprClassPrinter printer = visitArray(name);
      for (int i = 0; i < floatArray.length; i++) {
        printer.visit(null, floatArray[i]);
      }
    } else if (value instanceof double[]) {
      double[] doubleArray = (double[]) value;
      SExprClassPrinter printer = visitArray(name);
      for (int i = 0; i < doubleArray.length; i++) {
        printer.visit(null, doubleArray[i]);
      }
    } else {
      if (name != null) {
        breakLine();
        // name is null inside AnnotationDefault, or inside an array
        // "element_value_pairs"
        begin("element_value_pair").appendString(name);
      }

      appendObject(value, true);

      if (name != null) {
        end();
      }
    }
  }

  public void visitEnum(String name, String descriptor, String value) {
    if (name != null) {
      breakLine();
      // "element_value_pairs"
      begin("element_value_pair").appendString(name);
    }

    begin("enum").appendString(descriptor).appendString(value).end();

    if (name != null) {
      end();
    }
  }

  public SExprClassPrinter visitAnnotation(String name, String descriptor) {
    if (name != null) {
      breakLine();
      // "element_value_pairs"
      begin("element_value_pair").appendString(name);
    }

    begin("annotation").begin("type").appendString(descriptor).end();

    SExprClassPrinter printer = newPrinter();

    pop();

    if (name != null) {
      end();
    }

    return printer;
  }

  public SExprClassPrinter visitArray(String name) {
    if (name != null) {
      breakLine();
      // "element_value_pairs"
      begin("element_value_pair").appendString(name);
    }

    begin("array");

    SExprClassPrinter printer = newPrinter();

    pop();

    if (name != null) {
      end();
    }

    return printer;
  }

  public void visitAnnotationEnd() {
    end();
  }

  /*
   * RecordComponentVisitor
   */

  public SExprClassPrinter visitRecordComponentAnnotation(String descriptor, boolean visible) {
    return appendAnnotation(descriptor, visible);
  }

  public SExprClassPrinter visitRecordComponentTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
    return appendTypeAnnotation(typeRef, typePath, descriptor, visible);
  }

  public void visitRecordComponentAttribute(Attribute attribute) {
    appendAttribute(attribute);
  }

  public void visitRecordComponentEnd() {
    end();
  }

  private SExprClassPrinter appendAnnotation(String descriptor, boolean visible) {
    breakLine().begin(visible ? "runtime_visible_annotation" : "runtime_invisible_annotation");
    begin("type").appendString(descriptor).end();

    SExprClassPrinter printer = newPrinter();
    pop();

    return printer;
  }

  private SExprClassPrinter appendTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
    return appendTypeAnnotation(typeRef, typePath, null, null, null, descriptor, visible);
  }

  private SExprClassPrinter appendTypeAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
    breakLine().begin(visible ? "runtime_visible_type_annotation" : "runtime_invisible_type_annotation");
    begin("target").appendTarget(typeRef, start, end, index).end();

    if (typePath != null) {
      begin("target_path").appendTypePath(typePath).end();
    }

    begin("type").appendString(descriptor).end();

    SExprClassPrinter printer = newPrinter();
    pop();

    return printer;
  }

  /*
   * FieldVisitor
   */

  public SExprClassPrinter visitFieldAnnotation(String descriptor, boolean visible) {
    return appendAnnotation(descriptor, visible);
  }

  public SExprClassPrinter visitFieldTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
    return appendTypeAnnotation(typeRef, typePath, descriptor, visible);
  }

  public void visitFieldAttribute(Attribute attribute) {
    appendAttribute(attribute);
  }

  public void visitFieldEnd() {
    end();
  }

  /*
   * MethodVisitor
   */

  public void visitParameter(String name, int access) {
    breakLine();
    begin("method_parameter");

    if (name != null) {
      begin("name").appendString(name).end();
    }

    if (access != 0) {
      appendAccess(access, AccessFlagTarget.METHOD_PARAMETER);
    }

    end();
  }

  public SExprClassPrinter visitAnnotationDefault() {
    breakLine();
    begin("annotation_default");

    SExprClassPrinter printer = newPrinter();
    pop();

    return printer;
  }

  public SExprClassPrinter visitMethodAnnotation(String descriptor, boolean visible) {
    return appendAnnotation(descriptor, visible);
  }

  public SExprClassPrinter visitMethodTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
    return appendTypeAnnotation(typeRef, typePath, descriptor, visible);
  }

  private void ensureParameterAnnotations(int count, boolean visible) {
    if (visible) {
      while (count > visibleParameterAnnotations.size()) {
        breakLine().begin("runtime_visible_parameter_annotation");
        visibleParameterAnnotations.add(nodeStack.peek());
        pop();
      }
    } else {
      while (count > invisibleParameterAnnotations.size()) {
        breakLine().begin("runtime_invisible_parameter_annotation");
        invisibleParameterAnnotations.add(nodeStack.peek());
        pop();
      }
    }
  }

  public SExprClassPrinter visitAnnotableParameterCount(int parameterCount, boolean visible) {
    ensureParameterAnnotations(parameterCount, visible);
    return this;
  }

  public SExprClassPrinter visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
    ensureParameterAnnotations(parameter + 1, visible);

    if (visible) {
      push(visibleParameterAnnotations.get(parameter));
    } else {
      push(invisibleParameterAnnotations.get(parameter));
    }

    breakLine().begin("parameter_annotation").begin("type").appendString(descriptor).end();
    SExprClassPrinter printer = newPrinter();
    pop();
    pop();
    return printer;
  }

  private SExprClassPrinter appendAttribute(Attribute attribute) {
    // FIXME: attributes inside Code
    breakLine();
    begin("unknown");
    begin("name").appendString(attribute.type).end();
    byte[] content;

    if (attribute instanceof UnknownAttribute) {
      UnknownAttribute unknownAttribute = (UnknownAttribute) attribute;
      content = unknownAttribute.getContent();
    } else {
      content = AttributeHelper.getContent(attribute);
    }

    if (content != null) {
      begin("content").appendDataString(content).end();
    }

    end();
    return this;
  }

  public void visitMethodAttribute(Attribute attribute) {
    appendAttribute(attribute);
  }

  public void visitCode() {
    breakLine();
    begin("code");

    insnTail = mark();
  }

  private SExprClassPrinter appendFrameItem(Object i) {
    if (i instanceof Integer) {
      if (i == Opcodes.TOP) {
        begin("top");
      } else if (i == Opcodes.INTEGER) {
        begin("integer");
      } else if (i == Opcodes.FLOAT) {
        begin("float");
      } else if (i == Opcodes.DOUBLE) {
        begin("double");
      } else if (i == Opcodes.LONG) {
        begin("long");
      } else if (i == Opcodes.NULL) {
        begin("null");
      } else if (i == Opcodes.UNINITIALIZED_THIS) {
        begin("uninitialized_this");
      }
    } else if (i instanceof Label) {
      begin("uninitialized").appendSymbol(getLabelName((Label) i));
    } else if (i instanceof String) {
      // "Object"
      begin("object").appendString((String) i);
    } else {
      throw new IllegalArgumentException("Unknown frame item " + i);
    }

    end();
    return this;
  }

  private SExprClassPrinter appendFrameItems(int numItems, Object[] items) {
    for (int i = 0; i < numItems; i++) {
      appendFrameItem(items[i]);
    }

    return this;
  }

  private Label getCurrentLabel() {
    if (currentLabel == null) {
      // Insert label after insnTail
      currentLabel = new Label();
      insnTail.first.insertAfter(insnTail.second, new SExprSymbolNode(getLabelName(currentLabel)));
      insnTail.first.insertAfter(insnTail.second, new SExprSymbolNode("label"));
      insnTail.first.insertAfter(insnTail.second, new SExprLineBreakNode());
    }

    return currentLabel;
  }

  private Label getPrevLabel() {
    if (prevLabel == null) {
      // Insert label after insnHead
      prevLabel = new Label();
      insnHead.first.insertAfter(insnHead.second, new SExprSymbolNode(getLabelName(prevLabel)));
      insnHead.first.insertAfter(insnHead.second, new SExprSymbolNode("label"));
      insnHead.first.insertAfter(insnHead.second, new SExprLineBreakNode());
    }

    return prevLabel;
  }

  public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
    String labelName = getLabelName(getCurrentLabel());

    breakLine();
    begin("stack_map_frame");

    switch (type) {
      case Opcodes.F_NEW:
      case Opcodes.F_FULL:
        begin(type == Opcodes.F_FULL ? "full" : "new");
        begin("offset").appendSymbol(labelName).end();
        begin("local").appendFrameItems(numLocal, local).end();
        begin("stack").appendFrameItems(numStack, stack).end();
        end();
        break;
      case Opcodes.F_APPEND:
        begin("append");
        begin("offset").appendSymbol(labelName).end();
        begin("local").appendFrameItems(numLocal, local).end();
        end();
        break;
      case Opcodes.F_CHOP:
        begin("chop");
        begin("offset").appendSymbol(labelName).end();
        appendInteger(numLocal);
        end();
        break;
      case Opcodes.F_SAME:
        begin("same");
        begin("offset").appendSymbol(labelName).end();
        end();
        break;
      case Opcodes.F_SAME1:
        begin("same_locals_1_stack_item");
        begin("offset").appendSymbol(labelName).end();
        begin("stack").appendFrameItem(stack[0]).end();
        end();
        break;
    }

    end();
  }

  public void visitInsn(int opcode) {
    insnHead = mark();
    prevLabel = currentLabel;
    currentLabel = null;
    breakLine();

    appendSymbol(OpcodeMap.toString(opcode).toLowerCase());
    insnTail = mark();
  }

  public void visitIntInsn(int opcode, int operand) {
    insnHead = mark();
    prevLabel = currentLabel;
    currentLabel = null;
    breakLine();

    appendSymbol(OpcodeMap.toString(opcode).toLowerCase());

    if (opcode == Opcodes.NEWARRAY && (operand >= Opcodes.T_BOOLEAN && operand <= Opcodes.T_LONG)) {
      appendSymbol(ArrayTypeMap.toString(operand));
    } else {
      appendInteger(operand);
    }

    insnTail = mark();
  }

  public void visitVarInsn(int opcode, int varIndex) {
    insnHead = mark();
    prevLabel = currentLabel;
    currentLabel = null;
    breakLine();

    appendSymbol(OpcodeMap.toString(opcode).toLowerCase()).appendInteger(varIndex);

    insnTail = mark();
  }

  public void visitTypeInsn(int opcode, String type) {
    insnHead = mark();
    prevLabel = currentLabel;
    currentLabel = null;
    breakLine();

    appendSymbol(OpcodeMap.toString(opcode).toLowerCase()).appendConstantClass(type);

    insnTail = mark();
  }

  public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
    insnHead = mark();
    prevLabel = currentLabel;
    currentLabel = null;
    breakLine();

    appendSymbol(OpcodeMap.toString(opcode).toLowerCase()).appendConstantFieldref(owner, name, descriptor);

    insnTail = mark();
  }

  public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
    insnHead = mark();
    prevLabel = currentLabel;
    currentLabel = null;
    breakLine();

    appendSymbol(OpcodeMap.toString(opcode).toLowerCase()).appendConstantMethodref(owner, name, descriptor, isInterface);

    insnTail = mark();
  }

  public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
    insnHead = mark();
    prevLabel = currentLabel;
    currentLabel = null;
    breakLine();

    appendSymbol(OpcodeMap.toString(Opcodes.INVOKEDYNAMIC).toLowerCase());

    begin("invoke_dynamic");
    begin("bootstrap_method");
    begin("method_ref").appendObject(bootstrapMethodHandle).end();
    begin("argument");

    for (Object a : bootstrapMethodArguments) {
      appendObject(a);
    }

    end();
    end();
    appendConstantNameAndType("name_and_type", name, descriptor);
    end();

    insnTail = mark();
  }

  public void visitJumpInsn(int opcode, Label label) {
    insnHead = mark();
    prevLabel = currentLabel;
    currentLabel = null;
    breakLine();

    appendSymbol(OpcodeMap.toString(opcode).toLowerCase()).appendSymbol(getLabelName(label));

    insnTail = mark();
  }

  public void visitLabel(Label label) {
    breakLine();
    appendSymbol("label").appendSymbol(getLabelName(label));

    currentLabel = label;
  }

  public void visitLdcInsn(Object value) {
    insnHead = mark();
    prevLabel = currentLabel;
    currentLabel = null;
    breakLine();

    appendSymbol(OpcodeMap.toString(Opcodes.LDC).toLowerCase()).appendObject(value);

    insnTail = mark();
  }

  public void visitIincInsn(int varIndex, int increment) {
    insnHead = mark();
    prevLabel = currentLabel;
    currentLabel = null;
    breakLine();

    appendSymbol(OpcodeMap.toString(Opcodes.IINC).toLowerCase()).appendInteger(varIndex).appendInteger(increment);

    insnTail = mark();
  }

  public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
    insnHead = mark();
    prevLabel = currentLabel;
    currentLabel = null;
    breakLine();

    appendSymbol(OpcodeMap.toString(Opcodes.TABLESWITCH).toLowerCase()).appendInteger(min).appendInteger(max).appendSymbol(getLabelName(dflt));

    for (Label label : labels) {
      appendSymbol(getLabelName(label));
    }

    insnTail = mark();
  }

  public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
    insnHead = mark();
    prevLabel = currentLabel;
    currentLabel = null;
    breakLine();

    appendSymbol(OpcodeMap.toString(Opcodes.LOOKUPSWITCH).toLowerCase()).appendSymbol(getLabelName(dflt));

    for (int i = 0; i < keys.length; i++) {
      appendInteger(keys[i]).appendSymbol(getLabelName(labels[i]));
    }

    insnTail = mark();
  }

  public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
    insnHead = mark();
    prevLabel = currentLabel;
    currentLabel = null;
    breakLine();

    appendSymbol(OpcodeMap.toString(Opcodes.MULTIANEWARRAY).toLowerCase()).appendString(descriptor).appendInteger(numDimensions);

    insnTail = mark();
  }

  public SExprClassPrinter visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
    return appendTypeAnnotation(typeRef, typePath, descriptor, visible);
  }

  public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
    breakLine();
    // "exception_table"
    begin("exception").begin("start").appendSymbol(getLabelName(start)).end().begin("end").appendSymbol(getLabelName(end)).end().appendSymbol(getLabelName(handler));

    if (type != null) {
      begin("catch_type").appendString(type).end();
    }

    end();

    tryCatchBlock++;
  }

  public SExprClassPrinter visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
    return appendTypeAnnotation(typeRef, typePath, descriptor, visible);
  }

  public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
    breakLine();
    begin("local_variable").begin("start").appendSymbol(getLabelName(start)).end().begin("end").appendSymbol(getLabelName(end)).end().appendString(name).appendString(descriptor).appendInteger(index).end();

    if (signature != null) {
      breakLine();
      begin("local_variable_type").begin("start").appendSymbol(getLabelName(start)).end().begin("end").appendSymbol(getLabelName(end)).end().appendString(name).appendString(signature).appendInteger(index).end();
    }
  }

  private SExprClassPrinter appendTypePath(TypePath typePath) {
    for (int i = 0; i < typePath.getLength(); i++) {
      switch (typePath.getStep(i)) {
        case TypePath.ARRAY_ELEMENT:
          begin("array").end();
          break;
        case TypePath.INNER_TYPE:
          begin("inner_type").end();
          break;
        case TypePath.WILDCARD_BOUND:
          begin("wildcard_bound").end();
          break;
        case TypePath.TYPE_ARGUMENT:
          begin("type_argument").appendInteger(typePath.getStepArgument(i)).end();
          break;
      }
    }

    return this;
  }

  private SExprClassPrinter appendTarget(int typeRef, Label[] start, Label[] end, int[] index) {
    return appendTarget(new TypeReference(typeRef), start, end, index);
  }

  private SExprClassPrinter appendTarget(TypeReference typeRef, Label[] start, Label[] end, int[] index) {
    switch (typeRef.getSort()) {
      case TypeReference.CLASS_TYPE_PARAMETER:
      case TypeReference.METHOD_TYPE_PARAMETER:
        // type_parameter_target
        begin(TargetTypeMap.toString(typeRef.getSort()).toLowerCase());

        begin("type_parameter_index").appendInteger(typeRef.getTypeParameterIndex()).end();

        end();
        break;
      case TypeReference.CLASS_EXTENDS:
        // supertype_target
        begin(TargetTypeMap.toString(typeRef.getSort()).toLowerCase());

        begin("supertype_index").appendInteger(Short.toUnsignedInt((short) typeRef.getSuperTypeIndex())).end();

        end();
        break;
      case TypeReference.CLASS_TYPE_PARAMETER_BOUND:
      case TypeReference.METHOD_TYPE_PARAMETER_BOUND:
        // type_parameter_bound_target
        begin(TargetTypeMap.toString(typeRef.getSort()).toLowerCase());

        begin("type_parameter_index").appendInteger(typeRef.getTypeParameterIndex()).end();
        begin("bound_index").appendInteger(typeRef.getTypeParameterBoundIndex()).end();

        end();
        break;
      case TypeReference.FIELD:
      case TypeReference.METHOD_RETURN:
      case TypeReference.METHOD_RECEIVER:
        // empty_target
        begin(TargetTypeMap.toString(typeRef.getSort()).toLowerCase());
        end();
        break;
      case TypeReference.METHOD_FORMAL_PARAMETER:
        // formal_parameter_target
        begin(TargetTypeMap.toString(typeRef.getSort()).toLowerCase());

        begin("formal_parameter_index").appendInteger(typeRef.getFormalParameterIndex()).end();

        end();
        break;
      case TypeReference.THROWS:
        // throws_target
        begin(TargetTypeMap.toString(typeRef.getSort()).toLowerCase());

        begin("throws_type_index").appendInteger(typeRef.getExceptionIndex()).end();

        end();
        break;
      case TypeReference.LOCAL_VARIABLE:
      case TypeReference.RESOURCE_VARIABLE:
        // localvar_target
        for (int i = 0; i < start.length; i++) {
          begin(TargetTypeMap.toString(typeRef.getSort()).toLowerCase());

          // "table"
          begin("start").appendSymbol(getLabelName(start[i])).end().begin("end").appendSymbol(getLabelName(end[i])).end().begin("index").appendInteger(index[i]).end();

          end();
        }

        break;
      case TypeReference.EXCEPTION_PARAMETER:
        // catch_target
        begin(TargetTypeMap.toString(typeRef.getSort()).toLowerCase());

        begin("exception_table_index").appendInteger(tryCatchBlock).end();

        end();
        break;
      case TypeReference.INSTANCEOF:
      case TypeReference.NEW:
      case TypeReference.CONSTRUCTOR_REFERENCE:
      case TypeReference.METHOD_REFERENCE:
        // offset_target
        begin(TargetTypeMap.toString(typeRef.getSort()).toLowerCase());

        begin("offset").appendSymbol(getLabelName(getPrevLabel())).end();

        end();
        break;
      case TypeReference.CAST:
      case TypeReference.CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT:
      case TypeReference.METHOD_INVOCATION_TYPE_ARGUMENT:
      case TypeReference.CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT:
      case TypeReference.METHOD_REFERENCE_TYPE_ARGUMENT:
        // type_argument_target
        begin(TargetTypeMap.toString(typeRef.getSort()).toLowerCase());

        begin("type_argument_index").appendInteger(typeRef.getTypeArgumentIndex()).end();

        end();
        break;
    }

    return this;
  }

  public SExprClassPrinter visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
    return appendTypeAnnotation(typeRef, typePath, start, end, index, descriptor, visible);
  }

  public void visitLineNumber(int line, Label start) {
    breakLine();
    begin("line_number").begin("start").appendSymbol(getLabelName(start)).end().appendInteger(line).end();
  }

  public void visitMaxs(int maxStack, int maxLocals) {
    breakLine().begin("max_stack").appendInteger(maxStack).end();
    breakLine().begin("max_locals").appendInteger(maxLocals).end();
    end(); // end of code
  }

  public void visitMethodEnd() {
    for (SExprFormattableListNode an : visibleParameterAnnotations) {
      an.visitEnd();
    }

    for (SExprFormattableListNode an : invisibleParameterAnnotations) {
      an.visitEnd();
    }

    end();
  }

  /*
   * Utilities
   */

  private SExprClassPrinter appendObject(Object object) {
    return appendObject(object, false);
  }

  private SExprClassPrinter appendObject(Object object, boolean annotation) {
    if (object == null) {
      throw new IllegalArgumentException("object is null");
    }

    if (object instanceof Byte) {
      begin("byte").appendInteger((Byte) object).end();
    } else if (object instanceof Character) {
      begin("char").appendInteger((Character) object).end();
    } else if (object instanceof Short) {
      begin("short").appendInteger((Short) object).end();
    } else if (object instanceof Boolean) {
      begin("boolean").appendInteger(BooleanHelper.toInt((Boolean) object)).end();
    } else if (object instanceof Integer) {
      begin(annotation ? "int" : "integer").appendInteger((Integer) object).end();
    } else if (object instanceof Float) {
      begin("float").appendFloatingPoint((Float) object).end();
    } else if (object instanceof Long) {
      begin("long").appendInteger((Long) object).end();
    } else if (object instanceof Double) {
      begin("double").appendFloatingPoint((Double) object).end();
    } else if (object instanceof String) {
      begin("string").appendString((String) object).end();
    } else if (object instanceof Type) {
      Type type = (Type) object;

      if (type.getSort() == Type.METHOD) {
        begin("method_type");
      } else {
        begin("class");
      }

      appendString(type.getInternalName());

      end();
    } else if (object instanceof Handle) {
      Handle handle = (Handle) object;
      begin("method_handle");
      begin("reference_kind").appendSymbol(ReferenceKindMap.toString(handle.getTag())).end();
      begin("reference");

      switch (handle.getTag()) {
        case Opcodes.H_GETFIELD:
        case Opcodes.H_GETSTATIC:
        case Opcodes.H_PUTFIELD:
        case Opcodes.H_PUTSTATIC:
          appendConstantFieldref(handle.getOwner(), handle.getName(), handle.getDesc());
          break;
        case Opcodes.H_INVOKEVIRTUAL:
        case Opcodes.H_INVOKESTATIC:
        case Opcodes.H_INVOKESPECIAL:
        case Opcodes.H_NEWINVOKESPECIAL:
        case Opcodes.H_INVOKEINTERFACE:
          appendConstantMethodref(handle.getOwner(), handle.getName(), handle.getDesc(), HandleCompat.isInterface(handle));
          break;
      }

      end();
      end();
    } else if (ConstantDynamicCompat.instanceOf(object)) {
      begin("dynamic");
      begin("bootstrap_method");
      begin("method_ref").appendObject(ConstantDynamicCompat.getBootstrapMethod(object)).end();
      begin("argument");

      for (int i = 0; i < ConstantDynamicCompat.getBootstrapMethodArgumentCount(object); i++) {
        appendObject(ConstantDynamicCompat.getBootstrapMethodArgument(object, i));
      }

      end();
      end();
      appendConstantNameAndType("name_and_type", ConstantDynamicCompat.getName(object), ConstantDynamicCompat.getDescriptor(object));
      end();
    } else {
      throw new IllegalArgumentException("Unsupported object type");
    }

    return this;
  }

  /*
   * Constants
   */

  private SExprClassPrinter appendConstantClass(String className) {
    appendString(className);
    return this;
  }

  private SExprClassPrinter appendConstantClass(String key, String className) {
    if (key != null) {
      begin(key).appendConstantClass(null, className).end();
      return this;
    }

    appendString(className);
    return this;
  }

  private SExprClassPrinter appendConstantFieldref(String owner, String name, String descriptor) {
    begin("fieldref").appendConstantClass("class", owner).appendConstantNameAndType("name_and_type", name, descriptor).end();
    return this;
  }

  private SExprClassPrinter appendConstantMethodref(String owner, String name, String descriptor, boolean isInterface) {
    if (isInterface) {
      begin("interface_methodref");
    } else {
      begin("methodref");
    }

    appendConstantClass("class", owner).appendConstantNameAndType("name_and_type", name, descriptor);

    end();
    return this;
  }

  private SExprClassPrinter appendConstantNameAndType(String key, String name, String descriptor) {
    begin(key).appendString(name).appendString(descriptor).end();
    return this;
  }
}
