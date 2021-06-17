



package frc.robot.Libraries;

public class DeadZoneTuner{
    

    /**
     * 
     * @author Nikolai
     * 
     * This should handle customizations (especially for deadzones) that one may want to use on items that have a scale range
     * This should also be able to handle any additional scaling outside of the deadzones.
     */
    
     /**
     * adjustForDeadzone is entirely meant for the deadzone to be used, and it automatically utilizes a previously derived math equation
     * to start
     * @param input this number is typically a controller value, regardless of axis.
     * @param minimumrange
     * @param maximumrange
     * @param deadzone how big is it?
     * @param isInverted just to quickly
     * 
     */
    
    public double adjustForDeadzone(double input, double deadzone, boolean isInverted){
        double result;
        if(Math.abs(input) <= deadzone){
            result = 0;
        }
        else{
            result = Math.signum(input) *((Math.abs(input) - deadzone) *(1/(1 - deadzone)));
        }
        if (isInverted){
            return result * -1;
        }
        else{
        return result;
        }
    }
    /** 1st and foremost, this assumes that the minimum input is the opposite of maximum, otherwise initial scale for your input (as it is the most common for our applications)
     * This will let you adjust the input to any scale that you want by setting the minimum and maximum.
     * @param input this is the number that you will be adjusting to scale (usually on a scale of its own)
     * @param minimuminput this is the smallest number your input can give
     * @param maximuminput this is the biggest number your input can give
     * @param minimumdesired this will be the smallest number that the function will input that you want if you bring your input to its minimum
     * @param maximumdesired this will be the biggest if you put in the max.
     * 
     */
    public double changescale(double input, double minimumdesired, double maximumdesired, double minimuminput, double maximuminput){
        double output;

        double initialscale = (input + maximuminput) / (maximuminput - minimuminput);

        output = (initialscale * (maximumdesired - minimumdesired) + minimumdesired);
        
        return output;
    }

        


    /** These modules are an updated and probably
     *  improved version of Nikolai's previous modules
     *  
     *  They probably work....
     *  ngl im proud of them (:
     */

    
    /** This module will scale values starting from the minium to its maximum by creating a slope line
     * @param input The input value for the module to scale
     * @param minimum The lowest possible value for the input to reach
     * @param maximum The highest possible value for the input to reach
     */
    public double scaleValues(double input, double minimum, double maximum){
        double output = 0;
        
        double factor = (input - minimum);
        double slope = (maximum/(minimum));


        output = factor * slope;
        return output;
    }


    /** This is an improved version of Nikolai's DeadzoneTuner, this version uses a scale
     * to avoid creating touchyness in the controls when using large deadzones.
     * 
     * In other words, nikolias code is bad i think
     * 
     * @param input The input of the value before it is controlled by the deadzone
     * @param deadzone The area of values to return as 0 
     * @param isInverted To invert the value
     * @param maximum This value will show the maximum output that you want from
     * your input. Generally this should be set to 1 for 100% power from a joystick. For more precise controlls
     * this should be set lower.
     */
    public double scaleDeadzone(double input, double deadzone, boolean isInverted, double maximum){
        double output = 0;
        int invert = 1;
        if(isInverted = true){ invert = -1; }



        if(Math.abs(input) <= deadzone){output = 0;

        }else if(input >= 0){
            output = scaleValues(input, deadzone * 
            (input / Math.abs(input)), maximum  * 
            (input / Math.abs(input)));}
        return output * invert;
    }
    
}