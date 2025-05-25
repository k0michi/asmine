package com.koyomiji.asmine.sexpr.reader;

import com.koyomiji.asmine.common.*;
import com.koyomiji.asmine.compat.OpcodesCompat;
import com.koyomiji.asmine.sexpr.map.OpcodeMap;
import com.koyomiji.asmine.sexpr.map.TargetTypeMap;
import com.koyomiji.asmine.sexpr.tree.SExprListNode;
import com.koyomiji.asmine.sexpr.tree.SExprNodeContainer;
import com.koyomiji.asmine.tuple.Pair;
import com.koyomiji.asmine.tuple.Quartet;
import com.koyomiji.asmine.tuple.Triplet;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;

public class SExprClassReader {
  private final ClassNode classNode;
  private LabelMap labelMap;

  public SExprClassReader(SExprNodeContainer sexprNode) {
    if (sexprNode.child instanceof SExprListNode) {
      this.classNode = readClass(new SExprClassTraverser((SExprListNode) sexprNode.child));
    } else {
      throw new IllegalArgumentException("Invalid S-Expression node");
    }
  }

  public void accept(ClassVisitor classVisitor) {
    classNode.accept(classVisitor);
  }

  /*
   * Class
   */

  private ClassNode readClass(SExprClassTraverser traverser) {
    ClassNode classNode = new ClassNode();

    traverser.skipSymbol("class");
    classNode.version = (int) traverser.nextInteger("minor_version") << 16;
    classNode.version |= (int) traverser.nextInteger("major_version");
    classNode.access = traverser.nextFlags("access_flag", AccessFlagTarget.CLASS);
    classNode.name = traverser.nextConstantClass("this_class");
    classNode.superName = traverser.nextConstantClassOptional("super_class");
    classNode.interfaces = traverser.nextConstantClasses("interface");

    while (traverser.hasNext()) {
      if (traverser.isNextList("field")) {
        FieldNode fieldNode = readField(traverser.nextListTraverser());
        classNode.fields.add(fieldNode);
      } else if (traverser.isNextList("method")) {
        MethodNode methodNode = readMethod(traverser.nextListTraverser());
        classNode.methods.add(methodNode);
      } else if (traverser.isNextList("source_file")) {
        classNode.sourceFile = readSourceFile(traverser.nextListTraverser());
      } else if (traverser.isNextList("inner_class")) {
        classNode.innerClasses.add(readInnerClass(traverser.nextListTraverser()));
      } else if (traverser.isNextList("enclosing_method")) {
        Triplet<String, String, String> enclosingMethod = readEnclosingMethod(traverser.nextListTraverser());
        classNode.outerClass = enclosingMethod.first;
        classNode.outerMethod = enclosingMethod.second;
        classNode.outerMethodDesc = enclosingMethod.third;
      } else if (traverser.isNextList("source_debug_extension")) {
        classNode.sourceDebug = readSourceDebugExtension(traverser.nextListTraverser());
//      } else if (traverser.isNextList("bootstrap_method")) { // Bootstrap methods are inlined
//      } else if (traverser.isNextList("module")) {
//        if (classNode.module == null) {
//          classNode.module = new ModuleNode(OpcodesCompat.ASM_LATEST, null, 0, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
//        }
//
//        ModuleNode moduleNode = readModule(traverser.nextListTraverser());
//        classNode.module.name = moduleNode.name;
//        classNode.module.access = moduleNode.access;
//        classNode.module.version = moduleNode.version;
//        classNode.module.requires = moduleNode.requires;
//        classNode.module.exports = moduleNode.exports;
//        classNode.module.opens = moduleNode.opens;
//        classNode.module.uses = moduleNode.uses;
//        classNode.module.provides = moduleNode.provides;
//      } else if (traverser.isNextList("module_package")) {
//        if (classNode.module == null) {
//          classNode.module = new ModuleNode(OpcodesCompat.ASM_LATEST, null, 0, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
//        }
//
//        if (classNode.module.packages == null) {
//          classNode.module.packages = new ArrayList<>();
//        }
//
//        classNode.module.packages.add(readModulePackage(traverser.nextListTraverser()));
//      } else if (traverser.isNextList("module_main_class")) {
//        if (classNode.module == null) {
//          classNode.module = new ModuleNode(OpcodesCompat.ASM_LATEST, null, 0, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
//        }
//
//        classNode.module.mainClass = readModuleMainClass(traverser.nextListTraverser());
//      } else if (traverser.isNextList("nest_host")) {
//        classNode.nestHostClass = readNestHost(traverser.nextListTraverser());
//      } else if (traverser.isNextList("nest_member")) {
//        if (classNode.nestMembers == null) {
//          classNode.nestMembers = new ArrayList<>();
//        }
//
//        classNode.nestMembers.add(readNestMember(traverser.nextListTraverser()));
//      } else if (traverser.isNextList("record_component")) {
//        if (classNode.recordComponents == null) {
//          classNode.recordComponents = new ArrayList<>();
//        }
//
//        classNode.recordComponents.add(readRecordComponent(traverser.nextListTraverser()));
//      } else if (traverser.isNextList("permitted_subclass")) {
//        if (classNode.permittedSubclasses == null) {
//          classNode.permittedSubclasses = new ArrayList<>();
//        }
//
//        classNode.permittedSubclasses.add(readPermittedSubclass(traverser.nextListTraverser()));
      } else if (traverser.isNextList("synthetic")) {
        classNode.access |= readSynthetic(traverser.nextListTraverser());
      } else if (traverser.isNextList("deprecated")) {
        classNode.access |= readDeprecated(traverser.nextListTraverser());
      } else if (traverser.isNextList("signature")) {
        classNode.signature = readSignature(traverser.nextListTraverser());
      } else if (traverser.isNextList("runtime_visible_annotation")) {
        if (classNode.visibleAnnotations == null) {
          classNode.visibleAnnotations = new ArrayList<>();
        }

        classNode.visibleAnnotations.add(readAnnotation(traverser.nextListTraverser()));
      } else if (traverser.isNextList("runtime_invisible_annotation")) {
        if (classNode.invisibleAnnotations == null) {
          classNode.invisibleAnnotations = new ArrayList<>();
        }

        classNode.invisibleAnnotations.add(readAnnotation(traverser.nextListTraverser()));
      } else if (traverser.isNextList("runtime_visible_type_annotation")) {
        if (classNode.visibleTypeAnnotations == null) {
          classNode.visibleTypeAnnotations = new ArrayList<>();
        }

        classNode.visibleTypeAnnotations.add(readTypeAnnotation(traverser.nextListTraverser()));
      } else if (traverser.isNextList("runtime_invisible_type_annotation")) {
        if (classNode.invisibleTypeAnnotations == null) {
          classNode.invisibleTypeAnnotations = new ArrayList<>();
        }

        classNode.invisibleTypeAnnotations.add(readTypeAnnotation(traverser.nextListTraverser()));
      } else if (traverser.isNextList("unknown")) {
        if (classNode.attrs == null) {
          classNode.attrs = new ArrayList<>();
        }

        classNode.attrs.add(readUnknownAttribute(traverser.nextListTraverser(), false));
      } else {
        throw new IllegalArgumentException("Invalid S-Expression node");
      }
    }

    return classNode;
  }

