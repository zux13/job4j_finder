package dev.zux13;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Validator {

    public boolean validate(ArgsParser parser) throws InvalidArgumentException {
        File dir = new File(parser.getDirectory());
        if (!dir.isDirectory() || !dir.exists()) {
            return false;
        }

        File out = new File(parser.getOutputFile());
        try {
            out.getCanonicalPath();
        } catch (IOException e) {
            return false;
        }

        String pattern = parser.getPattern();
        if (pattern.isEmpty()) {
            return false;
        }

        SearchType type = parser.getSearchType();
        if (type == SearchType.REGEX) {
            try {
                Pattern.compile(pattern);
            } catch (PatternSyntaxException e) {
                return false;
            }
        } else if (type == SearchType.MASK) {
            String validMaskPattern = "^[\\w\\-.?*]+$";
            return pattern.matches(validMaskPattern) && (!pattern.equals("*") && !pattern.equals("?"));
        }
        return true;
    }
}
