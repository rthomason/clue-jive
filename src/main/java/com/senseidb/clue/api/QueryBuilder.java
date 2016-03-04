package com.senseidb.clue.api;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;

public interface QueryBuilder {

    void initialize(String defaultField, Analyzer analyzer);

    Query build(String rawQuery) throws ParseException;

}
