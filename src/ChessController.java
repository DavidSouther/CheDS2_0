/*
 * ChessController.java
 *
 * Created on November 1, 2005, 11:28 AM
 */

 

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;


/**
 * Main controller object for the Chess Game. 
 * @author David Souther
 */
public class ChessController {
	private JFrame			owner;
	private Chess			chess;
	private ChessPosition           currentPosition,
                                        lastPosition;
	private ChessHistory            history;
	private ChessDesktop            desktop;
	private ChessMenuBar            menu;
	
	private JFileChooser	fileChooser;
	
	public  boolean playing;
	private boolean TESTBOARD = 
               /* false;//*/ true; //for Debugging
	
//	private ChessDifficultyDialog	cdd;
//	private ChessLoadSaveSialog		clsd;
	
	/** Creates a new instance of ChessController */

	public ChessController(JFrame o) {
            owner = o;
            chess = new Chess();
            desktop = new ChessDesktop(this);
            menu = new ChessMenuBar(this);

            lastPosition = new ChessPosition();
            currentPosition = new ChessPosition();
            history = new ChessHistory();

            owner.setJMenuBar(menu.getMenuBar());
            owner.getContentPane().add(desktop.getDesktopPane());
            desktop.newGame();
	}
	
	/* 
	 * Main control structure.
	 * Currently called by desktop's ChessMovementInternalFrame actionListener.
	 * 
	 */
	
	public void makeMove(ChessMove m){
            if(!playing){
                playing = true;
                if(chess.isValidMove(currentPosition, m)){	
                    for(int i=0; i<120; i++)lastPosition.board[i] = currentPosition.board[i];
                    if(currentPosition.board[m.from] == ChessPosition.PAWN && (m.to >= 92 && m.to <= 99)) promote(m); //Check for promotion
                    currentPosition = (ChessPosition) chess.makeMove(lastPosition, chess.HUMAN, m);	              //make the move
                    addCapturedPiece(history.capturedPiece(lastPosition, currentPosition));                           //update captures
                    String ch = chess.check(currentPosition, chess.PROGRAM)? "Check" : "";
                    desktop.setStatus(m.toString() + ch); updateBoard(); updateHistory(m);								//updateGUI
                    if(chess.wonPosition(currentPosition, chess.HUMAN)) desktop.humanWon();							//check win
                    if(chess.drawnPosition(currentPosition))desktop.staleMate();									//and Draw
                    
                    //do it all again, for computer
                    //but....
                    //pop into the other thread:
                    computerPlayGame();
                } else {
                        desktop.setStatus(ChessMainFrame.getString("invalidMove") + m);
                }
                playing = false;
            }		
	}
	
	public void forceMove(ChessMove m){
            if(!playing){
                playing = true;
                currentPosition = (ChessPosition) chess.makeMove(currentPosition, chess.HUMAN, m);				//make the move
                chess.setPosition((Position) currentPosition);													//set the chess to this move
                desktop.setStatus(m.toString()); updateBoard(); updateHistory(m);								//updateGUI
                playing = false;
            }
	}
	
	public void forcePosition(ChessPosition p){
            if(!playing){
                playing = true;
                currentPosition = p;
                updateBoard();
                playing = false;
            }
	}
	
	public boolean isValidMove(ChessMove m){
            return chess.isValidMove(currentPosition, m);
	}
		
	/*
	 * public functions called by the menuBar action listener.
	 */
	 
	public void newGame(){
            flush();
            currentPosition = new ChessPosition();
            if(TESTBOARD){
                    for (int i=0; i<120; i++) currentPosition.board[i] = testBoard[i];			
            } else {
                    for (int i=0; i<120; i++) currentPosition.board[i] = Chess.initialBoard[i];
            }
            
            String[] options = {ChessMainFrame.getString("manFirst"), ChessMainFrame.getString("machineFirst")};
            String f = ChessMainFrame.getString("playFirst");
            String t = ChessMainFrame.getString("playFirstTitle");
            int n = JOptionPane.showOptionDialog(owner, f, t, JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, null,        /*don't use a custom Icon*/ options,     /*the titles of buttons*/ options[0]); /*default button title*/ 
            desktop.newGame();
            desktop.updateBoard(currentPosition);
            if(n == 1){
                computerPlayGame();
            }
	}
	
