package com.realcomp.data.schema.xml;

import com.realcomp.data.record.reader.RecordReader;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Uses xStream, JavaBeans and reflection to dynamically serialize/de-serialize a RecordReader
 * 
 * @author krenfro
 */
public class RecordReaderConverter extends DynamicPropertyGetter implements Converter{
    
    public RecordReaderConverter(){
        super();
        addIgnoredProperty("class");
        addIgnoredProperty("schema");
        addIgnoredProperty("count");
        addIgnoredProperty("beforeFirst");
        addIgnoredProperty("views");
   }

    @Override
    public boolean canConvert(Class type){
        return RecordReader.class.isAssignableFrom(type);
    }


    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mc) {
        try {
            for(Map.Entry<String,Object> entry: getProperties(o).entrySet())
                writer.addAttribute(entry.getKey(), entry.getValue().toString());

            RecordReader r = (RecordReader) o;
            if (!r.getViews().isEmpty()){
                for (String classname: r.getViews()){
                    writer.startNode("view");
                    writer.addAttribute("class", classname);
                    writer.endNode();
                }
            }

        }
        catch (DynamicPropertyException ex) {
            Logger.getLogger(RecordReaderConverter.class.getName()).log(Level.SEVERE, null, ex);
            throw new ConversionException(ex);
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader stream, UnmarshallingContext uc) {

       try {
            Class readerClass = Class.forName(stream.getAttribute("class"));
            RecordReader reader = (RecordReader) readerClass.newInstance();
            DynamicPropertySetter propSetter = new DynamicPropertySetter();
            propSetter.addIgnoredProperty("class");
            Map<String,String> properties = new HashMap<String,String>();
            Iterator<String> itr = stream.getAttributeNames();
            while(itr.hasNext()){
                String name = itr.next();
                String value = stream.getAttribute(name);
                if (value != null){
                    properties.put(name, value);
                }
            }
            propSetter.setProperties(reader, properties);


            List<String> views = new ArrayList<String>();
            while (stream.hasMoreChildren()){
                stream.moveDown();
                if (stream.getNodeName().equals("view"))
                    views.add(stream.getAttribute("class"));
                stream.moveUp();
            }
            
            if (!views.isEmpty())
                reader.setViews(views);
            
            return reader;
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
        
        boolean valid = super.isValidProperty(name, value);

        if (valid){
            if (name.equals("validationExceptionThreshold"))
                if (value.toString().equals(RecordReader.DEFAULT_VALIDATION_THREASHOLD.toString()))
                    valid = false;
        }
        
        return valid;
    }
  
}
