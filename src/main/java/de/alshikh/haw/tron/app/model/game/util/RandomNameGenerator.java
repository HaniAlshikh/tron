package de.alshikh.haw.tron.app.model.game.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomNameGenerator {
    public static String get() {
        String result = "";

        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("randomNames.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is)));
            List<String> words = reader.lines().collect(Collectors.toList());
            result = words.get(new Random(System.currentTimeMillis()).nextInt(words.size()));
        } catch (Exception ignored) {}

        return result;
    }
}
