package com.realcomp.data.util;

import com.realcomp.data.conversion.ConversionException;
import com.realcomp.data.record.io.IOContext;
import com.realcomp.data.record.io.IOContextBuilder;
import com.realcomp.data.record.io.RecordReader;
import com.realcomp.data.record.io.RecordReaderFactory;
import com.realcomp.data.schema.SchemaException;
import com.realcomp.data.schema.SchemaFactory;
import com.realcomp.data.validation.ValidationException;
import java.io.*;
import java.util.Arrays;
import java.util.logging.Logger;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * Validates a file against a schema
 *
 * @author krenfro
 */
public class Validate{

    private static final Logger logger = Logger.getLogger(Validate.class.getName());

    public void validate(IOContext context)
            throws SchemaException, IOException, ValidationException, ConversionException{

        RecordReader reader = RecordReaderFactory.build(context.getSchema());
        reader.open(context);

        long lineNumber = 1;
        try{
            while (reader.read() != null){
                lineNumber++;
            }
        }
        catch (IOException ex){
            throw new IOException(ex.getMessage() + " at record " + lineNumber, ex);
        }
        catch (ValidationException ex){
            throw new ValidationException(ex.getMessage() + " at record " + lineNumber, ex);
        }
        catch (ConversionException ex){
            throw new ConversionException(ex.getMessage() + " at record " + lineNumber, ex);
        }
    }

    private static void printHelp(OptionParser parser){
        try{
            parser.printHelpOn(System.err);
        }
        catch (IOException ignored){
        }
    }

    public static void main(String[] args){

        OptionParser parser = new OptionParser(){
            {
                accepts("is", "input schema")
                        .withRequiredArg().describedAs("schema").required();

                accepts("in", "input file (default: STDIN)").withRequiredArg().describedAs("file");
                acceptsAll(Arrays.asList("h", "?", "help"), "help");
            }
        };

        int result = 1;

        try{
            OptionSet options = parser.parse(args);
            if (options.has("?")){
                printHelp(parser);
            }
            else{
                Validate validator = new Validate();
                IOContextBuilder inputBuilder = new IOContextBuilder();
                inputBuilder.schema(
                        SchemaFactory.buildSchema(new FileInputStream((String) options.valueOf("is"))));
                inputBuilder.in(
                        options.has("in")
                        ? new BufferedInputStream(new FileInputStream((String) options.valueOf("in")))
                        : new BufferedInputStream(System.in));
                validator.validate(inputBuilder.build());
                result = 0;
            }
        }
        catch (SchemaException ex){
            System.err.println(ex.getMessage());
        }
        catch (ConversionException ex){
            System.err.println(ex.getMessage());
        }
        catch (ValidationException ex){
            System.err.println(ex.getMessage());
        }
        catch (FileNotFoundException ex){
            System.err.println(ex.getMessage());
        }
        catch (IOException ex){
            System.err.println(ex.getMessage());
        }
        catch (OptionException ex){
            System.err.println(ex.getMessage());
            printHelp(parser);
        }

        System.exit(result);
    }
}
