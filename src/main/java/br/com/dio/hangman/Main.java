package br.com.dio.hangman;

import br.com.dio.hangman.exception.GameIsFinishedException;
import br.com.dio.hangman.exception.LetterAlreadyInputtedException;
import br.com.dio.hangman.model.HangmanChar;
import br.com.dio.hangman.model.HangmanGame;
import br.com.dio.hangman.model.HangmanGameStatus;
import br.com.dio.hangman.model.Words;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);

    public static void main(String... args) {
        List<Words> palavras = List.of(
                new Words("java", "Linguagem de programacao"),
                new Words("arquiteto", "Profissao"),
                new Words("bombeiro", "Profissao"),
                new Words("basquete", "Esporte"),
                new Words ("handebol", "Esporte"),
                new Words ("violino", "Instrumento musical"),
                new Words ("flauta", "Instrumento musical"),
                new Words ("morcego", "Animal"),
                new Words ("canguru", "Animal")
        );
        Words sortedWord = palavras.get((int) (Math.random() * palavras.size()));
        String word = sortedWord.getWord();
        String tip = sortedWord.getTip();
        var characters = word.chars()
                .mapToObj(c -> new HangmanChar((char) c))
                .toList();


        var hangmanGame = new HangmanGame(characters, tip);
        int option;
        do {
            System.out.println("=========================");
            System.out.println("      JOGO DA FORCA");
            System.out.println("=========================");
            System.out.println("1 - Jogar");
            System.out.println("2 - Regras");
            System.out.println("3 - Sair");
            System.out.println("=========================");
            System.out.print(">> ");
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1 -> {
                    startGame(hangmanGame);
                    System.exit(0);
                }
                case 2 -> showInstructions();
                case 3 -> System.out.println("Espero que tenha se divertido! Ate mais!");


                default -> System.out.println("Digite uma opção válida...");
            }

        } while (option != 3);

    }

    private static void startGame(HangmanGame hangmanGame) {
        while (hangmanGame.getHangmanGameStatus() == HangmanGameStatus.PENDING) {
            System.out.println(hangmanGame);
            System.out.println("Dica: " + hangmanGame.getTip());
            System.out.println("Informe uma letra: ");
            var character = scanner.nextLine().charAt(0);
            try {
                hangmanGame.inputCharacter(character);
            } catch (LetterAlreadyInputtedException | GameIsFinishedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        if (hangmanGame.getHangmanGameStatus() == HangmanGameStatus.WIN)
            System.out.println("Voce ganhou! Parabens!");
        else
            System.out.println("Voce perdeu. Boa sorte na proxima tentativa!");
        System.out.println("Fim do jogo! Espero que tenha se divertido! ");
    }

    private static void showInstructions() {
        System.out.println("============ Regras ============");
        System.out.println("- Objetivo: descobrir a palavra secreta antes que o boneco da forca seja completado.");
        System.out.println("- Digite uma letra por vez");
        System.out.println("- A cada erro, uma parte do boneco aparece");
        System.out.println("- Voce tem apenas 6 tentativas");
        System.out.println("================================");
        backToMenu();
    }

    private static void backToMenu() {
        System.out.println("Pressione ENTER para retornar ao menu...");
        scanner.nextLine();
    }
}
