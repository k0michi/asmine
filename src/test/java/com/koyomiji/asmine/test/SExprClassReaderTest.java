package com.koyomiji.asmine.test;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.common.PrinterHelper;
import com.koyomiji.asmine.sexpr.tree.SExprNodeContainer;
import com.koyomiji.asmine.sexpr.SExprClassPrinter;
import com.koyomiji.asmine.sexpr.SExprReader;
import com.koyomiji.asmine.sexpr.reader.SExprClassReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;

public class SExprClassReaderTest {
  private ClassNode parse(String s) {
    ClassNode classNode = new ClassNode();
    SExprNodeContainer node = new SExprNodeContainer();
    SExprClassReader reader = null;
    try {
      new SExprReader(s).accept(node);
      reader = new SExprClassReader(node);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    reader.accept(classNode);
    return classNode;
  }

  private String roundTrip(String s) {
    ClassNode parsed = parse(s);
    return PrinterHelper.toString(new SExprClassPrinter(), parsed);
  }

  private void roundTripTest(String s) {
    Assertions.assertEquals(s, roundTrip(s));
  }

  @Test
  void test_class_0() {
    ClassNode classNode = parse("(class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) (this_class \"A\") (super_class \"B\") (interface \"C\"))");
    Assertions.assertEquals(1, classNode.version >> 16);
    Assertions.assertEquals(2, classNode.version & 0xFFFF);
    Assertions.assertEquals(Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, classNode.access);
    Assertions.assertEquals("A", classNode.name);
    Assertions.assertEquals("B", classNode.superName);
    Assertions.assertEquals(1, classNode.interfaces.size());
    Assertions.assertEquals("C", classNode.interfaces.get(0));
  }

  @Test
  void test_class_1() {
    ClassNode classNode = parse("(class 1 2 (access_flag ACC_PUBLIC ACC_SUPER) \"A\" \"B\" (interface \"C\"))");

    Assertions.assertEquals(1, classNode.version >> 16);
    Assertions.assertEquals(2, classNode.version & 0xFFFF);
    Assertions.assertEquals(Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, classNode.access);
    Assertions.assertEquals("A", classNode.name);
    Assertions.assertEquals("B", classNode.superName);
    Assertions.assertEquals(1, classNode.interfaces.size());
    Assertions.assertEquals("C", classNode.interfaces.get(0));
  }

  @Test
  void test_class_2() {
    ClassNode classNode = parse("(class 1 2 (access_flag ACC_PUBLIC ACC_SUPER) \"A\" \"B\" (interface \"C\") (interface \"D\"))");

    Assertions.assertEquals(1, classNode.version >> 16);
    Assertions.assertEquals(2, classNode.version & 0xFFFF);
    Assertions.assertEquals(Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, classNode.access);
    Assertions.assertEquals("A", classNode.name);
    Assertions.assertEquals("B", classNode.superName);
    Assertions.assertEquals(2, classNode.interfaces.size());
    Assertions.assertEquals("C", classNode.interfaces.get(0));
    Assertions.assertEquals("D", classNode.interfaces.get(1));
  }

  @Test
  void test_class_3() {
    ClassNode classNode = parse("(class 1 2 (access_flag ACC_PUBLIC ACC_SUPER) \"A\" \"B\" (interface \"C\" \"D\"))");

    Assertions.assertEquals(1, classNode.version >> 16);
    Assertions.assertEquals(2, classNode.version & 0xFFFF);
    Assertions.assertEquals(Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, classNode.access);
    Assertions.assertEquals("A", classNode.name);
    Assertions.assertEquals("B", classNode.superName);
    Assertions.assertEquals(2, classNode.interfaces.size());
    Assertions.assertEquals("C", classNode.interfaces.get(0));
    Assertions.assertEquals("D", classNode.interfaces.get(1));
  }

  @Test
  void test_class_4() {
    ClassNode classNode = parse("(class 1 2 \"A\" \"B\" (interface \"C\" \"D\"))");

    Assertions.assertEquals(1, classNode.version >> 16);
    Assertions.assertEquals(2, classNode.version & 0xFFFF);
    Assertions.assertEquals("A", classNode.name);
    Assertions.assertEquals("B", classNode.superName);
    Assertions.assertEquals(2, classNode.interfaces.size());
    Assertions.assertEquals("C", classNode.interfaces.get(0));
    Assertions.assertEquals("D", classNode.interfaces.get(1));
  }

  @Test
  void test_class_5() {
    ClassNode classNode = parse("(class 1 2 \"A\" \"B\")");

    Assertions.assertEquals(1, classNode.version >> 16);
    Assertions.assertEquals(2, classNode.version & 0xFFFF);
    Assertions.assertEquals("A", classNode.name);
    Assertions.assertEquals("B", classNode.superName);
  }

  @Test
  void test_class_6() {
    ClassNode classNode = parse("(class 1 2 \"A\" (interface \"C\"))");

    Assertions.assertEquals(1, classNode.version >> 16);
    Assertions.assertEquals(2, classNode.version & 0xFFFF);
    Assertions.assertEquals("A", classNode.name);
    Assertions.assertEquals(1, classNode.interfaces.size());
    Assertions.assertEquals("C", classNode.interfaces.get(0));
  }

  @Test
  void test_class_7() {
    ClassNode classNode = parse("(class 1 2 (this_class (class \"A\")))");

    Assertions.assertEquals(1, classNode.version >> 16);
    Assertions.assertEquals(2, classNode.version & 0xFFFF);
    Assertions.assertEquals("A", classNode.name);
  }

  @Test
  void test_class_8() {
    Assertions.assertThrows(RuntimeException.class, () -> {
      ClassNode classNode = parse("(class 1 2 (this_class (clazz \"A\")))");
    });
  }

  @Test
  void test_class_innerClass_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (inner_class \"A\" \"B\" \"C\" (access_flag ACC_PUBLIC)))");
    Assertions.assertEquals("A", classNode.innerClasses.get(0).name);
    Assertions.assertEquals("B", classNode.innerClasses.get(0).outerName);
    Assertions.assertEquals("C", classNode.innerClasses.get(0).innerName);
    Assertions.assertEquals(Opcodes.ACC_PUBLIC, classNode.innerClasses.get(0).access);
  }

