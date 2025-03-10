import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * TextFileIndexer.java creates a Lucene index in the specified indexDirPath of
 * the text files specified in dataDirPath
 * <p>
 * You can then use the Lucene search API to query the index and retrieve
 * documents based on their content or file names. This program provides a basic
 * framework for indexing .txt files.
 * 
 * You can extend it to handle other file types, add more metadata fields, and
 * implement more advanced indexing and search features.
 * 
 * @version March 2025
 * 
 */
public class TextFileIndexer {

    public static void main(String[] args) {
        String dataDirPath = "data"; // Replace with your directory
        String indexDirPath = "index"; // Replace with your index directory

        try {
            indexTextFiles(dataDirPath, indexDirPath);
            System.out.println("Indexing completed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * indexTextFiles Iterates over all of the .txt files in dataDirPath and calls
     * indexFile for
     * each one
     * 
     * @param dataDirPath  Path to the directory containing the .txt files
     * @param indexDirPath Path to the directory to write the indices in
     * @throws IOException
     */
    public static void indexTextFiles(String dataDirPath, String indexDirPath) throws IOException {
        // Open a FSDirectory (file system directory) to store the Lucene index.
        Directory indexDir = FSDirectory.open(Paths.get(indexDirPath));

        // Create a StandardAnalyzer for text analysis (tokenization, stemming, etc.).
        StandardAnalyzer analyzer = new StandardAnalyzer();

        // Create an IndexWriter to write documents to the index.
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(indexDir, config);

        // List all .txt files in the specified data directory.
        File dataDir = new File(dataDirPath);
        File[] files = dataDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        // Calls indexFile() for each .txt file.
        if (files != null) {
            for (File file : files) {
                indexFile(writer, file);
            }
        }

        // Close the IndexWriter.
        writer.close();
    }

    /**
     * indexFile Adds each individual .txt file to the index
     * 
     * @param writer The index writer
     * @param file   The file to be indexed
     * @throws IOException
     */
    private static void indexFile(IndexWriter writer, File file) throws IOException {
        // Creates a Lucene Document to represent a text file.
        Document document = new Document();

        // Reads the content of the file using a BufferedReader.
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            // Adds three fields to the document
            // content: The content of the file (using TextField for full-text indexing).
            document.add(new TextField("content", content.toString(), Field.Store.YES));
            // filename: The name of the file.
            document.add(new StringField("filename", file.getName(), Field.Store.YES));
            // filepath: The absolute path of the file.
            document.add(new StringField("filepath", file.getAbsolutePath(), Field.Store.YES));

            // Adds the document to the IndexWriter.
            writer.addDocument(document);
        }
    }
}