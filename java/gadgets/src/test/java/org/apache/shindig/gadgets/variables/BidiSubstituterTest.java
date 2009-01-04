/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.shindig.gadgets.variables;

import junit.framework.TestCase;

import org.apache.shindig.gadgets.spec.MessageBundle;

public class BidiSubstituterTest extends TestCase {

  public void testBidiWithRtl() {
    assertRightToLeft(MessageBundle.Direction.RTL);
  }

  public void testBidiWithLtr() {
    assertLeftToRight(MessageBundle.Direction.LTR);
  }

  public void testBidiWithEmpty() {
    assertLeftToRight(null);
  }

  public void testBidiWithNull() {
    assertLeftToRight(null);
  }

  private void assertRightToLeft(MessageBundle.Direction direction) {
    assertSubstitutions(direction, BidiSubstituter.RIGHT,
        BidiSubstituter.LEFT, MessageBundle.Direction.RTL, MessageBundle.Direction.LTR);
  }

  private void assertLeftToRight(MessageBundle.Direction direction) {
    assertSubstitutions(direction, BidiSubstituter.LEFT,
        BidiSubstituter.RIGHT, MessageBundle.Direction.LTR, MessageBundle.Direction.RTL);
  }

  private void assertSubstitutions(MessageBundle.Direction direction,
      String startEdge, String endEdge, MessageBundle.Direction dir, MessageBundle.Direction reverseDir) {
    Substitutions substitutions = new Substitutions();
    BidiSubstituter.addSubstitutions(substitutions, direction);

    assertEquals(startEdge, substitutions.getSubstitution(
        Substitutions.Type.BIDI, BidiSubstituter.START_EDGE));
    assertEquals(endEdge, substitutions.getSubstitution(
        Substitutions.Type.BIDI, BidiSubstituter.END_EDGE));
    assertEquals(dir.toString(), substitutions.getSubstitution(
        Substitutions.Type.BIDI, BidiSubstituter.DIR));
    assertEquals(reverseDir.toString(), substitutions.getSubstitution(
        Substitutions.Type.BIDI, BidiSubstituter.REVERSE_DIR));
  }
}
