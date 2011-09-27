package com.realcomp.data.conversion;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author krenfro
 */
@com.realcomp.data.annotation.Converter("resize")
public class Resize extends SimpleConverter {

    private static final int USE_ORIGINAL_LENGTH = -1;

    protected int length = USE_ORIGINAL_LENGTH;
    
    public Resize(){
        super();
    }
    
    public Resize(int length){
        super();
        this.length = length;
    }
    
    @Override
    public Object convert(Object value) throws ConversionException{
        if (value == null)
            throw new IllegalArgumentException("value is null");

        if (length == USE_ORIGINAL_LENGTH)
            return value;
        else
            return StringUtils.rightPad(value.toString(), length).substring(0, length);
    }

    @Override
    public Resize copyOf(){
        return new Resize(length);
    }
            
    public String getLength() {
        return Integer.toString(length);
    }

    public void setLength(String length) {
        this.length = Integer.parseInt(length);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Resize other = (Resize) obj;
        if (this.length != other.length)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.length;
        return hash;
    }
}
