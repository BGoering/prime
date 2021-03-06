package com.realcomp.prime.schema;

import com.realcomp.prime.record.Record;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pretty print the keys of Record with indentation.
 *
 */
public class PrettyPrinter{

    private RelationalSchema relationalSchema;
    private Map<String, Schema> schemas;

    public PrettyPrinter(){
        schemas = new HashMap<>();
    }

    public RelationalSchema getRelationalSchema(){
        return relationalSchema;
    }

    public void setRelationalSchema(RelationalSchema relationalSchema){
        this.relationalSchema = relationalSchema;
    }

    public void addFileSchema(Schema schema){
        schemas.put(schema.getName(), schema);
    }

    public void addFileSchemas(List<Schema> schemas){
        for (Schema s : schemas){
            addFileSchema(s);
        }
    }

    public void prettyPrint(Record record, OutputStream out) throws IOException{

        if (relationalSchema == null){
            out.write(record.toString().getBytes());
        }
        else{
            Table primaryTable = relationalSchema.getTables().iterator().next();
            Map<String, Object> map = new HashMap<>();
            for (Map.Entry<String, Object> e : record.entrySet()){
                map.put(e.getKey(), e.getValue());
            }
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
            prettyPrint(primaryTable, map, 0, writer);
            writer.flush();
        }
    }

    private void prettyPrint(Table table, Map<String, Object> map, int indent, PrintWriter out){

        for (int x = 0; x < indent; x++){
            out.print(" ");
        }

        Schema schema = schemas.get(table.getName());
        out.print(table.getName());
        out.print("[");
        try{
            if (schema == null){
                out.print(map.toString());
            }
            else{
                Record r = new Record(map);
                out.print(schema.classify(r).toString(r));
            }
        }
        catch (SchemaException ex){
            out.print(map.toString());
        }
        out.print("]");
        out.println();

        if (table.hasChildren()){
            for (Table child : table.getChildren()){
                List<Map<String, Object>> data = (List<Map<String, Object>>) map.get(child.getName());
                if (data != null && !data.isEmpty()){
                    for (Map d : data){
                        prettyPrint(child, d, indent + 4, out);
                    }
                }
            }
        }
    }
}
