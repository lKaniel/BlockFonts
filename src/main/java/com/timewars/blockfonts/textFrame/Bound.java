package com.timewars.blockfonts.textFrame;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import java.util.Objects;

public class Bound {

	private int x;
	private int y;
	private int z;
	private int x2;
	private int y2;
	private int z2;
	private String world;

	public Bound(String world, int x, int y, int z, int x2, int y2, int z2) {
		this.world = world;
		this.x = Math.min(x,x2);
		this.y = Math.min(y, y2);
		this.z = Math.min(z, z2);
		this.x2 = Math.max(x,x2);
		this.y2 = Math.max(y, y2);
		this.z2 = Math.max(z, z2);
	}

    public Bound(Location location, Location location2) {
        this(Objects.requireNonNull(location.getWorld()).getName(), ((int) location.getX()), ((int) location.getY()),
                ((int) location.getZ()), ((int) location2.getX()), ((int) location2.getY()), ((int) location2.getZ()));
    }

	public World getWorld() {
		return Bukkit.getWorld(world);
	}

	public Location getGreaterCorner() {
		return new Location(Bukkit.getWorld(world), x, y, z);
	}

	public Location getLesserCorner() {
		return new Location(Bukkit.getWorld(world), x2, y2, z2);
	}

	@Override
	public String toString() {
		return "Bound{" +
				"x=" + x +
				", y=" + y +
				", z=" + z +
				", x2=" + x2 +
				", y2=" + y2 +
				", z2=" + z2 +
				", world='" + world + '\'' +
				'}';
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	public int getZ2() {
		return z2;
	}
}
