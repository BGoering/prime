package com.realcomp.prime.record.io;

import com.realcomp.prime.schema.Schema;
import com.realcomp.prime.validation.Severity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


public class IOContextBuilder{

    protected Schema schema;
    protected InputStream in;
    protected OutputStream out;
    protected Map<String, String> attributes;
    protected Severity validationExceptionThreshold = Severity.HIGH;

    public IOContextBuilder(){
        attributes = new HashMap<>();
    }

    public IOContextBuilder(IOContext context){
        attributes = new HashMap<>();
        attributes.putAll(context.attributes);
        if (context.schema != null){
            schema = new Schema(context.schema);
        }
        in = context.in;
        out = context.out;
        validationExceptionThreshold = context.validationExeptionThreshold;
    }

    public IOContextBuilder schema(Schema schema){
        if (schema != null){
            this.schema = new Schema(schema);
        }
        return this;
    }

    public IOContextBuilder in(InputStream in){
        this.in = in;
        return this;
    }

    public IOContextBuilder out(OutputStream out){
        this.out = out;
        return this;
    }

    public IOContextBuilder attributes(Map<String, String> attributes){
        if (attributes != null){
            this.attributes.putAll(attributes);
        }
        return this;
    }

    public IOContextBuilder attribute(String name, String value){
        attributes.put(name, value);
        return this;
    }

    public IOContextBuilder validationExceptionThreshold(Severity severity){
        if (severity == null){
            throw new IllegalArgumentException("severity is null");
        }
        validationExceptionThreshold = severity;
        return this;
    }

    public IOContext build() throws IOException {
        return new IOContext(this);
    }
}
