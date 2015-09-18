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

class BigBitMap (_length: Long, initialValue: Byte = 0, _children: Array[BitMap] = null) extends BigCollection {

  override val length = _length

  override val children: Array[BitMap] = {
    if (_children != null) {
      // Allow construction from preallocated sub-arrays
      if (_children.length == nChildren) {
        verifyChildSizes(_children.map{_.length.toInt})
        _children
      } else {
        throw new IllegalArgumentException
      }
    } else {
      val a = new Array[BitMap](nChildren)

      if (length <= childSize) {
        // Only need one BitMap
        a(0) = new BitMap(length.toInt)
      } else {
        for (i <- 0 to nChildren - 2) {
          // Allocate full size BitMap
          a(i) = new BitMap(childSize.toInt)
        }
        // Last BitMap may be smaller
        a(nChildren - 1) = new BitMap((length % childSize).toInt)
      }
      a.toIndexedSeq
      a
    }
  }

  def get(i: Long): Boolean = {
    val id0: Int = getChildId(i)
    val id1: Long = getChildInternalId(i)
    children(id0).get(id1)
  }

  def set(i: Long): Unit = {
    val id0: Int = getChildId(i)
    val id1: Long = getChildInternalId(i)
    children(id0).set(id1)
  }

  def unset(i: Long): Unit = {
    val id0: Int = getChildId(i)
    val id1: Long = getChildInternalId(i)
    children(id0).unset(id1)
  }

  def scard: Long = {
    var bitMapId = 0
    var c: Long = 0L
    while (bitMapId < nChildren) {
      c += children(bitMapId).scard
      bitMapId += 1
    }
    c
  }

  def flip(): Unit = {
    var bitMapId = 0
    while (bitMapId < nChildren) {
      children(bitMapId).flip()
      bitMapId += 1
    }
  }

  def clear(): Unit = {
    var bitMapId = 0
    while (bitMapId < nChildren) {
      children(bitMapId).clear()
      bitMapId += 1
    }
  }

  def &= (x: BigBitMap): Unit = {
    compareSize(x)
    var i = 0
    while (i < nChildren) {
      children(i) &= x.children(i)
      i += 1
    }
  }

  def ^= (x: BigBitMap): Unit = {
    compareSize(x)
    var i = 0
    while (i < nChildren) {
      children(i) ^= x.children(i)
      i += 1
    }
  }

  def |= (x: BigBitMap): Unit = {
    compareSize(x)
    var i = 0
    while (i < nChildren) {
      children(i) |= x.children(i)
      i += 1
    }
  }

  def & (x: BigBitMap): BigBitMap = {
    compareSize(x)
    val b = clone
    var i = 0
    while (i < nChildren) {
      b.children(i) &= x.children(i)
      i += 1
    }
    b
  }

  def ^ (x: BigBitMap): BigBitMap = {
    compareSize(x)
    val b = clone
    var i = 0
    while (i < nChildren) {
      b.children(i) ^= x.children(i)
      i += 1
    }
    b
  }

  def | (x: BigBitMap): BigBitMap = {
    compareSize(x)
    val b = clone
    var i = 0
    while (i < nChildren) {
      b.children(i) |= x.children(i)
      i += 1
    }
    b
  }

  override def clone: BigBitMap = {
    val a = new Array[BitMap](nChildren)
    for (i <- a.indices) {
      a(i) = children(i).clone
    }
    new BigBitMap(length, _children = a)
  }

}
