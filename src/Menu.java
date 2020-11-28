package Lab5;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class Menu {
	
	// Parameters
	public static String color = "RED";
	public static int cursorPosition = 1;
	public static String selectedColor;
	
	public static String pressedButton;
	
	public String colorSelectionMenu() {

		// parameters - changes over operation and default values are assigned at the beginning

		do
		{
			DrawMenu(cursorPosition);
			
			if (anyButtonHasPressed() )
			{	
				if(pressedButton.equals("down")) {
					cursorPosition = downButtonHasPressed(cursorPosition);
					//if(cursorPosition == 1 ) {color = "REDE";}
					if(cursorPosition == 1 ) {color = "RED";}
					else if(cursorPosition == 2 ) {color = "BLUE";}
					else if(cursorPosition == 3 ) {color = "GREEN";}
					else if(cursorPosition == 4 ) {color = "YELLOW";}
					
				}
				
				else if(pressedButton.equals("up") ) {
					cursorPosition = upButtonHasPressed(cursorPosition);
					//if(cursorPosition == 1 ) {color = "RED";}
					if(cursorPosition == 1 ) {color = "RED";}
					else if(cursorPosition == 2 ) {color = "BLUE";}
					else if(cursorPosition == 3 ) {color = "GREEN";}
					else if(cursorPosition == 4 ) {color = "YELLOW";}
				}
				
				else if(pressedButton.equals("enter")) {
					selectedColor = enterButtonHasPressed();
				}
				
			}
			
		//}while(anyButtonHasPressed()); //menuObject.anyButtonHasPressed()
		}while(pressedButton != "enter");
			
		return selectedColor;
	}
	
	// ----------------------------------------
	public int getButtonID() {
		
		int buttonId = Button.waitForAnyPress(); 
		// Returns: the ID of the button that has been pressed or in rare cases a bitmask of button IDs
		return buttonId;
	}
	
	//-----------------------------------------
	public boolean anyButtonHasPressed() {
		
		int buttonId = getButtonID();
		boolean anybutton_has_pressed = false;
		
		if( buttonId == 2 || buttonId == 16 || buttonId == 8 || buttonId == 1 || buttonId == 4 || buttonId == 2) 
		{
			if(buttonId == 2) { pressedButton = "enter";}
			else if(buttonId == 1) { pressedButton = "up";}
			else if(buttonId == 4) { pressedButton = "down";}
					
			anybutton_has_pressed = true;
		}
		
		else if(buttonId == 32) {
			
			LCD.drawString("Exit menu", 5, 4);
			pressedButton = null;
			anybutton_has_pressed = false;
		}
		
		return anybutton_has_pressed;
	}
	
	//---------------------------------------------
	public int downButtonHasPressed(int oldCursorPosition) {
		LCD.clear(1,oldCursorPosition, 1);
		int newCursorPosition = oldCursorPosition + 1;  

		return newCursorPosition;
	}

	//---------------------------------------------
	public int upButtonHasPressed(int oldCursorPosition) {
		LCD.clear(1,oldCursorPosition, 1);
		int newCursorPosition = oldCursorPosition - 1;

		return newCursorPosition;
	}
	
	//---------------------------------------------
	public String enterButtonHasPressed() {
		
		Sound.setVolume(Robot.VOLUME);  // set volume from 0-100
		Sound.playTone(Robot.FREQUENCY, Robot.DURATION); // frequency in Hz and Duration in ms
		
		return color;
	}
		
	//---------------------------------------------
	public void DrawMenu(int cursorPosition) {
		
		LCD.drawString(">", 1, cursorPosition); LCD.drawString("Select Color", 1, 0); 
		LCD.drawString("RED", 2, 1);
		LCD.drawString("BLUE", 2, 2);
		LCD.drawString("GREEN", 2, 3);
		LCD.drawString("YELLOW", 2, 4);
		
	}

}
