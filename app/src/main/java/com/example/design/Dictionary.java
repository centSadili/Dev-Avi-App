package com.example.design;

import java.util.ArrayList;

public class Dictionary {

    public static String Word=null;
    public static String definition=null;
    public static String syntax=null;
    public static String explanation=null;
    public static String category=null;


    public static String getCategory() {
        return category;
    }

    public static void setCategory(String category) {
        Dictionary.category = category;
    }

    public static String getWord() {
        return Word;
    }

    public static void setWord(String word) {
        Word = word;
    }

    public static String getDefinition() {
        return definition;
    }

    public static void setDefinition(String definition) {
        Dictionary.definition = definition;
    }

    public static String getSyntax() {
        return syntax;
    }

    public static void setSyntax(String syntax) {
        Dictionary.syntax = syntax;
    }

    public static String getExplanation() {
        return explanation;
    }

    public static void setExplanation(String explanation) {
        Dictionary.explanation = explanation;
    }
}
