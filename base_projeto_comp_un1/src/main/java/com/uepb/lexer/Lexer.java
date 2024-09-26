package com.uepb.lexer;

import java.io.Closeable;
import java.io.IOException;

import com.uepb.token.Token;

public interface Lexer extends Closeable {
    public Token readNextToken() throws IOException;
}
