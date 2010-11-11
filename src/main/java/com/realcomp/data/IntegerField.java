
package com.realcomp.data;

/**
 *
 * @author krenfro
 */
public class IntegerField extends Field<Integer>{

    protected final DataType type = DataType.INTEGER;
    protected Integer value;
    protected String name;

    public IntegerField(){
    }

    public IntegerField(String name, Integer value){
        if (value == null)
            throw new IllegalArgumentException("value is null");
        this.name = name;
        this.value = value;
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
    public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(Integer value) {
        if (value == null)
            throw new IllegalArgumentException("value is null");
        this.value = value;
    }

    @Override
    public Field get(String key) {
        throw new UnsupportedOperationException("Not supported for Field type [Integer].");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final IntegerField other = (IntegerField) obj;
        if (this.type != other.type)
            return false;
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value)))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 67 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }
}
