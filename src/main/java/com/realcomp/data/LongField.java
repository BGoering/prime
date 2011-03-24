
package com.realcomp.data;

import java.io.Serializable;

/**
 *
 * @author krenfro
 */
public class LongField extends Field<Long> implements Serializable{

    private static final long serialVersionUID = 1L;

    protected static final DataType type = DataType.LONG;
    protected Long value;
    protected String name;

    protected LongField(){
    }

    public LongField(Long value){
        if (value == null)
            throw new IllegalArgumentException("value is null");
        this.value = value;
    }

    public LongField(String name, Long value){
        this(value);
        this.name = name;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public void setName(String name){
        this.name = name;
    }

    @Override
    public DataType getType() {
        return type;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public void setValue(Long value) {
        if (value == null)
            throw new IllegalArgumentException("value is null");
        this.value = value;
    }

    @Override
    public Field get(String key) {
        throw new UnsupportedOperationException("Not supported for Field type [Long].");
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
        final LongField other = (LongField) obj;
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
