package com.realcomp.prime.conversion;

import com.realcomp.prime.DataType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Divide, treating arguments as Doubles.
 *
 *
 */
@com.realcomp.prime.annotation.Converter("divide")
public class Divide implements Converter{

    protected List<DataType> supportedTypes;
    protected Double divisor = 1d;
    protected Double defaultValue;
    protected RemoveLeading removeLeadingZeros;

    public Divide(){
        supportedTypes = new ArrayList<>();
        supportedTypes.add(DataType.STRING);
        supportedTypes.add(DataType.INTEGER);
        supportedTypes.add(DataType.FLOAT);
        supportedTypes.add(DataType.DOUBLE);
        supportedTypes.add(DataType.LONG);
    }

    /**
     *
     * @return List of DataTypes supported by this converter. All Types except Map and List
     */
    @Override
    public List<DataType> getSupportedTypes(){
        return Collections.unmodifiableList(supportedTypes);
    }

    @Override
    public Divide copyOf(){
        Divide copy = new Divide();
        copy.setDivisor(divisor);
        copy.setDefault(defaultValue);
        return copy;
    }

    public Double getDivisor(){
        return divisor;
    }

    public void setDivisor(Double divisor){
        if (divisor == 0){
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        this.divisor = divisor;
    }

    public Double getDefault(){
        return defaultValue;
    }

    public void setDefault(Double defaultValue){
        this.defaultValue = defaultValue;
    }

    @Override
    public Object convert(Object value) throws ConversionException{

        Object retVal = value;
        if (value != null){
            if (value.toString().isEmpty() && defaultValue != null){
                retVal = defaultValue / divisor;
            }
            else{
                retVal = (Double) DataType.DOUBLE.coerce(value) / divisor;
            }
        }

        return retVal;
    }

    @Override
    public boolean equals(Object other){
        return (other instanceof Divide);
    }

    @Override
    public int hashCode(){
        int hash = 7;
        return hash;
    }
}
