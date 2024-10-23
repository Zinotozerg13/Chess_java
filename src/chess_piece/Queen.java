package chess_piece;

import main.game_panel;

public class Queen extends piece {
    public Queen(int color, int col, int row)
    {
        super(color,col,row);
        type=type.QUEEN;
        if(color==game_panel.WHITE)
        {
            image=getImage("res\\Pieces\\white-queen.png");
        }
        else{
            image=getImage("res\\Pieces\\black-queen.png");
        }
    }
    public boolean canMove(int targetCol,int targetRow){
        if(isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) ==false)
        {
            if(targetCol==preCol||targetRow==preRow)
            {
                if(isValidSquare(targetCol,targetRow) && pieceIsOnStraightLine(targetCol,targetRow)==false) {
                    return true;
                }
            }
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
