package com.senseidb.clue.commands;

import com.senseidb.clue.ClueContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Similarity;

import java.io.PrintStream;

public class NormsCommand extends ClueCommand {

    public NormsCommand(ClueContext ctx) {
        super(ctx);
    }

    @Override
    public final String getName() {
        return "norm";
    }

    @Override
    public final String help() {
        return "displays norm values for a field for a document";
    }

    @Override
    public void execute(String[] args, PrintStream out) throws Exception {
        if (args.length != 2) {
            out.println("usage: field docid");
            return;
        }

        String field = args[0];
        int docid;
        try {
            docid = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            out.println("couldn't parse docid");
            return;
        }

        IndexReader reader = ctx.getIndexReader();
        if (reader.hasNorms(field)) {
            byte[] fieldNorms = reader.norms(field);
            out.println("norm for doc " + docid + " is " + Similarity.decodeNorm(fieldNorms[docid]));
        } else {
            out.println("given field: " + field + " does not contain norms");
        }
    }

}