  private FieldNode readField(SExprClassTraverser traverser) {
    traverser.skip();
    FieldNode fieldNode = new FieldNode(0, null, null, null, null);
    fieldNode.access = traverser.nextFlags("access_flag", AccessFlagTarget.FIELD);
    fieldNode.name = traverser.nextString("name");
    fieldNode.desc = traverser.nextString("descriptor");

    while (traverser.hasNext()) {
      if (traverser.isNextList("constant_value")) {
        fieldNode.value = readConstantValue(traverser.nextListTraverser());
      } else if (traverser.isNextList("synthetic")) {
        fieldNode.access |= readSynthetic(traverser.nextListTraverser());
      } else if (traverser.isNextList("deprecated")) {
        fieldNode.access |= readDeprecated(traverser.nextListTraverser());
      } else if (traverser.isNextList("signature")) {
        fieldNode.signature = readSignature(traverser.nextListTraverser());
      } else if (traverser.isNextList("runtime_visible_annotation")) {
        if (fieldNode.visibleAnnotations == null) {
          fieldNode.visibleAnnotations = new ArrayList<>();
        }

        fieldNode.visibleAnnotations.add(readAnnotation(traverser.nextListTraverser()));
      } else if (traverser.isNextList("runtime_invisible_annotation")) {
        if (fieldNode.invisibleAnnotations == null) {
          fieldNode.invisibleAnnotations = new ArrayList<>();
        }

        fieldNode.invisibleAnnotations.add(readAnnotation(traverser.nextListTraverser()));
      } else if (traverser.isNextList("runtime_visible_type_annotation")) {
        if (fieldNode.visibleTypeAnnotations == null) {
          fieldNode.visibleTypeAnnotations = new ArrayList<>();
        }

        fieldNode.visibleTypeAnnotations.add(readTypeAnnotation(traverser.nextListTraverser()));
      } else if (traverser.isNextList("runtime_invisible_type_annotation")) {
        if (fieldNode.invisibleTypeAnnotations == null) {
          fieldNode.invisibleTypeAnnotations = new ArrayList<>();
        }

        fieldNode.invisibleTypeAnnotations.add(readTypeAnnotation(traverser.nextListTraverser()));
      } else if (traverser.isNextList("unknown")) {
        if (fieldNode.attrs == null) {
          fieldNode.attrs = new ArrayList<>();
        }

        fieldNode.attrs.add(readUnknownAttribute(traverser.nextListTraverser(), false));
      } else {
        throw new IllegalArgumentException("Invalid S-Expression node");
      }
    }

    return fieldNode;
  }

  private MethodNode readMethod(SExprClassTraverser traverser) {
    traverser.skip();

    labelMap = new LabelMap();
    MethodNode methodNode = new MethodNode(0, null, null, null, null);
    methodNode.access = traverser.nextFlags("access_flag", AccessFlagTarget.METHOD);
    methodNode.name = traverser.nextString("name");
    methodNode.desc = traverser.nextString("descriptor");

    while (traverser.hasNext()) {
      if (traverser.isNextList("code")) {
        MethodNode code = readCode(traverser.nextListTraverser());
        methodNode.instructions = code.instructions;
        methodNode.tryCatchBlocks = code.tryCatchBlocks;
        methodNode.maxStack = code.maxStack;
        methodNode.maxLocals = code.maxLocals;
        methodNode.localVariables = code.localVariables;
        methodNode.visibleLocalVariableAnnotations = code.visibleLocalVariableAnnotations;
        methodNode.invisibleLocalVariableAnnotations = code.invisibleLocalVariableAnnotations;

        if (code.attrs != null) {
          if (methodNode.attrs == null) {
            methodNode.attrs = new ArrayList<>();
          }

          methodNode.attrs.addAll(code.attrs);
        }
      } else if (traverser.isNextList("exception")) {
        if (methodNode.exceptions == null) {
          methodNode.exceptions = new ArrayList<>();
        }

        methodNode.exceptions.add(readException(traverser.nextListTraverser()));
      } else if (traverser.isNextList("runtime_visible_parameter_annotation")) {
        if (methodNode.visibleParameterAnnotations == null) {
          methodNode.visibleParameterAnnotations = new List[0];
        }

        methodNode.visibleParameterAnnotations = ArrayHelper.add(methodNode.visibleParameterAnnotations, readParameterAnnotation(traverser.nextListTraverser()));
//        methodNode.visibleAnnotableParameterCount = methodNode.visibleParameterAnnotations.length;
      } else if (traverser.isNextList("runtime_invisible_parameter_annotation")) {
        if (methodNode.invisibleParameterAnnotations == null) {
          methodNode.invisibleParameterAnnotations = new List[0];
        }

        methodNode.invisibleParameterAnnotations = ArrayHelper.add(methodNode.invisibleParameterAnnotations, readParameterAnnotation(traverser.nextListTraverser()));
//        methodNode.invisibleAnnotableParameterCount = methodNode.invisibleParameterAnnotations.length;
      } else if (traverser.isNextList("annotation_default")) {
        methodNode.annotationDefault = readAnnotationDefault(traverser.nextListTraverser());
      } else if (traverser.isNextList("method_parameter")) {
        if (methodNode.parameters == null) {
          methodNode.parameters = new ArrayList<>();
        }

        methodNode.parameters.add(readMethodParameter(traverser.nextListTraverser()));
      } else if (traverser.isNextList("synthetic")) {
        methodNode.access |= readSynthetic(traverser.nextListTraverser());
      } else if (traverser.isNextList("deprecated")) {
        methodNode.access |= readDeprecated(traverser.nextListTraverser());
      } else if (traverser.isNextList("signature")) {
        methodNode.signature = readSignature(traverser.nextListTraverser());
      } else if (traverser.isNextList("runtime_visible_annotation")) {
        if (methodNode.visibleAnnotations == null) {
          methodNode.visibleAnnotations = new ArrayList<>();
        }

        methodNode.visibleAnnotations.add(readAnnotation(traverser.nextListTraverser()));
      } else if (traverser.isNextList("runtime_invisible_annotation")) {
        if (methodNode.invisibleAnnotations == null) {
          methodNode.invisibleAnnotations = new ArrayList<>();
        }

        methodNode.invisibleAnnotations.add(readAnnotation(traverser.nextListTraverser()));
      } else if (traverser.isNextList("runtime_visible_type_annotation")) {
        if (methodNode.visibleTypeAnnotations == null) {
          methodNode.visibleTypeAnnotations = new ArrayList<>();
        }

        methodNode.visibleTypeAnnotations.add(readTypeAnnotation(traverser.nextListTraverser()));
      } else if (traverser.isNextList("runtime_invisible_type_annotation")) {
        if (methodNode.invisibleTypeAnnotations == null) {
          methodNode.invisibleTypeAnnotations = new ArrayList<>();
        }

        methodNode.invisibleTypeAnnotations.add(readTypeAnnotation(traverser.nextListTraverser()));
      } else if (traverser.isNextList("unknown")) {
        if (methodNode.attrs == null) {
          methodNode.attrs = new ArrayList<>();
        }

        methodNode.attrs.add(readUnknownAttribute(traverser.nextListTraverser(), false));
      } else {
        throw new IllegalArgumentException("Invalid S-Expression node");
      }
    }


    return methodNode;
  }

