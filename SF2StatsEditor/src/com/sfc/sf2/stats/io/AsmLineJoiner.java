/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.stats.io;

import com.sfc.sf2.helpers.StringHelpers;
import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author TiMMy
 */
final class AsmLineJoiner {

    private AsmLineJoiner() {
    }

    static String joinContinuations(BufferedReader reader, String firstLine) throws IOException {
        StringBuilder sb = new StringBuilder(StringHelpers.trimAndRemoveComments(firstLine));
        while (sb.length() > 0 && sb.charAt(sb.length() - 1) == '&') {
            sb.setLength(sb.length() - 1);
            while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
                sb.setLength(sb.length() - 1);
            }
            String next = reader.readLine();
            if (next == null) {
                break;
            }
            String code = StringHelpers.trimAndRemoveComments(next);
            if (code.isEmpty()) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(code);
        }
        return sb.toString().trim();
    }

    static String argsAfterKeyword(String line, String keyword) {
        if (line.length() <= keyword.length()) {
            return "";
        }
        return line.substring(keyword.length()).trim();
    }

    static String[] splitCsv(String value) {
        if (value == null || value.isEmpty()) {
            return new String[0];
        }
        String[] parts = value.split("\\s*,\\s*");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        return parts;
    }

    static void writeContinuedCsv(java.io.FileWriter writer, String keyword, String values) throws IOException {
        String[] parts = splitCsv(values);
        if (parts.length == 0) {
            writer.write("                " + keyword + "\n");
            return;
        }
        if (parts.length == 1) {
            writer.write("                " + keyword + " " + parts[0] + "\n");
            return;
        }
        writer.write("                " + keyword + " " + parts[0] + ", &\n");
        for (int i = 1; i < parts.length; i++) {
            if (i < parts.length - 1) {
                writer.write("                              " + parts[i] + ", &\n");
            } else {
                writer.write("                              " + parts[i] + "\n");
            }
        }
    }
}
