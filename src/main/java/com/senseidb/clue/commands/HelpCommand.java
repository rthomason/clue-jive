package com.senseidb.clue.commands;

import com.senseidb.clue.ClueContext;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Map;

public class HelpCommand extends ClueCommand {

    public static final String HELP_CMD = "help";

    public HelpCommand(ClueContext ctx) {
        super(ctx);
    }

    @Override
    public final String getName() {
        return "help";
    }

    @Override
    public final String help() {
        return "displays help";
    }

    @Override
    public void execute(String[] args, PrintStream out) {
        Map<String, ClueCommand> cmdMap = ctx.getCommandMap();
        Collection<ClueCommand> commands = cmdMap.values();

        for (ClueCommand cmd : commands) {
            out.println(cmd.getName() + " - " + cmd.help());
        }
        out.flush();
    }

}
