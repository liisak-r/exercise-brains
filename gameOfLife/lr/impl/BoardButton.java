package gameOfLife.lr.impl;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class BoardButton extends RadioButton {
	Boolean alive;
	int row, column;
	BoardButton(int i, int j)
	{
		super();
		row=i;
		column=j;
		alive=false;
		Circle shape=new Circle();	
    	shape.setRadius(10.0);
		shape.setStrokeWidth(2.0);
		shape.setStroke(Paint.valueOf("BLACK"));
    	shape.setFill(Paint.valueOf("WHITE"));
    	setShape(shape);
    	this.setOnAction( new EventHandler<ActionEvent>() {	 
     	            @Override
     	            public void handle(ActionEvent event) {
     	            	toggleLiveness();
     	            }
    	});
	
	}

	public void setAlive(Boolean liveness)
	{
		alive=liveness;
		Circle shape=(Circle) getShape();
		if (liveness)
			shape.setFill(Paint.valueOf("RED"));
		else 
			shape.setFill(Paint.valueOf("WHITE"));	
	}
	
	public void toggleLiveness()
	{
		alive=!alive;
		Circle shape=(Circle) getShape();
		if (alive)
			shape.setFill(Paint.valueOf("RED"));
		else 
			shape.setFill(Paint.valueOf("WHITE"));	
	    super.setSelected(alive.booleanValue());
	}
	
	public Boolean getLiveness()
	{
		return alive;
	}
	
	public int getRow()
	{
		return row;
	}
	
	public int getColumn()
	{
		return column;
	}
}
