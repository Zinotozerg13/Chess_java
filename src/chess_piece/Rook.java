package chess_piece;

import main.game_panel;

public class Rook extends piece {
    public Rook(int color,int col,int row)
    {
        super(color,col,row);
        type=type.ROOK;
        if(color==game_panel.WHITE)
        {
            image=getImage("res\\Pieces\\white-rook.png");
        }
        else{
            image=getImage("res\\Pieces\\black-rook.png");
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
            }

        return false;
    }

}
