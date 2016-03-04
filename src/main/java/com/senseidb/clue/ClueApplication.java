package com.senseidb.clue;

import com.senseidb.clue.commands.ClueCommand;
import com.senseidb.clue.commands.HelpCommand;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URI;

public class ClueApplication {

    private ClueContext ctx = null;
    private ClueCommand helpCommand = null;

    public ClueApplication(String idxLocation, ClueConfiguration config, boolean interactiveMode) throws Exception {
        Directory dir = config.getDirBuilder().build(new URI(idxLocation));
        if (!IndexReader.indexExists(dir)) {
            System.out.println("lucene index does not exist at: " + idxLocation);
            System.exit(1);
        }
        IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LUCENE_36, new StandardAnalyzer(Version.LUCENE_36));
        ctx = new ClueContext(dir, config, writerConfig, interactiveMode);
        helpCommand = ctx.getCommand(HelpCommand.HELP_CMD);
    }

    public void handleCommand(String cmdName, String[] args, PrintStream out) throws Exception {
        ClueCommand cmd = ctx.getCommand(cmdName);
        if (cmd == null) {
            out.println(cmdName + " is not supported:");
            cmd = helpCommand;
        }
        cmd.execute(args, out);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("usage: <index location> <command> <command args>");
            System.exit(1);
        }

        ClueConfiguration config = ClueConfiguration.load();
        String idxLocation = args[0];
        ClueApplication app = null;

        if (args.length > 1) {
            String cmd = args[1];
            if ("readonly".equalsIgnoreCase(cmd)) {
                if (args.length > 2) {
                    cmd = args[2];
                    app = new ClueApplication(idxLocation, config, false);
                    String[] cmdArgs;
                    cmdArgs = new String[args.length - 3];
                    System.arraycopy(args, 3, cmdArgs, 0, cmdArgs.length);
                    app.ctx.setReadOnlyMode(true);
                    app.handleCommand(cmd, cmdArgs, System.out);
                }
            } else {
                app = new ClueApplication(idxLocation, config, false);
                String[] cmdArgs;
                cmdArgs = new String[args.length - 2];
                System.arraycopy(args, 2, cmdArgs, 0, cmdArgs.length);
                app.handleCommand(cmd, cmdArgs, System.out);
            }
            return;
        }

        app = new ClueApplication(idxLocation, config, true);
        BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("> ");
            String line = inReader.readLine();
            if (line == null || line.isEmpty()) continue;
            line = line.trim();
            String[] parts = line.split("\\s");
            if (parts.length > 0) {
                String cmd = parts[0];
                String[] cmdArgs = new String[parts.length - 1];
                System.arraycopy(parts, 1, cmdArgs, 0, cmdArgs.length);
                app.handleCommand(cmd, cmdArgs, System.out);
            }
        }
    }

}
