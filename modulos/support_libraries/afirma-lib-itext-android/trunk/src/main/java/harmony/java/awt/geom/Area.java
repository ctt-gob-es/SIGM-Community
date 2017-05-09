/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package harmony.java.awt.geom;

import harmony.java.awt.Rectangle;
import harmony.java.awt.Shape;

import java.util.NoSuchElementException;

import org.apache.harmony.awt.geom.CrossingHelper;
import org.apache.harmony.awt.geom.CurveCrossingHelper;
import org.apache.harmony.awt.geom.GeometryUtil;
import org.apache.harmony.awt.geom.IntersectPoint;
import org.apache.harmony.awt.gl.Crossing;
import org.apache.harmony.awt.internal.nls.Messages;

public class Area implements Shape, Cloneable {

	/**
	 * the coordinates array of the shape vertices
	 */
	private double coords[] = new double[20];

	/**
	 * the coordinates quantity
	 */
	private int coordsSize = 0;

	/**
	 * the rules array for the drawing of the shape edges
	 */
	private int rules[] = new int[10];

	/**
	 * the rules quantity
	 */
	private int rulesSize = 0;

	/**
	 * offsets[i] - index in array of coords and i - index in array of rules
	 */
	private int offsets[] = new int[10];

	/**
	 * the quantity of MOVETO rule occurences
	 */
	private int moveToCount = 0;

	/**
	 * true if the shape is polygon
	 */
	private boolean isPolygonal = true;

	public Area() {
	}

	public Area(final Shape s) {
		final double segmentCoords[] = new double[6];
		double lastMoveX = 0.0;
		double lastMoveY = 0.0;
		int rulesIndex = 0;
		int coordsIndex = 0;

		for (final PathIterator pi = s.getPathIterator(null); !pi.isDone(); pi.next()) {
			this.coords = adjustSize(this.coords, coordsIndex + 6);
			this.rules = adjustSize(this.rules, rulesIndex + 1);
			this.offsets = adjustSize(this.offsets, rulesIndex + 1);
			this.rules[rulesIndex] = pi.currentSegment(segmentCoords);
			this.offsets[rulesIndex] = coordsIndex;

			switch (this.rules[rulesIndex]) {
			case PathIterator.SEG_MOVETO:
				this.coords[coordsIndex++] = segmentCoords[0];
				this.coords[coordsIndex++] = segmentCoords[1];
				lastMoveX = segmentCoords[0];
				lastMoveY = segmentCoords[1];
				++this.moveToCount;
				break;
			case PathIterator.SEG_LINETO:
				if (segmentCoords[0] != lastMoveX || segmentCoords[1] != lastMoveY) {
					this.coords[coordsIndex++] = segmentCoords[0];
					this.coords[coordsIndex++] = segmentCoords[1];
				} else {
					--rulesIndex;
				}
				break;
			case PathIterator.SEG_QUADTO:
				System.arraycopy(segmentCoords, 0, this.coords, coordsIndex, 4);
				coordsIndex += 4;
				this.isPolygonal = false;
				break;
			case PathIterator.SEG_CUBICTO:
				System.arraycopy(segmentCoords, 0, this.coords, coordsIndex, 6);
				coordsIndex += 6;
				this.isPolygonal = false;
				break;
			case PathIterator.SEG_CLOSE:
				break;
			}
			++rulesIndex;
		}

		if (rulesIndex != 0 && this.rules[rulesIndex - 1] != PathIterator.SEG_CLOSE) {
			this.rules[rulesIndex] = PathIterator.SEG_CLOSE;
			this.offsets[rulesIndex] = this.coordsSize;
		}

		this.rulesSize = rulesIndex;
		this.coordsSize = coordsIndex;
	}

	@Override
	public boolean contains(final double x, final double y) {
		return !isEmpty() && containsExact(x, y) > 0;
	}

	@Override
	public boolean contains(final double x, final double y, final double width, final double height) {
		final int crossCount = Crossing.intersectPath(getPathIterator(null), x, y, width, height);
		return crossCount != Crossing.CROSSING && Crossing.isInsideEvenOdd(crossCount);
	}

	@Override
	public boolean contains(final Point2D p) {
		return contains(p.getX(), p.getY());
	}

	@Override
	public boolean contains(final Rectangle2D r) {
		return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}

	public boolean equals(final Area obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		final Area area = (Area) clone();
		area.subtract(obj);
		return area.isEmpty();
	}

	@Override
	public boolean intersects(final double x, final double y, final double width, final double height) {
		if (width <= 0.0 || height <= 0.0) {
			return false;
		} else if (!getBounds2D().intersects(x, y, width, height)) {
			return false;
		}

		final int crossCount = Crossing.intersectShape(this, x, y, width, height);
		return Crossing.isInsideEvenOdd(crossCount);
	}

	@Override
	public boolean intersects(final Rectangle2D r) {
		return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}

	@Override
	public Rectangle getBounds() {
		return getBounds2D().getBounds();
	}

	@Override
	public Rectangle2D getBounds2D() {
		double maxX = this.coords[0];
		double maxY = this.coords[1];
		double minX = this.coords[0];
		double minY = this.coords[1];

		for (int i = 0; i < this.coordsSize;) {
			minX = Math.min(minX, this.coords[i]);
			maxX = Math.max(maxX, this.coords[i++]);
			minY = Math.min(minY, this.coords[i]);
			maxY = Math.max(maxY, this.coords[i++]);
		}

		return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
	}

	@Override
	public PathIterator getPathIterator(final AffineTransform t) {
		return new AreaPathIterator(this, t);
	}

	@Override
	public PathIterator getPathIterator(final AffineTransform t, final double flatness) {
		return new FlatteningPathIterator(getPathIterator(t), flatness);
	}

