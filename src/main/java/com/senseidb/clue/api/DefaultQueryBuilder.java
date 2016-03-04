package com.senseidb.clue.api;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

public class DefaultQueryBuilder implements QueryBuilder {

    private QueryParser parser = null;

    @Override
    public void initialize(String defaultField, Analyzer analyzer) {
        parser = new QueryParser(Version.LUCENE_36, defaultField, analyzer);
    }

    @Override
    public Query build(String rawQuery) throws ParseException {
        return parser.parse(rawQuery);
    }

}
