package com.senseidb.clue.commands;

import com.senseidb.clue.ClueContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermFreqVector;

import java.io.PrintStream;

public class TermVectorCommand extends ClueCommand {

    public TermVectorCommand(ClueContext ctx) {
        super(ctx);
    }

    @Override
    public final String getName() {
        return "tv";
    }

    @Override
    public final String help() {
        return "shows term vector of a field for a doc";
    }

    @Override
    public void execute(String[] args, PrintStream out) throws Exception {
        if (args.length != 2) {
            out.println("usage: field docid");
            return;
        }

        String field = args[0];
        int doc = Integer.parseInt(args[1]);

        IndexReader reader = ctx.getIndexReader();
        TermFreqVector tfv = reader.getTermFreqVector(doc, field);

        if (tfv == null) {
            out.println("term vector is not available for field: " + field);
        } else if (tfv.size() > 0) {
            String[] terms = tfv.getTerms();
            int[] freqs = tfv.getTermFrequencies();
            for (int i = 0; i < tfv.size(); i++) {
                out.println(terms[i] + " (" + freqs[i] + ")");
            }
        }
    }

}
