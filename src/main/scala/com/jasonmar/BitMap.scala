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

import java.nio.ByteBuffer

class BitMap (_length: Long, initialValue: Byte = 0, _buf: ByteBuffer = null) extends BigCollection {

  override val length = _length

  override val nChildren: Int = getByteCount(length)

  def getBuf: ByteBuffer = {
    if (_buf != null) {
      if (_buf.capacity == nChildren) _buf
      else throw new IllegalArgumentException
    } else {
      ByteUtil.getByteBuffer(nChildren, initialValue)
    }
  }

  val buf = getBuf

  override val children: Array[Byte] = buf.array()

  override def getChildInternalId(i: Long): Int = {
    checkBounds(i)
    if (i < 8L) 0
    else {
      (i >> 3).toInt
    }
  }

  def getByteCount(nBits: Long): Int = {
    if (nBits > Int.MaxValue * 8L) throw new IllegalArgumentException
    else if (nBits <= 0L) throw new IllegalArgumentException
    else ((nBits - 1L >> 3) + 1L).toInt
  }

  def scard: Long = {
    var byteId = 0
    var bitId = 0
    var c = 0L

    while (byteId < children.length) {
      bitId = 0
      while (bitId < 8) {
        if ((children(byteId) & ByteUtil.bits(bitId)) == ByteUtil.bits(bitId)) {
          c += 1L
        }
        bitId += 1
      }
      byteId += 1
    }
    c
  }

  def flip(): Unit = {
    var byteId = 0
    while (byteId < children.length) {
      flip(byteId)
      byteId += 1
    }
  }

  private def flip(byteId: Int): Unit = {
    children(byteId) = (children(byteId) ^ 0xFF).toByte
  }

  private def get(byteId: Int, bitMask: Int): Boolean = {
    (children(byteId) & bitMask) == bitMask
  }

  private def set(byteId: Int, bitId: Int): Unit = {
    children(byteId) = (children(byteId) | ByteUtil.bits(bitId)).toByte
  }

  private def unset(byteId: Int, bitId: Int): Unit = {
    children(byteId) = (children(byteId) & ByteUtil.mask(bitId)).toByte
  }

  def getBitId(i: Long): Int = (i % 8).toInt

  def get(i: Long): Boolean = get(getChildInternalId(i), ByteUtil.bits(getBitId(i)))

  def set(i: Long): Unit = set(getChildInternalId(i), getBitId(i))

  def unset(i: Long): Unit = unset(getChildInternalId(i), getBitId(i))

  def clear(): Unit = {
    var i = 0
    while (i < nChildren) {
      children(i) = 0
      i += 1
    }
  }

  def &= (x: BitMap): Unit = {
    compareSize(x)
    buf.clear()
    x.buf.clear()
    val read = buf.asReadOnlyBuffer()
    while (buf.hasRemaining) {
      buf.put((read.get() & x.buf.get()).toByte)
    }
  }

  def ^= (x: BitMap): Unit = {
    compareSize(x)
    buf.clear()
    x.buf.clear()
    val read = buf.asReadOnlyBuffer()
    while (buf.hasRemaining) {
      buf.put((read.get() ^ x.buf.get()).toByte)
    }
  }

  def |= (x: BitMap): Unit = {
    compareSize(x)
    buf.clear()
    x.buf.clear()
    val read = buf.asReadOnlyBuffer()
    while (buf.hasRemaining) {
      buf.put((read.get() | x.buf.get()).toByte)
    }
  }

  def & (x: BitMap): BitMap = {
    compareSize(x)
    buf.clear()
    val b = clone
    b &= x
    b
  }

  def ^ (x: BitMap): BitMap = {
    compareSize(x)
    buf.clear()
    val b = clone
    b ^= x
    b
  }

  def | (x: BitMap): BitMap = {
    compareSize(x)
    buf.clear()
    val b = clone
    b |= x
    b
  }

  override def clone: BitMap = new BitMap(length, _buf = buf.duplicate())

}