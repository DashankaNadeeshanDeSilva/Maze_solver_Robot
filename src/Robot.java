package Lab5;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class Robot {
	
	// constants	
	private final static int HALF_TILE_SIZE_CM = 20; // cm
	private final static int TURN_90_CCW = 90;
	private final static int TURN_90_CW = -90  ;
//	public final static int STRAIGHT = 0;
	public final static int FWD_DIRECTION = 1;
	public final static int BWD_DIRECTION = -1;
	
	public static int VOLUME = 50 ; 
	public static int FREQUENCY = 500;
	public static int DURATION= 1000;
	
	// class objects
	private static Distance distanceObject;
	private static Move moveObject;
	private static Color colorObject;
	private static Menu menuObject;
	
	// Parameters	

	public static void main(String[] args) {
		
		 distanceObject = new Distance();
		 colorObject = new Color();
		 menuObject = new Menu();
		 String	desiredColor = menuObject.colorSelectionMenu();
		 moveObject = new Move();
		 
		
		start(desiredColor);
	
	}
	
	private static void start(String colorToFind) {
		
		int distanceFromFront = 0;
		int distanceFromLeft = 0;
		int distanceFromRight = 0;
		boolean deadEndDetected = false;
		do {
			
			LCD.clearDisplay();
			// Measure distance from front direction
			distanceFromFront  = distanceObject.distanceToWall();
//			LCD.drawString("d_front: " + distanceFromFront, 1, 5);
//			Delay.msDelay(1000);
			
			if(deadEndDetected == false) {
			
			// turn counter clockwise and get the distance to wall in left side
			moveObject.turn(TURN_90_CCW + Move.logLastTurnAngleSetPoint);
			distanceFromLeft = distanceObject.distanceToWall();
//			LCD.drawString("d_left: " + distanceFromLeft, 1, 6);
//			Delay.msDelay(1000);
		
			// turn clockwise and get the distance to wall in right side
			moveObject.turn(Move.logLastTurnAngleSetPoint + TURN_90_CW + TURN_90_CW );
			distanceFromRight = distanceObject.distanceToWall();
//			LCD.drawString("d_right: " + distanceFromRight, 1, 7);
//			Delay.msDelay(1000);
			
			// turn back to original position
			moveObject.turn(TURN_90_CCW + Move.logLastTurnAngleSetPoint);
			}
			
			// Move forward to the next tile as there is no blocking
			if(distanceFromFront > distanceFromLeft && distanceFromFront > distanceFromRight && distanceFromFront > HALF_TILE_SIZE_CM) {
				deadEndDetected = false;
//				LCD.drawString("GO Front", 1, 1);
//				Delay.msDelay(2000);
//				LCD.clear(1, 1, 10);
				
				moveObject.moveStraightWithPController( Move.logLastTurnAngleSetPoint, distanceObject);
			}
			
			// Turn left and move to the next tile
			else if(distanceFromLeft > distanceFromFront && distanceFromLeft > distanceFromRight  && distanceFromLeft > HALF_TILE_SIZE_CM) {
				deadEndDetected = false;
//				LCD.drawString("GO Left", 1, 1);
//				Delay.msDelay(2000);
//				LCD.clear(1, 1, 10);
				moveObject.turn(TURN_90_CCW + Move.logLastTurnAngleSetPoint);
				moveObject.moveStraightWithPController(Move.logLastTurnAngleSetPoint, distanceObject);
			}
				
			// Turn right and move to the next tile
			else if(distanceFromRight > distanceFromFront && distanceFromRight > distanceFromLeft  && distanceFromRight > HALF_TILE_SIZE_CM) {
				deadEndDetected = false;
//				LCD.drawString("GO Right", 1, 1);
//				Delay.msDelay(2000);
//				LCD.clear(1, 1, 10);
				moveObject.turn(TURN_90_CW + Move.logLastTurnAngleSetPoint);
				moveObject.moveStraightWithPController(Move.logLastTurnAngleSetPoint,distanceObject);
			}
			
			// When robot reaches to a dead end
			else if(distanceFromFront < HALF_TILE_SIZE_CM && distanceFromLeft < HALF_TILE_SIZE_CM && distanceFromRight < HALF_TILE_SIZE_CM) {
				deadEndDetected =  true;
//				LCD.drawString("Dead End", 1, 1);
//				Delay.msDelay(2000);
//				LCD.clear(1, 1, 10);	
				
				if(ApproachToWallandDetectColor(colorToFind))
					{break;}
				else {
					// turn to left side and detect color of the wall
					moveObject.turn(TURN_90_CCW + Move.logLastTurnAngleSetPoint);
					}
				
				if(ApproachToWallandDetectColor(colorToFind))
					{break;}
				else {
					// turn to right side and detect color of the wall
					moveObject.turn(TURN_90_CW + TURN_90_CW + Move.logLastTurnAngleSetPoint);
					}
				
				if(ApproachToWallandDetectColor(colorToFind))
					{break;}
				
					// turn to return back to original position from the dead end
				moveObject.turn(TURN_90_CW + Move.logLastTurnAngleSetPoint);
			}

		}while(true);	
	}
	
	private static boolean ApproachToWallandDetectColor(String colorToFind) {
			
		LCD.clearDisplay();
		int robotToWallDistance = distanceObject.distanceToWall();
		moveObject.moveForColorDetection( robotToWallDistance ,FWD_DIRECTION, distanceObject);
		
		String color = colorObject.detectColor();
		LCD.drawString("Detect clr " + color , 1, 6);
		
		if( colorToFind == color) {
			LCD.clearDisplay();
			LCD.drawString("Detect clr " + color , 1, 6);
			Sound.setVolume(VOLUME);  // set volume from 0-100
			Sound.playTone(FREQUENCY, DURATION); // frequency in Hz and Duration in ms
			moveObject.stopRobot();
			Delay.msDelay(2000);
			return true;
		}
		
		else {
			// Move back 
			moveObject.moveForColorDetection(robotToWallDistance, BWD_DIRECTION, distanceObject);
			return false;

		}	
		
	}
	

}

