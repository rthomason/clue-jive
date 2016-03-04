package com.senseidb.clue;

import com.senseidb.clue.api.BytesRefDisplay;
import com.senseidb.clue.api.IndexReaderFactory;
import com.senseidb.clue.api.QueryBuilder;
import com.senseidb.clue.commands.ClueCommand;
import com.senseidb.clue.commands.DirectoryCommand;
import com.senseidb.clue.commands.ExitCommand;
import com.senseidb.clue.commands.ExplainCommand;
import com.senseidb.clue.commands.FieldsCommand;
import com.senseidb.clue.commands.HelpCommand;
import com.senseidb.clue.commands.InfoCommand;
import com.senseidb.clue.commands.NormsCommand;
import com.senseidb.clue.commands.ReadonlyCommand;
import com.senseidb.clue.commands.SearchCommand;
import com.senseidb.clue.commands.StoredFieldCommand;
import com.senseidb.clue.commands.TermVectorCommand;
import com.senseidb.clue.commands.TermsCommand;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ClueContext {

    private final IndexReaderFactory readerFactory;
    private final SortedMap<String, ClueCommand> cmdMap;
    private final boolean interactiveMode;
    private IndexWriter writer;
    private final Directory directory;
    private boolean readOnlyMode;
    private final IndexWriterConfig writerConfig;
    private final QueryBuilder queryBuilder;
    private final Analyzer analyzerQuery;
    private final BytesRefDisplay termBytesRefDisplay;
    private final BytesRefDisplay payloadBytesRefDisplay;

    public ClueContext(Directory dir, ClueConfiguration config, IndexWriterConfig writerConfig, boolean interactiveMode)
            throws Exception {
        this.directory = dir;
        this.analyzerQuery = config.getAnalyzerQuery();
        this.readerFactory = config.getIndexReaderFactory();
        this.readerFactory.initialize(directory);
        this.queryBuilder = config.getQueryBuilder();
        this.queryBuilder.initialize("contents", analyzerQuery);
        this.writerConfig = writerConfig;
        this.termBytesRefDisplay = config.getTermBytesRefDisplay();
        this.payloadBytesRefDisplay = config.getPayloadBytesRefDisplay();
        this.writer = null;
        this.interactiveMode = interactiveMode;
        this.cmdMap = new TreeMap<>();
        this.readOnlyMode = true;

        // register all the commands currently supported
        new HelpCommand(this);
        new ExitCommand(this);
        new InfoCommand(this);
        new SearchCommand(this);
        new TermsCommand(this);
        new ReadonlyCommand(this);
        new DirectoryCommand(this);
        new ExplainCommand(this);
        new NormsCommand(this);
        new TermVectorCommand(this);
        new StoredFieldCommand(this);

        // not necessary yet
        //new DocSetInfoCommand(this);
        //new DocValCommand(this);
        //new PostingsCommand(this);
        //new ReconstructCommand(this);

        // too risky to let customers use
        //new MergeCommand(this);
        //new DeleteCommand(this);
        //new ExportCommand(this);
        //new IndexTrimCommand(this);

        // new functions
        new FieldsCommand(this);
    }

    public QueryBuilder getQueryBuilder() {
        return queryBuilder;
    }

    public Analyzer getAnalyzerQuery() {
        return analyzerQuery;
    }

    public BytesRefDisplay getTermBytesRefDisplay() {
        return termBytesRefDisplay;
    }

    public BytesRefDisplay getPayloadBytesRefDisplay() {
        return payloadBytesRefDisplay;
    }

    public void registerCommand(ClueCommand cmd) {
        String cmdName = cmd.getName();
        if (cmdMap.containsKey(cmdName)) {
            throw new IllegalArgumentException(cmdName + " exists!");
        }
        cmdMap.put(cmdName, cmd);
    }

    public boolean isInteractiveMode() {
        return interactiveMode;
    }

    public boolean isReadOnlyMode() {
        return readOnlyMode;
    }

    public ClueCommand getCommand(String cmd) {
        return cmdMap.get(cmd);
    }

    public Map<String, ClueCommand> getCommandMap() {
        return cmdMap;
    }

    public IndexReader getIndexReader() throws Exception {
        return readerFactory.getIndexReader();
    }

    public IndexWriter getIndexWriter() {
        if (readOnlyMode) {
            return null;
        }
        if (writer == null) {
            try {
                writer = new IndexWriter(directory, writerConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return writer;
    }

    public Directory getDirectory() {
        return directory;
    }

    public void setReadOnlyMode(boolean readOnlyMode) {
        this.readOnlyMode = readOnlyMode;
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer = null;
        }
    }

    public void refreshReader() throws Exception {
        readerFactory.refreshReader();
    }

    public void shutdown() throws Exception {
        try {
            readerFactory.shutdown();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