	public boolean isEmpty() {
		return this.rulesSize == 0 && this.coordsSize == 0;
	}

	public boolean isPolygonal() {
		return this.isPolygonal;
	}

	public boolean isRectangular() {
		return this.isPolygonal && this.rulesSize <= 5 && this.coordsSize <= 8 && this.coords[1] == this.coords[3]
				&& this.coords[7] == this.coords[5] && this.coords[0] == this.coords[6] && this.coords[2] == this.coords[4];
	}

	public boolean isSingular() {
		return this.moveToCount <= 1;
	}

	public void reset() {
		this.coordsSize = 0;
		this.rulesSize = 0;
	}

	public void transform(final AffineTransform t) {
		copy(new Area(t.createTransformedShape(this)), this);
	}

	public Area createTransformedArea(final AffineTransform t) {
		return new Area(t.createTransformedShape(this));
	}

	@Override
	public Object clone() {
		final Area area = new Area();
		copy(this, area);
		return area;
	}

	public void add(final Area area) {
		if (area == null || area.isEmpty()) {
			return;
		} else if (isEmpty()) {
			copy(area, this);
			return;
		}

		if (isPolygonal() && area.isPolygonal()) {
			addPolygon(area);
		} else {
			addCurvePolygon(area);
		}

		if (getAreaBoundsSquare() < GeometryUtil.EPSILON) {
			reset();
		}
	}

	public void intersect(final Area area) {
		if (area == null) {
			return;
		} else if (isEmpty() || area.isEmpty()) {
			reset();
			return;
		}

		if (isPolygonal() && area.isPolygonal()) {
			intersectPolygon(area);
		} else {
			intersectCurvePolygon(area);
		}

		if (getAreaBoundsSquare() < GeometryUtil.EPSILON) {
			reset();
		}
	}

	public void subtract(final Area area) {
		if (area == null || isEmpty() || area.isEmpty()) {
			return;
		}

		if (isPolygonal() && area.isPolygonal()) {
			subtractPolygon(area);
		} else {
			subtractCurvePolygon(area);
		}

		if (getAreaBoundsSquare() < GeometryUtil.EPSILON) {
			reset();
		}
	}

	public void exclusiveOr(final Area area) {
		final Area a = (Area) clone();
		a.intersect(area);
		add(area);
		subtract(a);
	}

	private void addCurvePolygon(final Area area) {
		final CurveCrossingHelper crossHelper = new CurveCrossingHelper(new double[][] { this.coords, area.coords }, new int[] {
				this.coordsSize, area.coordsSize }, new int[][] { this.rules, area.rules },
				new int[] { this.rulesSize, area.rulesSize }, new int[][] { this.offsets, area.offsets });
		final IntersectPoint[] intersectPoints = crossHelper.findCrossing();

		if (intersectPoints.length == 0) {
			if (area.contains(getBounds2D())) {
				copy(area, this);
			} else if (!contains(area.getBounds2D())) {
				this.coords = adjustSize(this.coords, this.coordsSize + area.coordsSize);
				System.arraycopy(area.coords, 0, this.coords, this.coordsSize, area.coordsSize);
				this.coordsSize += area.coordsSize;
				this.rules = adjustSize(this.rules, this.rulesSize + area.rulesSize);
				System.arraycopy(area.rules, 0, this.rules, this.rulesSize, area.rulesSize);
				this.rulesSize += area.rulesSize;
				this.offsets = adjustSize(this.offsets, this.rulesSize + area.rulesSize);
				System.arraycopy(area.offsets, 0, this.offsets, this.rulesSize, area.rulesSize);
			}

			return;
		}

		final double[] resultCoords = new double[this.coordsSize + area.coordsSize + intersectPoints.length];
		final int[] resultRules = new int[this.rulesSize + area.rulesSize + intersectPoints.length];
		final int[] resultOffsets = new int[this.rulesSize + area.rulesSize + intersectPoints.length];
		int resultCoordPos = 0;
		int resultRulesPos = 0;
		boolean isCurrentArea = true;

		IntersectPoint point = intersectPoints[0];
		resultRules[resultRulesPos] = PathIterator.SEG_MOVETO;
		resultOffsets[resultRulesPos++] = resultCoordPos;

		do {
			resultCoords[resultCoordPos++] = point.getX();
			resultCoords[resultCoordPos++] = point.getY();
			final int curIndex = point.getEndIndex(true);

			if (curIndex < 0) {
				isCurrentArea = !isCurrentArea;
			} else if (area.containsExact(this.coords[2 * curIndex], this.coords[2 * curIndex + 1]) > 0) {
				isCurrentArea = false;
			} else {
				isCurrentArea = true;
			}

			final IntersectPoint nextPoint = getNextIntersectPoint(intersectPoints, point, isCurrentArea);
			final double[] coords = isCurrentArea ? this.coords : area.coords;
			final int[] offsets = isCurrentArea ? this.offsets : area.offsets;
			final int[] rules = isCurrentArea ? this.rules : area.rules;
			int offset = point.getRuleIndex(isCurrentArea);
			boolean isCopyUntilZero = false;

			if (point.getRuleIndex(isCurrentArea) > nextPoint.getRuleIndex(isCurrentArea)) {
				final int rulesSize = isCurrentArea ? this.rulesSize : area.rulesSize;
				resultCoordPos = includeCoordsAndRules(offset + 1, rulesSize, rules, offsets, resultRules,
						resultOffsets, resultCoords, coords, resultRulesPos, resultCoordPos, point, isCurrentArea,
						false, 0);
				resultRulesPos += rulesSize - offset - 1;
				offset = 1;
				isCopyUntilZero = true;
			}

			final int length = nextPoint.getRuleIndex(isCurrentArea) - offset + 1;

			if (isCopyUntilZero) {
				offset = 0;
			}

			resultCoordPos = includeCoordsAndRules(offset, length, rules, offsets, resultRules, resultOffsets,
					resultCoords, coords, resultRulesPos, resultCoordPos, point, isCurrentArea, true, 0);
			resultRulesPos += length - offset;
			point = nextPoint;
		} while (point != intersectPoints[0]);

		resultRules[resultRulesPos++] = PathIterator.SEG_CLOSE;
		resultOffsets[resultRulesPos - 1] = resultCoordPos;
		this.coords = resultCoords;
		this.rules = resultRules;
		this.offsets = resultOffsets;
		this.coordsSize = resultCoordPos;
		this.rulesSize = resultRulesPos;
	}

