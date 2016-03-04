package com.senseidb.clue.commands;

import com.senseidb.clue.ClueContext;
import org.apache.lucene.index.IndexReader;

import java.io.PrintStream;

public class InfoCommand extends ClueCommand {

    public InfoCommand(ClueContext ctx) {
        super(ctx);
    }

    @Override
    public final String getName() {
        return "info";
    }

    @Override
    public final String help() {
        return "displays information about the index, including number of lucene docs";
    }

    @Override
    public void execute(String[] args, PrintStream out) throws Exception {
        IndexReader reader = ctx.getIndexReader();
        out.println("readonly mode: " + getContext().isReadOnlyMode());
        out.println("numdocs: " + reader.numDocs());
        out.println("maxdoc: " + reader.maxDoc());
        out.println("num deleted docs: " + reader.numDeletedDocs());
        out.println("current version: " + reader.getVersion());

        IndexReader[] readers = reader.getSequentialSubReaders();
        out.println("segment count: " + ((readers == null) ? "" : readers.length));
        long sum = 0L;
        for (IndexReader ir : readers) {
            sum += ir.getUniqueTermCount();
        }
        out.println("unique term count: " + sum);
    }

}
