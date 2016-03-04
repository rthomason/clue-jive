package com.senseidb.clue.commands;

import com.senseidb.clue.ClueContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ExplainCommand extends ClueCommand {

    public ExplainCommand(ClueContext ctx) {
        super(ctx);
    }

    @Override
    public final String getName() {
        return "explain";
    }

    @Override
    public final String help() {
        return "shows score explanation of a doc";
    }

    @Override
    public void execute(String[] args, PrintStream out) throws Exception {
        if (args.length < 2) {
            out.println("usage: query docid");
            return;
        }

        String docString = args[args.length - 1];
        String[] docList = docString.split(",");

        List<Integer> docidList = new ArrayList<Integer>();
        try {
            for (String s : docList) {
                docidList.add(Integer.parseInt(s));
            }
        } catch (Exception e) {
            out.println("error in parsing docids: " + e.getMessage());
            return;
        }
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < args.length - 1; i++) {
            buf.append(args[i]).append(" ");
        }

        IndexReader r = ctx.getIndexReader();
        IndexSearcher searcher = new IndexSearcher(r);
        Query q;
        String qstring = buf.toString();

        try {
            q = ctx.getQueryBuilder().build(qstring);
        } catch (Exception e) {
            out.println("cannot parse query: " + e.getMessage());
            return;
        }

        for (Integer docid : docidList) {
            Explanation expl = searcher.explain(q, docid);
            out.println(expl);
        }

        out.flush();
    }

}
