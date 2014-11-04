package sqlparser;

/**
 * Created on 2014-10-04.
 */
public class WhereClauseSqlAST extends AbstractSQLCommand {
    @Override
    public Type getType() {
        return Type.Where;
    }
}
