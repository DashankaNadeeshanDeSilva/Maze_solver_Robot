package Lab5;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Move {
	
	// Constants

	public static int logLastTurnAngleSetPoint = 0;
	private final static int FORWARD_MOVEMENT_STOP_LIMIT_CM = 15; // cm

	private final static int FIXED_MOTOR_SPEED = 300;
	private final static int ROBOT_JUMP_DISTANCE_CM = 35; // cm
	private final static int COLOR_DETECT_DISTANCE_CM = 4; // cm
	private final static double KAPPA_TURN = 25; 
	private final static double KAPPA_STRAIGHT = 25;
	private final static int ANGEL_LIMIT = 1;
	
	// Parameters
	private static int motorSpeed;
	private static int setPointDistanceWRTWall;

	private static EV3LargeRegulatedMotor rightMotor;
	private static EV3LargeRegulatedMotor leftMotor;
	
	private static Gyro gyroObj;

	public Move()
	{	rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);
		leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);	
		gyroObj = new Gyro();
	}
	
	public void moveForColorDetection( int distanceSetPoint, int direction, Distance distanceObj) {
		
		int currentDistance = 0;
			
		leftMotor.setSpeed(100);
		rightMotor.setSpeed(100);
			
		if (direction == Robot.FWD_DIRECTION) {
		
			do {
				currentDistance  = distanceObj.distanceToWall();
				rightMotor.backward();
				leftMotor.backward();
				
			}while(currentDistance >= COLOR_DETECT_DISTANCE_CM);
			turn(logLastTurnAngleSetPoint);	// correct the robot orientation
		}
		
		else if  (direction == Robot.BWD_DIRECTION) {
				
			do {
				currentDistance  = distanceObj.distanceToWall();
				rightMotor.forward();
				leftMotor.forward();
			}while(currentDistance <= distanceSetPoint);
			turn(logLastTurnAngleSetPoint);	// correct the robot orientation
		}
		stopRobot();
		
	}
	
	public void moveStraightWithPController(int SetPointAngle, Distance distanceObj){
		
		int currentDistanceWRTWall;
		int MeasuredDistance = distanceObj.distanceToWall();
		setPointDistanceWRTWall = MeasuredDistance - ROBOT_JUMP_DISTANCE_CM;
		do {
			
			currentDistanceWRTWall  = distanceObj.distanceToWall();
			//printDistance(currentDistanceWRTWall);
			if(currentDistanceWRTWall >= FORWARD_MOVEMENT_STOP_LIMIT_CM && currentDistanceWRTWall <= ROBOT_JUMP_DISTANCE_CM ){
				setPointDistanceWRTWall = FORWARD_MOVEMENT_STOP_LIMIT_CM; 
			}
			
			motorSpeed = FIXED_MOTOR_SPEED;
			
			double currentAngle = gyroObj.getGyroAngle();
			double deltaAngle = SetPointAngle - currentAngle;
			LCD.drawString("currentAngle " + currentAngle, 1, 6);
			LCD.clear(1, 6, 15);
			Delay.msDelay(50);
			
			if ((int)Math.round(deltaAngle) <= -ANGEL_LIMIT) {
				leftMotor.setSpeed((int) (motorSpeed + (KAPPA_STRAIGHT*(Math.abs(deltaAngle)))));
				rightMotor.setSpeed(((int) (motorSpeed - (KAPPA_STRAIGHT*(Math.abs(deltaAngle))))));

			}
			else if ((int)Math.round(deltaAngle) >= ANGEL_LIMIT) {
			
				leftMotor.setSpeed((int) (motorSpeed - (KAPPA_STRAIGHT*(Math.abs(deltaAngle)))));
				rightMotor.setSpeed(((int) (motorSpeed + (KAPPA_STRAIGHT*(Math.abs(deltaAngle))))));
			}
			else if((int)Math.round(deltaAngle) < ANGEL_LIMIT && (int)Math.round(deltaAngle) > -ANGEL_LIMIT) {
				leftMotor.setSpeed(motorSpeed);
				rightMotor.setSpeed(motorSpeed);
			}
			
			leftMotor.backward();
			rightMotor.backward();
			
			
		}while(currentDistanceWRTWall >= setPointDistanceWRTWall);
		
		stopRobot();
	}
	
	public void turn(int AngleSetPoint) {
		
		logLastTurnAngleSetPoint = AngleSetPoint;
		double deltaAngle;
		do {
			double currentAngle = gyroObj.getGyroAngle();
			deltaAngle = AngleSetPoint - currentAngle;
			motorSpeed = (int)Math.round(Math.abs(deltaAngle) * KAPPA_TURN); // Assume that delta (deviation) * Kappa (gain constant) gives speed degrees per seconds
			if(motorSpeed >= 300)
			{	motorSpeed = 300;}
			
			leftMotor.setSpeed(motorSpeed);
			rightMotor.setSpeed(motorSpeed);
			
			// turn the robot
			// turning direction (clockwise or counter clockwise) depends on the sign ( + or - ) of the goal angle
			if ((int)Math.round(deltaAngle) < 0) {
				rightMotor.forward();
				leftMotor.backward();
			}
			else if ((int)Math.round(deltaAngle) > 0) {
				leftMotor.forward();
				rightMotor.backward();
			}
			else if((int)Math.round(deltaAngle) == 0) {
				
				stopRobot();
				break;
			}
		
		}while(deltaAngle != 0);
	}
		
	public void stopRobot() {
		
		leftMotor.setSpeed(0);
		rightMotor.setSpeed(0);
		rightMotor.backward();
		leftMotor.backward();
		rightMotor.stop();
		leftMotor.stop();
		
	}
	
}
