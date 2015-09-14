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

object ByteUtil {

  /** This converts an unsigned int to a Byte
    * This is tricky because Java and Scala byte binary values don't map directly to positive integers 
   */
  def toByte(uint: Int): Byte = {
    assert(uint < 256 && uint >= 0)
    if (uint < 128 && uint >= 0) uint.toByte
    else if (uint >= 128 && uint < 256) (uint - 256).toByte
    else 0
  }

  /** This just gets an empty ByteBuffer
    */
  def getByteBuffer(n: Int, initialValue: Byte = 0): ByteBuffer = {
    val a = ByteBuffer.allocate(n)
    while (a.hasRemaining) {
      a.put(initialValue)
    }
    a
  }

  /** These are used with AND to check whether a specific bit is set
    * They are also used to set a specific bit with OR 
   */
  val bits: IndexedSeq[Int] = IndexedSeq(
    128, // 10000000
    64,  // 01000000
    32,  // 00100000
    16,  // 00010000
    8,   // 00001000
    4,   // 00000100
    2,   // 00000010
    1    // 00000001
  )
  
  /** These are used with AND to unset a specific bit
   * 
   */
  val mask: IndexedSeq[Int] = IndexedSeq(
    0x7F, // 01111111
    0xBF, // 10111111
    0xDF, // 11011111
    0xEF, // 11101111
    0xF7, // 11110111
    0xFB, // 11111011
    0xFD, // 11111101
    0xFE  // 11111110
  )

}