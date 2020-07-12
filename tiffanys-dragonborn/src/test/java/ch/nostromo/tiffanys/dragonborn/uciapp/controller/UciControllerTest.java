package ch.nostromo.tiffanys.dragonborn.uciapp.controller;

import static org.junit.Assert.*;

import org.junit.Test;

public class UciControllerTest {

    @Test
    public void test() {
        UciController testee = new UciController();
        
        assertEquals("10", testee.getCmdVal("word pos 10", "pos"));
        assertEquals("20", testee.getCmdVal("word pos 20 hallo 5", "pos"));
        assertNull(testee.getCmdVal("hello world", "globi"));
    }

}
