package Lab5;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Distance {
	
	private static EV3UltrasonicSensor distanceSensor;
	private SampleProvider distance;
	
	public Distance() {
		distanceSensor = new EV3UltrasonicSensor(SensorPort.S4);
	}
	public int distanceToWall() {
	
		distanceSensor.enable();
		distance = distanceSensor.getDistanceMode();
		float[] distanceSample = new float[1];
		distance.fetchSample(distanceSample, 0);
		int measuredDistance_CM =  (int)Math.round(distanceSample[0] * 100);  // convert to cm
		printDistance(measuredDistance_CM);
		return measuredDistance_CM;	
	}
	
	public void printDistance(int dist) {
	LCD.drawString("Dist = " + dist , 0, 0);
	Delay.msDelay(20);
	LCD.clear(0, 0, 20 ); 
	
	}
	
}
