package it.uniroma2.ase.domain;

import org.eclipse.rdf4j.model.Statement;

/**
 *
 * @author L.Camerlengo
 */
public class EnrichedStatement {

    private Statement statement;

    private boolean isInferred;

    private boolean isInvented;

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public boolean isIsInferred() {
        return isInferred;
    }

    public void setIsInferred(boolean isInferred) {
        this.isInferred = isInferred;
    }

    public boolean isIsInvented() {
        return isInvented;
    }

    public void setIsInvented(boolean isInvented) {
        this.isInvented = isInvented;
    }

}
