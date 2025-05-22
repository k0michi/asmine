package com.koyomiji.asmine.test;

import com.koyomiji.asmine.common.AttributeBuilder;
import com.koyomiji.asmine.common.AttributeHelper;
import com.koyomiji.asmine.tree.FieldNodeHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.FieldNode;

public class FieldNodeHelperTest {
  @Test
  void test_clone_0() {
    FieldNode node = new FieldNode(0, "a", "I", null, null);
    node.access = 0;
    FieldNode cloned = FieldNodeHelper.clone(node);
    cloned.access = 1;

    Assertions.assertEquals(node.access, 0);
    Assertions.assertEquals(cloned.access, 1);
  }

  @Test
  void test_clone_1() {
    FieldNode node = new FieldNode(0, "a", "I", null, null);
    node.attrs = new java.util.ArrayList<>();
    node.attrs.add(new AttributeBuilder().setType("custom").setContent(new byte[] {0}).build());
    FieldNode cloned = FieldNodeHelper.clone(node);
    cloned.attrs.set(0, new AttributeBuilder().setType("custom").setContent(new byte[] {1}).build());

    Assertions.assertEquals(node.attrs.get(0).type, "custom");
    Assertions.assertArrayEquals(AttributeHelper.getContent(node.attrs.get(0)), new byte[] {0});
    Assertions.assertEquals(cloned.attrs.get(0).type, "custom");
    Assertions.assertArrayEquals(AttributeHelper.getContent(cloned.attrs.get(0)), new byte[] {1});
  }
}
