package com.uepb.parser;

import java.io.IOException;

import com.uepb.parser.exceptions.SyntaxError;
import com.uepb.token.TokenType;

public class Parser {

    private final TokenBuffer tokenBuffer;

    public Parser(TokenBuffer tokenBuffer) {
        this.tokenBuffer = tokenBuffer;
    }

    public void parse() throws IOException {
        prog();
        match(TokenType.EOF);
        System.out.println("Parsing completo.");
    }

//    prog: (expr SEMICOLON)*;
    private void prog() throws IOException {
        while (tokenBuffer.lookAhead(1).type() != TokenType.EOF) {
            expr();
            match(TokenType.SEMICOLON); 
        }
    }
    
//    expr: termo restExpr;
    private void expr() throws IOException {
        this.termo();  
        this.restExpr();  
    }
    
//    restExpr: op1 termo restExpr | ;
    private void restExpr() throws IOException {
        var la = tokenBuffer.lookAhead(1);  

        
        if (la.type() == TokenType.OPSUM || la.type() == TokenType.OPSUB) {
            match(la.type());  
            this.termo();  
            this.restExpr();  
        }
        
    }
    
//    termo: potencia restTermo;
    private void termo() throws IOException{
    	this.potencia();  
    	this.restTermo();  
    }
    
//    restTermo: op2 potencia restTermo | ;
    private void restTermo() throws IOException{
    	var la = tokenBuffer.lookAhead(1);  

        
        if (la.type() == TokenType.OPMUL || la.type() == TokenType.OPDIV) {
            match(la.type());  
            this.potencia();  
            this.restTermo();  
        }
        
    }
    
//    potencia: fator (op3 potencia)?;
    private void potencia() throws IOException{
    	this.fator(); 

        var la = tokenBuffer.lookAhead(1); 

        
        if (la.type() == TokenType.OPEXP) {
            match(TokenType.OPEXP);  
            this.potencia();  
        }
    }
    
//    fator: (OPSUM | OPSUB)? (INT | FLOAT | ID | LPAREN expr RPAREN);
    private void fator() throws IOException {
        var la = tokenBuffer.lookAhead(1);

        // Verifica se o próximo token é um operador unário (+ ou -)
        if (la.type() == TokenType.OPSUM || la.type() == TokenType.OPSUB) {
            match(la.type()); // Consome o operador unário
        }

        // Depois de consumir o operador, verifica o tipo de token esperado
        la = tokenBuffer.lookAhead(1); // Atualiza o lookahead após consumir o operador
        switch (la.type()) {
            case INT:
                match(TokenType.INT);  
                break;
            case FLOAT:
                match(TokenType.FLOAT);  
                break;
            case ID:
                match(TokenType.ID);  
                break;
            case LPAREN:
                match(TokenType.LPAREN);  
                this.expr();  
                match(TokenType.RPAREN);  
                break;
            default:
                
                throw new SyntaxError(la, TokenType.INT, TokenType.FLOAT, TokenType.ID, TokenType.LPAREN);
        }
    }
    
    private void match(TokenType expected) throws IOException {
        var la = tokenBuffer.lookAhead(1); // Olha o token atual
        if (la.type() == expected) {
            System.out.println("Token aceito: " + la); // Token aceito
            tokenBuffer.confirmToken(); // Avança para o próximo token
        } else {
            throw new SyntaxError(la, expected); // Erro de sintaxe se não for o token esperado
        }
    }

}