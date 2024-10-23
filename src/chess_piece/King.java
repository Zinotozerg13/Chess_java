package chess_piece;

import main.Type;
import main.game_panel;

public class King extends piece
{
    public King(int color, int col, int row)
    {
        super(color,col,row);
        type= Type.KING;
        if(color==game_panel.WHITE)
        {
            image=getImage("res\\Pieces\\white-king.png");
        }
        else{
            image=getImage("res\\Pieces\\black-king.png");
        }

    }
    public boolean canMove(int targetCol,int targetRow){
        if(isWithinBoard(targetCol,targetRow))
        {
            if(Math.abs(targetCol-preCol)+Math.abs(targetRow-preRow)==1||
                    Math.abs(targetCol-preCol)*Math.abs(targetRow-preRow)==1)
            {
                if(isValidSquare(targetCol,targetRow)) {
                    return true;
                }
            }
            if(moved==false) {   //RIGHT CASTLING//

                if (targetCol == preCol + 2 && targetRow == preRow && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                    for (piece Piece : game_panel.simPieces) {
                        if (Piece.col == preCol + 3 && Piece.row == preRow && Piece.moved == false) {
                            game_panel.castlingP = Piece;
                            return true;
                        }
                    }
                }
                //LEFT CASTLING//
                // LEFT CASTLING
                if (targetCol == preCol - 2 && targetRow == preRow && pieceIsOnStraightLine(targetCol, targetRow) == false) {
                    piece p[] = new piece[2];
                    for (piece Piece : game_panel.simPieces) {
                        if (Piece.col == preCol - 3 && Piece.row == preRow) {
                            p[0] = Piece;

                        }
                        if (Piece.col == preCol - 4 && Piece.row == preRow) {
                            p[1] = Piece;

                        }
                    }
                    // Ensure proper null check for both p[0] and p[1]
                    if (p[0] == null && p[1] != null) {
                        if (p[1].moved == false) {
                            game_panel.castlingP = p[1];
                            return true;
                        }
                    }
                }

            }
            }

        return false;
    }

}