	private void addPolygon(final Area area) {
		final CrossingHelper crossHelper = new CrossingHelper(new double[][] { this.coords, area.coords }, new int[] { this.coordsSize,
				area.coordsSize });
		final IntersectPoint[] intersectPoints = crossHelper.findCrossing();

		if (intersectPoints.length == 0) {
			if (area.contains(getBounds2D())) {
				copy(area, this);
			} else if (!contains(area.getBounds2D())) {
				this.coords = adjustSize(this.coords, this.coordsSize + area.coordsSize);
				System.arraycopy(area.coords, 0, this.coords, this.coordsSize, area.coordsSize);
				this.coordsSize += area.coordsSize;
				this.rules = adjustSize(this.rules, this.rulesSize + area.rulesSize);
				System.arraycopy(area.rules, 0, this.rules, this.rulesSize, area.rulesSize);
				this.rulesSize += area.rulesSize;
				this.offsets = adjustSize(this.offsets, this.rulesSize + area.rulesSize);
				System.arraycopy(area.offsets, 0, this.offsets, this.rulesSize, area.rulesSize);
			}
			return;
		}

		final double[] resultCoords = new double[this.coordsSize + area.coordsSize + intersectPoints.length];
		final int[] resultRules = new int[this.rulesSize + area.rulesSize + intersectPoints.length];
		final int[] resultOffsets = new int[this.rulesSize + area.rulesSize + intersectPoints.length];
		int resultCoordPos = 0;
		int resultRulesPos = 0;
		boolean isCurrentArea = true;

		IntersectPoint point = intersectPoints[0];
		resultRules[resultRulesPos] = PathIterator.SEG_MOVETO;
		resultOffsets[resultRulesPos++] = resultCoordPos;

		do {
			resultCoords[resultCoordPos++] = point.getX();
			resultCoords[resultCoordPos++] = point.getY();
			resultRules[resultRulesPos] = PathIterator.SEG_LINETO;
			resultOffsets[resultRulesPos++] = resultCoordPos - 2;
			final int curIndex = point.getEndIndex(true);
			if (curIndex < 0) {
				isCurrentArea = !isCurrentArea;
			} else if (area.containsExact(this.coords[2 * curIndex], this.coords[2 * curIndex + 1]) > 0) {
				isCurrentArea = false;
			} else {
				isCurrentArea = true;
			}

			final IntersectPoint nextPoint = getNextIntersectPoint(intersectPoints, point, isCurrentArea);
			final double[] coords = isCurrentArea ? this.coords : area.coords;
			int offset = 2 * point.getEndIndex(isCurrentArea);

			if (offset >= 0 && nextPoint.getBegIndex(isCurrentArea) < point.getEndIndex(isCurrentArea)) {
				final int coordSize = isCurrentArea ? this.coordsSize : area.coordsSize;
				final int length = coordSize - offset;
				System.arraycopy(coords, offset, resultCoords, resultCoordPos, length);

				for (int i = 0; i < length / 2; i++) {
					resultRules[resultRulesPos] = PathIterator.SEG_LINETO;
					resultOffsets[resultRulesPos++] = resultCoordPos;
					resultCoordPos += 2;
				}

				offset = 0;
			}

			if (offset >= 0) {
				final int length = 2 * nextPoint.getBegIndex(isCurrentArea) - offset + 2;
				System.arraycopy(coords, offset, resultCoords, resultCoordPos, length);

				for (int i = 0; i < length / 2; i++) {
					resultRules[resultRulesPos] = PathIterator.SEG_LINETO;
					resultOffsets[resultRulesPos++] = resultCoordPos;
					resultCoordPos += 2;
				}
			}

			point = nextPoint;
		} while (point != intersectPoints[0]);

		resultRules[resultRulesPos - 1] = PathIterator.SEG_CLOSE;
		resultOffsets[resultRulesPos - 1] = resultCoordPos;
		this.coords = resultCoords;
		this.rules = resultRules;
		this.offsets = resultOffsets;
		this.coordsSize = resultCoordPos;
		this.rulesSize = resultRulesPos;
	}

