package com.realcomp.data.schema.xml;

import com.realcomp.data.record.parser.RecordParser;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Uses xStream, JavaBeans and reflection to dynamically serialize/de-serialize a RecordParser
 * 
 * @author krenfro
 */
public class RecordParserConverter extends PropertyReader implements Converter{
    
    private Set<String> ignoredProperties;

    public RecordParserConverter(){
        ignoredProperties = new HashSet<String>();
        ignoredProperties.add("class");
        ignoredProperties.add("schema");
    }

    @Override
    public boolean canConvert(Class type){
        return RecordParser.class.isAssignableFrom(type);
    }


    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mc) {
        try {
            for(Map.Entry<String,Object> entry: read(o).entrySet())
                writer.addAttribute(entry.getKey(), entry.getValue().toString());
        }
        catch (DynamicPropertyException ex) {
            Logger.getLogger(RecordParserConverter.class.getName()).log(Level.SEVERE, null, ex);
            throw new ConversionException(ex);
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {

       try {
            Class parserClass = Class.forName(reader.getAttribute("class"));
            RecordParser parser = (RecordParser) parserClass.newInstance();
            PropertyWriter writer = new PropertyWriter();
            Map<String,String> properties = new HashMap<String,String>();
            Iterator<String> itr = reader.getAttributeNames();
            while(itr.hasNext()){
                String name = itr.next();
                String value = reader.getAttribute(name);
                if (value != null)
                    properties.put(name, value);
            }
            writer.write(parser, properties);
            return parser;
        }
        catch (DynamicPropertyException ex) {
            throw new ConversionException(ex);
        }
        catch (IllegalAccessException ex) {
            throw new ConversionException(ex);
        }
        catch (InstantiationException ex) {
            throw new ConversionException(ex);
        }
        catch (ClassNotFoundException ex) {
            throw new ConversionException(ex);
        }
    }


    /**
     * Override default implementation to remove <i>class</i> and <i>schema</i> attributes
     * from the serialization.
     * Also ignores validationExceptionThreshold if it is the default.
     * 
     * @param name
     * @param value
     * @return
     */
    @Override
    public boolean isValidProperty(String name, Object value){


        if (name.equals("class"))
            return false;
        if (name.equals("schema"))
            return false;
        
        boolean valid = super.isValidProperty(name, value);

        if (valid){
            if (name.equals("validationExceptionThreshold"))
                if (value.toString().equals(RecordParser.DEFAULT_VALIDATION_THREASHOLD.toString()))
                    valid = false;
        }
        
        return valid;
    }
  
}
