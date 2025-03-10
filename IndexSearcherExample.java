import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * IndexSearcherExample.java searches the index and prints the results to the
 * console.
 * <p>
 * This program provides a basic example of how to search a Lucene index.
 * You can extend it to implement more advanced search features.
 * 
 * @implNote Ensure Index Exists: Make sure you have already run the
 *           TextFileIndexer program to create the Lucene index.
 * 
 * @version March 2025
 * 
 */
public class IndexSearcherExample {
    final static int MAXRESULTS = 5; // Specifies the max number of results to retrieve

    public static void main(String[] args) {
        // prompt the user for their query
        System.out.print("Enter your query: ");
        Scanner scanner = new Scanner(System.in);
        String searchQuery = scanner.nextLine();
        scanner.close();

        String indexDirPath = "index"; // Replace with your index directory

        try {
            searchIndex(indexDirPath, searchQuery);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param indexDirPath Path to the directory to contains the indices
     * @param searchQuery  The uer's query
     * @throws IOException
     * @throws ParseException
     */
    public static void searchIndex(String indexDirPath, String searchQuery) throws IOException, ParseException {
        // Open the Lucene index directory using FSDirectory.
        Directory indexDir = FSDirectory.open(Paths.get(indexDirPath));

        // Create an IndexReader to read the index.
        IndexReader reader = DirectoryReader.open(indexDir);

        // Create an IndexSearcher to perform searches.
        IndexSearcher searcher = new IndexSearcher(reader);

        // Create a StandardAnalyzer for query parsing.
        StandardAnalyzer analyzer = new StandardAnalyzer();

        // Create a QueryParser to parse the search query. It searches the content
        // field.
        QueryParser parser = new QueryParser("content", analyzer);
        // Parse the search query.
        Query query = parser.parse(searchQuery);

        // Performs the search using searcher.search().
        TopDocs results = searcher.search(query, MAXRESULTS);
        ScoreDoc[] hits = results.scoreDocs;

        System.out.println("Found " + hits.length + " hits.");

        // Iterate through the search results (ScoreDoc[] hits).
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;

            // Retrieve the Document using searcher.doc().
            Document d = searcher.doc(docId);

            // Extract the file's content, filename, and path
            String content = d.get("content");
            String filename = d.get("filename");
            String filepath = d.get("filepath");

            System.out.println((i + 1) + ". " + filename + " (Path: " + filepath + ")");

            // Find the query in the content
            int queryIndex = content.toLowerCase().indexOf(searchQuery.toLowerCase()); // Case-insensitive search
            if (queryIndex != -1) {
                // Display a snippet of the content surrounding the query
                int snippetStart = Math.max(0, queryIndex - 50);
                int snippetEnd = Math.min(content.length(), queryIndex + searchQuery.length() + 50);
                String snippet = content.substring(snippetStart, snippetEnd);
                System.out.println("   Snippet: ..." + snippet.trim() + "...");
            } else {
                System.out.println("   Query not found in content snippet.");
            }
        }

        // Close the IndexReader
        reader.close();
    }
}
