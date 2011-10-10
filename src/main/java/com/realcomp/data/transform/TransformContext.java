package com.realcomp.data.transform;

import com.realcomp.data.Operation;
import com.realcomp.data.record.Record;
import com.realcomp.data.schema.Field;
import com.realcomp.data.schema.FieldList;
import com.realcomp.data.schema.FileSchema;
import com.realcomp.data.schema.SchemaException;
import com.realcomp.data.validation.Severity;
import com.realcomp.data.validation.ValidationException;
import com.realcomp.data.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author krenfro
 */
public class TransformContext {
    
    private static final Logger logger = Logger.getLogger(TransformContext.class.getName());
    
    private FileSchema schema;
    private List<Field> fields;
    private String key;
    private Record record;
    private Severity validationExceptionThreshold = Severity.getDefault();
    private List<String> aliases;
    private long recordCount;
    
    public TransformContext(){
        aliases = new ArrayList<String>();
        record = new Record();
    }
    
    public TransformContext(TransformContext copy){
        this();
        
        try {
            schema = new FileSchema(copy.schema);
        }
        catch (SchemaException ex) {
            throw new IllegalStateException(ex);//should not happen
        }
        
        
        if (copy.fields != null){
            fields = new ArrayList<Field>();
            for (Field field: copy.fields){
                fields.add(new Field(field));
            }
        }
        key = copy.key;
        validationExceptionThreshold = copy.validationExceptionThreshold;
        record = new Record(copy.record);
        aliases = new ArrayList<String>();
        aliases.addAll(copy.aliases);        
        recordCount = copy.recordCount;
        
    }

    @Override
    public String toString(){
        if (schema != null && record != null){
            return schema.toString(record);
        }
        else if (fields != null && record != null){
            return new FieldList(fields).toString(record);
        }
        else{
            return record.toString();
        }
    }
    
    public void handleValidationException(Operation op, ValidationException ex) 
            throws ValidationException{
        
        Severity severity = ((Validator) op).getSeverity();
        String message = String.format("%s for [%s] in record [%s]",
                            new Object[]{ex.getMessage(), key, toString()});
        
        if (severity.ordinal() >= validationExceptionThreshold.ordinal()){            
            throw new ValidationException(message, ex);
        }
        else{        
            switch(severity){
                case LOW:
                    logger.log(Level.INFO, message);
                    break;
                case MEDIUM:
                    logger.log(Level.WARNING, message);
                    break;
                case HIGH:
                    logger.log(Level.SEVERE, message);
                    break;
            }
        }
    }
    
    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        if (record == null)
            throw new IllegalArgumentException("record is null");
        this.record = record;
        aliases.clear();
    }

    public FileSchema getSchema() {
        return schema;
    }

    public void setSchema(FileSchema schema) {
        this.schema = schema;
        aliases.clear();
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
        aliases.clear();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        if (key == null)
            throw new IllegalArgumentException("key is null");
        this.key = key;
        aliases.clear();
    }
    
    public Severity getValidationExceptionThreshold() {
        return validationExceptionThreshold;
    }

    public void setValidationExceptionThreshold(Severity validationExceptionThreshold) {
        this.validationExceptionThreshold = validationExceptionThreshold;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }
    
    public void addAlias(String alias){
        aliases.add(alias);
    }

    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }
    
}
