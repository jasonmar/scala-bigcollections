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

  def getChildren: IndexedSeq[Array[Double]] = {
    if (_children != null) {
      if (_children.length == nChildren) _children
      else throw new IllegalArgumentException
    } else {
      val a = new Array[Array[Double]](nChildren)
      var i = 0
      while (i < nChildren - 1) {
        a(i) = new Array[Double](Int.MaxValue)
        i += 1
      }
      if (length > Int.MaxValue) {
        val size = (length % Int.MaxValue).toInt
        a(i) = new Array[Double](size)
      } else {
        a(i) = new Array[Double](length.toInt)
      }

      a.toIndexedSeq
    }
  }

  override val children: IndexedSeq[Array[Double]] = getChildren

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
    var i = 0
    while (i < a.length) {
      val src = children(i)
      val b = new Array[Double](src.length)
      var j = 0
      while (j < b.length) {
        b(j) = src(j)
        j += 1
      }
      a(i) = b
      i += 1
    }
    new BigVector(length, _children = a.toIndexedSeq)
  }

}
