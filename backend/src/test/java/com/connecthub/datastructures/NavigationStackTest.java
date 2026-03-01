package com.connecthub.datastructures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NavigationStackTest {

    @Test
    public void testVisitAndGoBack() {
        NavigationStack stack = new NavigationStack();
        
        stack.visitProfile(101L, 201L);
        stack.visitProfile(101L, 202L);
        stack.visitProfile(101L, 203L);
        
        assertTrue(stack.hasHistory(101L));
        
        assertEquals(203L, stack.goBack(101L));
        assertEquals(202L, stack.goBack(101L));
        assertEquals(201L, stack.goBack(101L));
        
        // Stack is now empty
        assertNull(stack.goBack(101L));
        assertFalse(stack.hasHistory(101L));
    }
}