	private void intersectCurvePolygon(final Area area) {
		final CurveCrossingHelper crossHelper = new CurveCrossingHelper(new double[][] { this.coords, area.coords }, new int[] {
				this.coordsSize, area.coordsSize }, new int[][] { this.rules, area.rules },
				new int[] { this.rulesSize, area.rulesSize }, new int[][] { this.offsets, area.offsets });
		final IntersectPoint[] intersectPoints = crossHelper.findCrossing();

		if (intersectPoints.length == 0) {
			if (contains(area.getBounds2D())) {
				copy(area, this);
			} else if (!area.contains(getBounds2D())) {
				reset();
			}
			return;
		}

		final double[] resultCoords = new double[this.coordsSize + area.coordsSize + intersectPoints.length];
		final int[] resultRules = new int[this.rulesSize + area.rulesSize + intersectPoints.length];
		final int[] resultOffsets = new int[this.rulesSize + area.rulesSize + intersectPoints.length];
		int resultCoordPos = 0;
		int resultRulesPos = 0;
		boolean isCurrentArea = true;

		IntersectPoint point = intersectPoints[0];
		IntersectPoint nextPoint = intersectPoints[0];
		resultRules[resultRulesPos] = PathIterator.SEG_MOVETO;
		resultOffsets[resultRulesPos++] = resultCoordPos;

		do {
			resultCoords[resultCoordPos++] = point.getX();
			resultCoords[resultCoordPos++] = point.getY();

			final int curIndex = point.getEndIndex(true);
			if (curIndex < 0 || area.containsExact(this.coords[2 * curIndex], this.coords[2 * curIndex + 1]) == 0) {
				isCurrentArea = !isCurrentArea;
			} else if (area.containsExact(this.coords[2 * curIndex], this.coords[2 * curIndex + 1]) > 0) {
				isCurrentArea = true;
			} else {
				isCurrentArea = false;
			}

			nextPoint = getNextIntersectPoint(intersectPoints, point, isCurrentArea);
			final double[] coords = isCurrentArea ? this.coords : area.coords;
			final int[] offsets = isCurrentArea ? this.offsets : area.offsets;
			final int[] rules = isCurrentArea ? this.rules : area.rules;
			int offset = point.getRuleIndex(isCurrentArea);
			boolean isCopyUntilZero = false;

			if (point.getRuleIndex(isCurrentArea) > nextPoint.getRuleIndex(isCurrentArea)) {
				final int rulesSize = isCurrentArea ? this.rulesSize : area.rulesSize;
				resultCoordPos = includeCoordsAndRules(offset + 1, rulesSize, rules, offsets, resultRules,
						resultOffsets, resultCoords, coords, resultRulesPos, resultCoordPos, point, isCurrentArea,
						false, 1);
				resultRulesPos += rulesSize - offset - 1;
				offset = 1;
				isCopyUntilZero = true;
			}

			int length = nextPoint.getRuleIndex(isCurrentArea) - offset + 1;

			if (isCopyUntilZero) {
				offset = 0;
				isCopyUntilZero = false;
			}
			if (length == offset && nextPoint.getRule(isCurrentArea) != PathIterator.SEG_LINETO
					&& nextPoint.getRule(isCurrentArea) != PathIterator.SEG_CLOSE
					&& point.getRule(isCurrentArea) != PathIterator.SEG_LINETO
					&& point.getRule(isCurrentArea) != PathIterator.SEG_CLOSE) {

				isCopyUntilZero = true;
				length++;
			}

			resultCoordPos = includeCoordsAndRules(offset, length, rules, offsets, resultRules, resultOffsets,
					resultCoords, coords, resultRulesPos, resultCoordPos, nextPoint, isCurrentArea, true, 1);
			resultRulesPos = length <= offset || isCopyUntilZero ? resultRulesPos + 1 : resultRulesPos + length;

			point = nextPoint;
		} while (point != intersectPoints[0]);

		if (resultRules[resultRulesPos - 1] == PathIterator.SEG_LINETO) {
			resultRules[resultRulesPos - 1] = PathIterator.SEG_CLOSE;
		} else {
			resultCoords[resultCoordPos++] = nextPoint.getX();
			resultCoords[resultCoordPos++] = nextPoint.getY();
			resultRules[resultRulesPos++] = PathIterator.SEG_CLOSE;
		}

		resultOffsets[resultRulesPos - 1] = resultCoordPos;
		this.coords = resultCoords;
		this.rules = resultRules;
		this.offsets = resultOffsets;
		this.coordsSize = resultCoordPos;
		this.rulesSize = resultRulesPos;
	}

	private void intersectPolygon(final Area area) {
		final CrossingHelper crossHelper = new CrossingHelper(new double[][] { this.coords, area.coords }, new int[] { this.coordsSize,
				area.coordsSize });
		final IntersectPoint[] intersectPoints = crossHelper.findCrossing();

		if (intersectPoints.length == 0) {
			if (contains(area.getBounds2D())) {
				copy(area, this);
			} else if (!area.contains(getBounds2D())) {
				reset();
			}
			return;
		}

		final double[] resultCoords = new double[this.coordsSize + area.coordsSize + intersectPoints.length];
		final int[] resultRules = new int[this.rulesSize + area.rulesSize + intersectPoints.length];
		final int[] resultOffsets = new int[this.rulesSize + area.rulesSize + intersectPoints.length];
		int resultCoordPos = 0;
		int resultRulesPos = 0;
		boolean isCurrentArea = true;

		IntersectPoint point = intersectPoints[0];
		resultRules[resultRulesPos] = PathIterator.SEG_MOVETO;
		resultOffsets[resultRulesPos++] = resultCoordPos;

		do {
			resultCoords[resultCoordPos++] = point.getX();
			resultCoords[resultCoordPos++] = point.getY();
			resultRules[resultRulesPos] = PathIterator.SEG_LINETO;
			resultOffsets[resultRulesPos++] = resultCoordPos - 2;
			final int curIndex = point.getEndIndex(true);

			if (curIndex < 0 || area.containsExact(this.coords[2 * curIndex], this.coords[2 * curIndex + 1]) == 0) {
				isCurrentArea = !isCurrentArea;
			} else if (area.containsExact(this.coords[2 * curIndex], this.coords[2 * curIndex + 1]) > 0) {
				isCurrentArea = true;
			} else {
				isCurrentArea = false;
			}

			final IntersectPoint nextPoint = getNextIntersectPoint(intersectPoints, point, isCurrentArea);
			final double[] coords = isCurrentArea ? this.coords : area.coords;
			int offset = 2 * point.getEndIndex(isCurrentArea);
			if (offset >= 0 && nextPoint.getBegIndex(isCurrentArea) < point.getEndIndex(isCurrentArea)) {
				final int coordSize = isCurrentArea ? this.coordsSize : area.coordsSize;
				final int length = coordSize - offset;
				System.arraycopy(coords, offset, resultCoords, resultCoordPos, length);

				for (int i = 0; i < length / 2; i++) {
					resultRules[resultRulesPos] = PathIterator.SEG_LINETO;
					resultOffsets[resultRulesPos++] = resultCoordPos;
					resultCoordPos += 2;
				}

				offset = 0;
			}

			if (offset >= 0) {
				final int length = 2 * nextPoint.getBegIndex(isCurrentArea) - offset + 2;
				System.arraycopy(coords, offset, resultCoords, resultCoordPos, length);

				for (int i = 0; i < length / 2; i++) {
					resultRules[resultRulesPos] = PathIterator.SEG_LINETO;
					resultOffsets[resultRulesPos++] = resultCoordPos;
					resultCoordPos += 2;
				}
			}

			point = nextPoint;
		} while (point != intersectPoints[0]);

		resultRules[resultRulesPos - 1] = PathIterator.SEG_CLOSE;
		resultOffsets[resultRulesPos - 1] = resultCoordPos;
		this.coords = resultCoords;
		this.rules = resultRules;
		this.offsets = resultOffsets;
		this.coordsSize = resultCoordPos;
		this.rulesSize = resultRulesPos;
	}

