# Appache Lucene Simple Indexer and Searcher 

**Before running:**
Add Lucene Libraries: You'll need to add the Apache Lucene libraries to your project. 
You can do this by manually downloading the JAR files and adding them to your classpath.
Repository contains .vscode/setting.json that has already added the JAR files to the classpath. 

Create (mkdir) an *index* directory.

***TextFileIndexer.java***
The program will create a Lucene index in the specified indexDirPath.
You can then use the Lucene search API to query the index and retrieve documents based on their content or file names.
This program provides a basic framework for indexing .txt files. You can extend it to handle other file types, add more metadata fields, and implement more advanced indexing and search features.


*indexTextFiles(String dataDirPath, String indexDirPath):*
- Opens a FSDirectory (file system directory) to store the Lucene index.
- Creates a StandardAnalyzer for text analysis (tokenization, stemming, etc.).
- Creates an IndexWriter to write documents to the index.
- Lists all .txt files in the specified data directory.
- Calls indexFile() for each .txt file.
- Closes the IndexWriter.


*indexFile(IndexWriter writer, File file):*
- Creates a Lucene Document to represent a text file.
- Reads the content of the file using a BufferedReader.
- Adds three fields to the document:
 1. content: The content of the file (using TextField for full-text indexing).
 2. filename: The name of the file.
 3. filepath: The absolute path of the file.
- Adds the document to the IndexWriter.



***IndexSearcherExample.java***
The program will search the index and print the results to the console.
This program provides a basic example of how to search a Lucene index. You can extend it to implement more advanced search features.

Ensure Index Exists: Make sure you have run the TextFileIndexer program to create the Lucene index.

*searchIndex(String indexDirPath, String searchQuery):*
- Opens the Lucene index directory using FSDirectory.
- Creates an IndexReader to read the index.
- Creates an IndexSearcher to perform searches.
- Creates a StandardAnalyzer for query parsing.
- Creates a QueryParser to parse the search query. It searches the content field.
- Parses the search query using parser.parse().
- Performs the search using searcher.search(). 
- Iterates through the search results (ScoreDoc[] hits).
For each result:
- Retrieves the Document using searcher.doc().
- Prints the filename, filepath, and a snippet of the content.
- Closes the IndexReader.

***data directory:*** Except for dog.txt, all of the public domain novels were downloaded from [Project Gutenbuerg](https://www.gutenberg.org)









