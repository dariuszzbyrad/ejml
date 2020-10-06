/*
 * Copyright (c) 2009-2020, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Efficient Java Matrix Library (EJML).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ejml.concurrency;

import java.util.ArrayList;
import java.util.List;

/**
 * Thread safe way to recycle work arrays and maximize memory reuse
 *
 * @author Peter Abeles
 */
public class LWorkArrays implements WorkArrays {
	List<long[]> storage = new ArrayList<>();
	int length;

	public LWorkArrays(int length) {
		this.length = length;
	}

	public LWorkArrays() {
	}

	/**
	 * Checks to see if the stored arrays have the specified length. If not the length is changed and old
	 * arrays are purged
	 * @param length Desired array length
	 */
	@Override
	public synchronized void reset( int length ) {
		if( this.length != length ) {
			this.length = length;
			storage.clear();
		}
	}

	/**
	 * If there are arrays in storage one of them is returned, otherwise a new array is returned
	 */
	public synchronized long[] pop() {
		if( storage.isEmpty() ) {
			return new long[length];
		} else {
			return storage.remove(storage.size()-1);
		}
	}

	/**
	 * Adds the array to storage. if the array length is unexpected an exception is thrown
	 * @param array array to be recycled.
	 */
	public synchronized void recycle( long[] array ) {
		if( array.length != length ) {
			throw new IllegalArgumentException("Unexpected array length. Expected "+length+" found "+array.length);
		}
		storage.add(array);
	}

	/**
	 * Length of arrays returned
	 */
	@Override
	public int length() {
		return length;
	}
}
