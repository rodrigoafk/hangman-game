package br.com.dio.hangman.model;

public class Words {

    private final String word;
    private final String tip;

    public Words(String word, String tip) {
        this.word = word;
        this.tip = tip;
    }

    public String getWord() {
        return word;
    }

    public String getTip() {
        return tip;
    }
}
