package com.senseidb.clue.commands;

import com.senseidb.clue.ClueContext;

import java.io.PrintStream;

public class ExitCommand extends ClueCommand {

    public ExitCommand(ClueContext ctx) {
        super(ctx);
    }

    @Override
    public final String getName() {
        return "exit";
    }

    @Override
    public final String help() {
        return "exits program";
    }

    @Override
    public void execute(String[] args, PrintStream out) {
        out.flush();
        try {
            ctx.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

}
