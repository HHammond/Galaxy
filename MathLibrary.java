

public class MathLibrary{
	

//MICRO


    /**
     * Value approximating PI
     */
    public static final double PI = Math.PI;


    private static void trace(String str) {
        System.out.println(str);
    }

    private static void trace(double in) {
        trace((double) (Math.round(in * 100)) / 100 + "");
    }

    private static void trace(double[] in) {
        for (int i = 0; i < in.length; i++) {
            System.out.print((double) (Math.round(in[i] * 100)) / 100 + " ");
        }

        System.out.print("\n");
    }

    private static void trace(double[][] in) {
        for (int i = 0; i < in.length; i++) {
            trace(in[i]);
        }

        trace("");
    }

    private static void trace(boolean in) {
        trace(in + "");
    }


    public static double projectOnX(double[] vector){
        return vector[0];
    }
    public static double projectOnX(double magnitude,double angle){
        return magnitude*Math.cos(angle);
    }
    public static double projectOnY(double magnitude,double angle){
        return magnitude*Math.sin(angle);
    }

    /**
     *
     * @param columnVector column vector to be converted
     * @return converted column vector in form of {a...n}
     */
    public static double[] toRowVector(double[][] columnVector) {
        assert columnVector[0].length == 1;

        double[] n = new double[columnVector.length];

        for (int i = 0; i < columnVector.length; i++) {
            n[i] = columnVector[i][0];
        }

        return n;
    }

    /**
     *
     * Convert row vector to a column vector
     *
     * @param rowVector the vector to be converted
     * @return column vector in from of {a},...,{n}
     */
    public static double[][] toColVector(double[] rowVector) {
        double[][] newVector = new double[rowVector.length][1];

        for (int i = 0; i < rowVector.length; i++) {
            newVector[i][0] = rowVector[i];
        }

        return newVector;
    }

    /**
     * Convert a row vector into a standardized form to allow easier calculations
     * @param rowVector vecttor to be converted
     * @return formatted row vector
     */
    public static double[][] rowVectorToMatrix(double[] rowVector) {
        return transpose(toColVector(rowVector));
    }

    /**
     * Calculate dot product of two vectors
     * @param vector1 input vector 1
     * @param vector2 input vector 2
     * @return numerical calculation of dot product
     */
    public static double dotProduct(double[] vector1, double[] vector2) {
        return vector1[0] * vector2[0] + vector1[1] * vector2[1];
    }

    /**
     * Calculate angle between two vectors.
     * <br /><b>Note: Will only return values between 0 and PI</b>
     * @param vector1
     * @param vector2
     * @return angle between 0 and PI
     */
    public static double angleBetween(double[] vector1, double[] vector2) {
        double dot = dotProduct(vector1, vector2);

        // tan pi/2 = undefined...
        // return the actual value of tan pi/2 before the error occurs
        if (dot == 0) {    
            return PI / 2;
        }

        return Math.acos(dot / (magnitude(vector1) * magnitude(vector2)));
    }

    /**
     * Calculate angle between x axis and a vector or point
     * @param vector
     * @return angle between 0 and 2PI
     */
    public static double angle(double[] vector) {
    	//just calculate the angle between standard line and vector
        double[] baseline = {1, 0};
        double angle = angleBetween(vector, baseline);

        if (vector[1] <= baseline[1]) {
            angle *= -1;
        }

        return angle;
    }

    /**
     * Length of a vector
     * @param vector
     * @return numeric value of a vector's length
     */
    public static double magnitude(double[] vector) {
        double value = 0;

        for (int i = 0; i < vector.length; i++) {
            value += vector[i] * vector[i];
        }

        return Math.sqrt(value);
    }

    /**
     * Multiply two row vectors in non formatted form.
     * <br />This is just a bit faster than using formatted form
     * @param vector1
     * @param vector2
     * @return
     */
    public static double[] multiply(double[] vector1, double vector2[]) {
        assert vector1.length == vector2.length;

        double[] n = new double[1];

        for (int i = 0; i < n.length; i++) {
            n[i] = 0;

            for (int j = 0; j < vector1.length; j++) {
                n[i] += vector1[j] * vector2[j];
            }
        }

        return n;
    }

