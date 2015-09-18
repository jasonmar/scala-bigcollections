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

trait BigCollection {
  
  val length: Long
  
  val children: Any

  val childSize: Int = 1073741824 // 2 ^ 30 by default

  final val nChildren = getChildId(length - 1) + 1

  final val powerOf2: Option[Int] = {
    if ((childSize & (childSize - 1)) == 0) {
      var exp = 1
      while ((1L << exp) != childSize && exp < 31) {
        exp += 1
      }
      Some(exp)
    } else {
      None
    }
  }

  final def getChildId(i: Long): Int = {
    if (i < 0) throw new ArrayIndexOutOfBoundsException
    else if (i >= length) throw new ArrayIndexOutOfBoundsException

    if (powerOf2.nonEmpty) {
      (i >> powerOf2.get).toInt
    } else {
      (i / childSize).toInt
    }
  }

  final def getChildInternalId(i: Long): Int = {
    if (i < childSize) {
      i.toInt
    } else {
      (i % childSize).toInt
    }
  }

  def compareSize(x: BigCollection): Unit = {
    if (length != x.length) {
      throw new ArrayIndexOutOfBoundsException
    }
  }
  
  def verifyChildSizes(childLengths: IndexedSeq[Int]): Unit = {
    if (length <= childSize) {
      assert(childLengths(0) == length)
    } else {
      for (i <- 0 to nChildren - 2) {
        assert(childLengths(i) == childSize)
      }
      assert(childLengths(nChildren - 1) == length % childSize)
    }
  }

}
