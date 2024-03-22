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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

/**
 * A reader that reads a text input that may have been rotated.
 * <p>
 * A simple header line is used to determine whether the text has been rotated.
 * If no header is specified, text is assumed to be rotated.
 *
 * @author Carl Harris
 */
public class Rot13Reader extends Reader {

    private final Reader delegate;  // 代表
    private final String header;
    private boolean needsRotation;  // 需要旋转
    private boolean started;

    /**
     * A reader that unconditionally performs a ROT-13 operation on all input that
     * appears on the given reader.
     *
     * @param input the subject input
     */
    public Rot13Reader(Reader input) {
        this(input, null);
    }

    /**
     * A reader that performs a ROT-13 operation on input that appears on the given
     * reader, assuming that the first (newline terminated) line of the file exactly
     * matches the given header. If the first line of the file does not match the
     * given header string, the resulting reader does not rotate the text.
     * <p>
     * The header, if present, does not appear as input from the resulting reader.
     *
     * @param input  the subject input
     * @param header header to match
     */
    public Rot13Reader(Reader input, String header) {
        this.delegate = input.markSupported() ? input : new BufferedReader(input);
        this.header = header;
        this.needsRotation = header == null;
    }

    @Override
    public int read() throws IOException {
        if (!needsRotation()) {
            return delegate.read();
        }
        int c = delegate.read();
        if (c != -1) {
            c = Rot13.rotate((char) c);
        }
        return c;
    }

    @Override
    public int read(CharBuffer target) throws IOException {
        if (!needsRotation()) {
            return delegate.read(target);
        }
        int offset = target.arrayOffset();
        int length = delegate.read(target);
        if (length > 0) {
            Rot13.rotate(target, offset, length);
        }
        return length;
    }

    @Override
    public int read(char[] buf) throws IOException {
        if (!needsRotation()) {
            return delegate.read(buf);
        }
        int length = delegate.read(buf);
        if (length > 0) {
            Rot13.rotate(buf, 0, length);
        }
        return length;
    }

    @Override
    public int read(char[] buf, int off, int len) throws IOException {
        if (!needsRotation()) {
            return delegate.read(buf, off, len);
        }
        int length = delegate.read(buf, off, len);
        if (length > 0) {
            Rot13.rotate(buf, 0, length);
        }
        return length;
    }

    @Override
    public long skip(long n) throws IOException {
        needsRotation();
        return super.skip(n);
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }

    @Override
    public boolean ready() throws IOException {
        return delegate.ready();
    }

    @Override
    public boolean markSupported() {
        return delegate.markSupported();
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        delegate.mark(readAheadLimit);
    }

    @Override
    public void reset() throws IOException {
        delegate.reset();
    }

    private boolean needsRotation() throws IOException {
        if (needsRotation)
            return true;
        if (started)
            return false;
        started = true;
        final int length = header.length();
        delegate.mark(length);
        int i = 0;
        int c = delegate.read();
        while (c != -1 && i < length && header.charAt(i) == c) {
            i++;
            c = delegate.read();
        }
        if (i < length) {
            delegate.reset();
            return false;
        }

        if (c == '\r') {
            c = delegate.read();
        }
        needsRotation = c == '\n';
        if (!needsRotation) {
            delegate.reset();
        }
        delegate.mark(0);
        return needsRotation;
    }

}




