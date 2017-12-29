
package gameOfLife.lr.impl;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Paint;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


import java.io.FileWriter;
import java.io.IOException;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.AnimationTimer;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.util.Duration;
import javafx.animation.Interpolator;

public class Game extends Application {
	Button aloita_btn ;
	TextField gridsize_text;
	int boardlength;
	Label sizetext;
	Tic gameboard;
	TilePane root;
	GridPane board_pane;
	RadioButton generoi_btn;
	RadioButton aseta_btn;
	ToggleGroup init_toggle;
	GridPane inputbuttons;
	Stage primaryStage;
	Scene scene;
	BoardPoint alive[];
	Boolean generated;
	FileWriter fileout;
	Button peli_btn;
	Game me;
	SimpleIntegerProperty alivecells;
	SequentialTransition oneroundtic;/* animation? of game*/
	
	@Override
	public void start(Stage applicationStage) throws Exception {
		primaryStage=applicationStage;
		// TODO Auto-generated method stub
		fileout= new FileWriter("GameOfLife_output.txt");
		peli_btn=new Button();
		peli_btn.setVisible(false);
		boardlength=0;
		aloita_btn = new Button();
		generoi_btn=new RadioButton();
		generoi_btn.setText("Generoi alkutilanne");
		generoi_btn.setSelected(true);
		aseta_btn=new RadioButton();
		aseta_btn.setText("Asettele alkutilanne");
		aseta_btn.setSelected(false);
		generated=true;
		init_toggle= new ToggleGroup();
		generoi_btn.setToggleGroup(init_toggle);
		aseta_btn.setToggleGroup(init_toggle);
		inputbuttons=null;
		gridsize_text= new TextField();
		gridsize_text.setMaxWidth(100.0);
		root = new TilePane();
		root.setPadding(new Insets(20,100,20,100));
		root.setPrefColumns(2);
		root.setHgap(20);
		root.setVgap(8);
		me=this;

		alivecells=new SimpleIntegerProperty(0);

		gridsize_text.setPromptText("Kokonaislukuna 1-20");
		gridsize_text.setOnMouseExited(new EventHandler<MouseEvent>() {		 
			@Override
			public void handle(MouseEvent event) {

				if (gridsize_text.getCharacters() != null && !gridsize_text.getCharacters().equals(""))
					try {
						boardlength=Integer.parseInt(gridsize_text.getCharacters().toString());
						if ((boardlength > 20) || (boardlength < 1))
							throw new NumberFormatException ("väärä arvoalue");
					}
				catch(NumberFormatException ex)
				{ 
					Alert virhetext=new Alert(Alert.AlertType.CONFIRMATION,"Pelilaudan sivun pituus oltava kokonaisluku arvoalueella 1-20");
					virhetext.show();
					primaryStage.close();
					Platform.exit();
				}
			};
		});


		sizetext=new Label("Peliruudukon sivun pituus:");
		sizetext.setLabelFor(gridsize_text);
		aseta_btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {	 
				try{
					if (gridsize_text != null && gridsize_text.isVisible() && boardlength == 0)
						boardlength=Integer.parseInt(gridsize_text.getCharacters().toString());
				}
				catch(NumberFormatException ex)
				{ 
					Alert virhetext=new Alert(Alert.AlertType.CONFIRMATION,"Pelilaudan koko on oltava kokonaisluku");
					virhetext.show();
					primaryStage.close();
					Platform.exit();
				}
				System.out.println("pelilaudan leveys: "+boardlength);
				generated=false;
				generoi_btn.setSelected(false);
				create_inputboard();

			}
		});
		aloita_btn.setText("Aloita peli!");
		aloita_btn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try{
					if (gridsize_text != null && gridsize_text.isVisible() && boardlength == 0)
						boardlength=Integer.parseInt(gridsize_text.getCharacters().toString());
				}
				catch(NumberFormatException ex)
				{ 
					Alert virhetext=new Alert(Alert.AlertType.CONFIRMATION,"Pelilaudan koko on oltava kokonaisluku");
					virhetext.show();
					primaryStage.close();
					Platform.exit();
				}
				if ( boardlength> 0)
				{
					System.out.println("peli aloitetaan!");

					System.out.println("pelilaudan leveys: "+boardlength);
					gameboard=new Tic(boardlength);

					init_gameboard(generated);
					play_game();
					try {
						if (fileout != null) {
							fileout.close();
							fileout=null;
						}
						primaryStage.close();
						Platform.exit();
					}
					catch(Exception ex_out)
					{
						ex_out.printStackTrace();
					}
				}
				else
					System.out.println("pelilaudan koko puuttuu!");
			}
		});


		root.getChildren().add(sizetext);
		root.getChildren().add(gridsize_text);	       
		root.getChildren().add(generoi_btn);
		root.getChildren().add(aseta_btn);
		root.getChildren().add(aloita_btn);
		primaryStage.setTitle("GameOfLife!");
		scene= new Scene(root,600,600);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	protected void create_inputboard()
	{
		root.getChildren().remove(sizetext);
		root.getChildren().remove(gridsize_text);;	       
		root.getChildren().remove(generoi_btn);
		root.getChildren().remove(aseta_btn);
		inputbuttons=new GridPane();

		for (int i= 0; i < boardlength; i++)
		{
			for (int j=0; j < boardlength; j++)
			{                
				inputbuttons.add(new BoardButton(i,j),j,i);
			}
		}
		root.getChildren().add(inputbuttons);

		primaryStage.show();
	}
	protected void init_gameboard(Boolean randomset)
	{

		int pointsgenerated=(boardlength*boardlength)  ;
		int alivepoints=0;
		ArrayList< BoardPoint> points= new ArrayList< BoardPoint>();
		BoardPoint point = new BoardPoint();
		if (randomset)
		{
			Random pos= new Random();
			boolean found=false;
			pos.setSeed(Instant.now().getEpochSecond());
			for (int i=0; i < pointsgenerated; i++)
			{   
				point.x=pos.nextInt(pointsgenerated) % boardlength;
				point.y=pos.nextInt(pointsgenerated) % boardlength;
				found=false;
				for(Iterator<BoardPoint> item=points.iterator(); item.hasNext() && !found;  )
				{found=BoardPoint.equal(item.next(), point);	}
				if (!found) {
					points.add(new BoardPoint(point.x, point.y));
					alivepoints++;
				}
			}

		}
		else
		{
			int index=0; 

			for (Node c: inputbuttons.getChildren())
			{
				final BoardButton b= (BoardButton) c;
				if (b.getLiveness())
				{
					points.add(new BoardPoint (b.getRow(), b.getColumn()));
				}
			}

		}
		System.out.println("eläviä soluja löydettiin: "+points.size());
		alive= new  BoardPoint[points.size()];
		alive= points.toArray(alive);
		System.out.println("eläviä soluja taulukko: "+alive.length);
		gameboard.init(alive); 
		alivecells.set(points.size());
	}

	Scene createNewScene()
	{

		int scenewidth=boardlength*20+40;
		board_pane=new GridPane();
		createViewBoard();
		board_pane.setBorder(new Border(new BorderStroke(Paint.valueOf("BLACK"),BorderStrokeStyle.SOLID,new CornerRadii(0.50) ,BorderStroke.THICK)));
		Group parentnode= new Group();
		parentnode.getChildren().add(board_pane);

		board_pane.setAlignment(Pos.CENTER);
		// scene = new Scene(board_pane,scenewidth,scenewidth);
		scene = new Scene(parentnode, scenewidth,scenewidth);
		return scene;
	}

	protected void createViewBoard()
	{
		int i,j;
		int alivepoints=0;
		for (i=0; i < boardlength; i++)
		{
			for (j=0; j < boardlength; j++)
			{
				board_pane.add(new LifeCell(i,j,gameboard.alive(i, j)),j,i);/*	add(Node child, int columnIndex, int rowIndex)*/
				if (gameboard.alive(i, j)) {
					alivepoints++;
					try {
						System.out.println("elävä solu paikassa: "+i+ ","+j); // to console window in Eclipse 
						fileout.write("elävä solu paikassa: "+i+ ","+j+" \n\r");
					}
					catch(Exception ioex)
					{
						ioex.printStackTrace();
					}
				}
			}
		}
		alivecells.set(alivepoints);
	}

	protected void renderTicToBoard()
	{
		int number=0;	
		for (Iterator<Node> cell=board_pane.getChildren().iterator();cell.hasNext(); )
		{
			LifeCell c=(LifeCell) (cell.next());
			c.set(gameboard.alive(c.row(), c.column()));
			if (gameboard.alive(c.row(), c.column())) {
				number++;
				try {
					System.out.println("elävä solu paikassa: "+c.row()+ ","+c.column()); // to console window in Eclipse 
					fileout.write("elävä solu paikassa: "+c.row()+ ","+c.column()+" \n\r");
				}
				catch(Exception ioex)
				{
					ioex.printStackTrace();
				}
			}
			c.fireEvent(new ActionEvent());
		}
		alivecells.set(number);;
	}
	
	void set_tic() 
	{
		try {
			if (me.alivecells.getValue() > 0) {
				gameboard=gameboard.computeNewTic();
				if (me.fileout != null) {
					me.fileout.write("\n\r----------------------------\n\r");
					me.fileout.write("\n\r  uusi elämäjakso laskettu \n\r");
					me.fileout.write("\n\r----------------------------\n\r");
				}
				//	 renderTicToBoard();
			}
			else 
				if (me.fileout != null) {
					me.fileout.write("\n\r----------------------------\n\r");
					me.fileout.write("\n\r populaatio tyhja \n\r");
					me.fileout.write("\n\r----------------------------\n\r");
				}
				else System.out.println("set_tic: tyhja populaatio");
		}

		catch (Tic.EndOfPopulation populException) {	

			Alert endtext=new Alert(Alert.AlertType.CONFIRMATION,"PELI LOPPUI- POPULAATIO KUOLI.");
			endtext.setTitle("GAMEOFLIFE");
			endtext.show();
			try { /* jotta alert ehtisi näkyä ennen sovelluksen sulkemista */
				me.fileout.write("\r\n---- POPULAATIO TYHJÄ---\r\n");  
				System.out.println("populaatio tyhjä");
				Thread.sleep(5000);
			}
			catch(InterruptedException inEx)
			{
				System.out.println("interrupted at middle of at  Population empty");
			}
			catch(IOException inEx)
			{
				System.out.println("tiedoston kirjoitus epäonnistui:at Population empty");
			}

			me.stop();
			Platform.exit();
		}  
		catch(Exception ioex)
		{
			ioex.printStackTrace();
		}
	}
	
	protected void  play_game()
	{

		primaryStage.setScene( createNewScene());

		scene.getWindow().centerOnScreen();
		primaryStage.show();
		Duration duration= new Duration(8000);
	
		Transition  [] cellTransitions= new Transition[boardlength*boardlength];
		for (Iterator<Node> cell=board_pane.getChildren().iterator();cell.hasNext(); )
		{
			final LifeCell c=(LifeCell) (cell.next());
			Transition  celltransition=new Transition() {
				LifeCell target;
				{setCycleDuration(Duration.millis(8000));	 target=c;
				}
				@Override
				protected void interpolate(double frac) {    
					if (gameboard.alive( target.row(),  target.column()) != target.get()) {
						target.set(gameboard.alive( target.row(),  target.column()));
						if (gameboard.alive( target.row(),  target.column())) {
							try {
							System.out.println("frac:" + frac +" elävä solu paikassa: "+ target.row()+ ","+ target.column()); // to console window in Eclipse 
							me.fileout.write("elävä solu paikassa: "+ target.row()+ ","+ target.column()+" \n\r");
							}
							catch(Exception ioex)
							{
							ioex.printStackTrace();
							}
						}
						else { 
							try {
								me.fileout.write("solu kuoli: "+ target.row()+ ","+ target.column()+" \n\r");
								me.fileout.flush();
							}
							catch(Exception ioex)
							{
							ioex.printStackTrace();
							}
						}
					}
					

				};
				@Override
				protected Node	getParentTargetNode() {
					return target;
				};
			};
			celltransition.setCycleCount(1);
			cellTransitions[c.row()* boardlength+ c.column()]=  celltransition;


		};
	
		ParallelTransition boardanimation=
				new ParallelTransition(cellTransitions);
		boardanimation.setCycleCount(1);

        
		//one can add a specific action when the keyframe is reached
		EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				me.set_tic();
			}
		};

		PauseTransition tickpause=new PauseTransition(duration);
		tickpause.setOnFinished(onFinished);
		tickpause.setCycleCount(1);
		oneroundtic= new SequentialTransition(board_pane,new PauseTransition(Duration.millis(1000)), boardanimation,tickpause);
		oneroundtic.setCycleCount(5);
		
		Duration ontime=oneroundtic.getCurrentTime();
		Duration prevtime=oneroundtic.getCurrentTime();
		int ticks=oneroundtic.getCycleCount();
		for (int i=0; i < 1; i++) {
			oneroundtic.playFromStart();

			while (oneroundtic.getStatus()==Status.RUNNING &&ontime.lessThan(oneroundtic.getTotalDuration()) )
				{
				 ontime=oneroundtic.getCurrentTime();
				 try {
					    if (prevtime.add(Duration.seconds(1.0)).lessThan(ontime))
					    {
						if (me.fileout != null ) {
			         		me.fileout.write("\r\n- animoitu: "+ontime +" ---\r\n");
						}
						 prevtime=ontime;
						}
				 		}
						catch(Exception ex_out)
				         {
				         	ex_out.printStackTrace();
				         }
				 	
				};
			oneroundtic.pause();	
		}
        /*
        System.out.println("total time for board animation with pause is "+oneroundtic.getTotalDuration());
		int ticks=oneroundtic.getCycleCount();// animation runs asynchronously,so lets this thread to poll 
        System.out.println("total time for parallel animation is "+boardanimation.getTotalDuration());
        System.out.println("rate for parallel animation is "+boardanimation.getRate());
		Boolean waitforChildren=false;
    
		 oneroundtic.stop();
		 */
		 Alert endtext=new Alert(Alert.AlertType.CONFIRMATION,"PELIAIKA LOPPUI");
		 endtext.setTitle("GAMEOFLIFE");
		 endtext.setX(2.00);
		 endtext.setY(200.0);
		 endtext.show(); 

		
		 try {
			 if (me.fileout != null ) {
		      		me.fileout.write("\r\n---- peli loppui ---\r\n");
		     }
	    	 Thread.currentThread().sleep(1000);
	     }
	     catch(InterruptedException inEx)
	     {
	    	 System.out.println("interrupted");
	     }
		 catch(Exception ex_out)
         {
         	ex_out.printStackTrace();
         }
	

	 }
	 @Override
	 public void stop()
	 {
		 try {
         	if (me.fileout != null ) {
         		me.fileout.write("\r\n---- peli lopetettiin ---\r\n");
         		me.fileout.close();
         	}
         	if (oneroundtic != null)
         		oneroundtic.stop();
         	primaryStage.close();
         }
         catch(Exception ex_out)
         {
         	ex_out.printStackTrace();
         }
	 }
	 public static void main(String[] args) {
	        launch(args);

	    }

}
