package com.rahavoi;

public class Joker {
    private static String[] jokes = {
           "Q: \"Whats the object-oriented way to become wealthy?\"\n" + "\n" + "A: Inheritance",
            "To understand what recursion is, you must first understand recursion.",
            "После бурного корпоратива java-программисты не убирают за собой, потому что Garbage Collector вызывается автоматически",
            "Why did programmer quit his job? Because he did not get arrays."
    };

    public static String tellAJoke(){
        return jokes[(int) (Math.random() * jokes.length)];
    }

}
