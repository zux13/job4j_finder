package dev.zux13;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class FinderApp {
    public static void main(String[] args) {
        try {
            ArgsParser parser = new ArgsParser(args);
            Validator validator = new Validator();

            if (!validator.validate(parser)) {
                System.out.println("Invalid arguments. Usage: -d=directory -n=filename -t=searchType -o=outputFile");
                return;
            }

            FileFinder finder = new FileFinder(
                    parser.getSearchType(),
                    parser.getPattern(),
                    parser.getDirectory()
            );

            List<Path> foundFiles = finder.findFiles();
            PrintWriter writer = new PrintWriter(new FileWriter(parser.getOutputFile()));
            for (Path file : foundFiles) {
                writer.println(file.toString());
            }
            writer.close();

        } catch (MissingArgumentException | InvalidArgumentException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}