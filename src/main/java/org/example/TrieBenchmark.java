/*
 * Copyright (c) 2005, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package org.example;

import org.openjdk.jmh.annotations.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class TrieBenchmark {

    private static String generateString() {
        String symbols = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder result = new StringBuilder();
        IntStream integers = new Random().ints(320, 0, symbols.length());
        int[] numbers = integers.toArray();
        integers.close();
        for (int number : numbers) {
            result.append(symbols.toCharArray()[number]);
        }
        return result.toString();
    }

    @State(Scope.Benchmark)
    public static class TrieState {
        @Setup(Level.Invocation)
        public void doSetup() {
            trie.clear();
            findString="";
            int random = new Random().nextInt(10000);
            for (int i = 0; i < 10000; i++) {
                String string = generateString();
                if (i == random)
                    findString = string;
                trie.add(string);
            }
        }
        private Trie trie = new Trie();
        private String findString = "";
    }

    @State(Scope.Thread)
    public static class SetState {
        @Setup (Level.Iteration)
        public void doSetup(){
            hashSet.clear();
            findString = "";
            int random = new Random().nextInt(10000);
            for (int i=0; i<10000; i++) {
                String string = generateString();
                if (i == random)
                    findString = string;
                hashSet.add(string);
            }
        }
        private HashSet<String> hashSet = new HashSet<>();
        private String findString = "";
    }

    @State(Scope.Benchmark)
    public static class ArrayState {
        @Setup (Level.Iteration)
        public void doSetup(){
            arrayList.clear();
            findString="";
            int random = new Random().nextInt(10000);
            for (int i=0; i<10000; i++) {
                String string = generateString();
                if (i == random)
                    findString = string;
                arrayList.add(string);
            }
        }
        private ArrayList<String> arrayList = new ArrayList<>();
        private String findString = "";
    }

    @Benchmark @Fork (1) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.MICROSECONDS) @Measurement(iterations = 10)
    public void testArray(ArrayState state) {
        state.arrayList.contains(state.findString);
    }
    @Benchmark @Fork (1) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.MICROSECONDS) @Measurement(iterations = 10)
    public void testSet(SetState state) {
        state.hashSet.contains(state.findString);
    }
    @Benchmark @Fork (1) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.MICROSECONDS) @Measurement(iterations = 10)
    public void testTrie(TrieState state) {
        state.trie.contains(state.findString);
    }
}
