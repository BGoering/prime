package com.realcomp.prime.schema;

import com.realcomp.prime.conversion.Trim;
import com.realcomp.prime.record.Record;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class SchemaTest{

    public SchemaTest(){
    }

    @Test
    public void addNull(){
        Schema schema = new Schema();
        FieldList fields = new FieldList();
        try{
            fields.add(null);
            fail("should have thrown IAE");
        }
        catch (NullPointerException expected){
        }

        try{
            schema.addFieldList(null);
            fail("should have thrown IAE");
        }
        catch (NullPointerException expected){
        }

    }

    @Test
    public void testClassification() throws SchemaException{

        Schema schema = new Schema();
        try{
            schema.classify((Record) null);
            fail("should have thrown IAE");
        }
        catch (IllegalArgumentException expected){
        }


        try{
            schema.classify(new Record());
            fail("should have thrown SchemaException");
        }
        catch (SchemaException expected){
        }

        FieldList fieldList = new FieldList();
        fieldList.add(new Field("a"));
        fieldList.setClassifier(FieldList.DEFAULT_CLASSIFIER);
        schema.addFieldList(fieldList);

        try{
            assertEquals(fieldList, schema.classify(new Record()));
        }
        catch (SchemaException expected){
        }

        Record record = new Record();
        record.put("a", "test");
        assertEquals(fieldList, schema.classify(record));
    }

    @Test
    public void testScrictClassification() throws SchemaException{

        Schema schema = new Schema();
        FieldList fieldList = new FieldList();
        fieldList.add(new Field("a"));
        fieldList.setClassifier(FieldList.DEFAULT_CLASSIFIER);

        try{
            assertEquals(fieldList, schema.classify(new Record()));
        }
        catch (SchemaException expected){
        }

        schema.addFieldList(fieldList);
        assertEquals(fieldList, schema.classify(new Record()));

        try{
            assertEquals(fieldList, schema.classify(new Record()));
        }
        catch (SchemaException expected){
        }

    }

    @Test
    public void testCopyConstructor() throws SchemaException{


        Schema a = new Schema();
        Field original = new Field("original");
        original.addOperation(new Trim());
        FieldList fields = new FieldList();
        fields.add(original);
        a.addFieldList(fields);

        Schema b = new Schema(a);

        assertEquals(a, b);
        assertTrue(a.getDefaultFieldList().size() == 1);
        assertTrue(a.getDefaultFieldList().get(0).getOperations().size() == 1);
        assertTrue(b.getDefaultFieldList().size() == 1);
        assertTrue(b.getDefaultFieldList().get(0).getOperations().size() == 1);

        b.getDefaultFieldList().get(0).clearOperations();

        assertFalse(a.equals(b));
        assertTrue(a.getDefaultFieldList().size() == 1);
        assertTrue(a.getDefaultFieldList().get(0).getOperations().size() == 1);
        assertTrue(b.getDefaultFieldList().size() == 1);
        assertTrue(b.getDefaultFieldList().get(0).getOperations().isEmpty());

    }
    
    @Test
    public void testDefaultFieldList(){
        
        Schema schema = SchemaFactory.buildSchema(SchemaTest.class.getResourceAsStream("default.schema"));
        FieldList fieldList = schema.getDefaultFieldList();
        assertTrue(fieldList.isDefault());
                
    }
}

