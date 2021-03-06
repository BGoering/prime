package com.realcomp.prime.conversion;


@com.realcomp.prime.annotation.Converter("trim")
public class Trim extends StringConverter{

    @Override
    public Object convert(Object value) throws ConversionException{
        return value == null ? null : value.toString().trim();
    }

    @Override
    public Trim copyOf(){
        return new Trim();
    }

    @Override
    public boolean equals(Object other){
        return (other instanceof Trim);
    }

    @Override
    public int hashCode(){
        int hash = 7;
        return hash;
    }
}
