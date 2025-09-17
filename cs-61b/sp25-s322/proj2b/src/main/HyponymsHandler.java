package main;
import java.util.*;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;

public class HyponymsHandler extends NgordnetQueryHandler {
    private final WordNet wordNet;

    public HyponymsHandler(WordNet wn) {
        wordNet = wn;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        String word = words.getFirst();
        Set<String> hyponyms = wordNet.getHyponyms(word);
        return hyponyms.toString();
    }
}
