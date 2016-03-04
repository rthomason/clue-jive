package com.senseidb.clue.api;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;

import java.io.IOException;

public interface IndexReaderFactory {

    void initialize(Directory dir) throws Exception;

    IndexReader getIndexReader() throws Exception;

    void refreshReader() throws Exception;

    void shutdown() throws IOException;

}
