package com.realcomp.data.conversion;

import com.realcomp.names.Name;
import com.realcomp.names.NameParser;
import java.util.List;

/**
 * Parses a name, returning the name prefix if the name is and instance of IndividualName; else ""
 *
 * @author krenfro
 */
@com.realcomp.data.annotation.Converter("nameSuffix")
public class NameSuffix extends StringConverter {

    private boolean lastNameFirst = true;
    private boolean recognizeCompanyNames = true;
    private boolean recognizeTrusts = true;


    @Override
    public Object convert(Object value) throws ConversionException{

        String retVal = null;
        if (value != null){
            retVal = "";
            List<Name> names = NameParser.parse(value.toString(), lastNameFirst, recognizeCompanyNames, recognizeTrusts);
            if (!names.isEmpty()){
                Name name = names.get(0);
                if (!name.isCompanyName()){
                    retVal = name.getSuffix();
                }
            }
        }

        return retVal;
    }

    public boolean isLastNameFirst() {
        return lastNameFirst;
    }

    public void setLastNameFirst(boolean lastNameFirst) {
        this.lastNameFirst = lastNameFirst;
    }

    public boolean isRecognizeCompanyNames(){
        return recognizeCompanyNames;
    }

    public void setRecognizeCompanyNames(boolean recognizeCompanyNames){
        this.recognizeCompanyNames = recognizeCompanyNames;
    }

    public boolean isRecognizeTrusts(){
        return recognizeTrusts;
    }

    public void setRecognizeTrusts(boolean recognizeTrusts){
        this.recognizeTrusts = recognizeTrusts;
    }

    @Override
    public NameSuffix copyOf(){
        NameSuffix copy = new NameSuffix();
        copy.setLastNameFirst(lastNameFirst);
        copy.setRecognizeCompanyNames(recognizeCompanyNames);
        copy.setRecognizeTrusts(recognizeTrusts);
        return copy;
    }

    @Override
    public int hashCode(){
        int hash = 5;
        hash = 17 * hash + (this.lastNameFirst ? 1 : 0);
        hash = 17 * hash + (this.recognizeCompanyNames ? 1 : 0);
        hash = 17 * hash + (this.recognizeTrusts ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        final NameSuffix other = (NameSuffix) obj;
        if (this.lastNameFirst != other.lastNameFirst){
            return false;
        }
        if (this.recognizeCompanyNames != other.recognizeCompanyNames){
            return false;
        }
        if (this.recognizeTrusts != other.recognizeTrusts){
            return false;
        }
        return true;
    }

    
}