  /*
   * Class Attributes
   */

  private String readSourceFile(SExprClassTraverser traverser) {
    traverser.skip();
    return traverser.nextString();
  }

  private InnerClassNode readInnerClass(SExprClassTraverser traverser) {
    traverser.skip();
    InnerClassNode innerClassNode = new InnerClassNode(null, null, null, 0);
    innerClassNode.name = traverser.nextConstantClass("inner_class");
    innerClassNode.outerName = traverser.nextConstantClassOptional("outer_class");
    innerClassNode.innerName = traverser.nextConstantClassOptional("inner_name");
    innerClassNode.access = traverser.nextFlags("access_flag", AccessFlagTarget.INNER_CLASS);
    return innerClassNode;
  }

  private Triplet<String, String, String> readEnclosingMethod(SExprClassTraverser traverser) {
    traverser.skip();
    String clazz = traverser.nextConstantClass("class");
    Pair<String, String> nameAndType = traverser.nextConstantNameAndTypeOptional("method");
    return Triplet.of(clazz, nameAndType.first, nameAndType.second);
  }

  private String readSourceDebugExtension(SExprClassTraverser traverser) {
    traverser.skip();
    return traverser.nextString();
  }

//  private ModuleNode readModule(SExprClassTraverser traverser) {
//    traverser.skip();
//    ModuleNode moduleNode = new ModuleNode(OpcodesCompat.ASM_LATEST, null, 0, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
//    moduleNode.name = traverser.nextConstantModule("name");
//    moduleNode.access = traverser.nextFlags("flag", AccessFlagTarget.MODULE);
//    moduleNode.version = traverser.nextStringOptional("version");
//
//    while (traverser.hasNext()) {
//      if (traverser.isNextList("requires")) {
//        moduleNode.requires.add(readRequires(traverser.nextListTraverser()));
//      } else if (traverser.isNextList("exports")) {
//        moduleNode.exports.add(readExports(traverser.nextListTraverser()));
//      } else if (traverser.isNextList("opens")) {
//        moduleNode.opens.add(readOpens(traverser.nextListTraverser()));
//      } else if (traverser.isNextList("uses")) {
//        moduleNode.uses.addAll(traverser.nextConstantClasses("uses"));
//      } else if (traverser.isNextList("provides")) {
//        moduleNode.provides.add(readProvides(traverser.nextListTraverser()));
//      } else {
//        throw new IllegalArgumentException("Invalid S-Expression node");
//      }
//    }
//
//    return moduleNode;
//  }
//
//  private ModuleRequireNode readRequires(SExprClassTraverser traverser) {
//    traverser.skip();
//    ModuleRequireNode moduleRequireNode = new ModuleRequireNode(null, 0, null);
//    moduleRequireNode.module = traverser.nextConstantModule("requires");
//    moduleRequireNode.access = traverser.nextFlags("flag", AccessFlagTarget.MODULE_REQUIRES);
//    moduleRequireNode.version = traverser.nextStringOptional("version");
//    return moduleRequireNode;
//  }
//
//  private ModuleExportNode readExports(SExprClassTraverser traverser) {
//    traverser.skip();
//    ModuleExportNode moduleExportNode = new ModuleExportNode(null, 0, new ArrayList<>());
//    moduleExportNode.packaze = traverser.nextConstantPackage("exports");
//    moduleExportNode.access = traverser.nextFlags("flag", AccessFlagTarget.MODULE_EXPORTS);
//    moduleExportNode.modules = traverser.nextConstantModules("to");
//    return moduleExportNode;
//  }
//
//  private ModuleOpenNode readOpens(SExprClassTraverser traverser) {
//    traverser.skip();
//    ModuleOpenNode moduleOpenNode = new ModuleOpenNode(null, 0, new ArrayList<>());
//    moduleOpenNode.packaze = traverser.nextConstantPackage("exports");
//    moduleOpenNode.access = traverser.nextFlags("flag", AccessFlagTarget.MODULE_OPENS);
//    moduleOpenNode.modules = traverser.nextConstantModules("to");
//    return moduleOpenNode;
//  }
//
//  private ModuleProvideNode readProvides(SExprClassTraverser traverser) {
//    traverser.skip();
//    ModuleProvideNode moduleProvideNode = new ModuleProvideNode(null, new ArrayList<>());
//    moduleProvideNode.service = traverser.nextConstantClass("provides");
//    moduleProvideNode.providers = traverser.nextConstantClasses("with");
//    return moduleProvideNode;
//  }

  private String readModulePackage(SExprClassTraverser traverser) {
    traverser.skip();
    return traverser.nextConstantPackage();
  }

  private String readModuleMainClass(SExprClassTraverser traverser) {
    traverser.skip();
    return traverser.nextConstantClass();
  }

  private String readNestHost(SExprClassTraverser traverser) {
    traverser.skip();
    return traverser.nextConstantClass();
  }

  private String readNestMember(SExprClassTraverser traverser) {
    traverser.skip();
    return traverser.nextConstantClass();
  }

//  private RecordComponentNode readRecordComponent(SExprClassTraverser traverser) {
//    traverser.skip();
//    RecordComponentNode recordComponentNode = new RecordComponentNode(null, null, null);
//    recordComponentNode.name = traverser.nextString("name");
//    recordComponentNode.descriptor = traverser.nextString("descriptor");
//
//    while (traverser.hasNext()) {
//      if (traverser.isNextList("signature")) {
//        recordComponentNode.signature = readSignature(traverser.nextListTraverser());
//      } else if (traverser.isNextList("runtime_visible_annotation")) {
//        if (recordComponentNode.visibleAnnotations == null) {
//          recordComponentNode.visibleAnnotations = new ArrayList<>();
//        }
//
//        recordComponentNode.visibleAnnotations.add(readAnnotation(traverser.nextListTraverser()));
//      } else if (traverser.isNextList("runtime_invisible_annotation")) {
//        if (recordComponentNode.invisibleAnnotations == null) {
//          recordComponentNode.invisibleAnnotations = new ArrayList<>();
//        }
//
//        recordComponentNode.invisibleAnnotations.add(readAnnotation(traverser.nextListTraverser()));
//      } else if (traverser.isNextList("runtime_visible_type_annotation")) {
//        if (recordComponentNode.visibleTypeAnnotations == null) {
//          recordComponentNode.visibleTypeAnnotations = new ArrayList<>();
//        }
//
//        recordComponentNode.visibleTypeAnnotations.add(readTypeAnnotation(traverser.nextListTraverser()));
//      } else if (traverser.isNextList("runtime_invisible_type_annotation")) {
//        if (recordComponentNode.invisibleTypeAnnotations == null) {
//          recordComponentNode.invisibleTypeAnnotations = new ArrayList<>();
//        }
//
//        recordComponentNode.invisibleTypeAnnotations.add(readTypeAnnotation(traverser.nextListTraverser()));
//      } else if (traverser.isNextList("unknown")) {
//        if (recordComponentNode.attrs == null) {
//          recordComponentNode.attrs = new ArrayList<>();
//        }
//
//        recordComponentNode.attrs.add(readUnknownAttribute(traverser.nextListTraverser(), false));
//      } else {
//        throw new IllegalArgumentException("Invalid S-Expression node");
//      }
//    }
//
//    return recordComponentNode;
//  }