	private void subtractCurvePolygon(final Area area) {
		final CurveCrossingHelper crossHelper = new CurveCrossingHelper(new double[][] { this.coords, area.coords }, new int[] {
				this.coordsSize, area.coordsSize }, new int[][] { this.rules, area.rules },
				new int[] { this.rulesSize, area.rulesSize }, new int[][] { this.offsets, area.offsets });
		final IntersectPoint[] intersectPoints = crossHelper.findCrossing();

		if (intersectPoints.length == 0 && contains(area.getBounds2D())) {
			copy(area, this);
			return;
		}

		final double[] resultCoords = new double[this.coordsSize + area.coordsSize + intersectPoints.length];
		final int[] resultRules = new int[this.rulesSize + area.rulesSize + intersectPoints.length];
		final int[] resultOffsets = new int[this.rulesSize + area.rulesSize + intersectPoints.length];
		int resultCoordPos = 0;
		int resultRulesPos = 0;
		boolean isCurrentArea = true;

		IntersectPoint point = intersectPoints[0];
		resultRules[resultRulesPos] = PathIterator.SEG_MOVETO;
		resultOffsets[resultRulesPos++] = resultCoordPos;

		do {
			resultCoords[resultCoordPos++] = point.getX();
			resultCoords[resultCoordPos++] = point.getY();
			final int curIndex = this.offsets[point.getRuleIndex(true)] % this.coordsSize;

			if (area.containsExact(this.coords[curIndex], this.coords[curIndex + 1]) == 0) {
				isCurrentArea = !isCurrentArea;
			} else if (area.containsExact(this.coords[curIndex], this.coords[curIndex + 1]) > 0) {
				isCurrentArea = false;
			} else {
				isCurrentArea = true;
			}

			final IntersectPoint nextPoint = isCurrentArea ? getNextIntersectPoint(intersectPoints, point, isCurrentArea)
					: getPrevIntersectPoint(intersectPoints, point, isCurrentArea);
			final double[] coords = isCurrentArea ? this.coords : area.coords;
			final int[] offsets = isCurrentArea ? this.offsets : area.offsets;
			final int[] rules = isCurrentArea ? this.rules : area.rules;
			int offset = isCurrentArea ? point.getRuleIndex(isCurrentArea) : nextPoint.getRuleIndex(isCurrentArea);
			boolean isCopyUntilZero = false;

			if (isCurrentArea && point.getRuleIndex(isCurrentArea) > nextPoint.getRuleIndex(isCurrentArea)
					|| !isCurrentArea && nextPoint.getRuleIndex(isCurrentArea) > nextPoint
							.getRuleIndex(isCurrentArea)) {

				final int rulesSize = isCurrentArea ? this.rulesSize : area.rulesSize;
				resultCoordPos = includeCoordsAndRules(offset + 1, rulesSize, rules, offsets, resultRules,
						resultOffsets, resultCoords, coords, resultRulesPos, resultCoordPos, point, isCurrentArea,
						false, 2);
				resultRulesPos += rulesSize - offset - 1;
				offset = 1;
				isCopyUntilZero = true;
			}

			final int length = nextPoint.getRuleIndex(isCurrentArea) - offset + 1;

			if (isCopyUntilZero) {
				offset = 0;
				isCopyUntilZero = false;
			}

			resultCoordPos = includeCoordsAndRules(offset, length, rules, offsets, resultRules, resultOffsets,
					resultCoords, coords, resultRulesPos, resultCoordPos, point, isCurrentArea, true, 2);

			if (length == offset
					&& (rules[offset] == PathIterator.SEG_QUADTO || rules[offset] == PathIterator.SEG_CUBICTO)) {

				resultRulesPos++;
			} else {
				resultRulesPos = length < offset || isCopyUntilZero ? resultRulesPos + 1 : resultRulesPos + length
						- offset;
			}

			point = nextPoint;
		} while (point != intersectPoints[0]);

		resultRules[resultRulesPos++] = PathIterator.SEG_CLOSE;
		resultOffsets[resultRulesPos - 1] = resultCoordPos;
		this.coords = resultCoords;
		this.rules = resultRules;
		this.offsets = resultOffsets;
		this.coordsSize = resultCoordPos;
		this.rulesSize = resultRulesPos;
	}