  @Test
  void test_class_innerClass_1() {
    ClassNode classNode = parse("(class 1 2 \"A\" (inner_class \"A\" (access_flag ACC_PUBLIC)))");
    Assertions.assertEquals("A", classNode.innerClasses.get(0).name);
    Assertions.assertEquals(Opcodes.ACC_PUBLIC, classNode.innerClasses.get(0).access);
  }

  @Test
  void test_class_innerClass_2() {
    ClassNode classNode = parse("(class 1 2 \"A\" (inner_class (inner_class \"A\") (outer_class \"B\") (inner_name \"C\") (access_flag ACC_PUBLIC)))");
    Assertions.assertEquals("A", classNode.innerClasses.get(0).name);
    Assertions.assertEquals("B", classNode.innerClasses.get(0).outerName);
    Assertions.assertEquals("C", classNode.innerClasses.get(0).innerName);
    Assertions.assertEquals(Opcodes.ACC_PUBLIC, classNode.innerClasses.get(0).access);
  }

  @Test
  void test_class_innerClass_3() {
    ClassNode classNode = parse("(class 1 2 \"A\" (inner_class (inner_class (class \"A\")) (outer_class (class \"B\")) (inner_name \"C\") (access_flag ACC_PUBLIC)))");
    Assertions.assertEquals("A", classNode.innerClasses.get(0).name);
    Assertions.assertEquals("B", classNode.innerClasses.get(0).outerName);
    Assertions.assertEquals("C", classNode.innerClasses.get(0).innerName);
    Assertions.assertEquals(Opcodes.ACC_PUBLIC, classNode.innerClasses.get(0).access);
  }

