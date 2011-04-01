
package com.realcomp.data;

import java.io.Serializable;

/**
 *
 * @author krenfro
 */
public class FloatField extends Field<Float> implements Serializable{

    private static final long serialVersionUID = 1L;

    protected Float value;

    protected FloatField(){
    }

    public FloatField(Float value){
        if (value == null)
            throw new IllegalArgumentException("value is null");
        this.value = value;
    }

    public FloatField(String name, Float value){
        this(value);
        this.name = name;
    }


    @Override
    public DataType getType() {
        return DataType.FLOAT;
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public void setValue(Float value) {
        if (value == null)
            throw new IllegalArgumentException("value is null");
        this.value = value;
    }

    @Override
    public String toString(){
        return value.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final FloatField other = (FloatField) obj;
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value)))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

}
