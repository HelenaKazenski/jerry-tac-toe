// Find optimal move
public Best chooseMove( int side )
{
    int opp; // the other side
    Best reply; // opponent's best reply
    int dc; // placeholder
    int simpleEval; // result of an immediate evaluation
    int bestRow = 0; //
    int bestColumn = 0;
    int value;

    if( ( simpleEval= positionValue( ) ) != UNCLEAR )
    {
        return new Best( simpleEval );
    }

    if( side == COMPUTER )
    {
        opp = HUMAN; value = HUMAN_WIN;
    }
    else
    {
        opp = COMPUTER; value = COMPUTER_WIN;
    }

    for( int row = 0; row < 3; row++ )
    {
        for( int column = 0; column < 3; column++ )
        {
            if( squareIsEmpty( row, column ))
            {
                place( row, column, side );
                reply = chooseMove( opp );
                place( row, column, EMPTY );

                // Update if side gets better position
                if( side == COMPUTER && reply.val >= value
                    || side == HUMAN && reply.val <= value )
                {
                    value = reply.val;
                    bestRow = row; bestColumn = column;
                }
            }

            return new Best( value, bestRow, bestColumn );
        }
    }
}