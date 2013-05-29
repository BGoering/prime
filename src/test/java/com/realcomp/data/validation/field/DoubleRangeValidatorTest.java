package com.realcomp.data.validation.field;

import com.realcomp.data.validation.ValidationException;
import com.realcomp.data.validation.field.DoubleRangeValidator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author krenfro
 */
public class DoubleRangeValidatorTest{

    public DoubleRangeValidatorTest(){
    }

    @Test(expected = ValidationException.class)
    public void testOutOfOrder() throws ValidationException{
        DoubleRangeValidator validator = new DoubleRangeValidator();
        validator.setMin(100);
        validator.setMax(99);
        validator.validate("100");
    }

    /**
     * Test of validate method, of class LongRangeValidator.
     */
    @Test
    public void testValidate() throws ValidationException{

        DoubleRangeValidator validator = new DoubleRangeValidator();
        validator.validate("0");
        validator.validate("0.00001");
        validator.validate(""); //coerces to 0


        validator.validate("00000");
        validator.validate("100000.001");
        validator.validate("-100000.0");
        validator.setMin(0);
        validator.validate("0");
        validator.validate("0.99999");
        validator.validate("0.00001");

        try{
            validator.validate("-.001");
            fail("should have thrown ValidationException");
        }
        catch (ValidationException ok){
        }

        try{
            validator.validate("-1");
            fail("should have thrown ValidationException");
        }
        catch (ValidationException ok){
        }

        validator.validate("1");
        validator.setMax(10);
        validator.validate("0");

        try{
            validator.validate("-1");
            fail("should have thrown ValidationException");
        }
        catch (ValidationException ok){
        }

        validator.validate("1");
        validator.validate("10");
        validator.validate("9.999");

        try{
            validator.validate("10.0000000001");
            fail("should have thrown ValidationException");
        }
        catch (ValidationException ok){
        }

        try{
            validator.validate("11");
            fail("should have thrown ValidationException");
        }
        catch (ValidationException ok){
        }

        validator.setMin(1);
        try{
            validator.validate("");
            fail("should have thrown ValidationException");
        }
        catch (ValidationException ok){
        }



    }
}
