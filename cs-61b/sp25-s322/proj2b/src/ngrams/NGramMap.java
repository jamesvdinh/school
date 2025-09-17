package ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.TreeMap;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {
    private TreeMap<String, TimeSeries> wordMap;
    private TimeSeries totalCounts;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        this.wordMap = new TreeMap<>();
        this.totalCounts = new TimeSeries();

        In wordsFile = new In(wordsFilename);
        while (wordsFile.hasNextLine()) {
            String line = wordsFile.readLine();
            String[] row = line.split("\\s+");

            String word = row[0]; // ex. "airport"
            int year = Integer.parseInt(row[1]); // ex. 2007
            Double numAppearances = Double.parseDouble(row[2]); // ex. 175702

            wordMap.putIfAbsent(word, new TimeSeries()); // putIfAbsent only put() if no year found for that word
            wordMap.get(word).put(year, numAppearances); // stores year/count of word in the TimeSeries value
        }

        In countsFile = new In(countsFilename);
        while (countsFile.hasNextLine()) {
            String line = countsFile.readLine();
            String[] row = line.split(",");

            int year = Integer.parseInt(row[0]); // ex. 1470
            Double numWords = Double.parseDouble(row[1]); // ex. 984

            totalCounts.put(year, numWords); // store numWords of each year in totalCounts
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries wordTs = wordMap.get(word);
        if (wordTs == null) {
            return new TimeSeries(); // Return empty TimeSeries if word is missing
        }
        return new TimeSeries(wordTs, startYear, endYear);    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        return countHistory(word, TimeSeries.MIN_YEAR, TimeSeries.MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return new TimeSeries(totalCounts, TimeSeries.MIN_YEAR, TimeSeries.MAX_YEAR);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries wordTs = wordMap.get(word);
        if (wordTs == null) {
            return new TimeSeries();
        }
        return new TimeSeries(wordTs, startYear, endYear).dividedBy(totalCounts);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        return weightHistory(word, TimeSeries.MIN_YEAR, TimeSeries.MAX_YEAR);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries sumTs = new TimeSeries();
        for (String word : words) {
            TimeSeries wordTs = weightHistory(word, startYear, endYear);
            if (wordTs != null) {
                sumTs = sumTs.plus(wordTs);
            }
        }
        return sumTs;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        return summedWeightHistory(words, TimeSeries.MIN_YEAR, TimeSeries.MAX_YEAR);
    }
}
