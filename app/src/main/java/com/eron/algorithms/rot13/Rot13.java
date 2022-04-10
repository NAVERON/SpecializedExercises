package com.eron.algorithms.rot13;

/*
 * File created on Apr 7, 2015
 *
 * Copyright (c) 2015 Carl Harris, Jr
 * and others as noted
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

import java.nio.CharBuffer;

/**
 * ROT-13 utility methods.
 *
 * @author Carl Harris
 */
public class Rot13 {

	/**
	 * Performs a ROT-13 operation on a character sequence to produce a string.
	 * 
	 * @param input the subject character sequence
	 * @return {@code input} with ASCII alphabetic characters rotated 13 positions
	 */
	public static String rotate(CharSequence input) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			sb.append(rotate(input.charAt(i)));
		}
		return sb.toString();
	}

	/**
	 * Performs an in-place ROT-13 operation on a character array.
	 * <p>
	 * ASCII alphabetic characters in the given buffer are rotated 13 positions.
	 * 
	 * @param buf target of the ROT-13 operation
	 */
	public static void rotate(char[] buf) {
		rotate(buf, 0, buf.length);
	}

	/**
	 * Performs an in-place ROT-13 operation on segment of a character array.
	 * <p>
	 * ASCII alphabetic characters in the given buffer are rotated 13 positions.
	 * 
	 * @param buf    target of the ROT-13 operation
	 * @param offset starting offset within {@code buf} which is the segment to be
	 *               rotated
	 * @param length length of the segment starting at {@code offset}
	 */
	public static void rotate(char[] input, int offset, int length) {
		for (int i = 0; i < length; i++) {
			input[offset + i] = rotate(input[offset + i]);
		}
	}

	/**
	 * Performs an in-place ROT-13 operation on a character buffer.
	 * <p>
	 * ASCII alphabetic characters in the given buffer are rotated 13 positions.
	 * 
	 * @param buf target of the ROT-13 operation
	 */
	public static void rotate(CharBuffer buf) {
		rotate(buf, 0, buf.length());
	}

	/**
	 * Performs an in-place ROT-13 operation on segment of a character buffer.
	 * <p>
	 * ASCII alphabetic characters in the given buffer are rotated 13 positions.
	 * 
	 * @param buf    target of the ROT-13 operation
	 * @param offset starting offset within {@code buf} which is the segment to be
	 *               rotated
	 * @param length length of the segment starting at {@code offset}
	 */
	public static void rotate(CharBuffer input, int offset, int length) {
		for (int i = 0; i < length; i++) {
			input.put(offset + i, rotate(input.charAt(offset + i)));
		}
	}

	/**
	 * Maps a single character to its ROT-13 equivalent.
	 * 
	 * @param c the subject character
	 * @return if {@code c} is an ASCII alphabetic character, the result is
	 *         {@code c} rotated 13 positions. Otherwise, the return value is
	 *         {@code c}.
	 */
	public static char rotate(char c) {
		// 整体上将26 字母且分为2部分, 前半段向后移, 后半段向前移 
		
		if (c >= 'a' && c <= 'm')
			c += 13;
		else if (c >= 'A' && c <= 'M')
			c += 13;
		else if (c >= 'n' && c <= 'z')
			c -= 13;
		else if (c >= 'N' && c <= 'Z')
			c -= 13;
		return c;
	}

}





