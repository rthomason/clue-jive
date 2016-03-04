package com.senseidb.clue.commands;

import com.senseidb.clue.ClueContext;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;

import java.io.PrintStream;

public class StoredFieldCommand extends ClueCommand {

    public StoredFieldCommand(ClueContext ctx) {
        super(ctx);
    }

    @Override
    public final String getName() {
        return "stored";
    }

    @Override
    public final String help() {
        return "displays stored data for a given field";
    }

    @Override
    public void execute(String[] args, PrintStream out) throws Exception {
        if (args.length != 2) {
            out.println("usage: field docid");
            return;
        }

        String field = args[0];
        int docId = Integer.parseInt(args[1]);

        IndexReader reader = ctx.getIndexReader();
        Document doc = reader.document(docId);

        boolean found = false;

        for (Fieldable f : doc.getFields()) {
            if (f.name().equals(field)) {
                if (f.isStored()) {
                    found = true;
                    out.println(f.stringValue());
                    break;
                }
            }
        }

        if (!found) {
            out.println("field was not found or is not a stored field");
            return;
        }
    }

}
