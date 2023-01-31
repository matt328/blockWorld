package org.blockworld.math;

/**
 * Helpful maths functions. Focus on using floats rather than doubles for speed
 * 
 * @author Matt Teeter
 */
public final class Maths {
   public static final float ROOT_TWO = (float) Math.sqrt(2);
   public static final float ROOT_THREE = (float) Math.sqrt(3);
   public static final float E = (float) Math.exp(1);
   public static final float PI = (float) Math.PI;
   public static final float TWO_PI = 2.0f * PI;
   public static final float TAU = TWO_PI;
   public static final float HALF_PI = 0.5f * PI;
   public static final float QUARTER_PI = 0.25f * PI;
   private static final double EPSILON = 0.00001;

   private Maths() {

   }

   public static float sqrt(final float a) {
      return (float) Math.sqrt(a);
   }

   public static double sqrt(final double a) {
      return Math.sqrt(a);
   }

   public static float fastInverseSqrt(final float x) {
      final float xhalf = 0.5f * x;
      int i = Float.floatToRawIntBits(x);
      i = 0x5F3759DF - (i >> 1);
      float nx = Float.intBitsToFloat(i);
      nx = x * (1.5f - xhalf * x * x);
      return nx;
   }

   /**
    * Interesting way to approximate a square root.... however seems to be
    * slower than standard Math.sqrt() approach
    * 
    * @param x
    * @return
    */
   public static float alternateSqrt(final float x) {
      if (x < 0) {
         return 0;
      }
      float r = approxSqrt(x);
      r = r - (0.5f * ((r * r) - x) / r); // Newton iteration
      r = r - (0.5f * ((r * r) - x) / r); // Newton iteration
      return r;
   }

   public static float approxSqrt(final float x) {
      int i = Float.floatToRawIntBits(x);
      // int exponent=i&0x7F800000; // 8 bits below sign bit
      // int value=(i&0x7FFFFFF)+((exponent!=0)?0:0x08000000); // 23 low bits,
      // implicit 1 unless exponent=0
      i = (i + 0x3F800000) >>> 1;
      return Float.intBitsToFloat(i);
   }

   public static int clampToInteger(final double value, final int min, final int max) {
      final int v = (int) value;
      if (v < min) {
         return min;
      }
      if (v > max) {
         return max;
      }
      return v;
   }

   public static int clampToInteger(final float value, final int min, final int max) {
      final int v = (int) value;
      if (v < min) {
         return min;
      }
      if (v > max) {
         return max;
      }
      return v;
   }

   /**
    * Return the middle value of 3 numbers Can use faster "bound" method if
    * first and last parameters are in order
    */
   public static int middle(final int a, final int b, final int c) {
      if (a < b) {
         if (b < c) {
            return b;
         }
         return (a < c) ? c : a;
      } else {
         if (a < c) {
            return a;
         }
         return (b < c) ? c : b;
      }
   }

   /**
    * Return the middle value of 3 numbers Can use faster "bound" method if
    * first and last parameters are in order
    */
   public static float middle(final float a, final float b, final float c) {
      if (a < b) {
         if (b < c) {
            return b;
         }
         return (a < c) ? c : a;
      } else {
         if (a < c) {
            return a;
         }
         return (b < c) ? c : b;
      }
   }

   public static int sign(final double a) {
      if (a == 0.0) {
         return 0;
      }
      return (a > 0) ? 1 : -1;
   }

   public static int sign(final float a) {
      if (a == 0.0f) {
         return 0;
      }
      return (a > 0) ? 1 : -1;
   }

   public static final int sign(final int a) {
      return (a == 0) ? 0 : ((a > 0) ? 1 : -1);
   }

   /**
    * Mike's fast integer sign algorithm
    * 
    * @param a
    * @return Sign of the given number (-1, 0 or 1)
    */
   public static final int sign2(final int a) {
      return (a >> 31) + ((a > 0) ? 1 : 0);
   }

   // branchless integer sign - however incorrect for MIN_INTEGER
   public static int sign2fast(final int a) {
      return 1 + (a >> 31) + ((a - 1) >> 31);
   }

   public static int sign(final long a) {
      if (a == 0) {
         return 0;
      }
      return (a > 0) ? 1 : -1;
   }

   public static float fmod(final float n, final float d) {
      final float x = n / d;
      return n - floor(x) * d;
   }

