package dev.zux13;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = new Validator();
    }

    @Test
    void shouldReturnFalseWhenDirectoryDoesNotExist() throws InvalidArgumentException, MissingArgumentException {
        // Передаем несуществующую директорию
        String[] args = {"-d=non_existing_directory", "-n=*.txt", "-t=mask", "-o=output.txt"};
        ArgsParser parser = new ArgsParser(args);

        assertFalse(validator.validate(parser), "Expected validation to fail when directory doesn't exist");
    }

    @Test
    void shouldReturnTrueForValidMaskPattern() throws InvalidArgumentException, MissingArgumentException {
        // Валидная маска
        String[] args = {"-d=" + System.getProperty("user.dir"), "-n=*.txt", "-t=mask", "-o=output.txt"};
        ArgsParser parser = new ArgsParser(args);

        assertTrue(validator.validate(parser), "Expected validation to pass for valid mask pattern");
    }

    @Test
    void shouldReturnFalseForInvalidMaskPattern() throws InvalidArgumentException, MissingArgumentException {
        // Невалидная маска (недопустимые символы)
        String[] args = {"-d=" + System.getProperty("user.dir"), "-n=*.|txt", "-t=mask", "-o=output.txt"};
        ArgsParser parser = new ArgsParser(args);

        assertFalse(validator.validate(parser), "Expected validation to fail for invalid mask pattern");
    }

    @Test
    void shouldReturnTrueForValidRegexPattern() throws InvalidArgumentException, MissingArgumentException {
        // Валидное регулярное выражение
        String[] args = {"-d=" + System.getProperty("user.dir"), "-n=^.*\\.txt$", "-t=regex", "-o=output.txt"};
        ArgsParser parser = new ArgsParser(args);

        assertTrue(validator.validate(parser), "Expected validation to pass for valid regex pattern");
    }

    @Test
    void shouldReturnFalseForInvalidRegexPattern() throws InvalidArgumentException, MissingArgumentException {
        // Невалидное регулярное выражение
        String[] args = {"-d=" + System.getProperty("user.dir"), "-n=[.*\\txt", "-t=regex", "-o=output.txt"};
        ArgsParser parser = new ArgsParser(args);

        assertFalse(validator.validate(parser), "Expected validation to fail for invalid regex pattern");
    }

    @Test
    void shouldReturnFalseWhenMaskPatternIsOnlyWildcard() throws InvalidArgumentException, MissingArgumentException {
        // Маска состоит только из символов * или ?
        String[] args = {"-d=" + System.getProperty("user.dir"), "-n=*", "-t=mask", "-o=output.txt"};
        ArgsParser parser = new ArgsParser(args);

        assertFalse(validator.validate(parser), "Expected validation to fail when mask pattern is only wildcard");
    }

    @Test
    void shouldReturnTrueForValidDirectoryAndOutputFile() throws InvalidArgumentException, MissingArgumentException {
        // Валидная директория и файл для записи
        String[] args = {"-d=" + System.getProperty("user.dir"), "-n=*.txt", "-t=mask", "-o=output.txt"};
        ArgsParser parser = new ArgsParser(args);

        assertTrue(validator.validate(parser), "Expected validation to pass for valid directory and output file");
    }

    @Test
    void shouldThrowInvalidArgumentExceptionForInvalidSearchType() {
        // Невалидный тип поиска
        String[] args = {"-d=" + System.getProperty("user.dir"), "-n=*.txt", "-t=invalid_type", "-o=output.txt"};

        Exception exception = assertThrows(InvalidArgumentException.class, () -> {
            ArgsParser parser = new ArgsParser(args);
            validator.validate(parser);
        });

        assertEquals("Invalid search type: invalid_type", exception.getMessage(), "Expected InvalidArgumentException for invalid search type");
    }
}
