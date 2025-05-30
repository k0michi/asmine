package com.koyomiji.asmine.test;

import com.koyomiji.asmine.common.ArrayListHelper;
import com.koyomiji.asmine.common.InsnStencils;
import com.koyomiji.asmine.common.Insns;
import com.koyomiji.asmine.query.ClassQuery;
import com.koyomiji.asmine.regex.RegexModule;
import com.koyomiji.asmine.regex.code.CodeRegexProcessor;
import com.koyomiji.asmine.regex.compiler.Regexes;
import com.koyomiji.asmine.regex.compiler.code.CodeRegexes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;

public class ClassQueryTest {
  @Test
  void test_0() {
    Object result = ClassQuery.ofNew()
            .setMinorVersion(1)
            .getMinorVersion();
    Assertions.assertEquals(1, result);
  }

  @Test
  void test_1() {
    Object result = ClassQuery.ofNew()
            .setMajorVersion(1)
            .getMajorVersion();
    Assertions.assertEquals(1, result);
  }

  @Test
  void test_2() {
    Object result = ClassQuery.ofNew()
            .setAccess(1)
            .addAccess(2)
            .addAccess(4)
            .removeAccess(4)
            .getAccess();
    Assertions.assertEquals(1 | 2, result);
  }

  @Test
  void test_3() {
    Object result = ClassQuery.ofNew()
            .setName("A")
            .getName();
    Assertions.assertEquals("A", result);
  }

  @Test
  void test_4() {
    Object result = ClassQuery.ofNew()
            .setSignature("A")
            .getSignature();
    Assertions.assertEquals("A", result);
  }

  @Test
  void test_5() {
    Object result = ClassQuery.ofNew()
            .setSuperName("java/lang/Object")
            .getSuperName();
    Assertions.assertEquals("java/lang/Object", result);
  }

  @Test
  void test_6() {
    Object result = ClassQuery.ofNew()
            .setInterfaces("java/lang/Cloneable")
            .addInterfaces("java/io/Serializable")
            .removeInterfaces("java/lang/Cloneable")
            .getInterfaces();
    Assertions.assertEquals(
            ArrayListHelper.of("java/io/Serializable"),
            result
    );
  }

  @Test
  void test_7() {
    Object result = ClassQuery.ofNew()
            .setSourceFile("Test.java")
            .getSourceFile();
    Assertions.assertEquals("Test.java", result);
  }

  @Test
  void test_8() {
    Object result = ClassQuery.ofNew()
            .setSourceDebug("A")
            .getSourceDebug();
    Assertions.assertEquals("A", result);
  }

  @Test
  void test_9() {
    Object result = ClassQuery.ofNew()
            .setOuterClass("A")
            .getOuterClass();
    Assertions.assertEquals("A", result);
  }

  @Test
  void test_10() {
    Object result = ClassQuery.ofNew()
            .setOuterMethod("A")
            .getOuterMethod();
    Assertions.assertEquals("A", result);
  }

  @Test
  void test_11() {
    Object result = ClassQuery.ofNew()
            .setOuterMethodDesc("()V")
            .getOuterMethodDesc();
    Assertions.assertEquals("()V", result);
  }

  @Test
  void test_12() {
    Object result = ClassQuery.ofNew()
            .setNestHostClass("java/lang/Object")
            .getNestHostClass();
    Assertions.assertEquals("java/lang/Object", result);
  }

  @Test
  void test_13() {
    Object result = ClassQuery.ofNew()
            .setNestMembers()
            .addNestMembers("java/lang/Object")
            .addNestMembers("java/lang/String")
            .removeNestMembers("java/lang/String")
            .getNestMembers();
    Assertions.assertEquals(
            ArrayListHelper.of("java/lang/Object"),
            result
    );
  }

  @Test
  void test_14() {
    Object result = ClassQuery.ofNew()
            .setPermittedSubclasses()
            .addPermittedSubclass("java/lang/Object")
            .addPermittedSubclass("java/lang/String")
            .removePermittedSubclass("java/lang/String")
            .getPermittedSubclasses();
    Assertions.assertEquals(
            ArrayListHelper.of("java/lang/Object"),
            result
    );
  }

  @Test
  void test_15() {
    Object result = ClassQuery.ofNew()
            .addMethod(0, "methodName", "()V", null, null)
            .selectMethod("methodName", "()V")
            .getNode();
    Assertions.assertNotNull(result);
  }

  @Test
  void test_16() {
    Object result = ClassQuery.ofNew()
            .addField(0, "fieldName", "I", null, null)
            .selectField("fieldName", "I");
    Assertions.assertNotNull(result);
  }
}
