package com.senseidb.clue.commands;

import com.senseidb.clue.ClueContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;

import java.io.PrintStream;

public class TermsCommand extends ClueCommand {

    public TermsCommand(ClueContext ctx) {
        super(ctx);
    }

    @Override
    public final String getName() {
        return "terms";
    }

    @Override
    public final String help() {
        return "gets terms from the index, <field:term>, term can be a prefix";
    }

    @Override
    public void execute(String[] args, PrintStream out) throws Exception {
        String field = null;
        String termVal = null;

        String[] parts = args[0].split(":");
        if (parts.length == 2) {
            field = parts[0];
            termVal = parts[1];
        } else {
            out.println("usage: field:value");
            return;
        }

        Term t = (new Term(field)).createTerm(termVal);
        IndexReader reader = ctx.getIndexReader();
        TermEnum enumerator = reader.terms(t);
        TermDocs termDocs = reader.termDocs();

        int found = 0;

        try {
            String prefixText = t.text();
            String prefixField = t.field();
            do {
                Term term = enumerator.term();
                if (term != null && term.text().startsWith(prefixText) && term.field() == prefixField) {
                    termDocs.seek(term);
                    while (termDocs.next()) {
                        found++;
                    }
                } else {
                    break;
                }
            } while (enumerator.next());
        } finally {
            termDocs.close();
            enumerator.close();
        }

        out.println(termVal + " found in " + field + " field " + found + " times");
    }

}
