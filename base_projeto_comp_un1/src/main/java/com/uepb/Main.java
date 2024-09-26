package com.uepb;

import java.io.IOException;
import java.util.Scanner;

import com.uepb.lexer.StaticLexer;
import com.uepb.parser.Parser;
import com.uepb.parser.TokenBuffer;
import com.uepb.token.Token;
import com.uepb.token.TokenType;

public class Main {
    public static void main(String[] args) {
        StaticLexer lexer = null;
        try {
            lexer = new StaticLexer("code_example.uepb");
        } catch (IOException e) {
            e.printStackTrace();
            return; // Finaliza o programa se não for possível ler o arquivo
        }

        // Pergunta ao usuário qual opção ele deseja
        Scanner scanner = new Scanner(System.in);
        System.out.println("Escolha uma opção:");
        System.out.println("1. Executar Analisador Léxico");
        System.out.println("2. Executar Analisador Sintático");
        System.out.print("Digite o número da opção: ");
        String escolha = scanner.nextLine();

        switch (escolha) {
            case "1":
                executarLexico(lexer);
                break;
            case "2":
                executarSintatico(lexer);
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }

        scanner.close();
    }

    // Método para executar o Analisador Léxico
    private static void executarLexico(StaticLexer lexer) {
        Token token;
        try {
            while ((token = lexer.readNextToken()) != null) {
                if (token.type() == TokenType.EOF) {
                    System.out.println("Fim do arquivo: " + token);
                    break;
                }
                System.out.println(token);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para executar o Analisador Sintático
    private static void executarSintatico(StaticLexer lexer) {
        try (TokenBuffer tokenBuffer = new TokenBuffer(lexer)) {
            Parser parser = new Parser(tokenBuffer);
            parser.parse(); // Chama o parser para análise sintática
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}