  private String readPermittedSubclass(SExprClassTraverser traverser) {
    traverser.skip();
    return traverser.nextConstantClass();
  }

  /*
   * Field Attributes
   */

  private Object readConstantValue(SExprClassTraverser traverser) {
    traverser.skip();
    return traverser.nextLoadableConstant(null);
  }

  /*
   * Method Attributes
   */

  private MethodNode readCode(SExprClassTraverser traverser) {
    traverser.skip();
    MethodNode methodNode = new MethodNode(0, null, null, null, null);
    int offset = 0;
    List<Pair<TypeReferenceExtended, TypeAnnotationNode>> visibleTypeAnnotations = new ArrayList<>();
    List<Pair<TypeReferenceExtended, TypeAnnotationNode>> invisibleTypeAnnotations = new ArrayList<>();
    List<LineNumberNode> lineNumbers = new ArrayList<>();
    List<String> localVariableTypes = new ArrayList<>();
    List<Pair<LabelNode, FrameNode>> frames = new ArrayList<>();

    while (traverser.hasNext()) {
      if (traverser.isNextList("max_stack")) {
        methodNode.maxStack = (int) traverser.nextInteger("max_stack");
      } else if (traverser.isNextList("max_locals")) {
        methodNode.maxLocals = (int) traverser.nextInteger("max_locals");
      } else if (traverser.isNextList("exception")) {
        LabelNode start = traverser.nextLabel("start", labelMap);
        LabelNode end = traverser.nextLabel("end", labelMap);
        LabelNode handler = traverser.nextLabel("handler", labelMap);
        String catchType = traverser.nextConstantClassOptional("catch_type");
        methodNode.tryCatchBlocks.add(new TryCatchBlockNode(start, end, handler, catchType));
      } else if (traverser.isNextList("line_number")) {
        LabelNode start = traverser.nextLabel("start", labelMap);
        long lineNumber = traverser.nextInteger("line_number");
        lineNumbers.add(new LineNumberNode((int) lineNumber, start));
      } else if (traverser.isNextList("local_variable")) {
        LabelNode start = traverser.nextLabel("start", labelMap);
        LabelNode end = traverser.nextLabel("end", labelMap);
        String name = traverser.nextString("name");
        String descriptor = traverser.nextString("descriptor");
        long index = traverser.nextInteger("index");
        methodNode.localVariables.add(new LocalVariableNode(name, descriptor, null, start, end, (int) index));
      } else if (traverser.isNextList("local_variable_type")) {
        traverser.nextLabel("start", labelMap);
        traverser.nextLabel("end", labelMap);
        traverser.nextString("name");
        String signature = traverser.nextString("signature");
        long index = traverser.nextInteger("index");

        if (index < localVariableTypes.size()) {
          ListHelper.resize(localVariableTypes, (int) index + 1);
        }

        localVariableTypes.set((int) index, signature);
      } else if (traverser.isNextList("stack_map_frame")) {
        frames.add(readStackMapFrame(traverser.nextListTraverser()));
      } else if (traverser.isNextList("runtime_visible_type_annotations")) {
        Pair<TypeReferenceExtended, TypeAnnotationNode> typeAnnotation = readTypeAnnotationExtended(traverser);

        if (typeAnnotation.first.getSort() == TypeReference.LOCAL_VARIABLE || typeAnnotation.first.getSort() == TypeReference.RESOURCE_VARIABLE) {
          methodNode.visibleLocalVariableAnnotations.add((LocalVariableAnnotationNode) typeAnnotation.second);
        } else {
          visibleTypeAnnotations.add(readTypeAnnotationExtended(traverser));
        }
      } else if (traverser.isNextList("runtime_invisible_type_annotations")) {
        invisibleTypeAnnotations.add(readTypeAnnotationExtended(traverser));

        Pair<TypeReferenceExtended, TypeAnnotationNode> typeAnnotation = readTypeAnnotationExtended(traverser);

        if (typeAnnotation.first.getSort() == TypeReference.LOCAL_VARIABLE || typeAnnotation.first.getSort() == TypeReference.RESOURCE_VARIABLE) {
          methodNode.invisibleLocalVariableAnnotations.add((LocalVariableAnnotationNode) typeAnnotation.second);
        } else {
          invisibleTypeAnnotations.add(readTypeAnnotationExtended(traverser));
        }
      } else if (traverser.isNextList("unknown")) {
        if (methodNode.attrs == null) {
          methodNode.attrs = new ArrayList<>();
        }

        methodNode.attrs.add(readUnknownAttribute(traverser.nextListTraverser(), true));
      } else {
        String insn = traverser.nextSymbol();
        int opcode = -1;

        if (!insn.equals("label")) {
          opcode = OpcodeMap.toInt(insn.toUpperCase());
        }

        switch (opcode) {
          case Opcodes.TABLESWITCH: {
            // TableSwitchInsn
            int min = (int) traverser.nextInteger(null);
            int max = (int) traverser.nextInteger(null);
            LabelNode dflt = traverser.nextLabel(null, labelMap);
            List<LabelNode> labels = new ArrayList<>();

            for (int i = min; i <= max; i++) {
              labels.add(traverser.nextLabel(null, labelMap));
            }

            methodNode.instructions.add(new TableSwitchInsnNode(min, max, dflt, labels.toArray(new LabelNode[0])));
            break;
          }
          case Opcodes.GETSTATIC:
          case Opcodes.PUTSTATIC:
          case Opcodes.GETFIELD:
          case Opcodes.PUTFIELD:
            // FieldInsn
            Triplet<String, String, String> fieldref = traverser.nextConstantFieldref(null);
            methodNode.instructions.add(new FieldInsnNode(opcode, fieldref.first, fieldref.second, fieldref.third));
            break;
          case Opcodes.IINC:
            // IincInsn
            methodNode.instructions.add(new IincInsnNode((int) traverser.nextInteger(null), (int) traverser.nextInteger(null)));
            break;
          case Opcodes.NOP:
          case Opcodes.ACONST_NULL:
          case Opcodes.ICONST_M1:
          case Opcodes.ICONST_0:
          case Opcodes.ICONST_1:
          case Opcodes.ICONST_2:
          case Opcodes.ICONST_3:
          case Opcodes.ICONST_4:
          case Opcodes.ICONST_5:
          case Opcodes.LCONST_0:
          case Opcodes.LCONST_1:
          case Opcodes.FCONST_0:
          case Opcodes.FCONST_1:
          case Opcodes.FCONST_2:
          case Opcodes.DCONST_0:
          case Opcodes.DCONST_1:
          case Opcodes.IALOAD:
          case Opcodes.LALOAD:
          case Opcodes.FALOAD:
          case Opcodes.DALOAD:
          case Opcodes.AALOAD:
          case Opcodes.BALOAD:
          case Opcodes.CALOAD:
          case Opcodes.SALOAD:
          case Opcodes.IASTORE:
          case Opcodes.LASTORE:
          case Opcodes.FASTORE:
          case Opcodes.DASTORE:
          case Opcodes.AASTORE:
          case Opcodes.BASTORE:
          case Opcodes.CASTORE:
          case Opcodes.SASTORE:
          case Opcodes.POP:
          case Opcodes.POP2:
          case Opcodes.DUP:
          case Opcodes.DUP_X1:
          case Opcodes.DUP_X2:
          case Opcodes.DUP2:
          case Opcodes.DUP2_X1:
          case Opcodes.DUP2_X2:
          case Opcodes.SWAP:
          case Opcodes.IADD:
          case Opcodes.LADD:
          case Opcodes.FADD:
          case Opcodes.DADD:
          case Opcodes.ISUB:
          case Opcodes.LSUB:
          case Opcodes.FSUB:
          case Opcodes.DSUB:
          case Opcodes.IMUL:
          case Opcodes.LMUL:
          case Opcodes.FMUL:
          case Opcodes.DMUL:
          case Opcodes.IDIV:
          case Opcodes.LDIV:
          case Opcodes.FDIV:
          case Opcodes.DDIV:
          case Opcodes.IREM:
          case Opcodes.LREM:
          case Opcodes.FREM:
          case Opcodes.DREM:
          case Opcodes.INEG:
          case Opcodes.LNEG:
          case Opcodes.FNEG:
          case Opcodes.DNEG:
          case Opcodes.ISHL:
          case Opcodes.LSHL:
          case Opcodes.ISHR:
          case Opcodes.LSHR:
          case Opcodes.IUSHR:
          case Opcodes.LUSHR:
          case Opcodes.IAND:
          case Opcodes.LAND:
          case Opcodes.IOR:
          case Opcodes.LOR:
          case Opcodes.IXOR:
          case Opcodes.LXOR:
          case Opcodes.I2L:
          case Opcodes.I2F:
          case Opcodes.I2D:
          case Opcodes.L2I:
          case Opcodes.L2F:
          case Opcodes.L2D:
          case Opcodes.F2I:
          case Opcodes.F2L:
          case Opcodes.F2D:
          case Opcodes.D2I:
          case Opcodes.D2L:
          case Opcodes.D2F:
          case Opcodes.I2B:
          case Opcodes.I2C:
          case Opcodes.I2S:
          case Opcodes.LCMP:
          case Opcodes.FCMPL:
          case Opcodes.FCMPG:
          case Opcodes.DCMPL:
          case Opcodes.DCMPG:
          case Opcodes.IRETURN:
          case Opcodes.LRETURN:
          case Opcodes.FRETURN:
          case Opcodes.DRETURN:
          case Opcodes.ARETURN:
          case Opcodes.RETURN:
          case Opcodes.ARRAYLENGTH:
          case Opcodes.ATHROW:
          case Opcodes.MONITORENTER:
          case Opcodes.MONITOREXIT:
            // Insn
            methodNode.instructions.add(new InsnNode(opcode));
            break;
          case Opcodes.BIPUSH:
          case Opcodes.SIPUSH:
          case Opcodes.NEWARRAY:
            // IntInsn
            // FIXME: Array type
            methodNode.instructions.add(new IntInsnNode(opcode, (int) traverser.nextInteger(null)));
            break;
          case Opcodes.INVOKEDYNAMIC:
            // InvokeDynamicInsn
            Quartet<Handle, Object[], String, String> invokeDynamic = traverser.nextConstantInvokeDynamic(null);
            methodNode.instructions.add(new InvokeDynamicInsnNode(invokeDynamic.third, invokeDynamic.fourth, invokeDynamic.first, invokeDynamic.second));
            break;
          case Opcodes.IFEQ:
          case Opcodes.IFNE:
          case Opcodes.IFLT:
          case Opcodes.IFGE:
          case Opcodes.IFGT:
          case Opcodes.IFLE:
          case Opcodes.IF_ICMPEQ:
          case Opcodes.IF_ICMPNE:
          case Opcodes.IF_ICMPLT:
          case Opcodes.IF_ICMPGE:
          case Opcodes.IF_ICMPGT:
          case Opcodes.IF_ICMPLE:
          case Opcodes.IF_ACMPEQ:
          case Opcodes.IF_ACMPNE:
          case Opcodes.GOTO:
          case Opcodes.JSR:
          case Opcodes.IFNULL:
          case Opcodes.IFNONNULL:
            // JumpInsn
            methodNode.instructions.add(new JumpInsnNode(opcode, traverser.nextLabel(null, labelMap)));
            break;
          case Opcodes.LDC:
            // LdcInsn
            methodNode.instructions.add(new LdcInsnNode(traverser.nextLoadableConstant(null)));
            break;
          case Opcodes.LOOKUPSWITCH: {
            // LookupSwitchInsn
            LabelNode dflt = traverser.nextLabel(null, labelMap);
            List<Integer> keys = new ArrayList<>();
            List<LabelNode> labels = new ArrayList<>();

            while (traverser.isNextInteger()) {
              keys.add((int) traverser.nextInteger(null));
              labels.add(traverser.nextLabel(null, labelMap));
            }

            methodNode.instructions.add(new LookupSwitchInsnNode(dflt, ListHelper.toIntArray(keys), labels.toArray(new LabelNode[0])));
            break;
          }
          case Opcodes.INVOKEVIRTUAL:
          case Opcodes.INVOKESPECIAL:
          case Opcodes.INVOKESTATIC:
          case Opcodes.INVOKEINTERFACE:
            // MethodInsn
            Quartet<String, String, String, Boolean> methodref = traverser.nextConstantMethodref(null);
            methodNode.instructions.add(new MethodInsnNode(opcode, methodref.first, methodref.second, methodref.third, methodref.fourth));
            break;
          case Opcodes.MULTIANEWARRAY:
          case Opcodes.NEW:
          case Opcodes.ANEWARRAY:
          case Opcodes.CHECKCAST:
          case Opcodes.INSTANCEOF:
            // TypeInsn
            methodNode.instructions.add(new TypeInsnNode(opcode, traverser.nextConstantClass(null)));
            break;
          case Opcodes.ILOAD:
          case Opcodes.LLOAD:
          case Opcodes.FLOAD:
          case Opcodes.DLOAD:
          case Opcodes.ALOAD:
          case Opcodes.ISTORE:
          case Opcodes.LSTORE:
          case Opcodes.FSTORE:
          case Opcodes.DSTORE:
          case Opcodes.ASTORE:
          case Opcodes.RET:
            // VarInsn
            methodNode.instructions.add(new VarInsnNode(opcode, (int) traverser.nextInteger(null)));
            break;
          case -1:
            String symbol = traverser.nextLabelSymbol(null);
            labelMap.resolveSymbolLabel(symbol);
            methodNode.instructions.add(labelMap.getSymbolLabel(symbol));
            break;
          default:
            throw new IllegalArgumentException("Invalid S-Expression node");
        }

        if (opcode != -1) {
          offset++;
        }
      }
    }

    // Insert offset labels
    offset = 0;
    for (int i = 0; i < methodNode.instructions.size(); i++) {
      if (!(methodNode.instructions.get(i) instanceof LabelNode)) {
        LabelNode offsetLabel = labelMap.getUnresolvedOffsetLabel(offset);

        if (offsetLabel != null) {
          methodNode.instructions.insertBefore(methodNode.instructions.get(i), offsetLabel);
          labelMap.resolveOffsetLabel(offset);
          i++;
        }

        offset++;
      }
    }

    // Restore line number nodes
    for (LineNumberNode ln : lineNumbers) {
      InsnListHelper.insert(methodNode.instructions, ln.start, ln);
    }

    // Restore local variable types
    for (int i = 0; i < localVariableTypes.size(); i++) {
      methodNode.localVariables.get(i).signature = localVariableTypes.get(i);
    }

    // Restore frames
    for (Pair<LabelNode, FrameNode> f : frames) {
      InsnListHelper.insert(methodNode.instructions, f.first, f.second);
    }

    // Restore type annotations
    for (Pair<TypeReferenceExtended, TypeAnnotationNode> an : visibleTypeAnnotations) {
      if (an.first.getSort() == TypeReference.EXCEPTION_PARAMETER) {
        if (!(an.first.getExceptionIndex() < methodNode.tryCatchBlocks.size())) {
          throw new IllegalArgumentException("Invalid S-Expression node");
        }

        TryCatchBlockNode tryCatchBlockNode = methodNode.tryCatchBlocks.get(an.first.getExceptionIndex());

        if (tryCatchBlockNode.visibleTypeAnnotations == null) {
          tryCatchBlockNode.visibleTypeAnnotations = new ArrayList<>();
        }

        tryCatchBlockNode.visibleTypeAnnotations.add(an.second);
      } else {
        AbstractInsnNode target = InsnListHelper.getNextReal(methodNode.instructions, an.first.getOffset());

        if (target != null) {
          if (target.visibleTypeAnnotations == null) {
            target.visibleTypeAnnotations = new ArrayList<>();
          }

          target.visibleTypeAnnotations.add(an.second);
        }
      }
    }

    for (Pair<TypeReferenceExtended, TypeAnnotationNode> an : invisibleTypeAnnotations) {
      if (an.first.getSort() == TypeReference.EXCEPTION_PARAMETER) {
        if (!(an.first.getExceptionIndex() < methodNode.tryCatchBlocks.size())) {
          throw new IllegalArgumentException("Invalid S-Expression node");
        }

        TryCatchBlockNode tryCatchBlockNode = methodNode.tryCatchBlocks.get(an.first.getExceptionIndex());

        if (tryCatchBlockNode.invisibleTypeAnnotations == null) {
          tryCatchBlockNode.invisibleTypeAnnotations = new ArrayList<>();
        }

        tryCatchBlockNode.invisibleTypeAnnotations.add(an.second);
      } else {
        AbstractInsnNode target = InsnListHelper.getNextReal(methodNode.instructions, an.first.getOffset());

        if (target != null) {
          if (target.invisibleTypeAnnotations == null) {
            target.invisibleTypeAnnotations = new ArrayList<>();
          }

          target.invisibleTypeAnnotations.add(an.second);
        }
      }
    }

    return methodNode;
  }

