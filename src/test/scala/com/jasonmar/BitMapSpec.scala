/*
 * Copyright (C) 2015 Jason Mar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.jasonmar

class BitMapSpec extends UnitSpec {

  "A BitMap" should "get individual bits correctly" in {
    val n = 8
    val bitMap0 = new BitMap(n)
    val bitMap1 = new BitMap(n,0xFF.toByte)

    var i = 0
    while (i < n) {
      bitMap0.get(i) should be (right = false)
      bitMap1.get(i) should be (right = true)
      i += 1
    }
  }

  it should "set individual bits correctly" in {
    val n = 32
    val bitMap = new BitMap(n)

    var i = 0
    while (i < n) {
      bitMap.set(i)
      i += 1
    }

    i = 0
    while (i < n) {
      bitMap.get(i) should be (right = true)
      i += 1
    }
  }

  it should "unset individual bits correctly" in {
    val n = 32
    val bitMap = new BitMap(n)

    var i = 0
    while (i < n) {
      bitMap.set(i)
      bitMap.get(i) should be (right = true)
      bitMap.unset(i)
      bitMap.get(i) should be (right = false)
      i += 1
    }
  }

  "A BitMap" should "flip" in {
    val n = 32
    val bitMap0 = new BitMap(n)

    var i = 0
    bitMap0.flip()
    while (i < n) {
      bitMap0.get(i) should be (right = true)
      i += 1
    }
  }

  it should "throw ArrayIndexOutOfBoundsException for an invalid index" in {
    val n = 32
    val bitMap = new BitMap(n)

    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      bitMap.get(n)
    }

    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      bitMap.get(-1)
    }

    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      bitMap.set(n)
    }

    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      bitMap.set(-1)
    }

    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      bitMap.unset(n)
    }

    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      bitMap.unset(-1)
    }

  }

  it should "create a byte array of correct size for a an invalid index" in {
    val n = 32
    val bitMap = new BitMap(n)

    bitMap.getChildId(0) should be (0)
    bitMap.getChildId(7) should be (0)
    bitMap.getChildId(8) should be (1)

    var x = 1
    while (x < 100) {
      bitMap.getChildId(x * bitMap.childSize - 1) should be (x)
      bitMap.getChildId(x * bitMap.childSize) should be (x)
      bitMap.getChildId(x * bitMap.childSize + 1) should be (x + 1)
      x += 1
    }

  }

  it should "throw ArrayIndexOutOfBoundsException for an invalid index" in {
    val n = 32
    val bitMap = new BitMap(n)

    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      bitMap.getChildId(-1)
    }

    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      bitMap.getChildId(32)
    }

  }

}
