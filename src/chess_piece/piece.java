package chess_piece;

import main.board;
import main.game_panel;
import main.Type;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.FileInputStream;
public class piece {

    public Type type;
    public BufferedImage image;
    public int x, y;
    public int col, row, preCol, preRow;
    public int color;
    public piece hittingP;
    public boolean moved,twoStepped;

    public piece(int color, int col, int row) {
        this.color = color;
        this.col = col;
        this.row = row;
        x = getX(col);
        y = getY(row);
        preRow=row;
        preCol=col;
        }


    public BufferedImage getImage(String imagePath) {
        BufferedImage image = null;
        try {

            image = ImageIO.read(new FileInputStream(imagePath));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public int getX(int col) {
        return col * board.SQUARE_SIZE;
    }

    public int getY(int row) {
        return row * board.SQUARE_SIZE;
    }

    public int getCol(int x) {
        return(x+board.HALF_SQUARE_SIZE)/board.SQUARE_SIZE;
    }
    public int getRow(int y) {
        return(y+board.HALF_SQUARE_SIZE)/board.SQUARE_SIZE;
    }
    public int getIndex()
    {
        for(int index=0;index<game_panel.simPieces.size();index++){
            if(game_panel.simPieces.get(index)==this)
            {
                return index;
            }
        }
        return 0;
    }
    public void updatePosition()
    {
        //to check en passant//
        if(type==Type.PAWN)
        {
            if(Math.abs(row-preRow)==2 )
            {
                twoStepped=true;
            }
        }
        x=getX(col);
        y=getY(row);
        preCol=getCol(x);
        preRow=getRow(y);
        moved=true;
    }
    public void resetPosition()
    {
        col=preCol;
        row=preRow;
        x=getX(col);
        y=getY(row);
    }
    public boolean canMove(int targetCol,int targetRow){
        return false;
    }
    public boolean isWithinBoard(int targetCol,int targetRow){
        if(targetCol>=0 && targetCol<=7 && targetRow>=0 && targetRow<=7)
        {
            return true;
        }
        return false;
    }
    public boolean isSameSquare(int targetCol,int targetRow)
    {

            if (targetCol==preCol && targetRow==preRow) {
                return true;
            }
        return false;
    }
    public piece getHittingP(int targetCol,int targetRow)
    {
        for(piece Piece: game_panel.simPieces) {
            if (Piece.col == targetCol && Piece.row == targetRow &&Piece!=this) {
                return Piece;
            }
        }
        return null;
    }
    public boolean isValidSquare(int targetCol,int targetRow)
    {
        hittingP=getHittingP(targetCol,targetRow);
        if(hittingP==null) {
        return true;
        }
        else {
            if(hittingP.color!=this.color) {
                return true;
            }
            else {
                hittingP = null;;
            }
        }
        return false;
    }
    public boolean pieceIsOnStraightLine(int targetCol, int targetRow) {
        // Moving left
        for (int c = preCol - 1; c > targetCol; c--) {
            for (piece Piece : game_panel.simPieces) {
                if (Piece.col == c && Piece.row == targetRow) {
                    hittingP = Piece;
                    return true;
                }
            }
        }
        // Moving right
        for (int c = preCol + 1; c < targetCol; c++) {
            for (piece Piece : game_panel.simPieces) {
                if (Piece.col == c && Piece.row == targetRow) {
                    hittingP = Piece;
                    return true;
                }
            }
        }
        // Moving up
        for (int r = preRow - 1; r > targetRow; r--) {
            for (piece Piece : game_panel.simPieces) {
                if (Piece.col == targetCol && Piece.row == r) {
                    hittingP = Piece;
                    return true;
                }
            }
        }
        // Moving down
        for (int r = preRow + 1; r < targetRow; r++) {
            for (piece Piece : game_panel.simPieces) {
                if (Piece.col == targetCol && Piece.row == r) {
                    hittingP = Piece;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean pieceIsOnDiagonalLine(int targetCol,int targetRow)
    {

        if( targetRow<preRow)
        {//UP LEFT//
            for(int c=preCol-1;c>targetCol;c--)
            {
                int diff=Math.abs(c-preCol);
                for(piece Piece  :game_panel.simPieces)
                {
                    if(Piece.col==c && Piece.row==preRow-diff)
                    {
                        hittingP=Piece;
                    }
                }
            }

        }
        //UP RIGHT//
            for(int c=preCol+1;c<targetCol;c++)
            {
                int diff=Math.abs(c-preCol);
                for(piece Piece  :game_panel.simPieces)
                {
                    if(Piece.col==c && Piece.row==preRow-diff)
                    {
                        hittingP=Piece;
                    }
                }
            }


        if(targetRow>preRow) {//DownLeft//
            for (int c = preCol - 1; c > targetCol; c--) {
                int diff = Math.abs(c - preCol);
                for (piece Piece : game_panel.simPieces) {
                    if (Piece.col == c && Piece.row == preRow - diff) {
                        hittingP = Piece;
                    }
                }
            }


            for (int c = preCol+1; c < targetCol; c++) {
                int diff = Math.abs(c - preCol);
                for (piece Piece : game_panel.simPieces) {
                    if (Piece.col == c && Piece.row == preRow - diff) {
                        hittingP = Piece;
                    }
                }
            }

        }
        return false;
    }
    public void draw(Graphics2D g2) {
    g2.drawImage(image,x,y,board.SQUARE_SIZE,board.SQUARE_SIZE,null);
    }
}
