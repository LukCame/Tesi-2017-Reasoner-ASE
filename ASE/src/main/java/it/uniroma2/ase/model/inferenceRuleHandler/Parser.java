/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma2.ase.model.inferenceRuleHandler;


import grammar.InferenceRulesGrammarBaseListener;
import grammar.InferenceRulesGrammarLexer;
import grammar.InferenceRulesGrammarParser;
import grammar.InferenceRulesGrammarParser.ParseInferenceRuleContext;
import it.uniroma2.ase.domain.Rules;
import it.uniroma2.ase.model.exception.InferenceRulesParsingException;
import it.uniroma2.ase.model.exception.InferenceRulesFileIoException;
import it.uniroma2.ase.model.exception.InferenceRulesRecognitonException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 *
 * @author L.Camerlengo
 */
public class Parser {
    private final static Logger LOGGER = Logger.getLogger(Parser.class.getName());
    
    public Rules parsingInferenceRulesFile (File inferenceRulesFile) throws InferenceRulesParsingException{
        try{
            LOGGER.log(Level.INFO, "Antlr parsing file");
            FileInputStream fileInputStream = new FileInputStream(inferenceRulesFile);
            ANTLRInputStream antlrInputStream = new ANTLRInputStream(fileInputStream);
            //Create Lexer
            InferenceRulesGrammarLexer lexer = new InferenceRulesGrammarLexer(antlrInputStream);
            //Get token stream from lexer
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            //Create new parser from token stream.
            InferenceRulesGrammarParser parser = new InferenceRulesGrammarParser(tokens);
            // remove ConsoleErrorListener
            parser.removeErrorListeners();

            // add custom ErrorListener
            GrammarErrorListner grammarErrorListner = new GrammarErrorListner();
            parser.addErrorListener(grammarErrorListner);
            //Create new tree parsing explorer.
            ParseTreeWalker walker = new ParseTreeWalker();
            //Create new GrammarListner
            InferenceRulesGrammarBaseListener listener = new InferenceRulesGrammarBaseListener();
            //create new LoadParser
            LoaderParser loaderParser = new LoaderParser();
            //Execute the parsing operation
            ParseInferenceRuleContext inferenceRuleContext = parser.parseInferenceRule();

            if (grammarErrorListner.isThereWasAnError()) {
                InferenceRulesParsingException exception = grammarErrorListner.getException();
                throw exception;
            }

            //Explore parsing tree
            walker.walk(loaderParser, inferenceRuleContext);
            //Add listner to parser
            parser.addParseListener(listener);
            //Get the inference rule parsed
            return loaderParser.getInferenceRules();
        }catch(RecognitionException e){
            throw new InferenceRulesRecognitonException(e);
        }catch(IOException e){
            throw new InferenceRulesFileIoException(e);
        }
    }
}
