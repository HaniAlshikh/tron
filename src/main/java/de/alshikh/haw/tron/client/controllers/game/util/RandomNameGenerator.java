package de.alshikh.haw.tron.client.controllers.game.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomNameGenerator {
    public static String get() {
        String result = "";

        try {
            BufferedReader reader = new BufferedReader(new FileReader("randomNames.txt"));
            List<String> words = reader.lines().collect(Collectors.toList());
            result = words.get(new Random(System.currentTimeMillis()).nextInt(words.size()));
        } catch (FileNotFoundException ignored) {}

        return result;
    }
}