  private Pair<LabelNode, FrameNode> readStackMapFrame(SExprClassTraverser traverser) {
    traverser.skip();
    int type = 0;
    LabelNode offset = null;
    int numLocal = 0;
    List<Object> local = new ArrayList<>();
    List<Object> stack = new ArrayList<>();

    if (traverser.isNextList("full") || traverser.isNextList("new")) {
      SExprClassTraverser child = traverser.nextListTraverser();
      type = traverser.isNextList("full") ? Opcodes.F_FULL : Opcodes.F_NEW;
      child.skip();
      offset = child.nextLabel("offset", labelMap);
      local = readFrameItems(child, "local");
      numLocal = local.size();
      stack = readFrameItems(child, "stack");
      child.expectEnd();
    } else if (traverser.isNextList("append")) {
      SExprClassTraverser child = traverser.nextListTraverser();
      child.skip();
      type = Opcodes.F_APPEND;
      offset = child.nextLabel("offset", labelMap);
      local = readFrameItems(child, "local");
      numLocal = local.size();
      child.expectEnd();
    } else if (traverser.isNextList("chop")) {
      SExprClassTraverser child = traverser.nextListTraverser();
      child.skip();
      type = Opcodes.F_CHOP;
      offset = child.nextLabel("offset", labelMap);
      numLocal = (int) traverser.nextInteger("local");
      child.expectEnd();
    } else if (traverser.isNextList("same")) {
      SExprClassTraverser child = traverser.nextListTraverser();
      child.skip();
      type = Opcodes.F_SAME;
      offset = child.nextLabel("offset", labelMap);
      child.expectEnd();
    } else if (traverser.isNextList("same_locals_1_stack_item")) {
      SExprClassTraverser child = traverser.nextListTraverser();
      child.skip();
      type = Opcodes.F_SAME1;
      offset = child.nextLabel("offset", labelMap);
      stack = ArrayListHelper.of(readFrameItem(child, "stack"));
      child.expectEnd();
    } else {
      throw new IllegalArgumentException("Invalid S-Expression node");
    }

    return Pair.of(offset, new FrameNode(type, numLocal, local.toArray(new Object[0]), stack.size(), stack.toArray(new Object[0])));
  }