        /*
         * Save and load functions.
         * Saves currentPossition to *.chess, or loads *.chess to currentPosition.
         * *.chess is a Serialized ChessPosition
         */
	public void save(){
            if(fileChooser == null){
                createFileChooser();
            }

            if(fileChooser.showSaveDialog(owner) == JFileChooser.APPROVE_OPTION){
                try {
                    StringBuffer fileN = new StringBuffer(fileChooser.getSelectedFile().getPath());
                    String end = fileN.substring(fileN.length() - 1 - 6);
                    if(end.equals(".chess")){
                        fileN.append(".chess");
                    }
                    String fileName = fileN.toString();

                    FileOutputStream out = new FileOutputStream(fileName);
                    ObjectOutputStream s = new ObjectOutputStream(out);

                    s.writeObject(currentPosition);
                    s.writeObject(history);
                    s.flush();
                    s.close();
                    out.close();	
                } catch (Exception e){
                    //temp
                    e.printStackTrace();
                    System.out.println("Error with file I/O(write)");
                }				
            }

	}
	
	public void load(){
            if(fileChooser == null){
                createFileChooser();
            }	
            if(fileChooser.showOpenDialog(owner) == JFileChooser.APPROVE_OPTION){
                try{
                    String fileName = fileChooser.getSelectedFile().getPath();
                    FileInputStream in = new FileInputStream(fileName);
                    ObjectInputStream s = new ObjectInputStream(in);

                    currentPosition = (ChessPosition) s.readObject();
                    history         = (ChessHistory)  s.readObject();
                    
                    s.close();
                    in.close();		
                    updateBoard();
                } catch (Exception e){
                    //temp
                    System.out.println("Error with file I/O(read)");
                }
            }
	}
	
	public void editCurrent(){
            desktop.edit(currentPosition);
	}
	
	public void editNew(){
            currentPosition = new ChessPosition();
            for (int i=0; i<120; i++) currentPosition.board[i] = Chess.initialBoard[i];
            editCurrent();
	}

	public void changeDifficulty(){
            desktop.showDiffFrame(chess.getOptimalMoves());
	}
		
	public void printBoard(){
            System.out.println(currentPosition.printPretty());
	}
	
	public void printControlData(){
            System.out.println(ChessMainFrame.getString("versionID"));
            currentPosition.printPretty();
            chess.printControlData(currentPosition);
            history.printHistory();
            desktop.printCaptures();
	}
	
	/*
	 * public funcitons called by desktop (more precisly, its descendants)
	 */
	public void setDifficulty(float diff){
            desktop.clearDiffFrame();
            chess.setOptimalMoves(diff);
	}

	public JFrame getFrame(){
		return owner;
	}	
	
	public ChessHistory getHistory(){
            if(history == null) history = new ChessHistory();
            return history;
	}
	
	//Private Methods
	private void updateBoard(){
            desktop.updateBoard(currentPosition);
	}
	
	private void updateHistory(ChessMove m){
            history.addMove(m);
            desktop.updateHistory();
	}
	
	private void createFileChooser(){
            fileChooser = new JFileChooser();
            ExampleFileFilter filter = new ExampleFileFilter();
            filter.addExtension("chess");
            filter.setDescription(ChessMainFrame.getString("savedGameSaveHint"));
            fileChooser.setFileFilter(filter);		
	}
	
	private void addCapturedPiece(int i){
            if(i!=0){
                desktop.updateCapturedPieces(i);
            }
	}
        
