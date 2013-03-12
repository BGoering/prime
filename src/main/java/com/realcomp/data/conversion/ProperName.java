package com.realcomp.data.conversion;

import com.realcomp.names.Name;
import com.realcomp.names.NameParser;
import java.util.List;

/**
 * Parses a name, returning a clean up version. If it is an IndividualName,
 * then format is prefix first middle last suffix; else if CompanyName, then the cleaned
 * company name is used.
 *
 * <p>
 * Only one name is returned, so if multiple names are in the input field, that
 * information will be lost.  For example, "Jeanine" would be lost if formatting
 * "Renfro, Ryan Kyle & Jeanine"
 * </p>
 *
 * @author krenfro
 */
@com.realcomp.data.annotation.Converter("properName")
public class ProperName extends StringConverter {

    private boolean lastNameFirst = true;
    private boolean recognizeCompanyNames = true;
    private boolean recognizeTrusts = true;

    @Override
    public Object convert(Object value) throws ConversionException{

        String retVal = null;
        if (value != null){
            retVal = value.toString();
            List<Name> names = NameParser.parse(value.toString(), lastNameFirst, recognizeCompanyNames, recognizeTrusts);
            if (!names.isEmpty())
                retVal = names.get(0).toString();
        }

        return retVal;
    }

    @Override
    public ProperName copyOf(){
        ProperName copy = new ProperName();
        copy.setLastNameFirst(lastNameFirst);
        copy.setRecognizeCompanyNames(recognizeCompanyNames);
        copy.setRecognizeTrusts(recognizeTrusts);
        return copy;
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
    public int hashCode(){
        int hash = 3;
        hash = 67 * hash + (this.lastNameFirst ? 1 : 0);
        hash = 67 * hash + (this.recognizeCompanyNames ? 1 : 0);
        hash = 67 * hash + (this.recognizeTrusts ? 1 : 0);
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
        final ProperName other = (ProperName) obj;
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
