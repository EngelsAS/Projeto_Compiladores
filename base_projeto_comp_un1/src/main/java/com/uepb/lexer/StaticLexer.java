package com.uepb.lexer;

import java.io.IOException;
import com.uepb.token.Token;
import com.uepb.token.TokenType;

public class StaticLexer implements Lexer {
    
    private final CharBuffer reader;

    public StaticLexer(String programSource) throws IOException {
        reader = new CharBuffer(programSource);
    }

    public Token readNextToken() throws IOException {
        int readedChar;

        while (true) {
            readedChar = reader.readNextChar();
            char c = (char) readedChar;

            if (readedChar == CharBuffer.FILE_END) {
                return new Token(TokenType.EOF, null);
            }

            if (Character.isWhitespace(c)) {
                continue;
            }

            if (c == ';') {
                return new Token(TokenType.SEMICOLON, ";");
            }

            if (c == '#') {
                while (c != '\n' && readedChar != CharBuffer.FILE_END) {
                    readedChar = reader.readNextChar();
                    c = (char) readedChar;
                }
                continue; // Continue to skip the comment.
            }
            if (c == '(') {
                return new Token(TokenType.LPAREN, "(");
            }

            if (c == ')') {
                return new Token(TokenType.RPAREN, ")");
            }
            if (Character.isLetter(c)) {
                StringBuilder identifierBuilder = new StringBuilder();
                identifierBuilder.append(c);

                while (true) {
                    readedChar = reader.readNextChar();
                    c = (char) readedChar;

                    if (Character.isLetterOrDigit(c)) {
                        identifierBuilder.append(c);
                    } else {
                        reader.pushback(readedChar); // Put back the non-identifier char.
                        break;
                    }
                }

                return new Token(TokenType.ID, identifierBuilder.toString());
            }

            if (Character.isDigit(c)) {
                StringBuilder numberBuilder = new StringBuilder();
                boolean decimalPointEncountered = false;

                do {
                    numberBuilder.append(c);
                    readedChar = reader.readNextChar();
                    c = (char) readedChar;

                    if (c == '.') {
                        decimalPointEncountered = true;
                        numberBuilder.append(c);
                        readedChar = reader.readNextChar();
                        c = (char) readedChar;
                    }

                } while (Character.isDigit(c));

                reader.pushback(readedChar); // Put back the non-numeric char.

                if (decimalPointEncountered) {
                    return new Token(TokenType.FLOAT, numberBuilder.toString());
                } else {
                    return new Token(TokenType.INT, numberBuilder.toString());
                }
            }

            if (c == '+' || c == '-' || c == '*' || c == '^' || c == '/') {
                switch (c) {
                    case '+':
                        return new Token(TokenType.OPSUM, String.valueOf(c));
                    case '-':
                        return new Token(TokenType.OPSUB, String.valueOf(c));
                    case '*':
                        return new Token(TokenType.OPMUL, String.valueOf(c));
                    case '^':
                        return new Token(TokenType.OPEXP, String.valueOf(c));
                    case '/':
                        return new Token(TokenType.OPDIV, String.valueOf(c));
                    default:
                        return null;
                }
            }
        }
    }



    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
    }
}