	private void subtractPolygon(final Area area) {
		final CrossingHelper crossHelper = new CrossingHelper(new double[][] { this.coords, area.coords }, new int[] { this.coordsSize,
				area.coordsSize });
		final IntersectPoint[] intersectPoints = crossHelper.findCrossing();

		if (intersectPoints.length == 0) {
			if (contains(area.getBounds2D())) {
				copy(area, this);
				return;
			}
			return;
		}

		final double[] resultCoords = new double[2 * (this.coordsSize + area.coordsSize + intersectPoints.length)];
		final int[] resultRules = new int[2 * (this.rulesSize + area.rulesSize + intersectPoints.length)];
		final int[] resultOffsets = new int[2 * (this.rulesSize + area.rulesSize + intersectPoints.length)];
		int resultCoordPos = 0;
		int resultRulesPos = 0;
		boolean isCurrentArea = true;
		int countPoints = 0;
		boolean curArea = false;
		boolean addArea = false;

		IntersectPoint point = intersectPoints[0];
		resultRules[resultRulesPos] = PathIterator.SEG_MOVETO;
		resultOffsets[resultRulesPos++] = resultCoordPos;

		do {
			resultCoords[resultCoordPos++] = point.getX();
			resultCoords[resultCoordPos++] = point.getY();
			resultRules[resultRulesPos] = PathIterator.SEG_LINETO;
			resultOffsets[resultRulesPos++] = resultCoordPos - 2;
			final int curIndex = point.getEndIndex(true);

			if (curIndex < 0
					|| area.isVertex(this.coords[2 * curIndex], this.coords[2 * curIndex + 1])
							&& crossHelper
									.containsPoint(new double[] { this.coords[2 * curIndex], this.coords[2 * curIndex + 1] }) && (this.coords[2 * curIndex] != point
							.getX() || this.coords[2 * curIndex + 1] != point.getY())) {
				isCurrentArea = !isCurrentArea;
			} else if (area.containsExact(this.coords[2 * curIndex], this.coords[2 * curIndex + 1]) > 0) {
				isCurrentArea = false;
			} else {
				isCurrentArea = true;
			}

			if (countPoints >= intersectPoints.length) {
				isCurrentArea = !isCurrentArea;
			}

			if (isCurrentArea) {
				curArea = true;
			} else {
				addArea = true;
			}

			final IntersectPoint nextPoint = isCurrentArea ? getNextIntersectPoint(intersectPoints, point, isCurrentArea)
					: getPrevIntersectPoint(intersectPoints, point, isCurrentArea);
			final double[] coords = isCurrentArea ? this.coords : area.coords;

			int offset = isCurrentArea ? 2 * point.getEndIndex(isCurrentArea) : 2 * nextPoint
					.getEndIndex(isCurrentArea);

			if (offset > 0
					&& (isCurrentArea && nextPoint.getBegIndex(isCurrentArea) < point.getEndIndex(isCurrentArea) || !isCurrentArea && nextPoint
							.getEndIndex(isCurrentArea) < nextPoint.getBegIndex(isCurrentArea))) {

				final int coordSize = isCurrentArea ? this.coordsSize : area.coordsSize;
				final int length = coordSize - offset;

				if (isCurrentArea) {
					System.arraycopy(coords, offset, resultCoords, resultCoordPos, length);
				} else {
					final double[] temp = new double[length];
					System.arraycopy(coords, offset, temp, 0, length);
					reverseCopy(temp);
					System.arraycopy(temp, 0, resultCoords, resultCoordPos, length);
				}

				for (int i = 0; i < length / 2; i++) {
					resultRules[resultRulesPos] = PathIterator.SEG_LINETO;
					resultOffsets[resultRulesPos++] = resultCoordPos;
					resultCoordPos += 2;
				}

				offset = 0;
			}

			if (offset >= 0) {
				final int length = isCurrentArea ? 2 * nextPoint.getBegIndex(isCurrentArea) - offset + 2 : 2
						* point.getBegIndex(isCurrentArea) - offset + 2;

				if (isCurrentArea) {
					System.arraycopy(coords, offset, resultCoords, resultCoordPos, length);
				} else {
					final double[] temp = new double[length];
					System.arraycopy(coords, offset, temp, 0, length);
					reverseCopy(temp);
					System.arraycopy(temp, 0, resultCoords, resultCoordPos, length);
				}

				for (int i = 0; i < length / 2; i++) {
					resultRules[resultRulesPos] = PathIterator.SEG_LINETO;
					resultOffsets[resultRulesPos++] = resultCoordPos;
					resultCoordPos += 2;
				}
			}

			point = nextPoint;
			countPoints++;
		} while (point != intersectPoints[0] || !(curArea && addArea));

		resultRules[resultRulesPos - 1] = PathIterator.SEG_CLOSE;
		resultOffsets[resultRulesPos - 1] = resultCoordPos;
		this.coords = resultCoords;
		this.rules = resultRules;
		this.offsets = resultOffsets;
		this.coordsSize = resultCoordPos;
		this.rulesSize = resultRulesPos;
	}

	private IntersectPoint getNextIntersectPoint(final IntersectPoint[] iPoints, final IntersectPoint isectPoint,
			final boolean isCurrentArea) {
		final int endIndex = isectPoint.getEndIndex(isCurrentArea);
		if (endIndex < 0) {
			return iPoints[Math.abs(endIndex) - 1];
		}

		IntersectPoint firstIsectPoint = null;
		IntersectPoint nextIsectPoint = null;
		for (final IntersectPoint point : iPoints) {
			final int begIndex = point.getBegIndex(isCurrentArea);

			if (begIndex >= 0) {
				if (firstIsectPoint == null) {
					firstIsectPoint = point;
				} else if (begIndex < firstIsectPoint.getBegIndex(isCurrentArea)) {
					firstIsectPoint = point;
				}
			}

			if (endIndex <= begIndex) {
				if (nextIsectPoint == null) {
					nextIsectPoint = point;
				} else if (begIndex < nextIsectPoint.getBegIndex(isCurrentArea)) {
					nextIsectPoint = point;
				}
			}
		}

		return nextIsectPoint != null ? nextIsectPoint : firstIsectPoint;
	}

