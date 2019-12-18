package org.usfirst.frc.team772.robot.commands;

import org.usfirst.frc.team772.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoDriveCommand extends Command {

	double Kp = 0.3;
	public double distanceNeededToTravel = 0;
	public double distanceLeftToTravel;
	double speedOfRobot;
	double seconds;

	Timer timer = new Timer();

	/**
	 * This command drives the Robot a certain distance using an artificial P loop
	 * 
	 * @param distanceNeededToTravel
	 *            This is the distance in INCHES that you want the robot to travel
	 * @param seconds
	 *            This is the timeout in seconds for this command. The command will
	 *            end either way if this times out.
	 */
	public AutoDriveCommand(double distanceNeededToTravel, double seconds) {
		// This makes the distance declared above equal to the distance in the
		// AutoDriveCommand
		this.distanceNeededToTravel = distanceNeededToTravel;
		this.seconds = seconds;

	}

	// Called just before this Command runs the first time
	protected void initialize() {
		SmartDashboard.putString("DB/String 9", "Drive Initialize");

		Robot.driveSubsystem.resetEncoders();
		Robot.driveSubsystem.resetGyro();
		// Robot.driveRobot.cancel();
		distanceLeftToTravel = (distanceNeededToTravel - Robot.driveSubsystem.getEncAve());
		timer.start();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		SmartDashboard.putString("DB/String 9", "Drive Execute");
		double angleOfRobot = Robot.driveSubsystem.getDegrees();

		distanceLeftToTravel = (distanceNeededToTravel - Robot.driveSubsystem.getEncAve());

		speedOfRobot = 0.05 * distanceLeftToTravel;

		if (speedOfRobot > 1) {
			speedOfRobot = 0.8;// over shoots for 6-7 feet, may consiter lowering
		} else if (speedOfRobot < -1) {
			speedOfRobot = -0.8;
		} else if ((speedOfRobot < 0.75) && speedOfRobot > 0) {
			speedOfRobot = 0.4;// 40
		} else if ((speedOfRobot > -0.75) && speedOfRobot < 0) {
			speedOfRobot = -0.4;// 40
		}

		Robot.driveSubsystem.auto(speedOfRobot, angleOfRobot * Kp);
		SmartDashboard.putString("DB/String 8", "Speed: " + speedOfRobot);

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if ((Math.abs(distanceLeftToTravel) <= 0.5) || (timer.hasPeriodPassed(seconds))) {
			return true;
		} else {
			return false;
		}
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.driveSubsystem.auto(0, 0);
		// Robot.driveRobot.start();
		SmartDashboard.putString("DB/String 9", "Drive End");
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
