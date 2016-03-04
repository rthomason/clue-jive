package com.senseidb.clue.commands;

import com.senseidb.clue.ClueContext;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.util.ReaderUtil;

import java.io.PrintStream;

public class ReconstructCommand extends ClueCommand {

    public ReconstructCommand(ClueContext ctx) {
        super(ctx);
    }

    @Override
    public final String getName() {
        return "reconstruct";
    }

    @Override
    public final String help() {
        return "reconstructs an indexed field for a document";
    }

    /*public String reconstructWithPositions(Term te, int docid, TermDocs liveDocs) throws IOException {
        TreeMap<Integer, List<String>> docTextMap = new TreeMap<Integer, List<String>>();

        //BytesRef text;
        //DocsAndPositionsEnum dpe = null;
        Term t = null;
        while ((text = te.next()) != null) {
            dpe = te.docsAndPositions(liveDocs, dpe);
            int iterDoc = dpe.advance(docid);
            if (iterDoc == docid) {
                int freq = dpe.freq();
                for (int i = 0; i < freq; ++i) {
                    int pos = dpe.nextPosition();
                    List<String> textList = docTextMap.get(pos);
                    if (textList == null) {
                        textList = new ArrayList<String>();
                        docTextMap.put(pos, textList);
                    }
                    textList.add(text.utf8ToString());
                }
            }
        }
        StringBuffer buf = new StringBuffer();
        for (Entry<Integer, List<String>> entry : docTextMap.entrySet()) {
            Integer pos = entry.getKey();
            List<String> terms = entry.getValue();
            for (String term : terms) {
                buf.append(term + "(" + pos + ") ");
            }
        }
        return buf.toString();
    }

    public String reconstructNoPositions(Term te, int docid, TermDocs liveDocs) throws IOException {
        List<String> textList = new ArrayList<String>();
        BytesRef text;
        DocsEnum dpe = null;
        while ((text = te.next()) != null) {
            dpe = te.docs(liveDocs, dpe);
            int iterDoc = dpe.advance(docid);
            if (iterDoc == docid) {
                textList.add(text.utf8ToString());
            }
        }
        StringBuffer buf = new StringBuffer();
        for (String s : textList) {
            buf.append(s + " ");
        }
        return buf.toString();
    }*/

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
        FieldInfo finfo = ReaderUtil.getMergedFieldInfos(reader).fieldInfo(field);

        if (!finfo.isIndexed) {
            out.println(field + " is not an indexed field");
            return;
        }

        TermEnum terms = reader.terms(new Term(field));

        //boolean found = false;
        //boolean hasPositions = terms.hasPositions();

        // http://lucene.apache.org/core/4_0_0/MIGRATE.html
        do {
            Term t = terms.term();

            out.println(t);

            //TermsEnum te = terms.iterator(null);
            //if (hasPositions) {
            //out.println(reconstructWithPositions(t, doc, reader.termDocs(t)));
            //} else {
            //out.println(reconstructNoPositions(terms, doc, atomicReader.getLiveDocs()));
            //}
            //found = true;
            //break;
            //}
        } while (terms.next());

        //if (!found) {
        //    out.println(doc + " not found");
        //    return;
        //}
    }

}
