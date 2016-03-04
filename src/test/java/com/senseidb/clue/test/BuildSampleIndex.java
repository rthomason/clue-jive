package com.senseidb.clue.test;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BuildSampleIndex {

    static void addMetaString(Document doc, String field, String value) {
        if (value != null) {
            doc.add(new Field(field, value, Field.Store.YES, Field.Index.ANALYZED));
        }
    }

    static Document buildDoc(JSONObject json) {
        Document doc = new Document();

        try {
            doc.add(new NumericField("id").setLongValue(json.getLong("id")));
            doc.add(new NumericField("groupid").setLongValue(json.getLong("id")));
            doc.add(new NumericField("price").setDoubleValue(json.optDouble("price")));
            doc.add(new NumericField("year").setIntValue(json.optInt("year")));
            doc.add(new NumericField("mileage").setIntValue(json.optInt("mileage")));

            doc.add(new Field("contents", json.optString("contents"),
                    Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));

            addMetaString(doc, "color", json.optString("color"));
            addMetaString(doc, "category", json.optString("category"));
            addMetaString(doc, "makemodel", json.optString("makemodel"));
            addMetaString(doc, "city", json.optString("city"));

            String tagsString = json.optString("tags");
            if (tagsString != null) {
                String[] parts = tagsString.split(",");
                if (parts != null && parts.length > 0) {
                    for (String part : parts) {
                        doc.add(new Field("tags", part, Field.Store.YES, Field.Index.NOT_ANALYZED));
                        doc.add(new Field("tags_indexed", part, Field.Store.NO, Field.Index.ANALYZED));
                    }
                }

                doc.add(new Field("tags_payload", tagsString,
                        Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
            }

            doc.add(new Field("json", json.toString(), Field.Store.NO, Field.Index.NOT_ANALYZED));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return doc;
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("usage: source_file index_dir");
        }

        IndexWriter writer = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])))) {
            IndexWriterConfig idxWriterConfig = new IndexWriterConfig(Version.LUCENE_36, new StandardAnalyzer(Version.LUCENE_36));
            Directory dir = FSDirectory.open(new File(args[1]));
            writer = new IndexWriter(dir, idxWriterConfig);
            int count = 0;
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                JSONObject json = new JSONObject(line);
                Document doc = buildDoc(json);
                writer.addDocument(doc);
                count++;
                if (count % 100 == 0) {
                    System.out.print(".");
                }
            }
            System.out.println(count + " docs indexed");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            writer.commit();
            writer.close();
        }
    }

}
