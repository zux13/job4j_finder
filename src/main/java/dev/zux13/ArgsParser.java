package dev.zux13;

import java.util.HashMap;

public class ArgsParser {

    private final HashMap<String, String> arguments = new HashMap<>();

    public ArgsParser(String[] args) throws MissingArgumentException {
        for (String arg : args) {
            String[] split = arg.split("=", 2);
            if (split.length < 2 || split[0].isEmpty() || split[1].isEmpty()) {
                throw new MissingArgumentException("Invalid argument: " + arg);
            }
            arguments.put(split[0], split[1]);
        }

        if (!arguments.containsKey("-d")) {
            throw new MissingArgumentException("Missing required argument: -d (directory)");
        }
        if (!arguments.containsKey("-n")) {
            throw new MissingArgumentException("Missing required argument: -n (pattern)");
        }
        if (!arguments.containsKey("-t")) {
            throw new MissingArgumentException("Missing required argument: -t (search type)");
        }
        if (!arguments.containsKey("-o")) {
            throw new MissingArgumentException("Missing required argument: -o (output file)");
        }
    }

    public String getDirectory() {
        return arguments.get("-d");
    }

    public String getPattern() {
        return arguments.get("-n");
    }

    public SearchType getSearchType() throws InvalidArgumentException {
        String type = arguments.get("-t");
        return switch (type) {
            case "mask" -> SearchType.MASK;
            case "name" -> SearchType.NAME;
            case "regex" -> SearchType.REGEX;
            default -> throw new InvalidArgumentException("Invalid search type: " + type);
        };
    }

    public String getOutputFile() {
        return arguments.get("-o");
    }
}
