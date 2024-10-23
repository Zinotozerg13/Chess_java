package chess_piece;
import main.Type;
import main.game_panel;


public class pawn extends piece {
    public pawn(int color, int col, int row) {
        super(color, col, row);
        type= Type.PAWN;
        if (color == game_panel.WHITE) {
            image = getImage("C:\\Users\\Ronish\\Desktop\\java prog\\chess\\res\\Pieces\\white-pawn.png");
        } else {
            image = getImage("C:\\Users\\Ronish\\Desktop\\java prog\\chess\\res\\Pieces\\black-pawn.png");
        }
    }

    public boolean canMove(int targetCol, int targetRow) {
        if (isWithinBoard(targetCol, targetRow) && isSameSquare(targetCol, targetRow) == false) {
            int moveValue;
            if (color == game_panel.WHITE) {
                moveValue = -1;
            } else {
                moveValue = 1;
            }
            hittingP = getHittingP(targetCol, targetRow);
            if (targetCol == preCol && targetRow == preRow + moveValue && hittingP == null) {
                return true;
            }
            if(targetCol==preCol  && targetRow==preRow+moveValue*2 && hittingP==null &&moved==false && pieceIsOnStraightLine(targetCol,targetRow)==false)
            {
                return true;
            }
            if(Math.abs(targetCol-preCol)==1 && targetRow==preRow+moveValue && hittingP!=null && hittingP.color!=color )
            {
                return true;
            }
            //EN Passant//
            if(Math.abs(targetCol-preCol)==1 && targetRow==preRow+moveValue)
            {
                for(piece Piece:game_panel.simPieces)
                {
                    if(Piece.col==targetCol && Piece.row==preRow && Piece.twoStepped==true)
                    {
                        hittingP=Piece;
                        return true;
                    }
                }
            }
        }

        return false;
    }
}