   /**
    * Integer modulus function
    * 
    * @param n
    *           number
    * @param d
    *           divisor
    * @return
    */
   public static int mod(final int number, final int divisor) {
      int r = (number % divisor);
      if (r < 0) {
         r += divisor;
      }
      return r;
   }

   /**
    * Detects the number of times that boundary is passed when adding increase
    * to base
    * 
    * @param increase
    * @param boundary
    * @param base
    * @return
    */
   public static long quantize(final long increase, final long boundary, final long base) {
      return ((base + increase) / boundary) - (base / boundary);
   }

   public static double min(final double a, final double b, final double c) {
      double result = a;
      if (b < result) {
         result = b;
      }
      if (c < result) {
         result = c;
      }
      return result;
   }

   public static double max(final double a, final double b, final double c) {
      double result = a;
      if (b > result) {
         result = b;
      }
      if (c > result) {
         result = c;
      }
      return result;
   }

   public static float min(final float a, final float b, final float c) {
      float result = a;
      if (b < result) {
         result = b;
      }
      if (c < result) {
         result = c;
      }
      return result;
   }

   public static float max(final float a, final float b, final float c) {
      float result = a;
      if (b > result) {
         result = b;
      }
      if (c > result) {
         result = c;
      }
      return result;
   }

   public static final float min(final float a, final float b, final float c, final float d) {
      float result = a;
      if (result > b) {
         result = b;
      }
      if (result > c) {
         result = c;
      }
      if (result > d) {
         result = d;
      }
      return result;
   }

   public static final float max(final float a, final float b, final float c, final float d) {
      float result = a;
      if (result < b) {
         result = b;
      }
      if (result < c) {
         result = c;
      }
      if (result < d) {
         result = d;
      }
      return result;
   }

   // branchless version of abs()
   public static int abs(final int a) {
      return (a ^ (a >> 31)) - (a >> 31);
   }

   // another branchless version of abs()
   public static int abs2(final int a) {
      final int mask = (a >> 31);
      return (a ^ mask) - mask;
   }

   public static int abs3(final int a) {
      if (a < 0) {
         return -a;
      }
      return a;
   }

   public static float abs(final float a) {
      if (a < 0) {
         return -a;
      }
      return a;
   }

   public static int min(final int a, final int b) {
      return (a < b) ? a : b;
   }

   public static int max(final int a, final int b) {
      return (a > b) ? a : b;
   }

   // branchless min
   public static int min2(final int a, final int b) {
      return a ^ ((a ^ b) & ((b - a) >> 31));
   }

   // branchless max
   public static int max2(final int a, final int b) {
      return a ^ ((a ^ b) & ((a - b) >> 31));
   }

   public static float min(final float a, final float b) {
      return (a < b) ? a : b;
   }

   public static float max(final float a, final float b) {
      return (a > b) ? a : b;
   }

   public static int min(final int a, final int b, final int c) {
      int result = a;
      if (b < result) {
         result = b;
      }
      if (c < result) {
         result = c;
      }
      return result;
   }

   public static int max(final int a, final int b, final int c) {
      int result = a;
      if (b > result) {
         result = b;
      }
      if (c > result) {
         result = c;
      }
      return result;
   }

   public static float sigmoid(final float a) {
      final double ea = Math.exp(-a);
      final float df = (float) (1 / (1.0f + ea));
      if (Float.isNaN(df)) {
         return (a > 0) ? 1 : 0;
      }
      return df;
   }

   public static double sigmoid(final double a) {
      final double ea = Math.exp(-a);
      final double df = (1 / (1.0f + ea));
      if (Double.isNaN(df)) {
         return (a > 0) ? 1 : 0;
      }
      return df;
   }

   public static float tanh(final float a) {
      final double ex = Math.exp(2 * a);
      final float df = (float) ((ex - 1) / (ex + 1));
      if (Float.isNaN(df)) {
         return (a > 0) ? 1 : -1;
      }
      return df;
   }

   public static double tanh(final double a) {
      final double ex = Math.exp(2 * a);
      final double df = ((ex - 1) / (ex + 1));
      if (Double.isNaN(df)) {
         return (a > 0) ? 1 : -1;
      }
      return df;
   }

   public static double tanhScaled(final double a) {
      return 1.7159 * tanh((2.0 / 3.0) * a);
   }

