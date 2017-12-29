package gameOfLife.lr.impl;

import java.util.Vector;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.util.ArrayList;
import java.util.BitSet;
/*
 * Tic describes the situation at one tic of the game
 * 1. Any live cell with fewer than two live neighbours dies, as if caused by underpopulation.
   2. Any live cell with more than three live neighbours dies, as if by overcrowding.
   3. Any live cell with two or three live neighbours lives on to the next generation.
   4. Any dead cell with exactly three live neighbours becomes a live cell.
 */
public class Tic implements Observable {

	public class EndOfPopulation extends Exception {
		public String getMessage() { return "Population empty" ;}
	}
	
	private Vector<BitSet> board;
	private int boardlength;
	SimpleIntegerProperty sizeproperty;
	ObjectProperty<Vector<BitSet> > boardproperty;
	Boolean dead=true;
    ArrayList< InvalidationListener > listeners;
    
    {
    	 listeners= new  ArrayList< InvalidationListener >();
    	  	  
    }
	
	public 	Tic(int length) { 
		boardlength=length;
	    sizeproperty= new SimpleIntegerProperty();
   	    sizeproperty.setValue( boardlength);
		board= new Vector<BitSet>(length) ;	
		for (int i=0; i <length; i++)
		{
			board.add(i, new BitSet(length));
		}
	 	boardproperty= new SimpleObjectProperty<Vector<BitSet>>(this, "boardproperty", board);
   	    boardproperty.setValue( board);
		
	}
	public 	Tic(int length, boolean rowsInserted) { 
		boardlength=length;
	    sizeproperty= new SimpleIntegerProperty();
   	    sizeproperty.setValue( boardlength);
		board= new Vector<BitSet>(length) ;	
		for (int i=0; i <length && rowsInserted; i++)
		{
			board.add(i, new BitSet(length));
		}
	 	boardproperty= new SimpleObjectProperty<Vector<BitSet>>(this, "boardproperty", board);
   	    boardproperty.setValue( board);
		
	}
	/*
	 * alivecells are given in an array of arrays of two: a[i, 0] gives x and a[i,1] gives y
	 * of point where alive cell is in the initial setting
	 */
	public void init (BoardPoint [] alivecells)
	{
		int i=0;
		for (i=0; i < alivecells.length; i++)
		{
		 if (alivecells[i]!= null)
		 board.get(alivecells[i].x).set(alivecells[i].y);
		 dead=false;
		}
		System.out.println("initialization of board set");
	}
	/* compute the row for alive or dead with the following rules:
	 * 
Any live cell with fewer than two live neighbours dies, as if caused by underpopulation.
Any live cell with two or three live neighbours lives on to the next generation.
Any live cell with more than three live neighbours dies, as if by overpopulation.
Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
	 */
	public BitSet computeRow(BitSet prevRow, BitSet focusRow, BitSet nextRow)
	{
		BitSet result=new BitSet(boardlength);
		int precol=0;
		int focuscol=0;
		int nextcol=0;
		int neighbours=0;
		int i=0;
		result.clear(); /* by default all bits are false*/
		if (focusRow == null) return result;
		if (boardlength ==0) return result;
		if (prevRow != null) 
		{
			focuscol= prevRow.get(0)?1:0;
		}
		if (nextRow != null) 
		{
			focuscol= focuscol+ (nextRow.get(0)?1:0);
		}
		
		for ( i=0; i <boardlength; i++)
		{
			
			
			if (i< (boardlength -1)) {
				if (prevRow != null) 
				{
					nextcol=prevRow.get(i+1)?1:0;	
				}
				else nextcol=0;
				if (nextRow != null) 
				{
					nextcol=nextcol+(nextRow.get(i+1)?1:0);
				}
				nextcol=nextcol+(focusRow.get(i+1)?1:0);
			}
			neighbours=precol+nextcol+focuscol;
			if (focusRow.get(i)) /*alive cell */
			{
				if (neighbours >= 2 && neighbours <= 3)
				result.set(i);
			}
			else {
				if (neighbours == 3)
					result.set(i);
			}
			
			if  (i< (boardlength -1 )) {
				precol=focuscol+ (focusRow.get(i)?1:0);
				focuscol=nextcol-(focusRow.get(i+1)?1:0);
			}
		}
		return result;
	}
	
	public Tic computeNewTic() throws EndOfPopulation
	{
		int i=0;
		Tic newTic=new Tic(this.boardlength, false);
		BitSet prev, focus, next;
		prev=null; focus=null; next=null;
		if (boardlength == 0) return newTic;
		focus= this.board.get(0);
		for (i=0; i <this.boardlength; i++ )
		{
			if (i <this.boardlength-1 ) next=this.board.get(i+1);
			else next=null;
			
			newTic.board.add(i,computeRow(prev, focus, next));
			if (newTic.dead)
				newTic.dead=newTic.board.get(i).cardinality()>0? false:true;
			prev=focus; focus=next; 
	
		}
		
		
		this.board=newTic.board;
		this.dead=newTic.dead;
		System.out.println("uusi elämäjakso laskettu");
		if (newTic.dead) throw new EndOfPopulation();
		return newTic;
	}
	
	Vector<BitSet> getBoard()
	{
		return board;
	}
	
	void setBoard(Vector<BitSet> b)
	{
		System.out.println("boardia yritetaan asettaaa..");;
	}
	
	ObjectProperty<Vector<BitSet>> getBoardProperty()
	{
		return boardproperty;
	}
	
	void setBoardProperty(ObjectProperty<Vector<BitSet>> b)
	{
		System.out.println("board property on asetettu uudelleen");
		boardproperty=b;
	}
	
	public Boolean isPopulationDead()
	{
		return dead;
	}
	
	public boolean alive(int i, int j)
	{
		return board.get(i).get(j);
	}
	@Override
	public void addListener(InvalidationListener listener) {
		// TODO Auto-generated method stub
		listeners.add(listener);
	}
	@Override
	public void removeListener(InvalidationListener listener) {
		// TODO Auto-generated method stub
		listeners.remove(listener);
	}

	
}
