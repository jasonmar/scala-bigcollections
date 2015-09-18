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

class BigVector (_length: Long, _children: IndexedSeq[Array[Double]] = null) extends BigCollection {

  override val length = _length

  override val children: IndexedSeq[Array[Double]] = {
    if (_children != null) {
      if (_children.length == nChildren) {
        verifyChildSizes(_children.map{_.length})
        _children
      } else {
        throw new IllegalArgumentException
      }
    } else {

      val a = new Array[Array[Double]](nChildren)

      if (length <= childSize) {
        // Only need one Array
        a(0) = new Array[Double](length.toInt)
      } else {
        for (i <- 0 to nChildren - 2) {
          // Allocate full size Arrays
          a(i) = new Array[Double](childSize.toInt)
        }
        // Last Array may be smaller
        a(nChildren - 1) = new Array[Double]((length % childSize).toInt)
      }
      a.toIndexedSeq
    }
  }

  def get(i: Long): Double = children(getChildId(i))(getChildInternalId(i))

  def update(i: Long, x: Double): Unit = children(getChildId(i)).update(getChildInternalId(i),x)

  def :+= (x: BigVector): Unit = {
    var i = 0L
    while (i < length) {
      update(i, get(i) + x.get(i))
      i += 1L
    }
  }

  def :+= (x: BigBitMap): Unit = {
    var i = 0L
    while (i < length) {
      if (x.get(i)) update(i, get(i) + 1.0d)
      i += 1L
    }
  }

  def :/= (x: BigVector): Unit = {
    var i = 0L
    while (i < length) {
      update(i, get(i) / x.get(i))
      i += 1L
    }
  }

  def :/= (x: Double): Unit = {
    var i = 0L
    while (i < length) {
      update(i, get(i) / x)
      i += 1L
    }
  }

  def collect(seq: IndexedSeq[BigBitMap]): Unit = {
    seq.foreach(:+=)
    if (seq.nonEmpty) :/= (seq.length.toDouble)
  }

  override def clone(): BigVector = {
    val a = new Array[Array[Double]](nChildren)
    for (i <- a.indices) {
      val src = children(i)
      val b = new Array[Double](src.length)
      for (j <- b.indices) {
        b(j) = src(j)
      }
      a(i) = b
    }
    new BigVector(length, _children = a.toIndexedSeq)
  }

}
