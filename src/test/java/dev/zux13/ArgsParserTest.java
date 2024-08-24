package dev.zux13;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgsParserTest {

    @Test
    void shouldParseArgumentsCorrectly() throws MissingArgumentException, InvalidArgumentException {
        String[] args = {"-d=/path/to/dir", "-n=*.txt", "-t=mask", "-o=output.txt"};
        ArgsParser parser = new ArgsParser(args);

        assertEquals("/path/to/dir", parser.getDirectory());
        assertEquals("*.txt", parser.getPattern());
        assertEquals(SearchType.MASK, parser.getSearchType());
        assertEquals("output.txt", parser.getOutputFile());
    }

    @Test
    void shouldThrowMissingArgumentExceptionWhenDirectoryIsMissing() {
        String[] args = {"-n=*.txt", "-t=mask", "-o=output.txt"};
        Exception exception = assertThrows(MissingArgumentException.class, () -> new ArgsParser(args));
        assertEquals("Missing required argument: -d (directory)", exception.getMessage());
    }

    @Test
    void shouldThrowMissingArgumentExceptionWhenPatternIsMissing() {
        String[] args = {"-d=/path/to/dir", "-t=mask", "-o=output.txt"};
        Exception exception = assertThrows(MissingArgumentException.class, () -> new ArgsParser(args));
        assertEquals("Missing required argument: -n (pattern)", exception.getMessage());
    }

    @Test
    void shouldThrowMissingArgumentExceptionWhenSearchTypeIsMissing() {
        String[] args = {"-d=/path/to/dir", "-n=*.txt", "-o=output.txt"};
        Exception exception = assertThrows(MissingArgumentException.class, () -> new ArgsParser(args));
        assertEquals("Missing required argument: -t (search type)", exception.getMessage());
    }

    @Test
    void shouldThrowMissingArgumentExceptionWhenOutputFileIsMissing() {
        String[] args = {"-d=/path/to/dir", "-n=*.txt", "-t=mask"};
        Exception exception = assertThrows(MissingArgumentException.class, () -> new ArgsParser(args));
        assertEquals("Missing required argument: -o (output file)", exception.getMessage());
    }

    @Test
    void shouldThrowInvalidArgumentExceptionForInvalidSearchType() {
        String[] args = {"-d=/path/to/dir", "-n=*.txt", "-t=invalidType", "-o=output.txt"};
        Exception exception = assertThrows(InvalidArgumentException.class, () -> {
            ArgsParser parser = new ArgsParser(args);
            parser.getSearchType();
        });
        assertEquals("Invalid search type: invalidType", exception.getMessage());
    }

    @Test
    void shouldThrowMissingArgumentExceptionForInvalidArgumentFormat() {
        String[] args = {"-d=/path/to/dir", "-n=*.txt", "-t", "-o=output.txt"}; // неправильный формат ключа "-t"
        Exception exception = assertThrows(MissingArgumentException.class, () -> new ArgsParser(args));
        assertEquals("Invalid argument: -t", exception.getMessage());
    }

    @Test
    void shouldThrowMissingArgumentExceptionForEmptyArgument() {
        String[] args = {"-d=/path/to/dir", "-n=*.txt", "-t=mask", "-o="}; // пустое значение для -o
        Exception exception = assertThrows(MissingArgumentException.class, () -> new ArgsParser(args));
        assertEquals("Invalid argument: -o=", exception.getMessage());
    }

    @Test
    void shouldThrowMissingArgumentExceptionForMissingEqualsSign() {
        String[] args = {"-d=/path/to/dir", "-n=*.txt", "-t=mask", "-o"}; // пропущен знак "=" для -o
        Exception exception = assertThrows(MissingArgumentException.class, () -> new ArgsParser(args));
        assertEquals("Invalid argument: -o", exception.getMessage());
    }

    @Test
    void shouldHandleMultipleArgumentsCorrectly() throws MissingArgumentException, InvalidArgumentException {
        String[] args = {"-d=/path/to/dir", "-n=*.txt", "-t=regex", "-o=output.log"};
        ArgsParser parser = new ArgsParser(args);

        assertEquals("/path/to/dir", parser.getDirectory());
        assertEquals("*.txt", parser.getPattern());
        assertEquals(SearchType.REGEX, parser.getSearchType());
        assertEquals("output.log", parser.getOutputFile());
    }
}
