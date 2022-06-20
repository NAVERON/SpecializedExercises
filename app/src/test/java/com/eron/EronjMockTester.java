package com.eron;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.jupiter.api.Test;

import junit.framework.TestCase;

public class EronjMockTester extends TestCase {
    Mockery context = new Mockery();
    
    @Test 
    public void testJMock(){
        Receiver receiver = context.mock(Receiver.class);
        context.checking(new Expectations() {{
            oneOf(receiver).receive("just for test");
        }});
        receiver.receive("just for test");
        context.assertIsSatisfied();
    }
    private static interface Receiver {
        public void receive(String message);
    }
}
