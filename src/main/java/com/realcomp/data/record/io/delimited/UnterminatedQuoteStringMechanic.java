package com.realcomp.data.record.io.delimited;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Repairs unterminated quoted strings.
 * The csv parser trips on poorly formatted delimited records.
 *
 * @author krenfro
 */
public class UnterminatedQuoteStringMechanic {


    private List<Fix> fixes;

    public UnterminatedQuoteStringMechanic(){
        fixes = new ArrayList<>();
        fixes.add(new Fix(Pattern.compile("(.+)(\"\",\")"), "$1\\\\\"\",\""));
    }

    public String repair(String hasProblem){
        for (Fix fix: fixes){
            hasProblem = fix.apply(hasProblem);
        }
        return hasProblem;
    }

    private class Fix{
        private Pattern original;
        private String replacement;

        public Fix(Pattern problem, String replacement){
            this.original = problem;
            this.replacement = replacement;
        }

        public String apply(String hasProblems){
            return original.matcher(hasProblems).replaceAll(replacement);
        }
    }
}
