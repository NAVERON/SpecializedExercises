package com.eron;

import org.junit.jupiter.api.Test;

import com.eron.algorithms.convexscan.GrahamConvexScanner;
import com.eron.structures.BinaryTree;

// JUnit 测试框架的使用 
public class EronSingleForTest {

    @Test 
    public void testGraham() {
        GrahamConvexScanner scanner = new GrahamConvexScanner();
        scanner.main(new String[] {});
        
        
    }
    
    @Test 
    public void testBinaryTree() {
        BinaryTree<Integer> tree = new BinaryTree<Integer>() {{
            add(14);
            add(23);
            add(1);
            add(56);
            add(5);
            add(10);
            add(44);
            add(90);
            add(3);
        }};
        
        tree.traversePreOrder(tree.root);
    }
    
    
}








