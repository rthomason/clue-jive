package com.senseidb.clue.commands;

import com.senseidb.clue.ClueContext;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.util.ReaderUtil;

import java.io.PrintStream;
import java.util.Iterator;

public class FieldsCommand extends ClueCommand {

    public FieldsCommand(ClueContext ctx) {
        super(ctx);
    }

    @Override
    public final String getName() {
        return "fields";
    }

    @Override
    public final String help() {
        return "gets the fields of the search index";
    }

    @Override
    public void execute(String[] args, PrintStream out) throws Exception {
        IndexReader reader = ctx.getIndexReader();
        Iterator<FieldInfo> ifi = ReaderUtil.getMergedFieldInfos(reader).iterator();
        out.println("available fields are:");
        while (ifi.hasNext()) {
            out.println("  " + ifi.next().name);
        }
    }

}
