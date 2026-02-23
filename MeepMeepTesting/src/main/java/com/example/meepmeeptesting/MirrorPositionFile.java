package com.example.meepmeeptesting;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MirrorPositionFile {
    public static void main(String[] args) throws IOException {

        // ORIGINAL FILE PATH
        String originalPath = "C:\\Users\\Barnyard\\StudioProjects\\BarnYard-FarminatorV1\\TeamCode\\src\\main\\java\\org\\firstinspires\\ftc\\teamcode\\opmodes\\auto\\red\\far\\RedFarTemp.java";
        File inputFile = new File(originalPath);
        if (!inputFile.exists()) {
            try {
                throw new IllegalArgumentException("Failed to find path");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        // -------------------- FLIP COLOR IN PATH --------------------
        String newPath = flipColorInPath(originalPath);


        File newFile = new File(newPath);

        // DELETE FILE IF EXISTS
        if (newFile.exists()) {
            if (!newFile.delete()) {
                try {
                    throw new IllegalArgumentException("Failed to delete existing file: " + newPath);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }

        newFile.createNewFile();

        try (Scanner reader = new Scanner(inputFile);
             FileWriter writer = new FileWriter(newFile)) {

            while (reader.hasNextLine()) {
                String data = reader.nextLine();

                // Mirror Pose2d if present (example: mirror along Y)
                if (data.contains("new Pose2d") || data.contains("Math.toRadians") || data.contains("new Rotation2d")) {
                    data = mirrorPose(data, "x");  // or "X" depending on your choice
                }

                // Flip color in content: blue ↔ red
                data = flipColor(data);

                writer.write(data + "\n");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("File created: " + newPath);
    }

    // -------------------- MIRROR FUNCTION --------------------
    public static String mirrorPose(String line, String axis) {
        axis = axis.toUpperCase();

        if (axis.equals("Y")) {
            Pattern xPattern = Pattern.compile("Pose2d\\s*\\(\\s*([-\\d.]+)");
            Matcher xMatcher = xPattern.matcher(line);

            if (xMatcher.find()) {
                double x = Double.parseDouble(xMatcher.group(1));
                line = xMatcher.replaceFirst("Pose2d(" + (-x));
            }

            Pattern anglePattern = Pattern.compile("Math\\.toRadians\\s*\\(\\s*([-\\d.]+)\\s*\\)");
            Matcher angleMatcher = anglePattern.matcher(line);
            if (angleMatcher.find()) {
                double angle = Double.parseDouble(angleMatcher.group(1));
                double mirroredAngle = 180 - angle;
                line = angleMatcher.replaceFirst("Math.toRadians(" + mirroredAngle + ")");
            }
            Pattern Rotation2dPattern = Pattern.compile("(Rotation2d\\s*\\(\\s*[-\\d.]+,\\s*)([-\\d.]+)");
            Matcher Rotation2dMatcher = Rotation2dPattern.matcher(line);
            if (Rotation2dMatcher.find()) {
                double Rotation = Double.parseDouble(Rotation2dMatcher.group(2));
                line = Rotation2dMatcher.replaceFirst("$1" + (-Rotation));
            }


        } else if (axis.equals("X")) {
            Pattern xPattern = Pattern.compile("(Pose2d\\s*\\(\\s*[-\\d.]+,\\s*)([-\\d.]+)");
            Matcher xMatcher = xPattern.matcher(line);
            if (xMatcher.find()) {
                double x = Double.parseDouble(xMatcher.group(2));
                line = xMatcher.replaceFirst("$1" + (-x));
            }

            Pattern anglePattern = Pattern.compile("Math\\.toRadians\\s*\\(\\s*([-\\d.]+)\\s*\\)");
            Matcher angleMatcher = anglePattern.matcher(line);
            if (angleMatcher.find()) {
                double angle = Double.parseDouble(angleMatcher.group(1));
                double mirroredAngle = -angle;
                line = angleMatcher.replaceFirst("Math.toRadians(" + mirroredAngle + ")");
            }
            Pattern Rotation2dPattern = Pattern.compile("Rotation2d\\s*\\(\\s*([-\\d.]+)");
            Matcher Rotation2dMatcher = Rotation2dPattern.matcher(line);

            if (Rotation2dMatcher.find()) {
                double Rotation = Double.parseDouble(Rotation2dMatcher.group(1));
                line = Rotation2dMatcher.replaceFirst("Rotation2d(" + (-Rotation));
            }
        }

        return line;
    }

    // -------------------- FLIP COLOR IN CONTENT --------------------
    public static String flipColor(String line) {
        Pattern pattern = Pattern.compile("(blue|red)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String match = matcher.group();
            String replacement = match.equalsIgnoreCase("blue") ? "red" : "blue";
            replacement = preserveCase(match, replacement);
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    // Preserve capitalization
    private static String preserveCase(String original, String replacement) {
        if (original.equals(original.toUpperCase())) return replacement.toUpperCase();
        if (Character.isUpperCase(original.charAt(0))) return replacement.substring(0,1).toUpperCase() + replacement.substring(1).toLowerCase();
        return replacement.toLowerCase();
    }

    // -------------------- FLIP COLOR IN PATH --------------------
    private static String flipColorInPath(String path) {
        Pattern pattern = Pattern.compile("(blue|red)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(path);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String match = matcher.group();
            String replacement = match.equalsIgnoreCase("blue") ? "red" : "blue";
            replacement = preserveCase(match, replacement);
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
