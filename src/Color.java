package Lab5;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;

public class Color {
	
	private  EV3ColorSensor colorSensor;
	private int indexOfTheSamples = 0; 
	private SensorMode colorID;
	//private static String colorName;
	
	public Color() {
	  colorSensor = new EV3ColorSensor(SensorPort.S1);
	  
	}
	
	public String detectColor() {
		
	  colorID = colorSensor.getColorIDMode(); 
	  float[] sampleArrayColorID = new float[colorID.sampleSize()]; 
	  colorID.fetchSample(sampleArrayColorID, indexOfTheSamples); 
	  int colorID_int = (int)Math.round(sampleArrayColorID[indexOfTheSamples]);
	  
	  if(colorID_int == lejos.robotics.Color.RED) {
		  return "RED" ;
	  } 
	  else if(colorID_int == lejos.robotics.Color.BLUE) {
		  return "BLUE";
	  }
	  else if(colorID_int == lejos.robotics.Color.GREEN) {
		  return "GREEN";
	  }
	  else if(colorID_int == lejos.robotics.Color.YELLOW) {
		  return "YELLOW";
	  }
	  else if(colorID_int == lejos.robotics.Color.BROWN) {
		  return "BROWN";
	  }
	  else {
		  return "NULL";
	  }
	  
	}
	
//	public String getColor() {
//		return colorName;}
	
	
}

