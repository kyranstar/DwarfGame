package game.map.weather;

import game.map.GameMap;
import game.map.weather.Biome.TempRange;
import game.map.weather.Cloud.CloudPart;
import game.math.MathUtils;
import game.math.Vec2D;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Weather {

    private final GameMap map;
    private List<Cloud> clouds = new ArrayList<>();

    private static final double RAIN_PICKUP_RATE = 0.001;
    private static final double RAIN_RATE = 0.03;

    public Weather(final GameMap map, final int clouds) {
	this.map = map;
	for (int i = 0; i < clouds; i++) {
	    getClouds().add(Cloud.createNew(MathUtils.randBetween(20, 60), MathUtils.randBetween(10, 20), map.getWidth(), map.getHeight()));
	}
    }

    /**
     * Updates all of the clouds this weather object contains. This method is in
     * charge of updating the water content of the clouds and how often they
     * rain.
     *
     * @param deltaTime
     */
    public void update(final Duration delta) {
	map.clearRainingIntensity();

	final double deltaTime = delta.toMinutes() / 60f;

	for (int i = getClouds().size() - 1; i >= 0; i--) {
	    final Cloud cloud = getClouds().get(i);
	    cloud.update(delta);
	    if (!cloud.isRaining()) {
		for (final CloudPart p : cloud.getParts()) {
		    int x = (int) p.getX();
		    int y = (int) p.getY();
		    if (x < 0) {
			x = 0;
		    }
		    if (y < 0) {
			y = 0;
		    }
		    if (x > map.getWidth() - 1) {
			x = map.getWidth() - 1;
		    }
		    if (y > map.getHeight() - 1) {
			y = map.getHeight() - 1;
		    }

		    final double averagePrecipitation = MathUtils.mapNum0to1(map.getAveragePrecipitation()[x][y], TempRange.getMin(), TempRange.getMax());
		    double pickedUp = RAIN_PICKUP_RATE * p.getIntensity() * averagePrecipitation;
		    if (map.getEvaporatedWater()[x][y] <= 0) {
			map.getEvaporatedWater()[x][y] = 0;
			pickedUp = 0;
		    } else if (map.getEvaporatedWater()[x][y] - pickedUp <= 0) {
			pickedUp = map.getEvaporatedWater()[x][y];
			map.getEvaporatedWater()[x][y] = 0;
		    } else {
			map.getEvaporatedWater()[x][y] -= pickedUp;
		    }
		    // + (1 - p.getIntensity()) * Math.random() / 15; // This
		    // would add extra rising on the sides
		    p.setWaterContent(Math.min(p.getWaterContent() + pickedUp * deltaTime, 1));

		    // TODO update cloud from the surrounding clouds
		    if (p.getWaterContent() >= 1) {
			cloud.setRaining(true);
		    }
		}
	    } else {
		int rainingCount = 0;
		for (final CloudPart p : cloud.getParts()) {
		    int x = (int) p.getX();
		    int y = (int) p.getY();
		    if (x < 0) {
			x = 0;
		    }
		    if (y < 0) {
			y = 0;
		    }
		    if (x > map.getWidth() - 1) {
			x = map.getWidth() - 1;
		    }
		    if (y > map.getHeight() - 1) {
			y = map.getHeight() - 1;
		    }
		    double amountToLose = RAIN_RATE * (Math.random() * p.getIntensity() * deltaTime);
		    if (p.getWaterContent() - amountToLose < 0) {
			amountToLose = p.getWaterContent();
		    }
		    p.setWaterContent(p.getWaterContent() - amountToLose);
		    if (p.getWaterContent() > 0) {
			map.getRainingIntensity()[x][y] = amountToLose;
			rainingCount++;
		    }
		}
		// To make sure there isn't one lonely piece of the cloud
		// raining indefinitely
		final double rainingRatio = rainingCount / cloud.getParts().size();

		if (rainingRatio <= 0.5) {
		    for (final CloudPart p : cloud.getParts()) {
			p.setWaterContent(0);
		    }
		    cloud.setRaining(false);
		}
	    }

	    if (cloud.isOffScreen(map.getWidth(), map.getHeight())) {
		double cloudX, cloudY;
		Vec2D velocity;

		final double rand = Math.random();
		if (rand < 0.25) {
		    // top
		    cloudX = MathUtils.randBetween(0, map.getWidth() - 1);
		    cloudY = 0 - cloud.getHeight();
		    velocity = new Vec2D(0, Cloud.BASE_CLOUD_SPEED);
		} else if (rand < 0.5) {
		    // bottom
		    cloudX = MathUtils.randBetween(0, map.getWidth() - 1);
		    cloudY = map.getHeight() - 1;
		    velocity = new Vec2D(0, -Cloud.BASE_CLOUD_SPEED);
		} else if (rand < 0.75) {
		    // right
		    cloudX = map.getWidth() - 1;
		    cloudY = MathUtils.randBetween(0, map.getHeight() - 1);
		    velocity = new Vec2D(-Cloud.BASE_CLOUD_SPEED, 0);
		} else {
		    // Left
		    cloudX = 0 - cloud.getWidth();
		    cloudY = MathUtils.randBetween(0, map.getHeight() - 1);
		    velocity = new Vec2D(Cloud.BASE_CLOUD_SPEED, 0);
		}
		cloud.setPosition(cloudX, cloudY);
		cloud.setVelocity(velocity);
	    }
	}
    }

    public List<Cloud> getClouds() {
	return clouds;
    }

    public void setClouds(final List<Cloud> clouds) {
	this.clouds = clouds;
    }
}