  @Test
  void test_class_enclosingMethod_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (enclosing_method (class \"A\") (method \"b\" \"()V\")))");
    Assertions.assertEquals("A", classNode.outerClass);
    Assertions.assertEquals("b", classNode.outerMethod);
    Assertions.assertEquals("()V", classNode.outerMethodDesc);
  }

  @Test
  void test_class_enclosingMethod_1() {
    ClassNode classNode = parse("(class 1 2 \"A\" (enclosing_method (class \"A\") (method (name \"b\") (descriptor \"()V\"))))");
    Assertions.assertEquals("A", classNode.outerClass);
    Assertions.assertEquals("b", classNode.outerMethod);
    Assertions.assertEquals("()V", classNode.outerMethodDesc);
  }

  @Test
  void test_class_enclosingMethod_2() {
    ClassNode classNode = parse("(class 1 2 \"A\" (enclosing_method (class \"A\") (method (name_and_type (name \"b\") (descriptor \"()V\")))))");
    Assertions.assertEquals("A", classNode.outerClass);
    Assertions.assertEquals("b", classNode.outerMethod);
    Assertions.assertEquals("()V", classNode.outerMethodDesc);
  }

  @Test
  void test_class_sourceDebugExtension_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (source_debug_extension \"A\"))");
    Assertions.assertEquals("A", classNode.sourceDebug);
  }

  @Test
  void test_class_module_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (module \"a\"))");
    Assertions.assertEquals("a", classNode.module.name);
  }

  @Test
  void test_class_module_1() {
    ClassNode classNode = parse("(class 1 2 \"A\" (module \"a\" (flag ACC_OPEN) \"b\"))");
    Assertions.assertEquals("a", classNode.module.name);
    Assertions.assertEquals(Opcodes.ACC_OPEN, classNode.module.access);
    Assertions.assertEquals("b", classNode.module.version);
  }

  @Test
  void test_class_module_requires_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (module \"a\" (requires \"c\")))");
    Assertions.assertEquals("c", classNode.module.requires.get(0).module);
  }

  @Test
  void test_class_module_requires_1() {
    ClassNode classNode = parse("(class 1 2 \"A\" (module \"a\" (requires \"c\" (flag ACC_TRANSITIVE) \"d\")))");
    Assertions.assertEquals("c", classNode.module.requires.get(0).module);
    Assertions.assertEquals(Opcodes.ACC_TRANSITIVE, classNode.module.requires.get(0).access);
    Assertions.assertEquals("d", classNode.module.requires.get(0).version);
  }

  @Test
  void test_class_module_exports_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (module \"a\" (exports \"c\")))");
    Assertions.assertEquals("c", classNode.module.exports.get(0).packaze);
  }

  @Test
  void test_class_module_exports_1() {
    ClassNode classNode = parse("(class 1 2 \"A\" (module \"a\" (exports \"c\" (flag ACC_SYNTHETIC) (to \"d\"))))");
    Assertions.assertEquals("c", classNode.module.exports.get(0).packaze);
    Assertions.assertEquals(Opcodes.ACC_SYNTHETIC, classNode.module.exports.get(0).access);
    Assertions.assertEquals("d", classNode.module.exports.get(0).modules.get(0));
  }

  @Test
  void test_class_module_opens_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (module \"a\" (opens \"c\")))");
    Assertions.assertEquals("c", classNode.module.opens.get(0).packaze);
  }

  @Test
  void test_class_module_opens_1() {
    ClassNode classNode = parse("(class 1 2 \"A\" (module \"a\" (opens \"c\" (flag ACC_SYNTHETIC) (to \"d\"))))");
    Assertions.assertEquals("c", classNode.module.opens.get(0).packaze);
    Assertions.assertEquals(Opcodes.ACC_SYNTHETIC, classNode.module.opens.get(0).access);
    Assertions.assertEquals("d", classNode.module.opens.get(0).modules.get(0));
  }

  @Test
  void test_class_module_uses_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (module \"a\" (uses \"B\")))");
    Assertions.assertEquals("B", classNode.module.uses.get(0));
  }

  @Test
  void test_class_module_provides_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (module \"a\" (provides \"B\" (with \"C\"))))");
    Assertions.assertEquals("B", classNode.module.provides.get(0).service);
    Assertions.assertEquals("C", classNode.module.provides.get(0).providers.get(0));
  }

  //

  @Test
  void test_class_modulePackage_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (module_package \"A\"))");
    Assertions.assertEquals("A", classNode.module.packages.get(0));
  }

  @Test
  void test_class_moduleMainClass_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (module_main_class \"A\"))");
    Assertions.assertEquals("A", classNode.module.mainClass);
  }

  //

  @Test
  void test_class_synthetic_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (synthetic))");
    Assertions.assertTrue((classNode.access & Opcodes.ACC_SYNTHETIC) != 0);
  }

  @Test
  void test_class_deprecated_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (deprecated))");
    Assertions.assertTrue((classNode.access & Opcodes.ACC_DEPRECATED) != 0);
  }

  @Test
  void test_class_signature_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (signature \"B\"))");
    Assertions.assertEquals("B", classNode.signature);
  }

  @Test
  void test_class_runtimeVisibleAnnotation_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (runtime_visible_annotation \"LB;\"))");
    Assertions.assertEquals(1, classNode.visibleAnnotations.size());
    Assertions.assertEquals("LB;", classNode.visibleAnnotations.get(0).desc);
  }

  @Test
  void test_class_runtimeVisibleAnnotation_1() {
    ClassNode classNode = parse("(class 1 2 \"A\" (runtime_visible_annotation \"LB;\" (element_value_pair (element_name \"a\") (value (int 1)))))");
    Assertions.assertEquals(1, classNode.visibleAnnotations.size());
    Assertions.assertEquals("LB;", classNode.visibleAnnotations.get(0).desc);
    Assertions.assertEquals("a", classNode.visibleAnnotations.get(0).values.get(0));
    Assertions.assertEquals(1, classNode.visibleAnnotations.get(0).values.get(1));
  }

  @Test
  void test_class_runtimeVisibleAnnotation_2() {
    ClassNode classNode = parse("(class 1 2 \"A\" (runtime_visible_annotation \"LB;\" (element_value_pair \"a\" (value (byte 1)))))");
    Assertions.assertEquals(1, classNode.visibleAnnotations.size());
    Assertions.assertEquals("LB;", classNode.visibleAnnotations.get(0).desc);
    Assertions.assertEquals("a", classNode.visibleAnnotations.get(0).values.get(0));
    Assertions.assertEquals((byte) 1, classNode.visibleAnnotations.get(0).values.get(1));
  }

  @Test
  void test_class_runtimeVisibleAnnotation_3() {
    ClassNode classNode = parse("(class 1 2 \"A\" (runtime_visible_annotation \"LB;\" (element_value_pair \"a\" (value (char 1)))))");
    Assertions.assertEquals(1, classNode.visibleAnnotations.size());
    Assertions.assertEquals("LB;", classNode.visibleAnnotations.get(0).desc);
    Assertions.assertEquals("a", classNode.visibleAnnotations.get(0).values.get(0));
    Assertions.assertEquals((char) 1, classNode.visibleAnnotations.get(0).values.get(1));
  }

  @Test
  void test_class_runtimeVisibleAnnotation_4() {
    ClassNode classNode = parse("(class 1 2 \"A\" (runtime_visible_annotation \"LB;\" (element_value_pair \"a\" (value (double 1.0)))))");
    Assertions.assertEquals(1, classNode.visibleAnnotations.size());
    Assertions.assertEquals("LB;", classNode.visibleAnnotations.get(0).desc);
    Assertions.assertEquals("a", classNode.visibleAnnotations.get(0).values.get(0));
    Assertions.assertEquals(1., classNode.visibleAnnotations.get(0).values.get(1));
  }

  @Test
  void test_class_runtimeVisibleAnnotation_5() {
    ClassNode classNode = parse("(class 1 2 \"A\" (runtime_visible_annotation \"LB;\" (element_value_pair \"a\" (value (float 1.0)))))");
    Assertions.assertEquals(1, classNode.visibleAnnotations.size());
    Assertions.assertEquals("LB;", classNode.visibleAnnotations.get(0).desc);
    Assertions.assertEquals("a", classNode.visibleAnnotations.get(0).values.get(0));
    Assertions.assertEquals(1.f, classNode.visibleAnnotations.get(0).values.get(1));
  }

  @Test
  void test_class_runtimeVisibleAnnotation_6() {
    ClassNode classNode = parse("(class 1 2 \"A\" (runtime_visible_annotation \"LB;\" (element_value_pair \"a\" (value (long 1)))))");
    Assertions.assertEquals(1, classNode.visibleAnnotations.size());
    Assertions.assertEquals("LB;", classNode.visibleAnnotations.get(0).desc);
    Assertions.assertEquals("a", classNode.visibleAnnotations.get(0).values.get(0));
    Assertions.assertEquals(1L, classNode.visibleAnnotations.get(0).values.get(1));
  }

  @Test
  void test_class_runtimeVisibleAnnotation_7() {
    ClassNode classNode = parse("(class 1 2 \"A\" (runtime_visible_annotation \"LB;\" (element_value_pair \"a\" (value (short 1)))))");
    Assertions.assertEquals(1, classNode.visibleAnnotations.size());
    Assertions.assertEquals("LB;", classNode.visibleAnnotations.get(0).desc);
    Assertions.assertEquals("a", classNode.visibleAnnotations.get(0).values.get(0));
    Assertions.assertEquals((short) 1, classNode.visibleAnnotations.get(0).values.get(1));
  }

  @Test
  void test_class_runtimeVisibleAnnotation_8() {
    ClassNode classNode = parse("(class 1 2 \"A\" (runtime_visible_annotation \"LB;\" (element_value_pair \"a\" (value (boolean 1)))))");
    Assertions.assertEquals(1, classNode.visibleAnnotations.size());
    Assertions.assertEquals("LB;", classNode.visibleAnnotations.get(0).desc);
    Assertions.assertEquals("a", classNode.visibleAnnotations.get(0).values.get(0));
    Assertions.assertEquals(true, classNode.visibleAnnotations.get(0).values.get(1));
  }

  @Test
  void test_class_runtimeVisibleAnnotation_9() {
    ClassNode classNode = parse("(class 1 2 \"A\" (runtime_visible_annotation \"LB;\" (element_value_pair \"a\" (value (string \"b\")))))");
    Assertions.assertEquals(1, classNode.visibleAnnotations.size());
    Assertions.assertEquals("LB;", classNode.visibleAnnotations.get(0).desc);
    Assertions.assertEquals("a", classNode.visibleAnnotations.get(0).values.get(0));
    Assertions.assertEquals("b", classNode.visibleAnnotations.get(0).values.get(1));
  }

  @Test
  void test_class_runtimeVisibleAnnotation_10() {
    ClassNode classNode = parse("(class 1 2 \"A\" (runtime_visible_annotation \"LB;\" (element_value_pair \"a\" (value (enum \"B\" \"b\")))))");
    Assertions.assertEquals(1, classNode.visibleAnnotations.size());
    Assertions.assertEquals("LB;", classNode.visibleAnnotations.get(0).desc);
    Assertions.assertEquals("a", classNode.visibleAnnotations.get(0).values.get(0));
    Assertions.assertArrayEquals(new String[]{"B", "b"}, (String[]) classNode.visibleAnnotations.get(0).values.get(1));
  }

  @Test
  void test_class_runtimeVisibleAnnotation_11() {
    ClassNode classNode = parse("(class 1 2 \"A\" (runtime_visible_annotation \"LB;\" (element_value_pair \"a\" (value (class \"B\")))))");
    Assertions.assertEquals(1, classNode.visibleAnnotations.size());
    Assertions.assertEquals("LB;", classNode.visibleAnnotations.get(0).desc);
    Assertions.assertEquals("a", classNode.visibleAnnotations.get(0).values.get(0));
    Assertions.assertEquals("B", ((Type) classNode.visibleAnnotations.get(0).values.get(1)).getInternalName());
  }

  @Test
  void test_class_runtimeVisibleAnnotation_12() {
    ClassNode classNode = parse("(class 1 2 \"A\" (runtime_visible_annotation \"LB;\" (element_value_pair \"a\" (value (annotation \"LB;\")))))");
    Assertions.assertEquals(1, classNode.visibleAnnotations.size());
    Assertions.assertEquals("LB;", classNode.visibleAnnotations.get(0).desc);
    Assertions.assertEquals("a", classNode.visibleAnnotations.get(0).values.get(0));
    Assertions.assertEquals("LB;", ((AnnotationNode) classNode.visibleAnnotations.get(0).values.get(1)).desc);
  }

  @Test
  void test_class_runtimeVisibleAnnotation_13() {
    ClassNode classNode = parse("(class 1 2 \"A\" (runtime_visible_annotation \"LB;\" (element_value_pair \"a\" (value (array (int 1) (int 1) (int 1))))))");
    Assertions.assertEquals(1, classNode.visibleAnnotations.size());
    Assertions.assertEquals("LB;", classNode.visibleAnnotations.get(0).desc);
    Assertions.assertEquals("a", classNode.visibleAnnotations.get(0).values.get(0));
    Assertions.assertEquals(ArrayListHelper.of(1, 1, 1), classNode.visibleAnnotations.get(0).values.get(1));
  }

  @Test
  void test_class_runtimeInvisibleAnnotation_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (runtime_invisible_annotation \"LB;\" (element_value_pair (element_name \"a\") (value (int 1)))))");
    Assertions.assertEquals(1, classNode.invisibleAnnotations.size());
    Assertions.assertEquals("LB;", classNode.invisibleAnnotations.get(0).desc);
    Assertions.assertEquals("a", classNode.invisibleAnnotations.get(0).values.get(0));
    Assertions.assertEquals(1, classNode.invisibleAnnotations.get(0).values.get(1));
  }

  @Test
  void test_field_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (field \"b\" \"I\"))");
    Assertions.assertEquals("b", classNode.fields.get(0).name);
    Assertions.assertEquals("I", classNode.fields.get(0).desc);
  }

  @Test
  void test_field_1() {
    ClassNode classNode = parse("(class 1 2 \"A\" (field (access_flag ACC_PUBLIC) \"b\" \"I\"))");
    Assertions.assertEquals(Opcodes.ACC_PUBLIC, classNode.fields.get(0).access);
    Assertions.assertEquals("b", classNode.fields.get(0).name);
    Assertions.assertEquals("I", classNode.fields.get(0).desc);
  }

  @Test
  void test_field_2() {
    ClassNode classNode = parse("(class 1 2 \"A\" (field (access_flag ACC_PUBLIC) (name \"b\") (descriptor \"I\")))");
    Assertions.assertEquals(Opcodes.ACC_PUBLIC, classNode.fields.get(0).access);
    Assertions.assertEquals("b", classNode.fields.get(0).name);
    Assertions.assertEquals("I", classNode.fields.get(0).desc);
  }

  @Test
  void test_field_constantValue_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (field \"b\" \"I\" (constant_value (integer 1))))");
    Assertions.assertEquals(1, classNode.fields.get(0).value);
  }

  @Test
  void test_field_constantValue_1() {
    ClassNode classNode = parse("(class 1 2 \"A\" (field \"b\" \"I\" (constant_value (float 1.0))))");
    Assertions.assertEquals(1.0f, classNode.fields.get(0).value);
  }

  @Test
  void test_field_constantValue_2() {
    ClassNode classNode = parse("(class 1 2 \"A\" (field \"b\" \"I\" (constant_value (long 1))))");
    Assertions.assertEquals(1L, classNode.fields.get(0).value);
  }

  @Test
  void test_field_constantValue_3() {
    ClassNode classNode = parse("(class 1 2 \"A\" (field \"b\" \"I\" (constant_value (double 1.0))))");
    Assertions.assertEquals(1.0, classNode.fields.get(0).value);
  }

  @Test
  void test_field_constantValue_4() {
    ClassNode classNode = parse("(class 1 2 \"A\" (field \"b\" \"I\" (constant_value (string \"a\"))))");
    Assertions.assertEquals("a", classNode.fields.get(0).value);
  }

  @Test
  void test_method_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (method \"b\" \"()I\"))");
    Assertions.assertEquals("b", classNode.methods.get(0).name);
    Assertions.assertEquals("()I", classNode.methods.get(0).desc);
  }

  @Test
  void test_method_visibleParameterAnnotation_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (method \"b\" \"(III)I\" (runtime_visible_parameter_annotation (parameter_annotation \"LB;\"))))");
    Assertions.assertEquals("LB;", classNode.methods.get(0).visibleParameterAnnotations[0].get(0).desc);
  }

  @Test
  void test_method_invisibleParameterAnnotation_0() {
    ClassNode classNode = parse("(class 1 2 \"A\" (method \"b\" \"(III)I\" (runtime_invisible_parameter_annotation (parameter_annotation \"LB;\"))))");
    Assertions.assertEquals("LB;", classNode.methods.get(0).invisibleParameterAnnotations[0].get(0).desc);
  }

  @Test
  void test_fail_0() {
    Assertions.assertThrows(RuntimeException.class, () -> {
      ClassNode classNode = parse("(class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) (this_class (class (class \"A\"))) (super_class \"B\") (interface \"C\"))");
    });
  }

  @Test
  void test_fail_1() {
    Assertions.assertThrows(RuntimeException.class, () -> {
      ClassNode classNode = parse("(class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) (class \"A\") (super_class \"B\") (interface \"C\"))");
    });
  }

  @Test
  void test_fail_2() {
    Assertions.assertThrows(RuntimeException.class, () -> {
      ClassNode classNode = parse("(class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) (this_class (this_class \"A\")) (super_class \"B\") (interface \"C\"))");
    });
  }

  @Test
  void test_fail_3() {
    Assertions.assertThrows(RuntimeException.class, () -> {
      ClassNode classNode = parse("(class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) (this_class \"A\") (super_class \"B\") (interface (class (class \"C\"))))");
    });
  }

  @Test
  void test_fail_4() {
    Assertions.assertThrows(RuntimeException.class, () -> {
      ClassNode classNode = parse("(class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) (this_class \"A\" \"B\") (super_class \"B\") (interface \"C\"))");
    });
  }

  @Test
  void test_roundTrip_0() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C"))
            """);
  }

  @Test
  void test_roundTrip_1() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (field "b" "I")
            )
            """);
  }

  @Test
  void test_roundTrip_2() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (field "b" "I"
                (constant_value (integer 1))
              )
            )
            """);
  }

  @Test
  void test_roundTrip_3() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (field "b" "I"
                (constant_value (float nan))
              )
            )
            """);
  }

  @Test
  void test_roundTrip_4() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (field "b" "I"
                (constant_value (float inf))
              )
            )
            """);
  }

  @Test
  void test_roundTrip_5() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (field "b" "I"
                (constant_value (float -inf))
              )
            )
            """);
  }

  @Test
  void test_roundTrip_typeAnnotation_0() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (runtime_visible_type_annotation (target (class_type_parameter (type_parameter_index 0))) (type "B;"))
            )
            """);
  }

  @Test
  void test_roundTrip_typeAnnotation_1() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (runtime_visible_type_annotation (target (class_type_parameter (type_parameter_index 0))) (target_path (array)) (type "B;"))
            )
            """);
  }

  @Test
  void test_roundTrip_method_0() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (method "a" "()V")
            )
            """);
  }

  @Test
  void test_roundTrip_code_nullary_0() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (method "a" "()V"
                (code
                  nop
                  (max_stack 0)
                  (max_locals 0)
                )
              )
            )
            """);
  }

  @Test
  void test_roundTrip_code_int_0() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (method "a" "()V"
                (code
                  bipush 1
                  (max_stack 0)
                  (max_locals 0)
                )
              )
            )
            """);
  }

  @Test
  void test_roundTrip_code_type_0() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (method "a" "()V"
                (code
                  new "A"
                  (max_stack 0)
                  (max_locals 0)
                )
              )
            )
            """);
  }

  @Test
  void test_roundTrip_code_ldc_0() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (method "a" "()V"
                (code
                  ldc (class "A")
                  (max_stack 0)
                  (max_locals 0)
                )
              )
            )
            """);
  }

  @Test
  void test_roundTrip_code_var_0() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (method "a" "()V"
                (code
                  iload 0
                  (max_stack 0)
                  (max_locals 0)
                )
              )
            )
            """);
  }

  @Test
  void test_roundTrip_code_iinc_0() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (method "a" "()V"
                (code
                  iinc 0 1
                  (max_stack 0)
                  (max_locals 0)
                )
              )
            )
            """);
  }

  @Test
  void test_roundTrip_code_label_0() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (method "a" "()V"
                (code
                  label $l0
                  nop
                  (max_stack 0)
                  (max_locals 0)
                )
              )
            )
            """);
  }

  @Test
  void test_roundTrip_code_jump_0() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (method "a" "()V"
                (code
                  label $l0
                  goto $l0
                  (max_stack 0)
                  (max_locals 0)
                )
              )
            )
            """);
  }

  @Test
  void test_roundTrip_code_tableSwitch_0() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (method "a" "()V"
                (code
                  tableswitch 0 1 $l0 $l1 $l2
                  (max_stack 0)
                  (max_locals 0)
                )
              )
            )
            """);
  }

  @Test
  void test_roundTrip_code_lookupSwitch_0() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (method "a" "()V"
                (code
                  lookupswitch $l0 0 $l1 1 $l2
                  (max_stack 0)
                  (max_locals 0)
                )
              )
            )
            """);
  }

  @Test
  void test_roundTrip_code_lookupSwitch_1() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (method "a" "()V"
                (code
                  lookupswitch $l0 0 $l1 1 $l2 2 $l3
                  nop
                  (max_stack 0)
                  (max_locals 0)
                )
              )
            )
            """);
  }

  @Test
  void test_roundTrip_code_frame_0() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (method "a" "()V"
                (code
                  label $l0
                  (stack_map_frame (same (offset $l0)))
                  (max_stack 0)
                  (max_locals 0)
                )
              )
            )
            """);
  }

  @Test
  void test_roundTrip_unknown_0() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (unknown (name "a"))
            )
            """);
  }

  @Test
  void test_roundTrip_unknown_1() {
    roundTripTest("""
            (class (minor_version 1) (major_version 2) (access_flag ACC_PUBLIC ACC_SUPER) "A" (super_class "B") (interface "C")
              (unknown (name "a") (content "\\00\\01\\02\\ff"))
            )
            """);
  }
}
