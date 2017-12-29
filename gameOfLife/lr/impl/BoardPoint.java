package gameOfLife.lr.impl;

public class BoardPoint {
  int x;
  int y;
 public BoardPoint()
 {
	x=0; y=0; 
 }
 public BoardPoint(int a, int b)
 {
	x=a; y=b; 
 }
 static public boolean equal(BoardPoint a, BoardPoint b )
 {
	 if (a== null && b== null) return true;
	 if (a== null && b!= null) return false;
	 if( (a.x==b.x) && (a.y== b.y)) return true;
	 else return false;
 }
}
