import java.io.InputStream;
import java.util.*;

public class SentimentAnalysis {
    public static void main(String[] args) {
        final boolean PRINT_TREES = false;  // whether or not to print extra info about the maps.

        BSTMap<String, Integer> wordFreqs = new BSTMap<String, Integer>();
        BSTMap<String, Integer> wordTotalScores = new BSTMap<String, Integer>();
        Set<String> stopwords = new TreeSet<String>();

        System.out.print("Enter filename: ");
        Scanner scan = new Scanner(System.in);
        String filename = scan.nextLine();

        processFile(filename, wordFreqs, wordTotalScores);

        System.out.println("Number of words is: " + wordFreqs.size());
        System.out.println("Height of the tree is: " + wordFreqs.height());

        if (PRINT_TREES)
        {
            System.out.println("Preorder:  " + wordFreqs.preorderKeys());
            System.out.println("Inorder:   " + wordFreqs.inorderKeys());
            System.out.println("Postorder: " + wordFreqs.postorderKeys());
            printFreqsAndScores(wordFreqs, wordTotalScores);
        }

        removeStopWords(wordFreqs, wordTotalScores, stopwords);

        System.out.println("After removing stopwords:");
        System.out.println("Number of words is: " + wordFreqs.size());
        System.out.println("Height of the tree is: " + wordFreqs.height());

        if (PRINT_TREES)
        {
            System.out.println("Preorder:  " + wordFreqs.preorderKeys());
            System.out.println("Inorder:   " + wordFreqs.inorderKeys());
            System.out.println("Postorder: " + wordFreqs.postorderKeys());
            printFreqsAndScores(wordFreqs, wordTotalScores);
        }

        while (true)
        {
            System.out.print("\nEnter a new review to analyze: ");
            String line = scan.nextLine();
            if (line.equals("quit")) break;
            analyzeReview(line, wordFreqs, wordTotalScores, stopwords);
        }
    }

    /**
     * Read the file specified to add proper items to the word frequencies and word scores maps.
     */
    public static void processFile(String filename, BSTMap<String, Integer> wordFreqs, BSTMap<String, Integer> wordTotalScores) {
        InputStream is = SentimentAnalysis.class.getResourceAsStream(filename);

        int lineNum = 0;

        if (is == null) {
            System.err.println("Bad filename: " + filename);
            System.exit(1);
        }
        Scanner scan = new Scanner(is);

        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] words = line.split(" ");

            for(int i = 1; i < words.length; i++){
                if(wordFreqs.containsKey(words[i])){
                    //updates old values
                    wordFreqs.put(words[i], wordFreqs.get(words[i]) + 1);
                    wordTotalScores.put(words[i], wordTotalScores.get(words[i]) + Integer.parseInt(words[0]));
                }
                else {
                    //in case where this is a new word we just use put() with base values such as 1
                    //and Integer.parseInt(words[0]) gets us the review score at the beginning of the line
                    wordFreqs.put(words[i], 1);
                    wordTotalScores.put(words[i], Integer.parseInt(words[0]));
                }
            }
        }
        scan.close();
    }

    /**
     * Print a table of the words found in the movie reviews, along with their frequencies and total scores.
     * Hint: Call wordFreqs.inorderKeys() to get a list of the words, and then loop over that list.
     */
    public static void printFreqsAndScores(BSTMap<String, Integer> wordFreqs, BSTMap<String, Integer> wordTotalScores) {
        List<String> words = wordFreqs.inorderKeys();

        for (String word : words) {
            System.out.println("Word: " + word + ", " + "frequency: " + wordFreqs.get(word) + ", " + "total score: " + wordTotalScores.get(word));
        }
    }

    /**
     * Read the stopwords.txt file and add each word to the stopwords set.  Also remove each word from the
     * word frequencies and word scores maps.
     */
    public static void removeStopWords(BSTMap<String, Integer> wordFreqs, BSTMap<String, Integer> wordTotalScores, Set<String> stopwords) {
        InputStream is = SentimentAnalysis.class.getResourceAsStream("stopwords.txt");
        if (is == null) {
            System.err.println("Bad filename: " + "stopwords.txt");
            System.exit(1);
        }
        Scanner scan = new Scanner(is);

        while (scan.hasNextLine()) {
            String word = scan.nextLine();

            stopwords.add(word);
            wordFreqs.remove(word);
            wordTotalScores.remove(word);
        }
        scan.close();
    }

    /**
     * Analyze the given review, using the stored word frequency & score data, ignoring stopwords.
     */
    public static void analyzeReview(String line, BSTMap<String, Integer> wordFreqs, BSTMap<String, Integer> wordTotalScores, Set<String> stopwords) {
        String[] words = line.split(" ");
        float avgSentimentPerWord;
        float sentiment = 0;
        float avgSentiment;
        //keeps track of how many items added or items not ignored
        float count = 0;

        for (int i = 0; i < words.length; i++){
            //checks to make sure word exists as a key
            if(wordFreqs.containsKey(words[i])){
                //gets the average sentiment value of a word
                avgSentimentPerWord = ((float)wordTotalScores.get(words[i]) / (float)wordFreqs.get(words[i]));
                System.out.println("The average sentiment of " + words[i] + " " + "is " + avgSentimentPerWord);
                //adds single sentiment value of a word to the total sentiment value of the review
                sentiment += avgSentimentPerWord;
                count++;
            }
            else {
                if(stopwords.contains(words[i])){
                    System.out.println("Skipping " + words[i] + " " + "(stopword)");
                }
                else {
                    System.out.println("Skipping " + words[i] + " " + "never seen before");
                }
            }

        }
        avgSentiment = sentiment / count;
        System.out.println("Sentiment score for this review is " + avgSentiment);
    }
}
