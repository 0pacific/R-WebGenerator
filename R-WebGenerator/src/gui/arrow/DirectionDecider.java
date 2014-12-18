package gui.arrow;

import debug.Debug;

import java.awt.Point;
import java.io.Serializable;

import javax.swing.JPanel;

import view.EditPanel;

public class DirectionDecider implements Serializable {
	public static final int LEFT_TO_RIGHT = 0;
	public static final int RIGHT_TO_LEFT = 1;
	public static final int UPPER_TO_LOWER = 2;
	public static final int LOWER_TO_UPPER = 3;
	
	public Point[] decide(EditPanel p1, EditPanel p2) {
		Point[] pointPair = new Point[2];

		Point startPoint = getCenterPoint(p1);
		Point endPoint = getCenterPoint(p2);

		double xDist = endPoint.getX() - startPoint.getX();
		double yDist = endPoint.getY() - startPoint.getY();

		// endPointがstartPointより左にある
		if(xDist<0) {
			double leftDist = -xDist;

			// endPointがstartPointより左上にある
			if(yDist<0) {
				double aboveDist = -yDist;

				// 左に遠い
				if(leftDist > aboveDist) {
					pointPair[0] = getLeftPoint(p1);
					pointPair[1] = getRightPoint(p2);
				}
				// 上に遠い
				else {
					pointPair[0] = getUpperPoint(p1);
					pointPair[1] = getLowerPoint(p2);
				}
			}
			// endPointがstartPointより左下にある
			else {
				double underDist = yDist;

				// 左に遠い
				if(leftDist > underDist) {
					pointPair[0] = getLeftPoint(p1);
					pointPair[1] = getRightPoint(p2);
				}
				// 下に遠い
				else {
					pointPair[0] = getLowerPoint(p1);
					pointPair[1] = getUpperPoint(p2);
				}
			}
		}
		// endPointがstartPointより右にある
		else {
			double rightDist = xDist;

			// endPointがstartPointより右上にある
			if(yDist<0) {
				double aboveDist = -yDist;

				// 右に遠い
				if(rightDist > aboveDist) {
					pointPair[0] = getRightPoint(p1);
					pointPair[1] = getLeftPoint(p2);
				}
				// 上に遠い
				else {
					pointPair[0] = getUpperPoint(p1);
					pointPair[1] = getLowerPoint(p2);
				}
			}
			// endPointがstartPointより右下にある
			else {
				double underDist = yDist;

				// 右に遠い
				if(rightDist > underDist) {
					pointPair[0] = getRightPoint(p1);
					pointPair[1] = getLeftPoint(p2);
				}
				// 下に遠い
				else {
					pointPair[0] = getLowerPoint(p1);
					pointPair[1] = getUpperPoint(p2);
				}
			}
		}
		
		return pointPair;
	}

	public Point getCenterPoint(EditPanel panel) {
		Point point = new Point(panel.getPosX()+(panel.getWidth()/2), panel.getPosY()+( panel.getHeight()/2 ));
		return point;
	}
	
	public Point getLeftPoint(EditPanel panel) {
		Point point = new Point(panel.getPosX(), panel.getPosY()+( panel.getHeight()/2 ));
		return point;
	}

	public Point getRightPoint(EditPanel panel) {
		Point point = new Point(panel.getPosX()+panel.getWidth(), panel.getPosY()+( panel.getHeight()/2 ));
		return point;
	}

	public Point getUpperPoint(EditPanel panel) {
		Point point = new Point(panel.getPosX()+(panel.getWidth()/2), panel.getPosY());
		return point;
	}

	public Point getLowerPoint(EditPanel panel) {
		Point point = new Point(panel.getPosX()+(panel.getWidth()/2), panel.getPosY()+panel.getHeight());
		return point;
	}
}
