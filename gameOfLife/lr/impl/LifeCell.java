package gameOfLife.lr.impl;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Paint;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;


public class LifeCell extends Circle {
	int rowIndex;
	int  columnIndex;
/*	SimpleObjectProperty<Paint> fillproperty;*/
    
    public LifeCell(int i, int j,Boolean alive)
    {
    	super();
    	rowIndex= i;
    	columnIndex=j;
    	
    	this.setRadius(10.0);
    	this.setStrokeWidth(2.0);
    	this.setStroke(Paint.valueOf("BLACK"));
    	if (alive)
    		this.setFill(Paint.valueOf("RED"));	
    	else
    	this.setFill(Paint.valueOf("WHITE"));
   /* 	fillproperty= new SimpleObjectProperty<Paint>(this, "fillproperty");
    	fillproperty.set(this.getFill());
    	this.fillproperty.setValue(this.getFill());*/
    }
    
 public void setFillColor(Paint a)
 {
	 this.setFill(a);
 }
    
 public Paint getFillColor()
 {
	return  this.getFill();
 }
 
  public void  set(Boolean alive)
  {
	  if (alive) {
		  {
		  System.out.println("node at row "+rowIndex + " column "+columnIndex + "eläväksi");
		  this.setFill(Paint.valueOf("RED"));
		  }
	  }
	  else {
		  this.setFill(Paint.valueOf("WHITE"));  
	  }
/*	  this.fillproperty.setValue(this.getFill()); */
  }
  public Boolean get()
  {
	  return (this.getFill().equals(Paint.valueOf("RED")));
  }
    
  public int row()
  {
	  return rowIndex;  
  }
  public int column()
  {
	  return columnIndex;
  }
  /*
  public ObjectProperty<Paint> getFillProperty()
  {
	  return fillproperty;
  }
  public void setFillProperty(SimpleObjectProperty<Paint> newproperty )
  {
	 System.out.println("property is readonly: ");
  }
  */
}
