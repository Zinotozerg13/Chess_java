package chess_piece;

import main.game_panel;

public class Knight extends piece {
    public Knight(int color,int col,int row)
    {
        super(color,col,row);
        type=type.KNIGHT;
        if(color==game_panel.WHITE)
        {
            image=getImage("res\\Pieces\\white-Knight.png");
        }
        else{
            image=getImage("res\\Pieces\\black-Knight.png");
        }
    }
    public boolean canMove(int targetCol,int targetRow){
        if(isWithinBoard(targetCol,targetRow))
        {
            if(Math.abs(targetCol-preCol)*Math.abs(targetRow-preRow)==2)
            {
                if(isValidSquare(targetCol,targetRow)) {
                    return true;
                }
            }
        }
        return false;
    }

}
