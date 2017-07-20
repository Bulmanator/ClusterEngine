/*
    MIT License

    Copyright (c) 2017 James Bulman

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
*/

package com.cluster.engine.Utilities;

import org.jsfml.graphics.Color;

/**
 * Various useful maths functions
 */
public final class MUtil {

    /**
     * A floating point representation of Pi
     */
    public static final float PI = (float)Math.PI;

    /**
     * A floating point representation of 2 * {@link #PI};
     */
    public static final float PI2 = PI * 2f;

    /**
     * A very small value for checking if floating point numbers are zero
     */
    public static final float EPSILON = 0.000001f;

    /**
     * Used to convert angles from Degrees to Radians
     */
    public static final float DEG_TO_RAD = PI / 180f;

    /**
     * Used to convert angles from Radians to Degrees
     */
    public static final float RAD_TO_DEG = 180f / PI;

    // Private constructor to prevent instantiation
    private MUtil() {}

    /**
     * Calculates the square of the given number
     * @param number The number to square
     * @return The square
     */
    public static float square(float number) { return number * number; }

    /**
     * Calculates the square root of the given number
     * @param number The number to square root
     * @return The square root
     */
    public static float sqrt(float number) {
        return (float) Math.sqrt(number);
    }

    /**
     * A floating point conversion for {@link Math#cos(double)}
     * @param radians The angle, in radians
     * @return The cosine of the angle
     */
    public static float cos(float radians) {
        // TODO(James): Implement a cosine table instead for lookups
        return (float)Math.cos(radians);
    }

    /**
     * A floating point conversion for {@link Math#sin(double)}
     * @param radians The angle, in radians
     * @return The sine of the angle
     */
    public static float sin(float radians) {
        // TODO(James): Implement a sine table instead for lookups
        return (float)Math.sin(radians);
    }

    /**
     * Generates a random integer between the values specified, [min, max)
     * @param min The lower bound to generate between
     * @param max The upper bound to generate between
     * @return A random integer between the values
     */
    public static int randomInt(int min, int max) {
        return min + (int)(Math.random() * (max - min));
    }

    /**
     * Generates a random float between the values specified, [min, max)
     * @param min The lower bound to generate between
     * @param max The upper bound to generate between
     * @return A random float between the values
     */
    public static float randomFloat(float min, float max) {
        return min + (float)(Math.random() * (max - min));
    }

    /**
     * Gets the linear interpolation value at the given value between the two points
     * @param begin The beginning value
     * @param end The end value
     * @param value The progression between the begin and end points
     * @return The linear interpolation between the two values
     */
    public static float lerp(float begin, float end, float value) {
        value = clamp(value, 0, 1);
        return (end * value) + (begin * (1 - value));
    }

    /**
     * Gets the linear interpolation colour between the two given colours
     * @param beginColour The beginning colour
     * @param endColour The ending colour
     * @param value The progression between the begin and end colours
     * @return The linear interpolation colour between the given ones
     */
    public static Color lerpColour(Color beginColour, Color endColour, float value) {
        value = clamp(value, 0, 1);
        int r = (int) ((endColour.r * value) + (beginColour.r * (1 - value)));
        int g = (int) ((endColour.g * value) + (beginColour.g * (1 - value)));
        int b = (int) ((endColour.b * value) + (beginColour.b * (1 - value)));

        return new Color(r, g, b);
    }

    /**
     * Clamps the given value between the min and max values
     * @param value The value to clamp
     * @param min The minimum that value can be
     * @param max The maximum that value can be
     * @return If value greater than max then max, else if value less than min then min, else value
     */
    public static float clamp(float value, float min, float max) {
        float end;

        if(max < min) {
            end = max;
            max = min;
            min = end;
        }

        end = value;

        if(value > max) {
            end = max;
        }
        else if(value < min) {
            end = min;
        }

        return end;
    }

    /**
     * Rounds a value to the specified number of decimal places
     * @param value The value to round
     * @param places The number of decimal places to leave
     * @return The value given rounded to the decimal places specified
     */
    public static float round(float value, int places) {
        int multi = (int)Math.pow(10, places);
        return (float)Math.round(value * multi) / (float)multi;
    }

    /**
     * Checks if the given floating point value is less than {@link MUtil#EPSILON}
     * @param value The value to check
     * @return True if the value is smaller than {@link MUtil#EPSILON}, otherwise False
     */
    public static boolean isZero(float value) {
        return Math.abs(value) <= EPSILON;
    }

    /**
     * Checks if the absolute value of the given floating point value is less than the tolerance given
     * @param value The value to check
     * @param tolerance The tolerance to check if the value is less than
     * @return True if the value is smaller than tolerance, otherwise False
     */
    public static boolean isZero(float value, float tolerance) {
        return Math.abs(value) <= tolerance;
    }

    /**
     * Will change an angles range to be between {@link #PI} and -{@link #PI}
     * @param radians the angle you wish to normalize in radians
     * @return the angle in radians between 180 degrees and -179 degrees
     */
    public static float normalizeAngle(float radians) {
        while (radians <= -MUtil.PI) radians += MUtil.PI2;
        while (radians > MUtil.PI) radians -= MUtil.PI2;
        return radians;
    }
}
