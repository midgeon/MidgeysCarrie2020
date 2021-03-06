/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

//IMPORTANT: No idea what is up with the climber, change this subsystem as things happen plz :)
package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class ClimberSubsystem extends SubsystemBase {
  public VictorSPX climberHook = new VictorSPX(10);
  public VictorSPX climberWinch = new VictorSPX(11);
  // ^ FIX: Holder values in here

  double hookSpeed;
  double winchSpeed;
  XboxController m_manipulatorXBox;

  public ClimberSubsystem(XboxController manipulatorXBox) {
    m_manipulatorXBox = manipulatorXBox;
    register();
  }

  @Override
  public void periodic() {
    hookSpeed = m_manipulatorXBox.getRawAxis(1);
    setClimberSpeed(hookSpeed);
    //SmartDashboard.putNumber("Hook Speed", hookSpeed);
  }

  public void setClimberSpeed(double hSpeed){
    if(hSpeed > 0.80){
      hookSpeed = -Constants.CLIMBER_HOOK_SPEED;
    }
    else if(hSpeed < -0.80){
      hookSpeed = Constants.CLIMBER_HOOK_SPEED;
    }
    else{
      hookSpeed = 0.0;
    }
    climberHook.set(ControlMode.PercentOutput, hookSpeed);
  }

  public void setWinchSpeed(double wSpeed){
    climberWinch.set(ControlMode.PercentOutput, wSpeed);
  }
}
