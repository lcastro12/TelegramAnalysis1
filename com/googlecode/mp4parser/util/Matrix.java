package com.googlecode.mp4parser.util;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import java.nio.ByteBuffer;

public class Matrix {
    public static final Matrix ROTATE_0 = new Matrix(1.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d);
    public static final Matrix ROTATE_180 = new Matrix(-1.0d, 0.0d, 0.0d, -1.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d);
    public static final Matrix ROTATE_270 = new Matrix(0.0d, -1.0d, 1.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d);
    public static final Matrix ROTATE_90 = new Matrix(0.0d, 1.0d, -1.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d);
    double f5a;
    double f6b;
    double f7c;
    double f8d;
    double tx;
    double ty;
    double f9u;
    double f10v;
    double f11w;

    public Matrix(double a, double b, double c, double d, double u, double v, double w, double tx, double ty) {
        this.f9u = u;
        this.f10v = v;
        this.f11w = w;
        this.f5a = a;
        this.f6b = b;
        this.f7c = c;
        this.f8d = d;
        this.tx = tx;
        this.ty = ty;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Matrix matrix = (Matrix) o;
        if (Double.compare(matrix.f5a, this.f5a) != 0) {
            return false;
        }
        if (Double.compare(matrix.f6b, this.f6b) != 0) {
            return false;
        }
        if (Double.compare(matrix.f7c, this.f7c) != 0) {
            return false;
        }
        if (Double.compare(matrix.f8d, this.f8d) != 0) {
            return false;
        }
        if (Double.compare(matrix.tx, this.tx) != 0) {
            return false;
        }
        if (Double.compare(matrix.ty, this.ty) != 0) {
            return false;
        }
        if (Double.compare(matrix.f9u, this.f9u) != 0) {
            return false;
        }
        if (Double.compare(matrix.f10v, this.f10v) != 0) {
            return false;
        }
        if (Double.compare(matrix.f11w, this.f11w) != 0) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.f9u);
        int result = (int) ((temp >>> 32) ^ temp);
        temp = Double.doubleToLongBits(this.f10v);
        result = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        temp = Double.doubleToLongBits(this.f11w);
        result = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        temp = Double.doubleToLongBits(this.f5a);
        result = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        temp = Double.doubleToLongBits(this.f6b);
        result = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        temp = Double.doubleToLongBits(this.f7c);
        result = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        temp = Double.doubleToLongBits(this.f8d);
        result = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        temp = Double.doubleToLongBits(this.tx);
        result = (result * 31) + ((int) ((temp >>> 32) ^ temp));
        temp = Double.doubleToLongBits(this.ty);
        return (result * 31) + ((int) ((temp >>> 32) ^ temp));
    }

    public String toString() {
        if (equals(ROTATE_0)) {
            return "Rotate 0°";
        }
        if (equals(ROTATE_90)) {
            return "Rotate 90°";
        }
        if (equals(ROTATE_180)) {
            return "Rotate 180°";
        }
        if (equals(ROTATE_270)) {
            return "Rotate 270°";
        }
        return "Matrix{u=" + this.f9u + ", v=" + this.f10v + ", w=" + this.f11w + ", a=" + this.f5a + ", b=" + this.f6b + ", c=" + this.f7c + ", d=" + this.f8d + ", tx=" + this.tx + ", ty=" + this.ty + '}';
    }

    public static Matrix fromFileOrder(double a, double b, double u, double c, double d, double v, double tx, double ty, double w) {
        return new Matrix(a, b, c, d, u, v, w, tx, ty);
    }

    public static Matrix fromByteBuffer(ByteBuffer byteBuffer) {
        return fromFileOrder(IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint0230(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint0230(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint1616(byteBuffer), IsoTypeReader.readFixedPoint0230(byteBuffer));
    }

    public void getContent(ByteBuffer byteBuffer) {
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.f5a);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.f6b);
        IsoTypeWriter.writeFixedPoint0230(byteBuffer, this.f9u);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.f7c);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.f8d);
        IsoTypeWriter.writeFixedPoint0230(byteBuffer, this.f10v);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.tx);
        IsoTypeWriter.writeFixedPoint1616(byteBuffer, this.ty);
        IsoTypeWriter.writeFixedPoint0230(byteBuffer, this.f11w);
    }
}
