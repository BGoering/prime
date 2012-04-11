
package com.realcomp.data.validation.field;

import com.realcomp.data.annotation.Validator;
import com.realcomp.data.validation.ValidationException;

/**
 * Marks the value as a 'key' field in the record, with an optional index.
 * A key field <i>can</i> be empty.
 * 
 * <p>
 * <h2>Key Indexes</h2>
 * The optional <i>index</i> property of a Key can be used to specify an ordering of the keys in a schema.
 * 
 * For records with multiple keys, it may be important to specify an order so Records can be joined properly.
 * </p>
 * 
 * @see com.realcomp.data.hdfs.SequenceFileRecordWriter
 * @author krenfro
 */
@Validator("key")
public class Key extends BaseFieldValidator {

    protected Integer index;
    
    @Override
    public Key copyOf() {        
        Key copy = new Key();
        copy.setSeverity(severity);
        return copy;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public void validate(Object value) throws ValidationException {
        
    }
    
}
