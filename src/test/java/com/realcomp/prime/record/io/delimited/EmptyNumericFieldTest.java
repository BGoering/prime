package com.realcomp.prime.record.io.delimited;

import com.realcomp.prime.schema.FieldList;
import com.realcomp.prime.schema.SchemaException;
import com.realcomp.prime.schema.Schema;
import com.realcomp.prime.DataType;
import com.realcomp.prime.record.Record;
import com.realcomp.prime.record.io.IOContext;
import com.realcomp.prime.record.io.IOContextBuilder;
import com.realcomp.prime.schema.Field;
import java.io.ByteArrayInputStream;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * If a field is defined as a numeric type, and the source is "",
 * it should be resolved as 'null' and not placed in the Record.
 *
 */
public class EmptyNumericFieldTest{

    public EmptyNumericFieldTest(){
    }

    @Test
    public void testEmptyFields() throws Exception{

        FieldList fields = getSchema().getDefaultFieldList();
        IOContext context = new IOContextBuilder()
                .schema(getSchema())
                .in(new ByteArrayInputStream(new byte[1]))
                .build();

        String[] data = new String[]{"123", "456", "789"};
        DelimitedFileReader instance = new DelimitedFileReader();
        instance.open(context);

        Record record = instance.loadRecord(fields, data);
        assertEquals(123d, (Integer) record.get("int"), .001d);
        assertEquals(456d, (Float) record.get("float"), .001d);
        assertEquals(789d, (Double) record.get("double"), .001d);

        data = new String[]{"", "", ""};
        instance = new DelimitedFileReader();
        instance.open(context);
        record = instance.loadRecord(fields, data);
        assertEquals(0, record.get("int"));
        assertEquals(0f, record.get("float"));
        assertEquals(0d, record.get("double"));


    }

    protected Schema getSchema() throws SchemaException{

        Schema schema = new Schema();
        schema.setName("test");
        schema.setVersion("0");
        FieldList fields = new FieldList();
        fields.add(new Field("int", DataType.INTEGER));
        fields.add(new Field("float", DataType.FLOAT));
        fields.add(new Field("double", DataType.DOUBLE));
        schema.addFieldList(fields);
        return schema;
    }
}