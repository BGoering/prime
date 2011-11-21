package com.realcomp.data.transform;

import com.realcomp.data.Operation;
import com.realcomp.data.conversion.ConversionException;
import com.realcomp.data.conversion.Converter;
import com.realcomp.data.conversion.MissingFieldException;
import com.realcomp.data.conversion.MultiFieldConverter;
import com.realcomp.data.record.Record;
import com.realcomp.data.record.RecordValueResolver;
import com.realcomp.data.validation.ValidationException;
import com.realcomp.data.validation.Validator;
import com.realcomp.data.validation.file.RecordCountValidator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Performs <i>Operations</i> on the value(s) of a field in a Record.
 * 
 * @author krenfro
 */
public class ValueSurgeon {
   
    private static final Logger logger = Logger.getLogger(ValueSurgeon.class.getName());
            
    
    /**
     * @param record holds the value(s) to be operated on
     * @param key the key of the value(s) in the record to operate on
     * @param operations the operations to perform
     * @return the result of the operation. never null. may be empty. One entry for each value 
     * referenced by the key.
     * @throws ConversionException
     * @throws ValidationException 
     */
    public List<Object> operate(List<Operation> operations, TransformContext context) 
            throws ConversionException, ValidationException{
        
        if (operations == null)
            throw new IllegalArgumentException("operations is null");
        if (context == null)
            throw new IllegalArgumentException("context is null");
        
        Record record = context.getRecord();
        String key = context.getKey();
        
        
        // The key specified may resolve to multiple values in the record
        List<Object> values = RecordValueResolver.resolve(record, key);
        
        if (values.isEmpty()){
            //the key did not exist in the record, but there may still be output if there are 
            // converters like concat or constant which can output values with null input.
            
            Object temp = null;
            for (Operation op: operations){
                temp = operate(op, temp, context);
            }
            if (temp != null)
                values.add(temp);
        }
        else{
            for (int x = 0; x < values.size(); x++){
                Object temp = values.get(x);
                for (Operation op: operations){
                    temp = operate(op, temp, context);
                }
                values.set(x, temp);
            }
        }
        
        return values;
    }
    

    /**
     * Perform the requested operation on the data, optionally using the provided Record
     * for multi-field operations.
     * 
     * @param operation to be performed
     * @param data to be operated on
     * @param context the context of the transformation
     * @return the result of the operation
     * @throws ConversionException
     * @throws ValidationException
     * @throws MissingFieldException 
     */
    protected Object operate(Operation operation, Object data, TransformContext context)
                throws ConversionException, ValidationException, MissingFieldException{

        Object result = data;

        if (operation instanceof Validator){
            
            try{
                if (operation instanceof RecordCountValidator){
                    ((Validator) operation).validate(context.getRecordCount());
                }
                else{
                    ((Validator) operation).validate(data);
                }
            }
            catch(ValidationException ve){
                context.handleValidationException(operation, ve);
            }
        }
        else if (operation instanceof MultiFieldConverter){
            result = ((MultiFieldConverter) operation).convert(data, context.getRecord());
        }
        else if (operation instanceof Converter){
            result = ((Converter) operation).convert(data);
        }
        else{
            throw new IllegalStateException(
                    "Unsupported operaton: " + operation.getClass().getName());
        }

        return result;
    }
    

}