        private void promote(ChessMove m){
            ChessPromotionDialog d = new ChessPromotionDialog(this.owner, this.desktop);
            d.setVisible(true);
            lastPosition.board[m.from] = d.getChoice();
            currentPosition.board[m.from] = ChessPosition.BLANK; 
            currentPosition.board[m.to] = d.getChoice();
//            System.out.println(d.getChoice());
//            System.out.println(lastPosition.printPretty());
//            System.out.println(currentPosition.printPretty());
        }
        
        private synchronized void computerPlayGame(){
            final SwingWorker worker = new SwingWorker() {
                ChessMove m;

                public Object construct() {
                    if(!playing) playing = true;
                    desktop.setStatus(ChessMainFrame.getString("thinking"));
                    chess.setPosition((Position) currentPosition);						//set chess to this position
                    chess.playGame(); m = (ChessMove)chess.getMove();   					//Find and get the move
                    currentPosition = (ChessPosition) chess.makeMove(currentPosition, chess.PROGRAM, m);        //make the move
                    chess.setPosition((Position) currentPosition);						//set the chess to this move
                    addCapturedPiece(history.capturedPiece(lastPosition, currentPosition));			//update captures
                    String ch = chess.check(currentPosition, chess.HUMAN)? "Check" : "";
//System.out.println(chess.check(currentPosition, chess.HUMAN));
                    updateBoard(); updateHistory(m); desktop.setStatus(m.toString() + ch);				//update GUI
                    if(chess.wonPosition(currentPosition, chess.PROGRAM))desktop.programWon();			//check Win
                    if(chess.drawnPosition(currentPosition))desktop.staleMate();				//and draw                                
                    return new Boolean(true); //return value not used by this program
                }

                //Runs on the event-dispatching thread.
                public void finished() {
                    playing = false;
                }
            };
            worker.start();             
        }
        
        private void flush(){}
	
	//Test methods
/*	private static int[] testBoard = 
{
7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
7, 7, ChessPosition.BLANK, ChessPosition.PAWN, ChessPosition.ROOK, ChessPosition.QUEEN, ChessPosition.KING, ChessPosition.ROOK, ChessPosition.PAWN, ChessPosition.BLANK, 
7, 7, ChessPosition.BLANK, ChessPosition.PAWN, ChessPosition.KNIGHT, ChessPosition.BISHOP, ChessPosition.BISHOP, ChessPosition.KNIGHT, ChessPosition.PAWN, ChessPosition.BLANK, 
7, 7, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.PAWN, ChessPosition.PAWN, ChessPosition.PAWN, ChessPosition.PAWN, ChessPosition.BLANK, ChessPosition.BLANK, 
7, 7, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK,
7, 7, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK,
7, 7, ChessPosition.BLANK, -ChessPosition.BLANK, -ChessPosition.PAWN, -ChessPosition.PAWN, -ChessPosition.PAWN, -ChessPosition.PAWN, -ChessPosition.BLANK, ChessPosition.BLANK, 
7, 7, ChessPosition.PAWN, -ChessPosition.PAWN, -ChessPosition.KNIGHT, -ChessPosition.BISHOP, -ChessPosition.BISHOP, -ChessPosition.KNIGHT, -ChessPosition.PAWN, ChessPosition.BLANK, 
7, 7, ChessPosition.BLANK, -ChessPosition.PAWN, -ChessPosition.ROOK, -ChessPosition.QUEEN, -ChessPosition.KING, -ChessPosition.ROOK, -ChessPosition.PAWN, ChessPosition.BLANK, 
7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
7, 7, 7, 7, 7, 7, 7, 7, 7, 7
        
        };
         */
        private static int[] testBoard = 
{
7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
7, 7, ChessPosition.BLANK, ChessPosition.KING, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK,
7, 7, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK,
7, 7, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK,
7, 7, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK,
7, 7, ChessPosition.BLANK, ChessPosition.ROOK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK,
7, 7, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.ROOK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, -ChessPosition.ROOK,
7, 7, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, -ChessPosition.KING,
7, 7, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK, ChessPosition.BLANK,
7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 
7, 7, 7, 7, 7, 7, 7, 7, 7, 7
        };

}
