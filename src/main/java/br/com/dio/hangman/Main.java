package br.com.dio.hangman;

import br.com.dio.hangman.exception.GameIsFinishedException;
import br.com.dio.hangman.exception.LetterAlreadyInputtedException;
import br.com.dio.hangman.model.HangmanChar;
import br.com.dio.hangman.model.HangmanGame;
import br.com.dio.hangman.model.HangmanGameStatus;

import java.util.Scanner;
import java.util.stream.Stream;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);

    public static void main(String... args) {
        var characters = Stream.of(args)
                .map(a -> a.toLowerCase().charAt(0))
                .map(HangmanChar::new).toList();
        var hangmanGame = new HangmanGame(characters);
        //System.out.println(characters);
        int option;
        do {
            System.out.println("=========================");
            System.out.println("      JOGO DA FORCA");
            System.out.println("=========================");
            System.out.println("1 - Jogar");
            System.out.println("2 - Instruções");
            System.out.println("3 - Sair");
            System.out.println("=========================");
            System.out.print(">> ");
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1 -> {
                    while (hangmanGame.getHangmanGameStatus() == HangmanGameStatus.PENDING) {
                        System.out.println("Informe uma letra: ");
                        var character = scanner.nextLine().charAt(0);
                        try {
                            hangmanGame.inputCharacter(character);
                        } catch (LetterAlreadyInputtedException | GameIsFinishedException ex) {
                            System.out.println(ex.getMessage());
                        } finally {
                            System.out.println(hangmanGame);
                        }
                    }
                    if (hangmanGame.getHangmanGameStatus() == HangmanGameStatus.WIN)
                        System.out.println("Você ganhou! Parabens!");
                    else
                        System.out.println("Você perdeu. Boa sorte na proxima tentativa!");
                    System.out.println("Fim do jogo! Espero que tenha se divertido! ");
                    System.exit(0);
                }
                case 2 -> showInstructions();
                case 3 -> System.out.println("Espero que tenha se divertido! Encerrando o jogo...");


                default -> System.out.println("Digite uma opção válida...");
            }

        } while (option != 3);

    }
    private static void showInstructions() {
        System.out.println("============ INSTRUÇÕES ============");
        System.out.println("- O objetivo é descobrir a palavra secreta antes que o boneco da forca seja completado.");
        System.out.println("- Digite uma letra por vez");
        System.out.println("- A cada erro, uma parte do boneco aparece");
        System.out.println("- Você tem apenas 6 tentativas");
        System.out.println("====================================");
        backToMenu();
    }

    private static void backToMenu() {
        System.out.println("Pressione ENTER para retornar ao menu...");
        scanner.nextLine();
    }
}
