package game.map.weather;

import game.map.noise.SimplexNoise;
import game.math.MathUtils;
import game.math.Vec2D;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import main.Game;

public class Cloud {

    public static final double CLOUD_SPEED = 0.15;

    private List<CloudPart> parts = new ArrayList<>();
    Vec2D velocity = new Vec2D();
    private boolean raining;

    private final int cloudWidth;

    private final int cloudHeight;

    // 2354 is random number to deviate from norm, but must be same every
    // time

    private Cloud(final List<CloudPart> cloudParts, final Vec2D velocity, final int cloudWidth, final int cloudHeight) {
	setParts(cloudParts);
	this.velocity = velocity;
	this.cloudWidth = cloudWidth;
	this.cloudHeight = cloudHeight;
    }

    public void update(final Duration delta) {
	final double deltaTime = delta.toMinutes() / 60f;

	for (int i = 0; i < getParts().size(); i++) {
	    final CloudPart point = getParts().get(i);
	    point.setPosition(point.getX() + velocity.x * deltaTime, point.getY() + velocity.y * deltaTime);
	}
    }

    public boolean isOffScreen(final int width, final int height) {
	for (final CloudPart point : getParts()) {
	    if (point.getX() >= 0 && point.getX() <= width && point.getY() >= 0 && point.getY() < height)
		return false;
	}
	return true;
    }

    static int i = 0;

    public static Cloud createNew(final int cloudWidth, final int cloudHeight, final int screenWidth, final int screenHeight) {

	final SimplexNoise CLOUD_NOISE = new SimplexNoise(10, 0.4, Game.RAND_SEED + 2354 + i++);
	double cloudX, cloudY;
	Vec2D velocity;

	final double rand = Math.random();
	if (rand < 0.25) {
	    // top
	    cloudX = MathUtils.randBetween(0, screenWidth - 1);
	    cloudY = 0 - cloudHeight;
	    velocity = new Vec2D(0, CLOUD_SPEED);
	} else if (rand < 0.5) {
	    // bottom
	    cloudX = MathUtils.randBetween(0, screenWidth - 1);
	    cloudY = screenHeight - 1;
	    velocity = new Vec2D(0, -CLOUD_SPEED);
	} else if (rand < 0.75) {
	    // right
	    cloudX = screenWidth - 1;
	    cloudY = MathUtils.randBetween(0, screenHeight - 1);
	    velocity = new Vec2D(-CLOUD_SPEED, 0);
	} else {
	    // Left
	    cloudX = 0 - cloudWidth;
	    cloudY = MathUtils.randBetween(0, screenHeight - 1);
	    velocity = new Vec2D(CLOUD_SPEED, 0);
	}
	// velocity = new Vec2D(0, 0);

	double min = Double.MAX_VALUE, max = Double.MIN_VALUE;

	final double[][] results = new double[cloudWidth][cloudHeight];

	final List<CloudPart> cloudParts = new ArrayList<>();

	for (int i = 0; i < cloudWidth; i++) {
	    for (int j = 0; j < cloudHeight; j++) {
		results[i][j] = (CLOUD_NOISE.getNoise(i, j) + 1) / 2;
		if (results[i][j] < min) {
		    min = results[i][j];
		}
		if (results[i][j] > max) {
		    max = results[i][j];
		}
	    }
	}
	for (int i = 0; i < cloudWidth; i++) {
	    for (int j = 0; j < cloudHeight; j++) {
		final double result = MathUtils.mapNumInRange(results[i][j], min, max, 0, 1);

		final double threshold = 0.5;

		if (result > threshold) {
		    // its always above the threshold, so re-map it from the
		    // (threshold to 1) to (0 to 1)
		    cloudParts.add(new CloudPart(i + cloudX, j + cloudY, MathUtils.mapNum0to1(result, threshold, 1), i, j));
		}
	    }
	}
	return new Cloud(cloudParts, velocity, cloudWidth, cloudHeight);
    }

    public List<CloudPart> getParts() {
	return parts;
    }

    public void setParts(final List<CloudPart> parts) {
	this.parts = parts;
    }

    public static class CloudPart {

	// Position on the GameMap
	double mapX, mapY;
	// How close to the "eye" of the storm
	private double intensity;
	private double waterContent;
	private final int cloudX;
	private final int cloudY;

	public CloudPart(final double mapX, final double mapY, final double intensity, final int cloudX, final int cloudY) {
	    setPosition(mapX, mapY);
	    setIntensity(intensity);
	    setWaterContent(0);
	    this.cloudX = cloudX;
	    this.cloudY = cloudY;
	}

	public double getX() {
	    return mapX;
	}

	public double getY() {
	    return mapY;
	}

	public void setPosition(final double mapX, final double mapY) {
	    this.mapX = mapX;
	    this.mapY = mapY;
	}

	public double getIntensity() {
	    return intensity;
	}

	public void setIntensity(final double intensity) {
	    this.intensity = intensity;
	}

	public double getWaterContent() {
	    return waterContent;
	}

	public void setWaterContent(double waterContent) {
	    if (waterContent > 1) {
		waterContent = 1;
	    } else if (waterContent < 0) {
		waterContent = 0;
	    }
	    this.waterContent = waterContent;
	}

    }

    public boolean isRaining() {
	return raining;
    }

    public void setRaining(final boolean raining) {
	this.raining = raining;
    }

    public int getWidth() {
	return cloudWidth;
    }

    public int getHeight() {
	return cloudHeight;
    }

    public void setPosition(final double cloudX, final double cloudY) {
	for (final CloudPart p : getParts()) {
	    p.setPosition(cloudX + p.cloudX, cloudY + p.cloudY);
	}
    }

    public void setVelocity(final Vec2D velocity2) {
	velocity = velocity2;
    }
}
