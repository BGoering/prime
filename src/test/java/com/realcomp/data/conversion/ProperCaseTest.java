package com.realcomp.data.conversion;

import com.realcomp.data.DataType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author krenfro
 */
public class ProperCaseTest extends ConverterTest{

    public ProperCaseTest(){
        converter = new ProperCase();
    }

    @Test
    public void testConversions() throws ConversionException{

        assertNull(converter.convert(null));
        assertEquals("", converter.convert(""));
        assertEquals("A", converter.convert("a"));
        assertEquals("Ab", converter.convert("ab"));
        assertEquals("Ab", converter.convert("AB"));
        assertEquals("Ab", converter.convert("aB"));
        assertEquals("Ab", converter.convert("Ab"));

        assertEquals("Ab C", converter.convert("Ab c"));
        assertEquals("Ab C", converter.convert("Ab C"));
        assertEquals("Ab Cd", converter.convert("Ab CD"));
        assertEquals("Ab Cd", converter.convert("Ab Cd"));
        assertEquals("Ab Cd", converter.convert("Ab cd"));
        assertEquals("Ab Cd ", converter.convert("Ab cd "));
        assertEquals(" Ab Cd ", converter.convert(" Ab cd "));


        assertEquals("McCall", converter.convert("Mccall"));
        assertEquals("McCall", converter.convert("MCCall"));
        assertEquals("Name McCall", converter.convert("Name MCCall"));
        assertEquals("Name McCall ", converter.convert("Name MCCall "));


        assertEquals("Name-McCall ", converter.convert("Name-MCCall "));

        assertEquals("Name-Other", converter.convert("name-other"));
    }

    @Override
    @Test
    public void testSupportedTypes(){

        for (DataType type : DataType.values()){
            if (type == DataType.STRING){
                assertTrue(converter.getSupportedTypes().contains(type));
            }
            else{
                assertFalse(converter.getSupportedTypes().contains(type));
            }
        }
    }
}
