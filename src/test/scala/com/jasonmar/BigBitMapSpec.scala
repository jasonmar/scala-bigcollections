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

class BigBitMapSpec extends UnitSpec {

  "A BigBitMap" should "perform bitwise AND operations" in {
    val n = 16
    val bigBitMap0 = new BigBitMap(n)
    val bigBitMap1 = new BigBitMap(n)

    bigBitMap0.set(0L)
    bigBitMap0.set(15L)
    bigBitMap1.set(15L)

    val bigBitMap2 = bigBitMap0 & bigBitMap1

    bigBitMap2.get(0L)  should be (right = false)
    bigBitMap2.get(15L) should be (right = true)

  }

  it should "perform bitwise XOR operations" in {
    val n = 16
    val bigBitMap0 = new BigBitMap(n)
    val bigBitMap1 = new BigBitMap(n)

    bigBitMap0.set(0L)
    bigBitMap0.set(15L)
    bigBitMap1.set(14L)
    bigBitMap1.set(15L)

    val bigBitMap2 = bigBitMap0 ^ bigBitMap1

    bigBitMap2.get(0L)  should be (right = true)
    bigBitMap2.get(1L)  should be (right = false)
    bigBitMap2.get(14L) should be (right = true)
    bigBitMap2.get(15L) should be (right = false)

  }

  it should "perform bitwise OR operations" in {
    val n = 16
    val bigBitMap0 = new BigBitMap(n)
    val bigBitMap1 = new BigBitMap(n)

    bigBitMap0.set(0L)
    bigBitMap0.set(15L)
    bigBitMap1.set(15L)

    val bigBitMap2 = bigBitMap0 | bigBitMap1

    bigBitMap2.get(0L)  should be (right = true)
    bigBitMap2.get(1L)  should be (right = false)
    bigBitMap2.get(15L) should be (right = true)

  }

  it should "throw ArrayIndexOutOfBoundsException for an invalid index" in {
    val n = 16
    val bigBitMap = new BigBitMap(n)

    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      bigBitMap.get(n)
    }

    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      bigBitMap.get(-1)
    }

    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      bigBitMap.set(n)
    }

    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      bigBitMap.set(-1)
    }

    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      bigBitMap.unset(n)
    }

    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      bigBitMap.unset(-1)
    }

  }

  it should "throw ArrayIndexOutOfBoundsException for bitwise operations on BigBitMaps of unequal size" in {
    val bigBitMap0 = new BigBitMap(16)
    val bigBitMap1 = new BigBitMap(32)

    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      val bigBitMap2 = bigBitMap0 & bigBitMap1
    }

    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      val bigBitMap2 = bigBitMap0 ^ bigBitMap1
    }

    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      val bigBitMap2 = bigBitMap0 | bigBitMap1
    }

  }


}
