package org.projectodd.nodej.integration.javascript;

import java.io.UnsupportedEncodingException;

/*
 * Copyright 2013 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * @author Lance Ball
 */

public class BufferIntegrationTests extends AbstractJavascriptIntegrationTest {
    public static final String TEST_STRING = "Now is the winter of our discontent made glorious summer";
    public static byte[] UTF8_BYTE_STRING;
    public static byte[] ASCII_BYTE_STRING;
    
    public BufferIntegrationTests() throws UnsupportedEncodingException {
        super( "buffer/buffer_test.js" );
        UTF8_BYTE_STRING = TEST_STRING.getBytes("UTF-8");
        UTF8_BYTE_STRING = TEST_STRING.getBytes("US-ASCII");
    }
    
}
