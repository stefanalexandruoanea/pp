package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EbookProcessor {
    public static void main(String[] args) {
        // Check if the input file exists
        if (args.length == 0) {
            System.out.println("Please provide the name of the input file.");
            return;
        }
        String inputFile = args[0];
        File file = new File(inputFile);
        if (!file.exists()) {
            System.out.println("The input file does not exist.");
            return;
        }

        // Read in the file
        String content = "";
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                content += line + "\n";
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the input file.");
            return;
        }

        // Remove multiple spaces and newlines
        content = content.replaceAll("\\s+", " ");
        content = content.replaceAll("(\\r\\n|\\r|\\n){2,}", "\n");

        // Detect and remove page numbers
        Pattern pattern = Pattern.compile("\\s+\\d+\\s+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String match = matcher.group(0);
            content = content.replace(match, " ");
        }

        // Output the processed text
        System.out.println(content);
    }
}
