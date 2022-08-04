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
package com.eron.algorithms.rot13;

import java.io.IOException;
import java.io.Writer;

/**
 * A {@link Writer} that rotates text before writing to an underlying delegate.
 *
 * @author Carl Harris
 */
public class Rot13Writer extends Writer {

	private final Writer delegate;
	private final String header;
	private boolean started;

	public Rot13Writer(Writer output) {
		this(output, null);
	}

	public Rot13Writer(Writer output, String header) {
		this.delegate = output;
		this.header = header;
	}

	@Override
	public void write(char[] buf, int off, int len) throws IOException {
		writeHeader();
		Rot13.rotate(buf, off, len);
		delegate.write(buf, off, len);
	}

	@Override
	public void flush() throws IOException {
		delegate.flush();
	}

	@Override
	public void close() throws IOException {
		delegate.close();
	}

	private void writeHeader() throws IOException {
		if (started)
			return;
		started = true;
		if (header == null)
			return;
		delegate.write(header.toCharArray());
		delegate.write(System.lineSeparator());
	}

}




