package de.alshikh.haw.tron.client.models.data.datatypes;

import de.alshikh.haw.tron.client.views.view_library.Coordinate;

public enum Direction {
	UP, DOWN, LEFT, RIGHT;

	public boolean isAllowed(Direction currentDirection) {
		switch (currentDirection) {
			case UP: case DOWN:
				return (this.equals(Direction.LEFT) || this.equals(Direction.RIGHT));
			case LEFT: case RIGHT:
				return (this.equals(Direction.UP) || this.equals(Direction.DOWN));
			default: throw new EnumConstantNotPresentException(this.getClass(), this.name());
		}
	}

	public Coordinate calculateNewPosition(Coordinate currentCoordinate) {
		switch (this) {
			case LEFT: return currentCoordinate.sub(new Coordinate(1, 0));
			case RIGHT: return currentCoordinate.add(new Coordinate(1, 0));
			case UP: return currentCoordinate.sub(new Coordinate(0, 1));
			case DOWN: return currentCoordinate.add(new Coordinate(0, 1));
			default: throw new EnumConstantNotPresentException(this.getClass(), this.name());
		}
	}
}