	private IntersectPoint getPrevIntersectPoint(final IntersectPoint[] iPoints, final IntersectPoint isectPoint,
			final boolean isCurrentArea) {

		final int begIndex = isectPoint.getBegIndex(isCurrentArea);

		if (begIndex < 0) {
			return iPoints[Math.abs(begIndex) - 1];
		}

		IntersectPoint firstIsectPoint = null;
		IntersectPoint predIsectPoint = null;
		for (final IntersectPoint point : iPoints) {
			final int endIndex = point.getEndIndex(isCurrentArea);

			if (endIndex >= 0) {
				if (firstIsectPoint == null) {
					firstIsectPoint = point;
				} else if (endIndex < firstIsectPoint.getEndIndex(isCurrentArea)) {
					firstIsectPoint = point;
				}
			}

			if (endIndex <= begIndex) {
				if (predIsectPoint == null) {
					predIsectPoint = point;
				} else if (endIndex > predIsectPoint.getEndIndex(isCurrentArea)) {
					predIsectPoint = point;
				}
			}
		}

		return predIsectPoint != null ? predIsectPoint : firstIsectPoint;
	}

	private int includeCoordsAndRules(final int offset, int length, final int[] rules, final int[] offsets, final int[] resultRules,
			final int[] resultOffsets, final double[] resultCoords, final double[] coords, int resultRulesPos, final int resultCoordPos,
			final IntersectPoint point, final boolean isCurrentArea, boolean way, final int operation) {

		final double[] temp = new double[8 * length];
		int coordsCount = 0;
		boolean isMoveIndex = true;
		boolean isMoveLength = true;
		boolean additional = false;

		if (length <= offset) {
			for (int i = resultRulesPos; i < resultRulesPos + 1; i++) {
				resultRules[i] = PathIterator.SEG_LINETO;
			}
		} else {
			int j = resultRulesPos;
			for (int i = offset; i < length; i++) {
				resultRules[j++] = PathIterator.SEG_LINETO;
			}
		}

		if (length == offset
				&& (rules[offset] == PathIterator.SEG_QUADTO || rules[offset] == PathIterator.SEG_CUBICTO)) {
			length++;
			additional = true;
		}
		for (int i = offset; i < length; i++) {
			int index = offsets[i];

			if (!isMoveIndex) {
				index -= 2;
			}

			if (!isMoveLength) {
				length++;
				isMoveLength = true;
			}

			switch (rules[i]) {
			case PathIterator.SEG_MOVETO:
				isMoveIndex = false;
				isMoveLength = false;
				break;
			case PathIterator.SEG_LINETO:
			case PathIterator.SEG_CLOSE:
				resultRules[resultRulesPos] = PathIterator.SEG_LINETO;
				resultOffsets[resultRulesPos++] = resultCoordPos + 2;
				boolean isLeft = CrossingHelper.compare(coords[index], coords[index + 1], point.getX(), point.getY()) > 0;

				if (way || !isLeft) {
					temp[coordsCount++] = coords[index];
					temp[coordsCount++] = coords[index + 1];
				}
				break;
			case PathIterator.SEG_QUADTO:
				resultRules[resultRulesPos] = PathIterator.SEG_QUADTO;
				resultOffsets[resultRulesPos++] = resultCoordPos + 4;
				double[] coefs = new double[] { coords[index - 2], coords[index - 1], coords[index], coords[index + 1],
						coords[index + 2], coords[index + 3] };
				isLeft = CrossingHelper.compare(coords[index - 2], coords[index - 1], point.getX(), point.getY()) > 0;

				if (!additional && (operation == 0 || operation == 2)) {
					isLeft = !isLeft;
					way = false;
				}
				GeometryUtil.subQuad(coefs, point.getParam(isCurrentArea), isLeft);

				if (way || isLeft) {
					temp[coordsCount++] = coefs[2];
					temp[coordsCount++] = coefs[3];
				} else {
					System.arraycopy(coefs, 2, temp, coordsCount, 4);
					coordsCount += 4;
				}
				break;
			case PathIterator.SEG_CUBICTO:
				resultRules[resultRulesPos] = PathIterator.SEG_CUBICTO;
				resultOffsets[resultRulesPos++] = resultCoordPos + 6;
				coefs = new double[] { coords[index - 2], coords[index - 1], coords[index], coords[index + 1],
						coords[index + 2], coords[index + 3], coords[index + 4], coords[index + 5] };
				isLeft = CrossingHelper.compare(coords[index - 2], coords[index - 1], point.getX(), point.getY()) > 0;
				GeometryUtil.subCubic(coefs, point.getParam(isCurrentArea), !isLeft);

				if (isLeft) {
					System.arraycopy(coefs, 2, temp, coordsCount, 6);
					coordsCount += 6;
				} else {
					System.arraycopy(coefs, 2, temp, coordsCount, 4);
					coordsCount += 4;
				}
				break;
			}
		}

		if (operation == 2 && !isCurrentArea && coordsCount > 2) {
			reverseCopy(temp);
			System.arraycopy(temp, 0, resultCoords, resultCoordPos, coordsCount);
		} else {
			System.arraycopy(temp, 0, resultCoords, resultCoordPos, coordsCount);
		}

		return resultCoordPos + coordsCount;
	}

	// the method check up the array size and necessarily increases it.
	private static double[] adjustSize(final double[] array, final int newSize) {
		if (newSize <= array.length) {
			return array;
		}
		final double[] newArray = new double[2 * newSize];
		System.arraycopy(array, 0, newArray, 0, array.length);
		return newArray;
	}

