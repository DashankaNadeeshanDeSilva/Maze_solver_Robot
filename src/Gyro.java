package Lab5;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

public class Gyro {

	
	private static EV3GyroSensor gyroSensor;
	private SampleProvider sample;
	
	public Gyro() {
		gyroSensor= new EV3GyroSensor(SensorPort.S3);
		sample = gyroSensor.getAngleMode();
	}
	public double getGyroAngle() {
		
		float [] sampleArray = new float[sample.sampleSize()];
		sample.fetchSample(sampleArray, 0);
		double currentAngle = sampleArray[0];
		return currentAngle;
	}

}