  private List<Object> readFrameItems(SExprClassTraverser traverser, String name) {
    List<Object> result = new ArrayList<>();

    while (traverser.isNextList(name)) {
      SExprClassTraverser child = traverser.nextListTraverser();
      child.skip();

      while (child.hasNext()) {
        result.add(readFrameItem(child, null));
      }
    }

    return result;
  }

  private Object readFrameItem(SExprClassTraverser traverser, String name) {
    if (name != null && traverser.isNextList(name)) {
      SExprClassTraverser next = traverser.nextListTraverser();
      next.skip();
      Object value = readFrameItem(next, null);
      next.expectEnd();
      return value;
    } else {
      if (traverser.isNextList("top")) {
        traverser.skip();
        traverser.expectEnd();
        return Opcodes.TOP;
      } else if (traverser.isNextList("integer")) {
        traverser.skip();
        traverser.expectEnd();
        return Opcodes.INTEGER;
      } else if (traverser.isNextList("float")) {
        traverser.skip();
        traverser.expectEnd();
        return Opcodes.FLOAT;
      } else if (traverser.isNextList("double")) {
        traverser.skip();
        traverser.expectEnd();
        return Opcodes.DOUBLE;
      } else if (traverser.isNextList("long")) {
        traverser.skip();
        traverser.expectEnd();
        return Opcodes.LONG;
      } else if (traverser.isNextList("null")) {
        traverser.skip();
        traverser.expectEnd();
        return Opcodes.NULL;
      } else if (traverser.isNextList("uninitialized_this")) {
        traverser.skip();
        traverser.expectEnd();
        return Opcodes.UNINITIALIZED_THIS;
      } else if (traverser.isNextList("uninitialized")) {
        traverser.skip();
        LabelNode offset = traverser.nextLabel(null, labelMap);
        traverser.expectEnd();
        return offset;
      } else if (traverser.isNextList("object")) {
        traverser.skip();
        String clazz = traverser.nextConstantClass(null);
        traverser.expectEnd();
        return clazz;
      }
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  private String readException(SExprClassTraverser traverser) {
    traverser.skip();
    return traverser.nextConstantClass();
  }

  private List<AnnotationNode> readParameterAnnotation(SExprClassTraverser traverser) {
    traverser.skip();
    List<AnnotationNode> annotationNodes = new ArrayList<>();

    while (traverser.hasNext()) {
      if (traverser.isNextList("parameter_annotation")) {
        annotationNodes.add(readAnnotation(traverser.nextListTraverser()));
      } else {
        throw new IllegalArgumentException("Invalid S-Expression node");
      }
    }


    return annotationNodes;
  }

  private Object readAnnotationDefault(SExprClassTraverser traverser) {
    return readAnnotation(traverser);
  }

  private ParameterNode readMethodParameter(SExprClassTraverser traverser) {
    traverser.skip();
    ParameterNode parameterNode = new ParameterNode(null, 0);
    parameterNode.name = traverser.nextString("name");
    parameterNode.access = traverser.nextFlags("access_flag", AccessFlagTarget.METHOD_PARAMETER);
    return parameterNode;
  }

  /*
   * Common Attributes
   */

  private int readSynthetic(SExprClassTraverser traverser) {
    return Opcodes.ACC_SYNTHETIC;
  }

  private int readDeprecated(SExprClassTraverser traverser) {
    return Opcodes.ACC_DEPRECATED;
  }

  private String readSignature(SExprClassTraverser traverser) {
    traverser.skip();
    return traverser.nextString();
  }

  private Object readElementValue(SExprClassTraverser traverser, String name) {
    if (name != null) {
      if (traverser.isNextList(name)) {
        SExprClassTraverser child = traverser.nextListTraverser();
        child.skip();
        Object value = readElementValue(child, null);
        child.expectEnd();
        return value;
      }
    } else {
      if (traverser.isNextList("byte")) {
        return (byte) traverser.nextConstantInteger("byte");
      } else if (traverser.isNextList("char")) {
        return (char) traverser.nextConstantInteger("char");
      } else if (traverser.isNextList("double")) {
        return traverser.nextConstantDouble("double");
      } else if (traverser.isNextList("float")) {
        return traverser.nextConstantFloat("float");
      } else if (traverser.isNextList("int")) {
        return traverser.nextConstantInteger("int");
      } else if (traverser.isNextList("long")) {
        return traverser.nextConstantLong("long");
      } else if (traverser.isNextList("short")) {
        return (short) traverser.nextConstantInteger("short");
      } else if (traverser.isNextList("boolean")) {
        return BooleanHelper.fromInt(traverser.nextConstantInteger("boolean"));
      } else if (traverser.isNextList("string")) {
        return traverser.nextString("string");
      } else if (traverser.isNextList("class")) {
        return Type.getType(traverser.nextConstantClass("class"));
      } else if (traverser.isNextList("enum")) {
        SExprClassTraverser child = traverser.nextListTraverser();
        child.skip();
        String typeName = child.nextString("type_name");
        String constName = child.nextString("const_name");
        child.expectEnd();
        return new String[]{typeName, constName};
      } else if (traverser.isNextList("annotation")) {
        return readAnnotation(traverser.nextListTraverser());
      } else if (traverser.isNextList("array")) {
        SExprClassTraverser child = traverser.nextListTraverser();
        child.skip();
        List<Object> values = new ArrayList<>();

        while (child.hasNext()) {
          values.add(readElementValue(child, null));
        }

        return values;
      }
    }

    throw new IllegalArgumentException("Invalid S-Expression node");
  }

  private Pair<String, Object> readElementValuePair(SExprClassTraverser traverser) {
    String first = null;
    Object second = null;
    traverser.skip();
    first = traverser.nextString("element_name");
    second = readElementValue(traverser, "value");
    return Pair.of(first, second);
  }

  private AnnotationNode readAnnotation(SExprClassTraverser traverser) {
    AnnotationNode annotationNode = new AnnotationNode(null);
    traverser.skip();
    annotationNode.desc = traverser.nextString("type");
    annotationNode.values = new ArrayList<>();

    while (traverser.hasNext()) {
      Pair<String, Object> value = readElementValuePair(traverser.nextListTraverser());
      annotationNode.values.add(value.first);
      annotationNode.values.add(value.second);
    }

    return annotationNode;
  }

  private TypeReferenceExtended readTarget(SExprClassTraverser traverser, String name) {
    if (name != null) {
      if (traverser.isNextList(name)) {
        SExprClassTraverser child = traverser.nextListTraverser();
        child.skip();
        TypeReferenceExtended typeReference = readTarget(child, null);
        child.expectEnd();
        return typeReference;
      } else {
        throw new IllegalArgumentException("Invalid S-Expression node");
      }
    }

    LabelNode offset = null;
    LabelNode[] start = new LabelNode[0];
    LabelNode[] end = new LabelNode[0];
    int[] index = new int[0];
    int lastSort = -1;
    boolean first = true;

    while (traverser.hasNext()) {
      SExprClassTraverser child = traverser.nextListTraverser();
      int sort = TargetTypeMap.toInt(child.nextSymbol().toUpperCase());

      if (first) {
        lastSort = sort;
        first = false;
      } else if (lastSort != sort) {
        throw new IllegalArgumentException("Invalid S-Expression node");
      }

      switch (sort) {
        case TypeReference.CLASS_TYPE_PARAMETER:
        case TypeReference.METHOD_TYPE_PARAMETER: {
          // type_parameter_target
          long typeParameterIndex = child.nextInteger("type_parameter_index");
          child.expectEnd();
          traverser.expectEnd();
          return new TypeReferenceExtended(TypeReference.newTypeParameterReference(sort, (int) typeParameterIndex).getValue());
        }
        case TypeReference.CLASS_EXTENDS: {
          // supertype_target
          long supertypeIndex = child.nextInteger("supertype_index");
          child.expectEnd();
          traverser.expectEnd();
          return new TypeReferenceExtended(TypeReference.newSuperTypeReference((int) supertypeIndex).getValue());
        }
        case TypeReference.CLASS_TYPE_PARAMETER_BOUND:
        case TypeReference.METHOD_TYPE_PARAMETER_BOUND: {
          // type_parameter_bound_target
          long typeParameterIndex = child.nextInteger("type_parameter_index");
          long boundIndex = child.nextInteger("bound_index");
          child.expectEnd();
          traverser.expectEnd();
          return new TypeReferenceExtended(TypeReference.newTypeParameterBoundReference(sort, (int) typeParameterIndex, (int) boundIndex).getValue());
        }
        case TypeReference.FIELD:
        case TypeReference.METHOD_RETURN:
        case TypeReference.METHOD_RECEIVER: {
          // empty_target
          child.expectEnd();
          traverser.expectEnd();
          return new TypeReferenceExtended(TypeReference.newTypeReference(sort).getValue());
        }
        case TypeReference.METHOD_FORMAL_PARAMETER: {
          // formal_parameter_target
          long formalParameterIndex = child.nextInteger("formal_parameter_index");
          child.expectEnd();
          traverser.expectEnd();
          return new TypeReferenceExtended(TypeReference.newFormalParameterReference((int) formalParameterIndex).getValue());
        }
        case TypeReference.THROWS: {
          // throws_target
          long throwsTypeIndex = child.nextInteger("throws_type_index");
          child.expectEnd();
          traverser.expectEnd();
          return new TypeReferenceExtended(TypeReference.newExceptionReference((int) throwsTypeIndex).getValue());
        }
        case TypeReference.LOCAL_VARIABLE:
        case TypeReference.RESOURCE_VARIABLE: {
          // localvar_target
          start = ArrayHelper.add(start, child.nextLabel("start", labelMap));
          end = ArrayHelper.add(end, child.nextLabel("end", labelMap));
          index = ArrayHelper.add(index, (int) child.nextInteger("index"));
          child.expectEnd();
          break;
        }
        case TypeReference.EXCEPTION_PARAMETER: {
          // catch_target
          long exceptionTableIndex = child.nextInteger("exception_table_index");
          child.expectEnd();
          traverser.expectEnd();
          return new TypeReferenceExtended(TypeReference.newExceptionReference((int) exceptionTableIndex).getValue());
        }
        case TypeReference.INSTANCEOF:
        case TypeReference.NEW:
        case TypeReference.CONSTRUCTOR_REFERENCE:
        case TypeReference.METHOD_REFERENCE: {
          // offset_target
          offset = child.nextLabel("offset", labelMap);
          child.expectEnd();
          traverser.expectEnd();
          return new TypeReferenceExtended(TypeReference.newTypeReference(sort).getValue(), offset, null, null, null);
        }
        case TypeReference.CAST:
        case TypeReference.CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT:
        case TypeReference.METHOD_INVOCATION_TYPE_ARGUMENT:
        case TypeReference.CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT:
        case TypeReference.METHOD_REFERENCE_TYPE_ARGUMENT: {
          // type_argument_target
          long typeArgumentIndex = child.nextInteger("type_argument_index");
          child.expectEnd();
          traverser.expectEnd();
          return new TypeReferenceExtended(TypeReference.newTypeArgumentReference(sort, (int) typeArgumentIndex).getValue());
        }
      }
    }

    return new TypeReferenceExtended(TypeReference.newTypeReference(lastSort).getValue(), offset, start, end, index);
  }

  private TypePath readTypePathOptional(SExprClassTraverser traverser, String name) {
    if (name != null) {
      if (traverser.isNextList(name)) {
        SExprClassTraverser child = traverser.nextListTraverser();
        child.skip();
        TypePath typePath = readTypePathOptional(child, null);
        child.expectEnd();
        return typePath;
      } else {
        return null;
      }
    }

    StringBuilder path = new StringBuilder();

    while (traverser.hasNext()) {
      if (traverser.isNextList("array")) {
        path.append('[');
        traverser.next();
      } else if (traverser.isNextList("inner_type")) {
        path.append('.');
        traverser.next();
      } else if (traverser.isNextList("wildcard_bound")) {
        path.append('*');
        traverser.next();
      } else if (traverser.isNextList("type_argument")) {
        SExprClassTraverser child = traverser.nextListTraverser();
        child.skip();
        long typeArgument = child.nextInteger("type_argument");
        path.append((char) typeArgument);
        path.append(';');
        child.expectEnd();
      } else {
        throw new IllegalArgumentException("Invalid S-Expression node");
      }
    }

    return TypePath.fromString(path.toString());
  }

  private TypeAnnotationNode readTypeAnnotation(SExprClassTraverser traverser) {
    return readTypeAnnotationExtended(traverser).second;
  }

  private Pair<TypeReferenceExtended, TypeAnnotationNode> readTypeAnnotationExtended(SExprClassTraverser traverser) {
    TypeAnnotationNode typeAnnotationNode = new TypeAnnotationNode(0, null, null);
    traverser.skip();
    TypeReferenceExtended typeReference = readTarget(traverser, "target");
    typeAnnotationNode.typeRef = typeReference.getValue();
    typeAnnotationNode.typePath = readTypePathOptional(traverser, "target_path");
    typeAnnotationNode.desc = traverser.nextString("type");
    typeAnnotationNode.values = new ArrayList<>();

    while (traverser.hasNext()) {
      Pair<String, Object> value = readElementValuePair(traverser.nextListTraverser());
      typeAnnotationNode.values.add(value.first);
      typeAnnotationNode.values.add(value.second);
    }

    return Pair.of(typeReference, typeAnnotationNode);
  }

  private UnknownAttribute readUnknownAttribute(SExprClassTraverser traverser, boolean codeAttribute) {
    traverser.skip();
    String type = traverser.nextString("name");
    byte[] content = traverser.nextDataStringOptional("content");
    return new UnknownAttribute(type, content, codeAttribute);
  }
}
