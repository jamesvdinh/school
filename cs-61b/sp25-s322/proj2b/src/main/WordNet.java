package main;
import java.util.*;
import edu.princeton.cs.algs4.In;

public class WordNet {
    private final Map<Integer, Set<String>> idToSynsets;
    private final Map<String, Set<Integer>> wordToIds;
    private final DirectedGraph graph;

    public WordNet(String synsetFilename, String hyponymFilename) {
        idToSynsets = new HashMap<>();
        wordToIds = new HashMap<>();
        graph = new DirectedGraph();

        parseSynsets(synsetFilename);
        parseHyponyms(hyponymFilename);
    }

    private void parseSynsets(String filename) {
        In idFile = new In(filename);
        while (idFile.hasNextLine()) {
            String line = idFile.readLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            String[] words = parts[1].split(" ");
            Set<String> wordSet = new HashSet<>(Arrays.asList(words));
            idToSynsets.put(id, wordSet);
            for (String word : wordSet) {
                wordToIds.computeIfAbsent(word, k -> new HashSet<>()).add(id);
            }
        }
    }

    private void parseHyponyms(String filename) {
        In hypoFile = new In(filename);
        while (hypoFile.hasNextLine()) {
            String line = hypoFile.readLine();
            String[] parts = line.split(",");
            int synsetId = Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                int hypoId = Integer.parseInt(parts[i]);
                graph.addEdge(synsetId, hypoId);
            }
        }
    }

    public Set<String> getHyponyms(String word) {
        Set<Integer> ids = wordToIds.getOrDefault(word, Set.of());
        Set<Integer> allHypoIds = graph.getDescendants(ids);
        Set<String> res = new TreeSet<>();
        for (int id : allHypoIds) {
            Set<String> wordsFromID = idToSynsets.getOrDefault(id, Set.of());
            res.addAll(wordsFromID);
        }
        return res;
    }
}
