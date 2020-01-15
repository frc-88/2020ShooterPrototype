/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private CANSparkMax shooter;
  private CANSparkMax spinup;
  private TalonSRX upperBelt;
  private TalonSRX lowerBelt;
  private Joystick controller;

  private static final double SHOOTER_RATIO = 3./5.;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    shooter = new CANSparkMax(1, MotorType.kBrushless);
    shooter.restoreFactoryDefaults();
    SmartDashboard.putNumber("kP", 0.00015);
    SmartDashboard.putNumber("kI", 0.0000001);
    SmartDashboard.putNumber("kD", 0);
    SmartDashboard.putNumber("kF", 0.00018);
    SmartDashboard.putNumber("IZone", 250);
    SmartDashboard.putNumber("IMax", 0);
    SmartDashboard.putNumber("Command Speed", 6800);

    spinup = new CANSparkMax(2, MotorType.kBrushless);
    spinup.restoreFactoryDefaults();

    upperBelt = new TalonSRX(3);
    upperBelt.configFactoryDefault();
    lowerBelt = new TalonSRX(4);
    lowerBelt.configFactoryDefault();

    controller = new Joystick(0);
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    shooter.getPIDController().setP(SmartDashboard.getNumber("kP", 0.00015));
    shooter.getPIDController().setI(SmartDashboard.getNumber("kI", 0.0000001));
    shooter.getPIDController().setD(SmartDashboard.getNumber("kD", 0));
    shooter.getPIDController().setFF(SmartDashboard.getNumber("kF", 0.00018));
    shooter.getPIDController().setIZone(SmartDashboard.getNumber("IZone", 250));
    shooter.getPIDController().setIMaxAccum(SmartDashboard.getNumber("IMax", 0), 0);
    
    shooter.getPIDController().setReference(controller.getRawButton(6) ? SmartDashboard.getNumber("Command Speed", 0) * SHOOTER_RATIO : 0, ControlType.kVelocity);

    SmartDashboard.putNumber("Actual Speed", shooter.getEncoder().getVelocity() / SHOOTER_RATIO);
    SmartDashboard.putNumber("Current", shooter.getOutputCurrent());

    spinup.set(controller.getRawButton(5) ? 1 : 0);

    SmartDashboard.putNumber("Spinup Speed", spinup.getEncoder().getVelocity());

    double indexerSpeed = controller.getRawAxis(3);
    upperBelt.set(ControlMode.PercentOutput, indexerSpeed);
    lowerBelt.set(ControlMode.PercentOutput, indexerSpeed);
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
