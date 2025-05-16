package com.koyomiji.jasmine.test;

import com.koyomiji.jasmine.common.AttributeHelper;
import com.koyomiji.jasmine.common.PrinterHelper;
import com.koyomiji.jasmine.sexpr.SExprClassPrinter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.*;
import org.objectweb.asm.util.Printer;

public class SExprClassPrinterTest {
  @Test
  void test_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "a", null, "java/lang/Object", null);
    Printer methodPr =printer.visitMethod(0, "a", "()V", null, null);
    methodPr.visitCode();
    methodPr.visitInsn(0);
    methodPr.visitMethodEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "a" (super_class "java/lang/Object")
                      (method "a" "()V"
                        (code
                          nop
                        )
                      )
                    )
                    """, result);
  }

  @Test
  void test_1() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "a", null, "java/lang/Object", null);
    Printer methodPr =printer.visitMethod(0, "a", "()V", null, null);
    printer.visitClassEnd();
    methodPr.visitCode();
    methodPr.visitInsn(0);
    methodPr.visitMethodEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "a" (super_class "java/lang/Object")
                      (method "a" "()V"
                        (code
                          nop
                        )
                      )
                    )
                    """, result);
  }

  @Test
  void test_2() {
    SExprClassPrinter printer = new SExprClassPrinter();
    Printer methodPr =printer.visitMethod(0, "a", "()V", null, null);
    methodPr.visitCode();
    methodPr.visitInsn(0);
    methodPr.visitMethodEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
            (method "a" "()V"
              (code
                nop
              )
            )
            """, result);
  }

  @Test
  void test_label_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    Printer methodPr =printer.visitMethod(0, "a", "()V", null, null);
    methodPr.visitCode();
    methodPr.visitTypeInsn(Opcodes.NEW, "java/lang/Object");
    methodPr.visitInsnAnnotation(TypeReference.newTypeReference(TypeReference.NEW).getValue(), null, "LA;", false);
    methodPr.visitMethodEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
(method "a" "()V"
  (code
    label $l0
    new "java/lang/Object"
    (runtime_invisible_type_annotation (target (new (offset $l0))) (type "LA;"))
  )
)
""", result);
  }

  /*
   * Exhaustive tests - class
   */

  @Test
  void test_class_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object"))
                    """, result);
  }

  @Test
  void test_class_1() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, Opcodes.ACC_PUBLIC, "A", null, "java/lang/Object", null);
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) (access_flag ACC_PUBLIC) "A" (super_class "java/lang/Object"))
                    """, result);
  }

  @Test
  void test_class_2() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, Opcodes.ACC_PUBLIC, "A", null, "java/lang/Object", new String[]{"B"});
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) (access_flag ACC_PUBLIC) "A" (super_class "java/lang/Object") (interface "B"))
                    """, result);
  }

  @Test
  void test_class_3() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit((2 << 16) | 1, Opcodes.ACC_PUBLIC, "A", null, "java/lang/Object", new String[]{"B"});
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 2) (major_version 1) (access_flag ACC_PUBLIC) "A" (super_class "java/lang/Object") (interface "B"))
                    """, result);
  }

  @Test
  void test_class_signature_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", "A", "java/lang/Object", null);
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (signature "A")
                    )
                    """, result);
  }

  @Test
  void test_class_deprecated_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, Opcodes.ACC_DEPRECATED, "A", null, "java/lang/Object", null);
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (deprecated)
                    )
                    """, result);
  }

  @Test
  void test_class_synthetic_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.setPreferAccessFlags(false);
    printer.visit(0, Opcodes.ACC_SYNTHETIC, "A", null, "java/lang/Object", null);
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (synthetic)
                    )
                    """, result);
  }

  @Test
  void test_class_synthetic_1() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.setPreferAccessFlags(true);
    printer.visit(0, Opcodes.ACC_SYNTHETIC, "A", null, "java/lang/Object", null);
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) (access_flag ACC_SYNTHETIC) "A" (super_class "java/lang/Object"))
                    """, result);
  }

  @Test
  void test_class_sourceFile_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    printer.visitSource("A.java", null);
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (source_file "A.java")
                    )
                    """, result);
  }

  @Test
  void test_class_innerClass_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    printer.visitInnerClass("A", "B", "C", Opcodes.ACC_PUBLIC);
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (inner_class (inner_class "A") (outer_class "B") (inner_name "C") (access_flag ACC_PUBLIC))
                    )
                    """, result);
  }

  @Test
  void test_class_enclosingMethod_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    printer.visitOuterClass("A", "b", "()V");
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (enclosing_method (class "A") (method "b" "()V"))
                    )
                    """, result);
  }

  @Test
  void test_class_sourceDebugExtension_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    printer.visitSource(null, "A");
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (source_debug_extension "A")
                    )
                    """, result);
  }

  @Test
  void test_class_module_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    printer.visitModule("B", Opcodes.ACC_OPEN, "C");
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (module "B" (flag ACC_OPEN) (version "C"))
                    )
                    """, result);
  }

  @Test
  void test_class_module_1() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    Printer modulePr = printer.visitModule("B", Opcodes.ACC_OPEN, "C");
    modulePr.visitRequire("D", Opcodes.ACC_TRANSITIVE, "E");
    modulePr.visitExport("F", Opcodes.ACC_SYNTHETIC, "G", "H");
    modulePr.visitOpen("I", Opcodes.ACC_SYNTHETIC, "J", "K");
    modulePr.visitUse("L");
    modulePr.visitUse("M");
    modulePr.visitProvide("N", "O", "P");

    modulePr.visitModuleEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (module "B" (flag ACC_OPEN) (version "C")
                        (requires "D" (flag ACC_TRANSITIVE) (version "E"))
                        (exports "F" (flag ACC_SYNTHETIC) (to "G" "H"))
                        (opens "I" (flag ACC_SYNTHETIC) (to "J" "K"))
                        (uses "L")
                        (uses "M")
                        (provides "N" (with "O" "P"))
                      )
                    )
                    """, result);
  }

  @Test
  void test_class_modulePackage_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    Printer modulePr = printer.visitModule("B", Opcodes.ACC_OPEN, "C");
    modulePr.visitPackage("A");
    modulePr.visitModuleEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (module "B" (flag ACC_OPEN) (version "C"))
                      (module_package "A")
                    )
                    """, result);
  }

  @Test
  void test_class_moduleMainClass_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    Printer modulePr = printer.visitModule("B", Opcodes.ACC_OPEN, "C");
    modulePr.visitMainClass("A");
    modulePr.visitModuleEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (module "B" (flag ACC_OPEN) (version "C"))
                      (module_main_class "A")
                    )
                    """, result);
  }

  @Test
  void test_class_nestHost_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    printer.visitNestHost("A");
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (nest_host "A")
                    )
                    """, result);
  }

  @Test
  void test_class_nestMember_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    printer.visitNestMember("A");
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (nest_member "A")
                    )
                    """, result);
  }

  @Test
  void test_class_recordComponent_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter recordPr = printer.visitRecordComponent("a", "I", null);
    recordPr.visitRecordComponentEnd();
    recordPr = printer.visitRecordComponent("b", "I", "J");
    recordPr.visitRecordComponentEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (record_component "a" "I")
                      (record_component "b" "I"
                        (signature "J")
                      )
                    )
                    """, result);
  }

  @Test
  void test_class_permittedSubclasses_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    printer.visitPermittedSubclass("B");
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (permitted_subclass "B")
                    )
                    """, result);
  }

  @Test
  void test_class_runtimeVisibleAnnotation_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter annoPr = printer.visitClassAnnotation("LA;", true);
    annoPr.visitAnnotationEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (runtime_visible_annotation (type "LA;"))
                    )
                    """, result);
  }

  @Test
  void test_class_runtimeVisibleAnnotation_1() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter annoPr = printer.visitClassAnnotation("LA;", true);
    annoPr.visit("a", (byte) 1);
    annoPr.visit("b", true);
    annoPr.visit("c", (char) 1);
    annoPr.visit("d", (short) 1);
    annoPr.visit("e", 1);
    annoPr.visit("f", 1L);
    annoPr.visit("g", 1.0f);
    annoPr.visit("h", 1.0);
    annoPr.visit("i", "A");
    annoPr.visit("k", new byte[]{1});
    annoPr.visit("l", new boolean[]{true});
    annoPr.visit("m", new char[]{1});
    annoPr.visit("n", new short[]{1});
    annoPr.visit("o", new int[]{1});
    annoPr.visit("p", new long[]{1});
    annoPr.visit("q", new float[]{1.0f});
    annoPr.visit("r", new double[]{1.0});
    annoPr.visit("s", Type.getType("LA;"));
    annoPr.visit("t", Type.getType("[LA;"));

    SExprClassPrinter annoPr2 = annoPr.visitArray("u");
    annoPr2.visit(null, Type.getType("LA;"));
    annoPr2.visitAnnotationEnd();

    annoPr2 = annoPr.visitArray("v");
    annoPr2.visit(null, Type.getType("[LA;"));
    annoPr2.visitAnnotationEnd();

    annoPr.visitEnum("w", "A", "B");
    annoPr2 = annoPr.visitArray("x");
    annoPr2.visitEnum(null, "A", "B");
    annoPr2.visitAnnotationEnd();
    annoPr.visitAnnotationEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (runtime_visible_annotation (type "LA;")
                        (element_value_pair "a" (byte 1))
                        (element_value_pair "b" (boolean 1))
                        (element_value_pair "c" (char 1))
                        (element_value_pair "d" (short 1))
                        (element_value_pair "e" (int 1))
                        (element_value_pair "f" (long 1))
                        (element_value_pair "g" (float 1.0))
                        (element_value_pair "h" (double 1.0))
                        (element_value_pair "i" (string "A"))
                        (element_value_pair "k" (array (byte 1)))
                        (element_value_pair "l" (array (boolean 1)))
                        (element_value_pair "m" (array (char 1)))
                        (element_value_pair "n" (array (short 1)))
                        (element_value_pair "o" (array (int 1)))
                        (element_value_pair "p" (array (long 1)))
                        (element_value_pair "q" (array (float 1.0)))
                        (element_value_pair "r" (array (double 1.0)))
                        (element_value_pair "s" (class "A"))
                        (element_value_pair "t" (class "[LA;"))
                        (element_value_pair "u" (array (class "A")))
                        (element_value_pair "v" (array (class "[LA;")))
                        (element_value_pair "w" (enum "A" "B"))
                        (element_value_pair "x" (array (enum "A" "B")))
                      )
                    )
                    """, result);
  }

  @Test
  void test_class_runtimeInvisibleAnnotation_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter annoPr = printer.visitClassAnnotation("LA;", false);
    annoPr.visitAnnotationEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (runtime_invisible_annotation (type "LA;"))
                    )
                    """, result);
  }

  @Test
  void test_class_runtimeVisibleTypeAnnotation_1() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter annoPr = printer.visitClassTypeAnnotation(TypeReference.newTypeParameterReference(TypeReference.CLASS_TYPE_PARAMETER, 0).getValue(), null, "LA;", true);
    annoPr.visitAnnotationEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (runtime_visible_type_annotation (target (class_type_parameter (type_parameter_index 0))) (type "LA;"))
                    )
                    """, result);
  }

  @Test
  void test_field_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter fieldPr = printer.visitField(Opcodes.ACC_PUBLIC, "a", "I", null, null);
    fieldPr.visitFieldEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (field (access_flag ACC_PUBLIC) "a" "I")
                    )
                    """, result);
  }

  @Test
  void test_field__signature_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter fieldPr = printer.visitField(Opcodes.ACC_PUBLIC, "a", "I", "J", null);
    fieldPr.visitFieldEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (field (access_flag ACC_PUBLIC) "a" "I"
                        (signature "J")
                      )
                    )
                    """, result);
  }

  @Test
  void test_field_deprecated_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter fieldPr = printer.visitField(Opcodes.ACC_PUBLIC | Opcodes.ACC_DEPRECATED, "a", "I", null, null);
    fieldPr.visitFieldEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (field (access_flag ACC_PUBLIC) "a" "I"
                        (deprecated)
                      )
                    )
                    """, result);
  }

  @Test
  void test_field_synthetic_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter fieldPr = printer.visitField(Opcodes.ACC_PUBLIC | Opcodes.ACC_SYNTHETIC, "a", "I", null, null);
    fieldPr.visitFieldEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (field (access_flag ACC_PUBLIC) "a" "I"
                        (synthetic)
                      )
                    )
                    """, result);
  }

  @Test
  void test_field_constantValue_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter fieldPr = printer.visitField(Opcodes.ACC_PUBLIC, "a", "I", null, 1);
    fieldPr.visitFieldEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (field (access_flag ACC_PUBLIC) "a" "I"
                        (constant_value (integer 1))
                      )
                    )
                    """, result);
  }

  @Test
  void test_method_0()  {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter methodPr = printer.visitMethod(Opcodes.ACC_PUBLIC, "a", "()V", null, null);
    methodPr.visitMethodEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (method (access_flag ACC_PUBLIC) "a" "()V")
                    )
                    """, result);
  }

  @Test
  void test_method_signature_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter methodPr = printer.visitMethod(Opcodes.ACC_PUBLIC, "a", "()V", "()I", null);
    methodPr.visitMethodEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (method (access_flag ACC_PUBLIC) "a" "()V"
                        (signature "()I")
                      )
                    )
                    """, result);
  }

  @Test
  void test_method_code_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter methodPr = printer.visitMethod(Opcodes.ACC_PUBLIC, "a", "()V", null, null);
    methodPr.visitCode();
    methodPr.visitInsn(Opcodes.NOP);
    methodPr.visitMethodEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (method (access_flag ACC_PUBLIC) "a" "()V"
                        (code
                          nop
                        )
                      )
                    )
                    """, result);
  }

  @Test
  void test_method_exception_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter methodPr = printer.visitMethod(Opcodes.ACC_PUBLIC, "a", "()V", null, new String[]{"A"});
    methodPr.visitMethodEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (method (access_flag ACC_PUBLIC) "a" "()V"
                        (exception "A")
                      )
                    )
                    """, result);
  }

  @Test
  void test_method_runtimeVisibleParameterAnnotation_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter methodPr = printer.visitMethod(Opcodes.ACC_PUBLIC, "a", "()V", null, null);
    SExprClassPrinter paramPr = methodPr.visitParameterAnnotation(0, "LA;", true);
    paramPr.visitAnnotationEnd();
    methodPr.visitMethodEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (method (access_flag ACC_PUBLIC) "a" "()V"
                        (runtime_visible_parameter_annotation
                          (parameter_annotation (type "LA;"))
                        )
                      )
                    )
                    """, result);
  }

  @Test
  void test_method_runtimeVisibleParameterAnnotation_1() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter methodPr = printer.visitMethod(Opcodes.ACC_PUBLIC, "a", "()V", null, null);
    SExprClassPrinter paramPr = methodPr.visitParameterAnnotation(0, "LA;", true);
    paramPr.visit("a", (byte) 1);
    paramPr.visitAnnotationEnd();
    methodPr.visitMethodEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (method (access_flag ACC_PUBLIC) "a" "()V"
                        (runtime_visible_parameter_annotation
                          (parameter_annotation (type "LA;")
                            (element_value_pair "a" (byte 1))
                          )
                        )
                      )
                    )
                    """, result);
  }

  @Test
  void test_method_runtimeInvisibleParameterAnnotation_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter methodPr = printer.visitMethod(Opcodes.ACC_PUBLIC, "a", "()V", null, null);
    SExprClassPrinter paramPr = methodPr.visitParameterAnnotation(0, "LA;", false);
    paramPr.visitAnnotationEnd();
    methodPr.visitMethodEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (method (access_flag ACC_PUBLIC) "a" "()V"
                        (runtime_invisible_parameter_annotation
                          (parameter_annotation (type "LA;"))
                        )
                      )
                    )
                    """, result);
  }

  @Test
  void test_method_annotationDefault_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter methodPr = printer.visitMethod(Opcodes.ACC_PUBLIC, "a", "()V", null, null);
    SExprClassPrinter annoPr = methodPr.visitAnnotationDefault();
    annoPr.visit(null, (byte) 1);
    annoPr.visitAnnotationEnd();
    methodPr.visitMethodEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (method (access_flag ACC_PUBLIC) "a" "()V"
                        (annotation_default (byte 1))
                      )
                    )
                    """, result);
  }

  @Test
  void test_method_methodParameter_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter methodPr = printer.visitMethod(Opcodes.ACC_PUBLIC, "a", "()V", null, null);
    methodPr.visitParameter("b", Opcodes.ACC_FINAL);
    methodPr.visitMethodEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (method (access_flag ACC_PUBLIC) "a" "()V"
                        (method_parameter (name "b") (access_flag ACC_FINAL))
                      )
                    )
                    """, result);
  }

  @Test
  void test_method_lineNumber_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter methodPr = printer.visitMethod(Opcodes.ACC_PUBLIC, "a", "()V", null, null);
    methodPr.visitCode();
    Label l0 = new Label();
    methodPr.visitLabel(l0);
    methodPr.visitLineNumber(1, l0);
    methodPr.visitInsn(Opcodes.NOP);
    methodPr.visitMethodEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (method (access_flag ACC_PUBLIC) "a" "()V"
                        (code
                          label $l0
                          (line_number (start $l0) 1)
                          nop
                        )
                      )
                    )
                    """, result);
  }

  @Test
  void test_method_localVariable_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter methodPr = printer.visitMethod(Opcodes.ACC_PUBLIC, "a", "()V", null, null);
    methodPr.visitCode();
    Label l0 = new Label(),           l1 = new Label();
    methodPr.visitLabel(l0);
    methodPr.visitInsn(Opcodes.NOP);
    methodPr.visitLabel(l1);
    methodPr.visitLocalVariable("a", "I", null, l0, l1, 1);
    methodPr.visitMethodEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (method (access_flag ACC_PUBLIC) "a" "()V"
                        (code
                          label $l0
                          nop
                          label $l1
                          (local_variable (start $l0) (end $l1) "a" "I" 1)
                        )
                      )
                    )
                    """, result);
  }

  @Test
  void test_method_localVariableType_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter methodPr = printer.visitMethod(Opcodes.ACC_PUBLIC, "a", "()V", null, null);
    methodPr.visitCode();
    Label l0 = new Label(),           l1 = new Label();
    methodPr.visitLabel(l0);
    methodPr.visitInsn(Opcodes.NOP);
    methodPr.visitLabel(l1);
    methodPr.visitLocalVariable("a", "I", "J", l0, l1, 1);
    methodPr.visitMethodEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (method (access_flag ACC_PUBLIC) "a" "()V"
                        (code
                          label $l0
                          nop
                          label $l1
                          (local_variable (start $l0) (end $l1) "a" "I" 1)
                          (local_variable_type (start $l0) (end $l1) "a" "J" 1)
                        )
                      )
                    )
                    """, result);
  }

  @Test
  void test_method_stackMapFrame_0() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter methodPr = printer.visitMethod(Opcodes.ACC_PUBLIC, "a", "()V", null, null);
    methodPr.visitCode();
    methodPr.visitFrame(Opcodes.F_FULL, 1, new Object[]{"A"}, 1, new Object[]{"B"});
    methodPr.visitInsn(Opcodes.NOP);
    methodPr.visitMethodEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (method (access_flag ACC_PUBLIC) "a" "()V"
                        (code
                          label $l0
                          (stack_map_frame (full (offset $l0) (local (object "A")) (stack (object "B"))))
                          nop
                        )
                      )
                    )
                    """, result);
  }

  @Test
  void test_method_stackMapFrame_1() {
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    SExprClassPrinter methodPr = printer.visitMethod(Opcodes.ACC_PUBLIC, "a", "()V", null, null);
    methodPr.visitCode();
    methodPr.visitFrame(Opcodes.F_FULL, 1, new Object[]{"A"}, 1, new Object[]{"B"});
    methodPr.visitInsn(Opcodes.NOP);
    methodPr.visitFrame(Opcodes.F_APPEND, 1, new Object[]{"A"}, 0, null);
    methodPr.visitInsn(Opcodes.NOP);
    methodPr.visitMethodEnd();
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (method (access_flag ACC_PUBLIC) "a" "()V"
                        (code
                          label $l0
                          (stack_map_frame (full (offset $l0) (local (object "A")) (stack (object "B"))))
                          nop
                          label $l1
                          (stack_map_frame (append (offset $l1) (local (object "A"))))
                          nop
                        )
                      )
                    )
                    """, result);
  }

  @Test
  void test_class_unknown_0(){
    SExprClassPrinter printer = new SExprClassPrinter();
    printer.visit(0, 0, "A", null, "java/lang/Object", null);
    Attribute attr = AttributeHelper.newInstance("test");
    AttributeHelper.setContent(attr, new byte[]{0x00, 0x01, 0x02, 0x03});
    printer.visitClassAttribute(attr);
    printer.visitClassEnd();
    String result = PrinterHelper.toString(printer);
    Assertions.assertEquals(
            """
                    (class (minor_version 0) (major_version 0) "A" (super_class "java/lang/Object")
                      (unknown (name "test") (content "\\00\\01\\02\\03"))
                    )
                    """, result);
  }
}
