package ch.nostromo.tiffanys.uci.controller;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UciControllerTest {

    @Test
    public void test() {
        UciController testee = new UciController();
        
        assertEquals("10", testee.getCmdVal("word pos 10", "pos"));
        assertEquals("20", testee.getCmdVal("word pos 20 hallo 5", "pos"));
        assertNull(testee.getCmdVal("hello world", "globi"));
    }

}