	private static int[] adjustSize(final int[] array, final int newSize) {
		if (newSize <= array.length) {
			return array;
		}
		final int[] newArray = new int[2 * newSize];
		System.arraycopy(array, 0, newArray, 0, array.length);
		return newArray;
	}

	private void copy(final Area src, final Area dst) {
		dst.coordsSize = src.coordsSize;
		dst.coords = src.coords.clone();
		dst.rulesSize = src.rulesSize;
		dst.rules = src.rules.clone();
		dst.moveToCount = src.moveToCount;
		dst.offsets = src.offsets.clone();
	}

	private int containsExact(final double x, final double y) {
		PathIterator pi = getPathIterator(null);
		final int crossCount = Crossing.crossPath(pi, x, y);

		if (Crossing.isInsideEvenOdd(crossCount)) {
			return 1;
		}

		final double[] segmentCoords = new double[6];
		final double[] resultPoints = new double[6];
		int rule;
		double curX = -1;
		double curY = -1;
		double moveX = -1;
		double moveY = -1;

		for (pi = getPathIterator(null); !pi.isDone(); pi.next()) {
			rule = pi.currentSegment(segmentCoords);
			switch (rule) {
			case PathIterator.SEG_MOVETO:
				moveX = curX = segmentCoords[0];
				moveY = curY = segmentCoords[1];
				break;
			case PathIterator.SEG_LINETO:
				if (GeometryUtil.intersectLines(curX, curY, segmentCoords[0], segmentCoords[1], x, y, x, y,
						resultPoints) != 0) {
					return 0;
				}
				curX = segmentCoords[0];
				curY = segmentCoords[1];
				break;
			case PathIterator.SEG_QUADTO:
				if (GeometryUtil.intersectLineAndQuad(x, y, x, y, curX, curY, segmentCoords[0], segmentCoords[1],
						segmentCoords[2], segmentCoords[3], resultPoints) > 0) {
					return 0;
				}
				curX = segmentCoords[2];
				curY = segmentCoords[3];
				break;
			case PathIterator.SEG_CUBICTO:
				if (GeometryUtil.intersectLineAndCubic(x, y, x, y, curX, curY, segmentCoords[0], segmentCoords[1],
						segmentCoords[2], segmentCoords[3], segmentCoords[4], segmentCoords[5], resultPoints) > 0) {
					return 0;
				}
				curX = segmentCoords[4];
				curY = segmentCoords[5];
				break;
			case PathIterator.SEG_CLOSE:
				if (GeometryUtil.intersectLines(curX, curY, moveX, moveY, x, y, x, y, resultPoints) != 0) {
					return 0;
				}
				curX = moveX;
				curY = moveY;
				break;
			}
		}
		return -1;
	}

	private void reverseCopy(final double[] coords) {
		final double[] temp = new double[coords.length];
		System.arraycopy(coords, 0, temp, 0, coords.length);

		for (int i = 0; i < coords.length;) {
			coords[i] = temp[coords.length - i - 2];
			coords[i + 1] = temp[coords.length - i - 1];
			i = i + 2;
		}
	}

	private double getAreaBoundsSquare() {
		final Rectangle2D bounds = getBounds2D();
		return bounds.getHeight() * bounds.getWidth();
	}

	private boolean isVertex(final double x, final double y) {
		for (int i = 0; i < this.coordsSize;) {
			if (x == this.coords[i++] && y == this.coords[i++]) {
				return true;
			}
		}
		return false;
	}

	// the internal class implements PathIterator
	private class AreaPathIterator implements PathIterator {

		AffineTransform transform;
		Area area;
		int curRuleIndex = 0;
		int curCoordIndex = 0;

		AreaPathIterator(final Area area) {
			this(area, null);
		}

		AreaPathIterator(final Area area, final AffineTransform t) {
			this.area = area;
			this.transform = t;
		}

		@Override
		public int getWindingRule() {
			return WIND_EVEN_ODD;
		}

		@Override
		public boolean isDone() {
			return this.curRuleIndex >= Area.this.rulesSize;
		}

		@Override
		public void next() {
			switch (Area.this.rules[this.curRuleIndex]) {
			case PathIterator.SEG_MOVETO:
			case PathIterator.SEG_LINETO:
				this.curCoordIndex += 2;
				break;
			case PathIterator.SEG_QUADTO:
				this.curCoordIndex += 4;
				break;
			case PathIterator.SEG_CUBICTO:
				this.curCoordIndex += 6;
				break;
			}
			this.curRuleIndex++;
		}

		@Override
		public int currentSegment(final double[] c) {
			if (isDone()) {
				throw new NoSuchElementException(Messages.getString("awt.4B")); //$NON-NLS-1$
			}

			int count = 0;

			switch (Area.this.rules[this.curRuleIndex]) {
			case PathIterator.SEG_CUBICTO:
				c[4] = Area.this.coords[this.curCoordIndex + 4];
				c[5] = Area.this.coords[this.curCoordIndex + 5];
				count = 1;
			case PathIterator.SEG_QUADTO:
				c[2] = Area.this.coords[this.curCoordIndex + 2];
				c[3] = Area.this.coords[this.curCoordIndex + 3];
				count += 1;
			case PathIterator.SEG_MOVETO:
			case PathIterator.SEG_LINETO:
				c[0] = Area.this.coords[this.curCoordIndex];
				c[1] = Area.this.coords[this.curCoordIndex + 1];
				count += 1;
			}

			if (this.transform != null) {
				this.transform.transform(c, 0, c, 0, count);
			}

			return Area.this.rules[this.curRuleIndex];
		}

		@Override
		public int currentSegment(final float[] c) {
			final double[] doubleCoords = new double[6];
			final int rule = currentSegment(doubleCoords);

			for (int i = 0; i < 6; i++) {
				c[i] = (float) doubleCoords[i];
			}
			return rule;
		}
	}
}