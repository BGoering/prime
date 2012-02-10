package com.realcomp.data.record.io.fixed;

import com.realcomp.data.conversion.ConversionException;
import com.realcomp.data.record.Record;
import com.realcomp.data.record.io.BaseRecordReader;
import com.realcomp.data.record.io.IOContext;
import com.realcomp.data.record.io.SkippingBufferedReader;
import com.realcomp.data.schema.Field;
import com.realcomp.data.schema.FieldList;
import com.realcomp.data.schema.FileSchema;
import com.realcomp.data.schema.SchemaException;
import com.realcomp.data.validation.Severity;
import com.realcomp.data.validation.ValidationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 * There is an implicit Resize converter on all fields that runs after all
 * 'after' operations.
 * 
 * @author krenfro
 */
public class FixedFileReader extends BaseRecordReader{
    
    private static final Logger logger = Logger.getLogger(FixedFileReader.class.getName());
    
    protected SkippingBufferedReader reader;
    
    public FixedFileReader(){
        super();
        format.putDefaultValue("header", "false");
        
    }
    
    public FixedFileReader(FixedFileReader copy){
        super(copy);
    }
    
    @Override
    public void open(IOContext context) throws IOException, SchemaException{        
        super.open(context);
        ensureFieldLengthsSpecified(context.getSchema());
        reader = new SkippingBufferedReader(new InputStreamReader(context.getIn(), getCharset()));
        reader.setSkipLeading(getSkipLeading());
        reader.setSkipTrailing(getSkipTrailing());
        
        if (isHeader() && reader.getSkipLeading() == 0){
            reader.setSkipLeading(1);
        }
        
        count = 0;
    }


    @Override
    public void close(){        
        super.close();
        IOUtils.closeQuietly(reader);        
    }
    
    @Override
    public Record read()
            throws IOException, ValidationException, ConversionException, SchemaException{

        if (context.getSchema() == null)
            throw new IllegalStateException("schema not specified");

        if (!beforeFirstOperationsRun){
            executeBeforeFirstOperations();
            beforeFirstOperationsRun = true;
        }

        Record record = null;
        String data = reader.readLine();
        if (data != null){
            FieldList fields = context.getSchema().classify(data);            
            String[] parsed = parse(data, fields);
            record = loadRecord(fields, parsed);
        }

        if (record != null)
            count++;
        else
            executeAfterLastOperations();
        
        return record;
    }

    
    protected Record loadRecord(FieldList fields, String[] data)
            throws ValidationException, ConversionException{

        if (fields == null)
            throw new IllegalArgumentException("fields is null");
        if (data == null)
            throw new IllegalArgumentException("data is null");

        if (fields.size() != data.length)
            throw new ValidationException(
                    "number of fields in schema does not match data.",
                    fields.size() + " != " + data.length,
                    Severity.HIGH);

        return recordFactory.build(fields, data);
    }
   

    protected String[] parse(String record, FieldList fields)
            throws ValidationException, SchemaException{

        if (fields == null)
            throw new IllegalArgumentException("fields is null");
        if (fields.isEmpty())
            throw new IllegalArgumentException("fields is empty");

        int expectedLength = getExpectedLength(fields);
        int length = record.length();

        if (expectedLength != length)
            throw new ValidationException(
                String.format("Record length != expected length (%s != %s)", length, expectedLength),
                record,
                Severity.HIGH);

        String[] result = new String[fields.size()];
        int index = 0;
        int start = 0;
        int stop;
        
        for (Field field: fields){
            stop = start + field.getLength();
            result[index] = record.substring(start, stop);
            index++;
            start = stop;
        }

        return result;
    }



    protected void ensureFieldLengthsSpecified(FileSchema schema) throws SchemaException{        
        for (FieldList fields: schema.getFieldLists())
            ensureFieldLengthsSpecified(fields);
    }
    
    
    protected void ensureFieldLengthsSpecified(FieldList fields) throws SchemaException{        
        for (Field field: fields)
            if (field.getLength() <= 0)
                throw new SchemaException("field length not specified for: " + field);
    }

    
    protected int getExpectedLength(List<Field> fields){

        assert(fields != null);
        int retVal = 0;
        for (Field field: fields)
            retVal += field.getLength();
        return retVal;
    }
    
    
    protected boolean isHeader(){
        return Boolean.parseBoolean(format.get("header"));
    }
    
    @Override
    protected void validateAttributes(){
        super.validateAttributes();
        isHeader();
    }
    
}