    /**
     * Multiply any two matrices.
     * @param matrix1
     * @param matrix2
     * @return
     */
    public static double[][] multiply(double[][] matrix1, double[][] matrix2) {
        if ((matrix1[0].length == matrix2.length) != true) {
            return null;
        }

        //row,col
        double[][] newMatrix = new double[matrix1.length][matrix2[0].length];

        for (int r = 0; r < newMatrix.length; r++) {
            for (int c = 0; c < newMatrix[0].length; c++) {
                newMatrix[r][c] = 0;
                for (int i = 0; i < matrix1[0].length; i++) {
                    newMatrix[r][c] += matrix1[r][i] * matrix2[i][c];
                }
            }
        }

        return newMatrix;
    }

    /**
     * Multiply vector by a matrix
     * @param vector a vector
     * @param matrix a matrix
     * @return
     */
    public static double[] multiply(double[] vector, double[][] matrix) {
        assert vector.length == matrix[0].length;

        double[] newVector = new double[vector.length];

        for (int i = 0; i < newVector.length; i++) {
            newVector[i] = 0;

            for (int j = 0; j < matrix.length; j++) {
                newVector[i] += vector[j] * matrix[i][j];
            }
        }

        return newVector;
    }

    /**
     * Multiply a matrix by a scalar
     * @param matrix any matrix
     * @param scalar any scalar
     * @return
     */
    public static double[][] multiply(double[][] matrix, double scalar) {
        double[][] newMatrix = matrix;

        for (int r = 0; r < newMatrix.length; r++) {
            for (int c = 0; c < newMatrix[0].length; c++) {
                newMatrix[r][c] *= scalar;
            }
        }

        return newMatrix;
    }

    /**
     * Multiply a vector by a scalar
     * @param vector any vector
     * @param scalar any scalar
     */
    public static double[] multiply(double[] vector, double scalar) {
        double[] newVector = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            newVector[i] = vector[i] * scalar;
        }

