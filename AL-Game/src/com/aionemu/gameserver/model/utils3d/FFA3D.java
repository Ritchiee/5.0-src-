package com.aionemu.gameserver.model.utils3d;


/**
 * @author Goong ADM
 */
public class FFA3D {

	public double x;
	public double y;
	public double z;
	public int h;
	
	public FFA3D() {
		this.x = 0.0;
		this.y = 0.0;
		this.z = 0.0;
	}

	public FFA3D(double x, double y, double z, int h) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.h = h;
	}

	public FFA3D(float x, float y, float z, int h) {
		this.x = (double) x;
		this.y = (double) y;
		this.z = (double) z;
		this.h = (int) h;
	}


	@Override
	public String toString() {
		return "x=" + x + ", y=" + y + ", z=" + z;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
}
