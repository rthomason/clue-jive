package com.senseidb.clue.commands;

import com.senseidb.clue.ClueContext;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import java.io.PrintStream;

public class DeleteCommand extends ClueCommand {

    public DeleteCommand(ClueContext ctx) {
        super(ctx);
    }

    @Override
    public final String getName() {
        return "delete";
    }

    @Override
    public final String help() {
        return "deletes a list of documents from searching via a query, input: query";
    }

    @Override
    public void execute(String[] args, PrintStream out) throws Exception {
        QueryParser qparser = new QueryParser(Version.LUCENE_36, "contents", new StandardAnalyzer(Version.LUCENE_36));
        Query q = null;
        if (args.length == 0) {
            q = new MatchAllDocsQuery();
        } else {
            StringBuilder buf = new StringBuilder();
            for (String s : args) {
                buf.append(s).append(" ");
            }
            String qstring = buf.toString();
            try {
                q = qparser.parse(qstring);
            } catch (Exception e) {
                out.println("cannot parse query: " + e.getMessage());
                return;
            }
        }

        if (q != null) {
            IndexWriter writer = ctx.getIndexWriter();
            if (writer != null) {
                writer.deleteDocuments(q);
                writer.commit();
                ctx.refreshReader();
            } else {
                out.println("unable to edit readonly index");
            }
        }
    }

}