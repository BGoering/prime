
package com.realcomp.data.validation.field;

import com.realcomp.data.DataType;
import com.realcomp.data.annotation.Validator;
import com.realcomp.data.conversion.ConversionException;
import com.realcomp.data.validation.ValidationException;


/**
 *
 * @author krenfro
 */
@Validator("validateLength")
public class LengthValidator extends BaseFieldValidator {

    protected int min = 0;
    protected int max = Integer.MAX_VALUE;

    @Override
    public void validate(Object value) throws ValidationException{
        if (value == null)
            throw new ValidationException("cannot validate null Object");
        if (min > max)
            throw new ValidationException(String.format("Schema problem: min (%s) > max (%s)", min, max));

        try {
            String coerced = (String) DataType.STRING.coerce(value);
            int length = coerced.length();

            if (length < min){
                throw new ValidationException(
                        String.format("too short (min: %s)", min), value, getSeverity());
            }
            else if (length > max){
                throw new ValidationException(
                        String.format("too long (max: %s)", max), value, getSeverity());
            }
        }
        catch(ConversionException ex){
            throw new ValidationException(ex.getMessage(), value, getSeverity());
        }
    }

    @Override
    public LengthValidator copyOf(){
        LengthValidator copy = new LengthValidator();
        copy.setSeverity(severity);
        copy.min = min;
        copy.max = max;
        return copy;
    }
    

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max < 0)
            throw new IllegalArgumentException("max < 0");
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        if (min < 0)
            throw new IllegalArgumentException("min < 0");
        this.min = min;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final LengthValidator other = (LengthValidator) obj;
        if (this.min != other.min)
            return false;
        if (this.max != other.max)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.min;
        hash = 61 * hash + this.max;
        return hash;
    }

}
