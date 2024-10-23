package main;

import chess_piece.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class game_panel extends JPanel implements Runnable {
    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;
    final int FPS = 60;
    Thread gameThread;
    board Board = new board();
    Mouse mouse = new Mouse();
    // pieces
    public static ArrayList<piece> pieces = new ArrayList<>();
    public static ArrayList<piece> simPieces = new ArrayList<>();
    ArrayList<piece>promoPieces=new ArrayList<>();
    piece activeP,checkingP;
    public static piece castlingP;
    // Color
    public static final int WHITE = 0;
    public static final int BLACK = 1;
    int currentColor = WHITE;
    //Boolean
    boolean canMove;
    boolean validSquare;
    boolean promotion;
    boolean gameover;

    public game_panel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
        setPieces();
        copyPieces(pieces, simPieces);
    }

    public void LaunchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setPieces() {
        pieces.add(new pawn(WHITE, 0, 6));
        pieces.add(new pawn(WHITE, 1, 6));
        pieces.add(new pawn(WHITE, 2, 6));
        pieces.add(new pawn(WHITE, 3, 6));
        pieces.add(new pawn(WHITE, 4, 6));
        pieces.add(new pawn(WHITE, 5, 6));
        pieces.add(new pawn(WHITE, 6, 6));
        pieces.add(new pawn(WHITE, 7, 6));
        pieces.add(new Rook(WHITE, 0, 7));
        pieces.add(new Rook(WHITE, 7, 7));
        pieces.add(new Knight(WHITE, 1, 7));
        pieces.add(new Knight(WHITE, 6, 7));
        pieces.add(new Bishop(WHITE, 2, 7));
        pieces.add(new Bishop(WHITE, 5, 7));
        pieces.add(new Queen(WHITE, 3, 7));
        pieces.add(new King(WHITE, 4, 7));
        // BLACK
        pieces.add(new pawn(BLACK, 0, 1));
        pieces.add(new pawn(BLACK, 1, 1));
        pieces.add(new pawn(BLACK, 2, 1));
        pieces.add(new pawn(BLACK, 3, 1));
        pieces.add(new pawn(BLACK, 4, 1));
        pieces.add(new pawn(BLACK, 5, 1));
        pieces.add(new pawn(BLACK, 6, 1));
        pieces.add(new pawn(BLACK, 7, 1));
        pieces.add(new Rook(BLACK, 0, 0));
        pieces.add(new Rook(BLACK, 7, 0));
        pieces.add(new Knight(BLACK, 1, 0));
        pieces.add(new Knight(BLACK, 6, 0));
        pieces.add(new Bishop(BLACK, 2, 0));
        pieces.add(new Bishop(BLACK, 5, 0));
        pieces.add(new Queen(BLACK, 3, 0));
        pieces.add(new King(BLACK, 4, 0));
    }

    private void copyPieces(ArrayList<piece> source, ArrayList<piece> target) {
        target.clear();
        for (int i = 0; i < source.size(); i++) {
            target.add(source.get(i));
        }
    }

    @Override
    public void run() {
        // game loop
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
        if (promotion) {
            promoting();
        } else if (gameover==false)
            {
            // Mouse Button Pressed
            if (mouse.pressed) {
                if (activeP == null) {
                    // If active piece is null, check if you can pick up a piece
                    for (piece Piece : simPieces) {
                        // If the mouse is on an ally piece, pick it up as the active piece
                        if (Piece.color == currentColor &&
                                Piece.col == mouse.x / board.SQUARE_SIZE &&
                                Piece.row == mouse.y / board.SQUARE_SIZE) {
                            activeP = Piece;
                        }
                    }
                } else {
                    // If the player is holding a piece, simulate the move
                    simulate();
                }
            }
            //mouse buttonn released//
            if (mouse.pressed == false) {
                if (activeP != null) {
                    if (validSquare) {
                        //move confirmed

                        //update the piece list in case a piece has captured and removed during the simulation
                        copyPieces(simPieces, pieces);
                        activeP.updatePosition();
                        if (castlingP != null) {
                            castlingP.updatePosition();
                        }
                        if (isKingInCheck() && isCheckmate()) {
                            gameover = true;

                        } else {
                            if (canPromote()) {
                                promotion = true;
                            } else {
                                changePlayer();
                            }
                        }
                    }

                    else {
                        //the move is not valid so reset everything
                        copyPieces(pieces, simPieces);
                        activeP.resetPosition();
                        activeP = null;
                    }
                }
            }
        }

    }
    private void simulate() {
        canMove=false;
        validSquare=false;
        //reeset the ppiece list in every loop
        //this is basically for restoring the rrermoced piece during the simulation
        copyPieces(pieces, simPieces);
        //reset the castling piece position
        if(castlingP!=null)
        {
            castlingP.col=castlingP.preCol;
            castlingP.x=castlingP.getX(castlingP.col);
            castlingP=null;
        }

        //if a piece is being held update its position
        activeP.x = mouse.x - board.HALF_SQUARE_SIZE;
        activeP.y = mouse.y - board.HALF_SQUARE_SIZE;
        activeP.col = activeP.getCol(activeP.x);
        activeP.row = activeP.getRow(activeP.y);
        //check if the piece is hovering over a reachable square
        if(activeP.canMove(activeP.col,activeP.row))
        {
            canMove=true;
            if(activeP.hittingP!=null)
            {
                simPieces.remove(activeP.hittingP.getIndex());
            }
            checkCastling();
            if(isIllegal(activeP)==false && opponentCanCaptureKing()==false)
            {
                validSquare=true;
            }

        }
    }
    private boolean isIllegal(piece king)
    {
        if(king.type==Type.KING)
        {
            for(piece  Piece:simPieces)
            {
               if(Piece!=king && Piece.color !=king.color && Piece.canMove(king.col,king.row))
                   return true;
            }
        }
        return false;
    }
    private boolean opponentCanCaptureKing()
    {
        piece king=getKing(false);
        for(piece  Piece:simPieces)
        {
            if(Piece.color!=king.color && Piece.canMove(king.col,king.row))
            {
                return true;
        }}
        return false;
    }
    private boolean isKingInCheck() {
        piece king = getKing(true); // Get the current player's king

        if(activeP.canMove(king.col,king.row))
        {
            checkingP=activeP;
            return true;
        }
        else
        {
            checkingP=null;
        }



        return false;
    }



    private piece getKing(boolean opponent) {
        piece king = null; // Initialize king as null
        for (piece p : simPieces) {
            if (opponent) {
                if (p.type == Type.KING && p.color != currentColor) {
                    king = p;
                    break; // Exit loop once king is found
                }
            }
            else{
                if(p.type==Type.KING&&p.color==currentColor)
                {
                    king=p;                }
            }
        }
        return king;
    }

    private boolean isCheckmate()
    {
        piece king=getKing(true);
        if(kingCanMove(king))
        {
            return false;
        }
        else{
            //but u still got a chance
            //check if u can block the attack with ur piece
            //check the position of the checking oiece and the king in check
            int colDiff=Math.abs(checkingP.col-king.col);
            int rowDiff=Math.abs(checkingP.row-king.row);
            if(colDiff==0)
            {
                //the checking is piece is attacking vertically
                if(checkingP.row<king.row)
                {
                    for(int row=checkingP.row;row<king.row;row++)
                    {
                        for(piece p:simPieces)
                        {
                            if(p!=king &&p.color!=currentColor && p.canMove(checkingP.col,row) )
                            {
                                return false;
                            }
                        }
                    }
                }
                if(checkingP.row>king.row)
                {
                    for(int row=checkingP.row;row>king.row;row--)
                    {
                        for(piece p:simPieces)
                        {
                            if(p!=king &&p.color!=currentColor && p.canMove(checkingP.col,row) )
                            {
                                return false;
                            }
                        }
                    }
                }
            }
            else if(rowDiff==0)
            {
                //the checking is piece is attacking horizantally
                if(checkingP.col<king.col)
                {
                    for(int col=checkingP.col;col<king.col;col++)
                    {
                        for(piece p:simPieces)
                        {
                            if(p!=king &&p.color!=currentColor && p.canMove(col,checkingP.row) )
                            {
                                return false;
                            }
                        }
                    }
                }
                //the checking is piece is attacking horizantally
                if(checkingP.col>king.col)
                {
                    for(int col=checkingP.col;col>king.col;col--)
                    {
                        for(piece p:simPieces)
                        {
                            if(p!=king &&p.color!=currentColor && p.canMove(col,checkingP.row) )
                            {
                                return false;
                            }
                        }
                    }
                }

            }
            if(colDiff==rowDiff)
            {
                //the chiecking is piece is attacking diagonally
                if(checkingP.row<king.row)
                {


                    if (checkingP.col <king.col)
                    {

                            for (int col = checkingP.col,row=checkingP.row ; col< king.col ; col++, row++) {
                                for (piece p : simPieces) {
                                    if (p != king && p.color != currentColor && p.canMove(col, row)) {
                                        return false;
                                    }
                                }
                            }


                    }
                    if(checkingP.col>king.col)
                    {
                        for (int col = checkingP.col,row=checkingP.row ; col> king.col ; col--, row++) {
                            for (piece p : simPieces) {
                                if (p != king && p.color != currentColor && p.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }

                    }
                }
                if(checkingP.row>king.row)
                {
                    if(checkingP.col<king.col)
                    {
                        for (int col = checkingP.col,row=checkingP.row ; col< king.col ; col++, row--) {
                            for (piece p : simPieces) {
                                if (p != king && p.color != currentColor && p.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }

                    }
                    if(checkingP.col>king.col)
                    {
                        for (int col = checkingP.col,row=checkingP.row ; col> king.col ; col--, row--) {
                            for (piece p : simPieces) {
                                if (p != king && p.color != currentColor && p.canMove(col, row)) {
                                    return false;
                                }
                            }
                        }

                    }

                    }
                }
            else
            {
                //the checking piece is knight
            }
        }
        return true;
    }
    public boolean kingCanMove(piece king)
    {
        //Simulate if there is any sqaure where the king can move to
        if(isValidMove(king,-1,-1)){return true;}
        if(isValidMove(king,0,-1)){return true;}
        if(isValidMove(king,1,-1)){return true;}
        if(isValidMove(king,-1,0)){return true;}
        if(isValidMove(king,1,0)){return true;}
        if(isValidMove(king,-1,1)){return true;}
        if(isValidMove(king,0,1)){return true;}
        if(isValidMove(king,1,1)){return true;}
        return false;
    }
    private boolean isValidMove(piece king, int colPlus, int rowPlus) {
        boolean isValidMove = false;


        // Update the king's position temporarily
        king.col += colPlus;
        king.row += rowPlus;

        if (king.canMove(king.col, king.row)) {



            if (king.hittingP!= null) {
                simPieces.remove(king.hittingP.getIndex());
            }

            if (isIllegal(king)==false) {
                isValidMove = true;
            }
        }
        king.resetPosition();
        copyPieces(pieces,simPieces);

        return isValidMove;
    }

    private void checkCastling()
    {
        if(castlingP!=null)
        {
            if(castlingP.col==0)
            {
            castlingP.col=3;
            }
            else if (castlingP.col==7)
            {
                castlingP.col -=2;
            }
            castlingP.x= castlingP.getX(castlingP.col);
        }
    }
private void changePlayer()
{
    if(currentColor==WHITE)
    {
        currentColor=BLACK;
        //reset black two step status//
        for(piece  Piece:pieces)
        {
            if(Piece.color==BLACK)
            {
                Piece.twoStepped=false;
            }
        }
    }
    else{
        currentColor=WHITE;
        //reset whites two stepped status
        for(piece  Piece:pieces)
        {
            if(Piece.color==WHITE)
            {
                Piece.twoStepped=false;
            }
        }
    }
    activeP=null;
}
private boolean canPromote()
{
    if(activeP.type==Type.PAWN)
    {
        if(currentColor==WHITE && activeP.row==0|| currentColor==BLACK && activeP.row==7)
        {
            promoPieces.clear();
            promoPieces.add(new Rook(currentColor,9,2));
            promoPieces.add(new Knight(currentColor,9,3));
            promoPieces.add(new Bishop(currentColor,9,4));
            promoPieces.add(new Queen(currentColor,9,5));
            return true;
        }
    }
    return false;
}
private void promoting() {
        if(mouse.pressed) {
            for (piece p : promoPieces)
            {
                if(p.col==mouse.x/board.SQUARE_SIZE&&p.row==mouse.y/board.SQUARE_SIZE) {
                    switch (p.type) {
                        case ROOK:
                            simPieces.add(new Rook(currentColor, activeP.col, activeP.row));
                            break;
                        case KNIGHT:
                            simPieces.add(new Knight(currentColor, activeP.col, activeP.row));
                            break;
                        case BISHOP:
                            simPieces.add(new Bishop(currentColor, activeP.col, activeP.row));
                            break;
                        case QUEEN:
                            simPieces.add(new Queen(currentColor, activeP.col, activeP.row));
                            break;
                        default:
                            break;
                    }
                    simPieces.remove(activeP.getIndex());
                    copyPieces(simPieces,pieces);
                    activeP=null;
                    promotion=false;
                    changePlayer();
                }
            }
        }
}
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Board.draw(g2);

        for (piece p : new ArrayList<>(simPieces)) {
            if (p != null) {
                p.draw(g2);
            }
        }

        if (activeP != null && canMove) {
            if (isIllegal(activeP) || opponentCanCaptureKing()) {
                g2.setColor(Color.red);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                g2.fillRect(activeP.col * board.SQUARE_SIZE, activeP.row * board.SQUARE_SIZE, board.SQUARE_SIZE, board.SQUARE_SIZE);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            } else {
                g2.setColor(Color.white);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                g2.fillRect(activeP.col * board.SQUARE_SIZE, activeP.row * board.SQUARE_SIZE, board.SQUARE_SIZE, board.SQUARE_SIZE);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
            activeP.draw(g2);
        }

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Book Antiqua", Font.BOLD, 30));
        g2.setColor(Color.white);

        if (promotion) {
            g2.drawString("promote to", 840, 150);
            for (piece p : promoPieces) {
                g2.drawImage(p.image, p.getX(p.col), p.getY(p.row), board.SQUARE_SIZE, board.SQUARE_SIZE, null);
            }
        } else {
            if (currentColor == WHITE) {
                g2.drawString("SETE KO PALO", 840, 550);
                if (checkingP != null && checkingP.color == BLACK) {
                    g2.setColor(Color.red);
                    g2.drawString("The King", 840, 650);
                    g2.drawString("is in check!", 840, 700);
                }
            } else {
                g2.drawString("KALE KO PALO", 840, 250);
                if (checkingP != null && checkingP.color == WHITE) {
                    g2.setColor(Color.red);
                    g2.drawString("The King", 840, 100);
                    g2.drawString("is in check!", 840, 150);
                }
            }
        }

        if (gameover) {
            String s = currentColor == WHITE ? "White Wins" : "Black Wins";
            g2.setColor(Color.green);
            g2.setFont(new Font("Arial", Font.BOLD, 90));
            g2.drawString(s, 200, 420);
        }
    }

}
