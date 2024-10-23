package chess_piece;

import main.game_panel;

public class Bishop extends piece {
    public Bishop(int color, int col, int row)
    {
        super(color,col,row);
        type=type.BISHOP;
        if(color==game_panel.WHITE)
        {
            image=getImage("res\\Pieces\\white-bishop.png");
        }
        else{
            image=getImage("res\\Pieces\\black-bishop.png");
        }
    }

    public boolean canMove(int targetCol,int targetRow){
        if(isWithinBoard(targetCol,targetRow) && isSameSquare(targetCol, targetRow) ==false)
        {
            if(Math.abs(targetCol-preCol)==Math.abs(targetRow-preRow))
            {
                if(isValidSquare(targetCol,targetRow) && pieceIsOnDiagonalLine(targetCol,targetRow)==false) {
                    return true;
                }
            }
        }
        return false;
    }
}
