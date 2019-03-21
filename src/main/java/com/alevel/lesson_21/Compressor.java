package com.alevel.lesson_21;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class Compressor {
    protected static void CompressionText() throws Exception {
        String string = readingDataTextFromInputFile();
        Map<String, Integer> symbolRepetitions = buildSymbolRepetitionsMap(string);

        FileWriter frequencyTable = new FileWriter("src/main/java/frequencyTable.txt");
        for (Map.Entry<String, Integer> entry : symbolRepetitions.entrySet()) {
            frequencyTable.write(entry.getKey() + " : " + entry.getValue() + "\n");
        }
        frequencyTable.close();

        Map<String, Node> symbolBundleCodeHuffman = buildCodeHuffman(symbolRepetitions);

        FileWriter matchTable = new FileWriter("src/main/java/matchTable.txt");
        for (Map.Entry<String, Node> entry : symbolBundleCodeHuffman.entrySet()) {
            matchTable.write(entry.getKey() + " : " + entry.getValue().code + "\n");
        }
        matchTable.close();

        StringBuffer encodedString = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            String symbol = "" + string.charAt(i);
            encodedString.append(symbolBundleCodeHuffman.get(symbol).code);
        }

        FileWriter encodingText = new FileWriter("src/main/java/encodingText.txt");
        encodingText.write(encodedString.toString());
        encodingText.close();

        System.out.println("Encoding text :");
        System.out.println(encodedString.toString());
        System.out.println("Length of encoded string = " + encodedString.length());
    }

    protected static Map<String, Node> buildCodeHuffman(Map<String, Integer> symbolRepetitions) {
        Map<String, Node> symbolBundleCodeHuffman = new HashMap<>();
        Queue<Node> nodeQueue = new PriorityQueue<>();
        for (Map.Entry<String, Integer> entry : symbolRepetitions.entrySet()) {
            Node node = new Node(entry.getKey(), entry.getValue(), null, null);
            symbolBundleCodeHuffman.put(entry.getKey(), node);
            nodeQueue.add(node);
        }

        while (nodeQueue.size() > 1) {
            Node leftNode = nodeQueue.poll();
            Node rightNode = nodeQueue.poll();

            nodeQueue.offer(new Node(leftNode.getSymbol() + rightNode.getSymbol(),
                    leftNode.getWeight() + rightNode.getWeight(),
                    leftNode,
                    rightNode));
        }

        Node root = nodeQueue.poll();
        if (symbolRepetitions.size() == 1) {
            root.code = "0";
        } else {
            System.out.println("Table Huffman");
            System.out.println("symbol : code");
            root.buildCode("");
        }

        System.out.println("Huffman Tree");
        System.out.println(root.toString());

        return symbolBundleCodeHuffman;
    }

    protected static Map<String, Integer> buildSymbolRepetitionsMap(String string) {
        Map<String, Integer> symbolRepetitions = new HashMap<>();
        String symbol;
        int frequency;
        for (int i = 0; i < string.length(); i++) {
            symbol = "" + string.charAt(i);
            if (symbolRepetitions.containsKey(symbol)) {
                frequency = symbolRepetitions.get(symbol) + 1;
                symbolRepetitions.put(symbol, frequency);
            } else {
                symbolRepetitions.put(symbol, 1);
            }
        }

        System.out.println("symbol : frequency");
        for (Map.Entry<String, Integer> entry : symbolRepetitions.entrySet()) {
            System.out.println("   " + entry.getKey() + "   :     " + entry.getValue());
        }

        return symbolRepetitions;
    }

    protected static String readingDataTextFromInputFile() throws FileNotFoundException {
        FileReader input = new FileReader("src/main/java/input.txt");
        Scanner scanner = new Scanner(input);
        String string = scanner.nextLine();

        System.out.println("Input data text :");
        System.out.println(string);
        System.out.println("Length of input string = " + string.length());

        return string;
    }
}