   public static double tanhScaledDerivative(final double a) {
      final double ta = tanh((2.0 / 3.0) * a);
      return (1.7159 * (2.0 / 3.0)) * (ta * (1 - ta));
   }

   public static float inverseSigmoid(final float a) {
      if (a >= 1) {
         return 800;
      }
      if (a <= 0) {
         return -800;
      }
      final double ea = a / (1.0 - a);
      return (float) Math.log(ea);
   }

   public static float sigmoidDerivative(final float a) {
      final float sa = sigmoid(a);
      return sa * (1 - sa);
   }

   public static float tanhDerivative(final float a) {
      final float sa = tanh(a);
      return 1 - sa * sa;
   }

   public static float sin(double a) {
      a = a % Maths.TWO_PI;
      return (float) Math.sin(a);
   }

   public static float cos(double a) {
      a = a % Maths.TWO_PI;
      return (float) Math.cos(a);
   }

   public static float sin(float a) {
      a = a % Maths.TWO_PI;
      return (float) Math.sin(a);
   }

   public static float cos(float a) {
      a = a % Maths.TWO_PI;
      return (float) Math.cos(a);
   }

   public static int floor(final float a) {
      if (a >= 0) {
         return (int) a;
      }
      final int x = (int) a;
      return (a == x) ? x : x - 1;
   }

   public static float frac(final float a) {
      return a - Maths.floor(a);
   }

   public static double frac(final double a) {
      return a - Maths.floor(a);
   }

   public static int floor(final double a) {
      if (a >= 0) {
         return (int) a;
      }
      final int x = (int) a;
      return (a == x) ? x : x - 1;
   }

   public static int square(final byte b) {
      return b * b;
   }

   public static int square(final int a) {
      return a * a;
   }

   public static float square(final float a) {
      return a * a;
   }

   public static double square(final double a) {
      return a * a;
   }

   public static float round(final float f, final int dp) {
      final float factor = (float) Math.pow(10, -dp);
      return Math.round(f / factor) * factor;
   }

   public static int roundUp(final double d) {
      final int i = (int) d;
      return (i == d) ? i : (i + 1);
   }

   public static int roundUp(final Number d) {
      return roundUp(d.doubleValue());
   }

   public static int roundUp(final float d) {
      final int i = (int) d;
      return (i == d) ? i : (i + 1);
   }

   /**
    * Soft maximum function
    */
   public static double softMaximum(final double x, final double y) {
      final double max = Math.max(x, y);
      final double min = Math.min(x, y);
      return max + Math.log(1.0 + Math.exp(max - min));
   }

   /**
    * Computes a fast approximation to <code>Math.pow(a, b)</code>. Adapted from
    * <url>http://www.dctsystems.co.uk/Software/power.html</url>.
    * 
    * @param a
    *           a positive number
    * @param b
    *           a number
    * @return a^b
    */
   public static final float fastPower(final float a, float b) {
      // adapted from: http://www.dctsystems.co.uk/Software/power.html
      float x = Float.floatToRawIntBits(a);
      x *= 1.0f / (1 << 23);
      x = x - 127;
      float y = x - (int) Math.floor(x);
      b *= x + (y - y * y) * 0.346607f;
      y = b - (int) Math.floor(b);
      y = (y - y * y) * 0.33971f;
      return Float.intBitsToFloat((int) ((b + 127 - y) * (1 << 23)));
   }

   public static final float smoothStep(final float a, final float b, final float x) {
      if (x <= a) {
         return 0;
      }
      if (x >= b) {
         return 1;
      }
      final float t = bound(0.0f, (x - a) / (b - a), 1.0f);
      return t * t * (3 - 2 * t);
   }

   public static final float lerp(final float t, final float a, final float b) {
      return (1 - t) * a + t * b;
   }

   public static final float smoothFactor(final float t) {
      return t * t * (3 - 2 * t);
   }

   public static final float bound(final float min, final float v, final float max) {
      if (v < min) {
         return min;
      }
      if (v > max) {
         return max;
      }
      return v;
   }

   public static final double bound(final double min, final double v, final double max) {
      if (v < min) {
         return min;
      }
      if (v > max) {
         return max;
      }
      return v;
   }

   public static final int bound(final int min, final int v, final int max) {
      if (v < min) {
         return min;
      }
      if (v > max) {
         return max;
      }
      return v;
   }

   public static boolean notNearZero(final double d) {
      return (d < -EPSILON) || (d > EPSILON);
   }

}