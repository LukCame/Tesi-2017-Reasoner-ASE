package it.uniroma2.ase.model.inferenceRuleHandler;

import grammar.InferenceRulesGrammarParser;
import it.uniroma2.ase.model.exception.InferenceRulesParseFileException;
import it.uniroma2.ase.model.exception.InferenceRulesParsingException;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

/**
 * A custom Error Listener to catch the lexer errors of inference rules gramamr.
 *
 * @author L.Camerlengo
 *
 */
public class GrammarErrorListner extends BaseErrorListener {

    private final static Logger LOGGER = Logger.getLogger(GrammarErrorListner.class.getName());

    private InferenceRulesParseFileException exception;

    private boolean thereWasAnError;

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg,
            RecognitionException e) {
        List<String> stack = ((InferenceRulesGrammarParser) recognizer).getRuleInvocationStack();
        Collections.reverse(stack);

        exception = new InferenceRulesParseFileException("rule stack: " + stack + " line: " + line + ": " + charPositionInLine + " at " + offendingSymbol + ": " + msg);
        setThereWasAnError(true);
    }

    public boolean isThereWasAnError() {
        return thereWasAnError;
    }

    public void setThereWasAnError(boolean thereWasAnError) {
        this.thereWasAnError = thereWasAnError;
    }

    public InferenceRulesParseFileException getException() {
        return exception;
    }

    public void setException(InferenceRulesParseFileException exception) {
        this.exception = exception;
    }

    
    
    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
        LOGGER.log(Level.INFO, "errore nel parsing");
        super.reportAmbiguity(recognizer, dfa, startIndex, stopIndex, exact, ambigAlts, configs); //To change body of generated methods, choose Tools | Templates.
    }

}
