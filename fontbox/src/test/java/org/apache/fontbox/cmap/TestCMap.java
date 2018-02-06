/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.fontbox.cmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import junit.framework.TestCase;

import static junit.framework.TestCase.assertEquals;
import org.apache.fontbox.ttf.CmapSubtable;
import org.apache.fontbox.ttf.TTFParser;
import org.apache.fontbox.ttf.TrueTypeFont;

/**
 * This will test the CMap implementation.
 *
 */
public class TestCMap extends TestCase 
{

    /**
     * Check whether the mapping is working correct.
     * @throws IOException If something went wrong during adding a mapping
     */
    public void testLookup() throws IOException
    {
        byte[] bs = new byte[1];
        bs[0] = (byte)200;

        CMap cMap = new CMap();
        cMap.addCharMapping(bs, "a");
        assertTrue("a".equals(cMap.toUnicode(200)));
    }

    /**
     * PDFBOX-3997: test unicode that is above the basic multilingual plane, here: helicopter
     * symbol, or D83D DE81 in the Noto Emoji font.
     *
     * @throws IOException
     */
    public void testPDFBox3997() throws IOException
    {
        InputStream is;
        try
        {
            System.out.println("Downloading Noto Emoji font...");
            is = new URL("https://issues.apache.org/jira/secure/attachment/12896461/NotoEmoji-Regular.ttf").openStream();
            System.out.println("Download finished!");
        }
        catch (IOException ex)
        {
            System.err.println("Noto Emoji font could not be downloaded, test skipped");
            return;
        }
        TrueTypeFont ttf = new TTFParser().parse(is);
        CmapSubtable cmap = ttf.getUnicodeCmap(false);
        assertEquals(886, cmap.getGlyphId(0x1F681));
        ttf.close();
    }
}
