/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.structures;

/**
 * 图转树算法相关 
 *  
 * @author ERON_AMD
 */
public class GraphToTree {
    // Function to find root of the vertex

    static int find(int x, int a[],
            int vis[], int root[]) {
        if (vis[x] == 1) {
            return root[x];
        }

        vis[x] = 1;
        root[x] = x;
        root[x] = find(a[x], a, vis, root);
        return root[x];
    }

// Function to convert directed graph into tree
    static void Graph_to_tree(int a[], int n) {
        // Vis array to check if an index is
        // visited or not root[] array is to
        // store parent of each vertex
        int[] vis = new int[n];
        int[] root = new int[n];

        // Find parent of each parent
        for (int i = 0; i < n; i++) {
            find(a[i], a, vis, root);
        }

        // par stores the root of the resulting tree
        int par = -1;
        for (int i = 0; i < n; i++) {
            if (i == a[i]) {
                par = i;
            }
        }

        // If no self loop exists
        if (par == -1) {
            for (int i = 0; i < n; i++) {

                // Make vertex in a cycle as root of the tree
                if (i == find(a[i], a, vis, root)) {
                    par = i;
                    a[i] = i;
                    break;
                }
            }
        }

        // Remove all cycles
        for (int i = 0; i < n; i++) {
            if (i == find(a[i], a, vis, root)) {
                a[i] = par;
            }
        }

        // Print the resulting array
        for (int i = 0; i < n; i++) {
            System.out.print(a[i] + " ");
        }
    }

    // Driver Code
    static public void main(String[] arr) {
        int a[] = {6, 6, 0, 1, 4, 3, 3, 4, 0};

        int n = a.length;

        // Function call
        Graph_to_tree(a, n);
    }
}











