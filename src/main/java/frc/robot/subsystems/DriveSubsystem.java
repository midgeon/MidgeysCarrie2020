/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class DriveSubsystem extends SubsystemBase {

  public static DifferentialDrive m_drive;

  /*
   * As of Feb 26:
   *
   * For Carrie:
   * front left 1  front right 2
   * back left  3  back right  4
   *
   * For Chassis:
   * front left 4  front right 3
   * back left  2  back right  1
   */
  
  private CANSparkMax frontLeftMotor = new CANSparkMax(1, MotorType.kBrushless);
  private CANSparkMax frontRightMotor = new CANSparkMax(2, MotorType.kBrushless);
  private CANSparkMax backLeftMotor = new CANSparkMax(3, MotorType.kBrushless);
  private CANSparkMax backRightMotor = new CANSparkMax(4, MotorType.kBrushless);

  private CANEncoder m_frontLeftEncoder = new CANEncoder(frontLeftMotor);
  private CANEncoder m_frontRightEncoder = new CANEncoder(frontRightMotor);
  private CANEncoder m_backLeftEncoder = new CANEncoder(backLeftMotor);
  private CANEncoder m_backRightEncoder = new CANEncoder(backRightMotor);

  public DriveSubsystem() {

    boolean isCarrie = true;
    double encoderPCF;
    if (!isCarrie) {  
      frontLeftMotor.setInverted(true);
      frontRightMotor.setInverted(false);
      backLeftMotor.setInverted(true);
      backRightMotor.setInverted(false);
      encoderPCF = 1.77;
      // ^ This for Chasis
    } else {
      frontLeftMotor.setInverted(false);
      frontRightMotor.setInverted(true);
      backLeftMotor.setInverted(false);
      backRightMotor.setInverted(true);
      encoderPCF = 1.77; // needs tuning for Carrie
      // ^ This for Carrie
    }

    frontLeftMotor.setSmartCurrentLimit(80);
    frontRightMotor.setSmartCurrentLimit(80);
    backLeftMotor.setSmartCurrentLimit(80);
    backRightMotor.setSmartCurrentLimit(80);

    backLeftMotor.follow(frontLeftMotor);
    backRightMotor.follow(frontRightMotor);

    // ???? Configure encoders here
    m_frontLeftEncoder.setPositionConversionFactor(encoderPCF);
    m_frontRightEncoder.setPositionConversionFactor(encoderPCF);
    m_backLeftEncoder.setPositionConversionFactor(encoderPCF);
    m_backRightEncoder.setPositionConversionFactor(encoderPCF);

    this.resetEncoders();
    
    m_drive = new DifferentialDrive(frontLeftMotor, frontRightMotor);
    m_drive.setRightSideInverted(false);
    m_drive.setMaxOutput(Constants.k);
  }

  public void arcadeDrive(double speed, double rotation) {
    m_drive.arcadeDrive(speed, rotation);
  }
    
  public void tankDrive(double leftSpeed, double rightSpeed){
    // May need invert left
    m_drive.tankDrive(leftSpeed, rightSpeed);
  }

  @Override
  public void periodic() {
    m_drive.feedWatchdog(); // check this
  }

  public void resetEncoders() {
    m_frontLeftEncoder.setPosition(0.0);
    m_frontRightEncoder.setPosition(0.0);
    m_backLeftEncoder.setPosition(0.0);
    m_backRightEncoder.setPosition(0.0);
  }

  public double getMeanEncoderDistance() {
    // currently report leaders only
    return (getLeftEncoderDistance() + getRighttEncoderDistance()) / 2.0;
  }

  public double getLeftEncoderDistance() {
    // currently report leader only
    return m_frontLeftEncoder.getPosition();
  }

  public double getRighttEncoderDistance() {
    // currently report leader only      
    return m_frontRightEncoder.getPosition();
  }

  // May want to try this rather than multiplying by constant scale everywhere
  public void setMaxOutput(double maxOutput) {
    m_drive.setMaxOutput(maxOutput);
  }
  
}
