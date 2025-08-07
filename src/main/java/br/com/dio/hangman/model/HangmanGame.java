package br.com.dio.hangman.model;

import br.com.dio.hangman.exception.GameIsFinishedException;
import br.com.dio.hangman.exception.LetterAlreadyInputtedException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static br.com.dio.hangman.model.HangmanGameStatus.*;

public class HangmanGame {

    private final static int HANGMAN_INITIAL_LINE_LENGTH = 9;
    private final static int HANGMAN_INITIAL_LINE_LENGTH_WITH_LINE_SEPARATOR = 10;

    private final int lineSize;
    private final int hangmanInitialSize;
    private final List<HangmanChar> hangmanPaths;
    private final List<HangmanChar> characters;
    private final List<Character> failAttempts = new ArrayList<>();
    private final String tip;

    private String hangman;
    private HangmanGameStatus hangmanGameStatus;

    public HangmanGame(final List<HangmanChar> characters, final String tip) {
        var whiteSpaces = " ".repeat(characters.size());
        var characterSpace = "-".repeat(characters.size());
        this.lineSize = HANGMAN_INITIAL_LINE_LENGTH_WITH_LINE_SEPARATOR + whiteSpaces.length();
        this.hangmanGameStatus = PENDING;
        this.hangmanPaths = buildHangmanPathsPosition();
        buildHangmanDesgin(whiteSpaces, characterSpace);
        this.characters = setCharacterSpacePositionInGame(characters, whiteSpaces.length());
        this.hangmanInitialSize = hangman.length();
        this.tip = tip;

    }

    public void inputCharacter(final char character) {
        String message;
        if (this.hangmanGameStatus != PENDING) {
            if (this.hangmanGameStatus == WIN) message = "Parabens, voce ganhou!";
            else message = "Você perdeu, tente novamente! ";
            throw new GameIsFinishedException(message);
        }
        var found = this.characters.stream()
                .filter(c -> c.getCharacter() == character)
                .toList();

        if (this.failAttempts.contains(character)){
            throw new LetterAlreadyInputtedException("A letra " + character + " ja foi informada anteriormente!");
        }

        if (found.isEmpty()) {
            failAttempts.add(character);
            if (failAttempts.size() >= 6) {
                this.hangmanGameStatus = LOSE;
            }
            rebuildHangman(this.hangmanPaths.removeFirst());
            return;
        }

        if (found.getFirst().isVisible()) {
            throw new LetterAlreadyInputtedException("A letra " + character + " ja foi informada anteriormente!");
        }
        this.characters.forEach((c -> {
            if (c.getCharacter() == found.getFirst().getCharacter()) {
                c.enableVisibility();
            }
        }));
        if (this.characters.stream().noneMatch(HangmanChar::isInvisible)) {
            this.hangmanGameStatus = WIN;
        }
        rebuildHangman(found.toArray(HangmanChar[]::new));
    }


    @Override
    public String toString() {
        return this.hangman;
    }

    private List<HangmanChar> buildHangmanPathsPosition() {
        final var HEAD_LINE = 2;
        final var ARMS_BODY_LINE = 3;
        final var LEGS_LINE = 4;

        return new ArrayList<>(List.of(
                new HangmanChar('0', this.lineSize * HEAD_LINE + 5),
                new HangmanChar('|', this.lineSize * ARMS_BODY_LINE + 5),
                new HangmanChar('/', this.lineSize * ARMS_BODY_LINE + 4),
                new HangmanChar('\\', this.lineSize * ARMS_BODY_LINE + 6),
                new HangmanChar('/', this.lineSize * LEGS_LINE + 4),
                new HangmanChar('\\', this.lineSize * LEGS_LINE + 6)
        ));
    }


    private List<HangmanChar> setCharacterSpacePositionInGame(final List<HangmanChar> characters, final int whiteSpacesAmount) {
        final var LINE_LETTER = 6;
        for (int i = 0; i < characters.size(); i++) {
            characters.get(i).setPosition(this.lineSize * LINE_LETTER + HANGMAN_INITIAL_LINE_LENGTH + i);
        }
        return characters;
    }

    private void rebuildHangman(final HangmanChar... hangmanChars) {
        var hangmanBuilder = new StringBuilder(this.hangman);
        Stream.of(hangmanChars).forEach(h -> hangmanBuilder.setCharAt(h.getPosition(), h.getCharacter()));
        String failMessage = "";
        if (!this.failAttempts.isEmpty()) failMessage = "Tentativas" + this.failAttempts;
        this.hangman = hangmanBuilder.substring(0, hangmanInitialSize) + failMessage;
    }


    private void buildHangmanDesgin(final String whiteSpaces, final String characterSpaces){
        hangman =  " -----  " + whiteSpaces + System.lineSeparator() +
                " |   |  " + whiteSpaces + System.lineSeparator() +
                " |      " + whiteSpaces + System.lineSeparator() +
                " |      " + whiteSpaces + System.lineSeparator() +
                " |      " + whiteSpaces + System.lineSeparator() +
                " |      " + whiteSpaces + System.lineSeparator() +
                " |      " + whiteSpaces + System.lineSeparator() +
                "========" + characterSpaces + System.lineSeparator();
    }

    public HangmanGameStatus getHangmanGameStatus() {
        return hangmanGameStatus;
    }

    public String getTip() {
        return tip;
    }
}