        return newVector;
    }

    /**
     * Generate a rotation matrix
     * @param angle of rotation
     * @return a new rotation matrix
     */
    public static double[][] rotation(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double[][] rotationMatrix = {
            {cos, -sin}, {sin, cos},};

        return rotationMatrix;
    }

    /**
     * Scale a matrix to a larger size
     * @param magnitude
     * @return a new scale matrix
     */
    public static double[][] scale(double magnitude) {
        return stretch(magnitude, magnitude);
    }

    public static double[][] stretch(double x, double y) {
        double[][] n = {
            {x, 0},
            {0, y}
        };
        return n;
    }

    /**
     * Shear matrix on x axis
     * @param shear
     * @return a new shear matrix
     */
    public static double[][] shearY(double shear) {
        double[][] n = {
            {1, 0}, {shear, 1},};

        return n;
    }

    /**
     * Shear matrix on y axis
     * @param shear
     * @return a new shear matrix
     */
    public static double[][] shearX(double shear) {
        double[][] n = {
            {1, shear}, {0, 1},};

        return n;
    }

    /**
     * flip a matrix in the x direction
     * @return new transform matrix
     */
    public static double[][] flipX() {
        double[][] n = {
            {-1, 0}, {0, 1},};

        return n;
    }

    /**
     * flip a matrix in the y direction
     * @return new transform matrix
     */
    public static double[][] flipY() {
        double[][] n = {
            {1, 0}, {0, -1},};

        return n;
    }

    /**
     * Find and return the determinant of a matrix
     * @param m matrix
     * @return determinant
     */
    public static double determinant(double[][] matrix) {
        //trace(matrix);
        assert matrix.length == matrix[0].length;

        if (matrix.length == 2) {
            double det = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
            return det;
        } else {
            double det = 0;
            double temp = 0;

            for (int col = 0; col < matrix.length; col++) {
                temp = 1;

                for (int h = 0; h < matrix.length; h++) {
                    temp *= matrix[h][(col + h) % matrix.length];
                }

                det += temp;
            }

            for (int col = 0; col < matrix.length; col++) {
                temp = 1;

                for (int h = 0; h < matrix.length; h++) {
                    temp *= matrix[h][(matrix.length + (col - h)) % matrix.length];
                }

                det -= temp;
            }

            return det;
        }
    }

    public static double[][] invert(double[][] matrix) {
        assert isSquare(matrix);

        if (determinant(matrix) != 0) {
            double[][] newMatrix = multiply(adjugate(matrix), 1 / determinant(matrix));
            return newMatrix;
        }
        //the matrix is not invertable
        return null;
    }

    public static double[][] adjugate(double[][] m) {

        double[][] newMatrix = transpose(m);
        int[] coords = new int[2];
        for (int r = 0; r < newMatrix.length; r++) {
            for (int c = 0; c < newMatrix.length; c++) {
                coords[0] = c; coords[1] = r; newMatrix[r][c] = determinant(minor(coords, m)); }
        }

        int i = -1;
        for (int r = 0; r < newMatrix.length; r++) {
            for (int c = 0; c < newMatrix[0].length; c++) {
                i *= -1;
                newMatrix[r][c] *= i;
            }
        }
        return newMatrix;
    }

    public static double[][] minor(int[] coords, double[][] m) {
        double[][] minor = new double[2][2];
        int rows = 0;
        int cols = 0;

        for (int r = 0; r < m.length; r++) {
            if (r != coords[0]) {
                for (int c = 0; c < m[0].length; c++) {
                    if (c != coords[1]) {
                        minor[rows][cols] = m[r][c];
                        cols++;
                        if (cols >= 2) {
                            cols = 0;
                            rows++;
                        }
                    }
                }
            }
        }

        return minor;
    }

    /**
     * Reflect data in an axis
     * @param angle angle of line of reflection
     * @return
     */
    public static double[][] reflection(double angle) {

        double sin = Math.sin(2 * angle);
        double cos = Math.cos(2 * angle);

        double[][] n = {
            {cos, sin}, {sin, -cos}
        };

        return n;

    }

    /**
     * Add two matrices
     * @param matrix1
     * @param matrix2
     * @return Summative matrix of A and B
     */
    public static double[][] add(double[][] matrix1, double[][] matrix2) {
        assert (matrix1.length == matrix2.length) && (matrix1[0].length == matrix2[0].length);

        double[][] newMatrix = matrix1;

        for (int r = 0; r < newMatrix.length; r++) {
            for (int c = 0; c < newMatrix[0].length; c++) {
                newMatrix[r][c] += matrix2[r][c];
            }
        }

        return newMatrix;
    }

    /**
     * Add any two vectors or points
     * @param vector1
     * @param vector2
     * @return The new point
     */
    public static double[] add(double[] vector1, double[] vector2) {
        assert vector1.length == vector2.length;

        double[] newVector = vector1.clone();

        for (int i = 0; i < newVector.length; i++) {
            newVector[i] += vector2[i];
        }

        return newVector;
    }

    /**
     * Subtract vector a from vector b
     * @param a
     * @param b
     * @return new vector (a-b)
     */
    public static double[] subtract(double[] a, double[] b) {
        assert a.length == b.length;

        double[] n = a.clone();

        for (int i = 0; i < n.length; i++) {
            n[i] -= b[i];
        }

        return n;
    }

    /**
     * Subtract matrix a from matrix b
     * @param a
     * @param b
     * @return new matrix
     */
    public static double[][] subtract(double[][] a, double[][] b) {
        assert (a.length == b.length) && (a[0].length == b[0].length);

        double[][] n = a;

        for (int r = 0; r < n.length; r++) {
            for (int c = 0; c < n[0].length; c++) {
                n[r][c] -= b[r][c];
            }
        }

        return n;
    }

    /**
     * get width of a matrix
     * @param m matrix
     * @return
     */
    public static int width(double[][] m) {
        return m[0].length;
    }

    /**
     * get height of a matrix
     * @param m matrix
     * @return
     */
    public static int height(double[][] m) {
        return m.length;
    }

    /**
     * Check if a matrix is square or not.
     * @param m
     * @return
     */
    public static boolean isSquare(double[][] m) {
        return (m.length == m[0].length);
    }

    /**
     * Generate transpose of any row vector m
     * @param m
     * @return
     */
    public static double[][] transpose(double[] m) {
        return toColVector(m);
    }

    /**
     * Generate transpose matrix of any input matrix
     * @param m
     * @return
     */
    public static double[][] transpose(double[][] m) {
        double[][] n = new double[m[0].length][m.length];

        for (int r = 0; r < n.length; r++) {
            for (int c = 0; c < n[0].length; c++) {
                n[r][c] = m[c][r];
            }
        }

        return n;
    }

    /**
     * Check if two matrices are equal
     * @param matrix1
     * @param matrix2
     * @return
     */
    public static boolean isEqual(double[][] matrix1, double[][] matrix2) {
        if ((matrix1.length != matrix2.length) || (matrix1[0].length != matrix2[0].length)) {
            return false;
        }

        for (int r = 0; r < matrix1.length; r++) {
            for (int c = 0; c < matrix1[0].length; c++) {
                if (matrix1[r][c] != matrix2[r][c]) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check if any matrix is symmetrical
     * @param m
     * @return
     */
    public static boolean symmetric(double[][] m) {
        return isEqual(m, transpose(m));
    }

    public static double squaredDistance(double[] a, double[] b){
    	return Math.pow(b[0]-a[0],2) + Math.pow(b[1]-a[1],2);
    }

    public static double distance(double[] a, double[] b) {

        double d = Math.sqrt(squaredDistance(a,b));

        return d;
    }

//MACRO

    public static double[] shiftPoint(double[] originalPoint, double[] amount) {
        double[] n = {originalPoint[0] + amount[0], originalPoint[1] + amount[1]};

        return n;
    }

    public static double[] unshiftPoint(double[] originalPoint, double[] amount) {
        double[] n = {originalPoint[0] - amount[0], originalPoint[1] - amount[1]};

        return n;
    }

    public static double[] rotatePoint(double[] point, double angle) {
        double a = angle(point);
        double[][] r = rotation(angle);

        return multiply(point, r);
    }

    public static double[] scalePoint(double[] point, double magnitude) {
        return multiply(point, scale(magnitude));
    }

    public static double[] stretchPoint(double[] point, double x, double y) {
        return multiply(point, stretch(x, y));
    }

    public static double[][] rotatePoly(double[][] poly, double angle) {
        double[][] r = rotation(angle);
        double[][] n = poly;

        for (int i = 0; i < n.length; i++) {
            n[i] = multiply(n[i], r);
        }

        return n;
    }

    public static double[][] scalePoly(double[][] poly, double amount) {
        return stretchPoly(poly, amount, amount);
    }

    public static double[][] stretchPoly(double[][] poly, double x, double y) {
        double[][] n = poly;

        for (int i = 0; i < n.length; i++) {
            n[i] = stretchPoint(n[i], x, y);
        }

        return n;
    }

    public static double[][] shiftPoly(double[][] poly, double[] amount) {
        double[][] n = poly;

        for (int i = 0; i < n.length; i++) {
            n[i] = shiftPoint(n[i], amount);
        }

        return n;
    }

    public static double[][] unshiftPoly(double[][] poly, double[] amount) {
        double[][] n = poly;

        for (int i = 0; i < n.length; i++) {
            n[i] = unshiftPoint(n[i], amount);
        }

        return n;
    }


    public static double angleBetweenPoints(double[] p1, double[] p2) {
        double[] n = subtract(p1, p2);

        return angle(n);
    }

    public static double[] moveByAngle(double angle, double distance) {
        double[] n = new double[2];
        n[0] = distance * Math.cos(angle);
        n[1] = distance * Math.sin(angle);

        return n;
    }

    public static double [] polarToVector(double angle,double distance){
        return moveByAngle(angle,distance);
    }	

}