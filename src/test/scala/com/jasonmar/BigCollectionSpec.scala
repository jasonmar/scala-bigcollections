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

class BigCollectionSpec extends UnitSpec {

  "A BigCollection" should "select the correct SubCollection ID for a given index" in {
    val bigCollection = new BigCollection {
      override val length = 8L
      override val children = null
    }

    bigCollection.getChildId(0) should be (0)

    var x = 1
    while (x < 100) {
      bigCollection.getChildId(Int.MaxValue.toLong * x - 1L) should be (x - 1)
      bigCollection.getChildId(Int.MaxValue.toLong * x)      should be (x)
      bigCollection.getChildId(Int.MaxValue.toLong * x + 1L) should be (x)
      x += 1
    }
  }

  it should "select the correct sub-index for a given bit index" in {
    val bigCollection = new BigCollection {
      override val length = 8L
      override val children = null
    }

    bigCollection.getChildInternalId(0L) should be (0)
    bigCollection.getChildInternalId(1L) should be (1)
    bigCollection.getChildInternalId(Int.MaxValue.toLong - 1L) should be (Int.MaxValue - 1)

    var x = 0
    while (x < 100) {
      bigCollection.getChildInternalId(Int.MaxValue.toLong + x) should be (x)
      x += 1
    }

  }


}
