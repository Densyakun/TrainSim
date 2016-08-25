package net.densyakun.trainsim.android;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import net.densyakun.trainsim.Line;
import net.densyakun.trainsim.LineColor;
import net.densyakun.trainsim.RailwayListener;
import net.densyakun.trainsim.RailwayManager;
import net.densyakun.trainsim.Station;
import net.densyakun.trainsim.Train;
import net.densyakun.trainsim.TrainSimMath;

import java.util.List;

public final class TIDView extends View implements RailwayListener {
	private MainActivity main;
	private LinearLayout.LayoutParams params;
	private boolean aaa = false;
	private Handler handler = new Handler();
	public TIDView(MainActivity main) {
		super(main);
		this.main = main;
		resize();
		main.getRailwayManager().addListener(this);
		setBackgroundColor(Color.BLACK);
	}
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (aaa) {
			setBackgroundColor(Color.BLACK);
		}
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(16);
		/*paint.setColor(Color.RED);
		for (int x = 0; x <= params.width; x+=64) {
			for (int y = 0; y <= params.height; y+=64) {
				canvas.drawLine(x, y, 64 + x, y, paint);
				canvas.drawLine(x, y, x, 64 + y, paint);
			}
		}*/
		RailwayManager railwaymanager = main.getRailwayManager();
		List<Line> lines = railwaymanager.getLines();
		for (int a = 0; a < lines.size(); a++) {
			for (int b = 0; b < lines.get(a).getRails().size(); b++) {
				/*if (lines.get(a).getRails().get(b) instanceof Station) {
					paint.setStrokeWidth(12);
					paint.setColor(Color.LTGRAY);
				} else {*/
					paint.setStrokeWidth(8);
					paint.setColor(Color.GRAY);
				//}
				double c = 0;
				for (int d = 0; d < b; d++) {
					c += lines.get(a).getRails().get(d).getLength();
				}
				canvas.drawLine(32 + (int) c, 128 * a + 32, 32 + (int) (c + lines.get(a).getRails().get(b).getLength()), 128 * a + 32, paint);
				if (b != 0) {
					paint.setStrokeWidth(16);
					paint.setColor(Color.WHITE);
					canvas.drawLine(32 + (int) c - 1, 128 * a + 32, 32 + (int) c + 1, 128 * a + 32, paint);
				}
				String d = (lines.get(a).getRails().get(b) instanceof Station ? ((Station) lines.get(a).getRails().get(b)).getName() : "") + "(" + (double) Math.round(lines.get(a).getRails().get(b).getLength() / 100) / 10 + "km";
				if (lines.get(a).getRails().get(b).getLimitSpeed() != null) {
					d += ", limitspeed: " + TrainSimMath.getSpeed(lines.get(a).getRails().get(b).getLimitSpeed()) + "km/h";
				}
				d += ")";
				float e = paint.measureText(d);
				paint.setColor(Color.LTGRAY);
				canvas.drawText(d, (int) (c + lines.get(a).getRails().get(b).getLength() / 2) - (e / 2) + 32 + 1, 128 * a + 64 - 4 + 1, paint);
				paint.setColor(Color.WHITE);
				canvas.drawText(d, (int) (c + lines.get(a).getRails().get(b).getLength() / 2) - (e / 2) + 32, 128 * a + 64 - 4, paint);
			}
			for (int b = 0; b < railwaymanager.getTrains().size(); b++) {
				if ((railwaymanager.getTrains().get(b).getRunningLine() != null) && railwaymanager.getTrains().get(b).getRunningLine().equals(lines.get(a))) {
					for (int c = 0; c < railwaymanager.getTrains().get(b).getCars().length; c++) {
						double d = 0;
						for (int e = 0; e < c; e++) {
							d += railwaymanager.getTrains().get(b).getCars()[e].getLength();
						}
						paint.setStrokeWidth(3);
						LineColor color = railwaymanager.getTrains().get(b).getLineColor();
						paint.setColor(color == null ? Color.WHITE : Color.rgb(color.getMainRed(), color.getMainGreen(), color.getMainBlue()));
						color = null;
						canvas.drawLine((float) (32 + railwaymanager.getTrains().get(b).getPosition() - railwaymanager.getTrains().get(b).getLength() / 2 + d), 32 + 128 * a, (float) (32 + railwaymanager.getTrains().get(b).getPosition() - railwaymanager.getTrains().get(b).getLength() / 2 + d + railwaymanager.getTrains().get(b).getCars()[c].getLength()), 32 + 128 * a, paint);
						if (c != 0) {
							paint.setStrokeWidth(4);
							paint.setColor(Color.DKGRAY);
							canvas.drawLine((float) (32 + railwaymanager.getTrains().get(b).getPosition() - railwaymanager.getTrains().get(b).getLength() / 2 + d) - 1, 32 + 128 * a, (float) (32 + railwaymanager.getTrains().get(b).getPosition() - railwaymanager.getTrains().get(b).getLength() / 2 + d) + 1, 32 + 128 * a, paint);
						}
					}
					paint.setStrokeWidth(2);
					paint.setColor(Color.WHITE);
					canvas.drawLine((float) (32 + railwaymanager.getTrains().get(b).getPosition() - railwaymanager.getTrains().get(b).getLength() / 2), 64 + 128 * a, (float) (32 + railwaymanager.getTrains().get(b).getPosition() - railwaymanager.getTrains().get(b).getLength() / 2), 128 - 16 + 128 * a, paint);
					double x = -1;
					if ((railwaymanager.getTrains().get(b).getRunningLine() != null) && (railwaymanager.getTrains().get(b).getDriver() != null) && (railwaymanager.getTrains().get(b).getDriver().getDiagram() != null) && (railwaymanager.getTrains().get(b).getDriver().getDiagram().getStopStationList() != null) && (railwaymanager.getTrains().get(b).getDriver().getDiagram().getStopStationList().size() > 0)) {
						for (int c = 0; c < railwaymanager.getTrains().get(b).getRunningLine().getRails().size(); c++) {
							if (railwaymanager.getTrains().get(b).getDriver().getDiagram().getStopStationList().get(0) == railwaymanager.getTrains().get(b).getRunningLine().getRails().get(c)) {
								x = railwaymanager.getTrains().get(b).getDriver().getDiagram().getStopStationList().get(0).getLength() / 2;
								for (int d = 0; d < c; d++) {
									x += railwaymanager.getTrains().get(b).getRunningLine().getRails().get(d).getLength();
								}
								break;
							}
						}
					}
					if (x == -1) {
						x = railwaymanager.getTrains().get(b).getPosition();
					}
					String c = "(" + (double) Math.round(railwaymanager.getTrains().get(b).getPosition() * 10) / 10 + "m, speed: " + (double) Math.round(railwaymanager.getTrains().get(b).getSpeed() * 10) / 10 + "km/h, reverser: " + railwaymanager.getTrains().get(b).getReverser() + ", accel: " + railwaymanager.getTrains().get(b).getAccel() + ", brake: " + railwaymanager.getTrains().get(b).getBrake() + ", to: " + x + ")";
					paint.setColor(Color.LTGRAY);
					canvas.drawText(c, 32 + (float) (railwaymanager.getTrains().get(b).getPosition() - railwaymanager.getTrains().get(b).getLength() / 2) + 1, 128 * (a + 1) + 1, paint);
					paint.setColor(Color.WHITE);
					canvas.drawText(c, 32 + (float) (railwaymanager.getTrains().get(b).getPosition() - railwaymanager.getTrains().get(b).getLength() / 2), 128 * (a + 1), paint);
				}
			}
			String b = lines.get(a).getName() + "(" + (double) Math.round(lines.get(a).getLength() / 100) / 10 + "km, max: " + TrainSimMath.getSpeed(lines.get(a).getLimitSpeed()) + "km/h)";
			float c = paint.measureText(b);
			paint.setColor(Color.LTGRAY);
			canvas.drawText(b, (float) (lines.get(a).getLength() / 2) - (c / 2) + 32 + 1, 128 * (a + 1) - 16 + 1, paint);
			paint.setColor(Color.WHITE);
			canvas.drawText(b, (float) (lines.get(a).getLength() / 2) - (c / 2) + 32, 128 * (a + 1) - 16, paint);
		}
		String a = (railwaymanager.isPlaying() ? "再生中" + (railwaymanager.isFast() ? "x3" : "x1") : "停止中") + "(" + railwaymanager.getTime() + ")";
		paint.setColor(Color.LTGRAY);
		canvas.drawText(a, 32 + 1, 4 + 16 + 1, paint);
		paint.setColor(Color.GREEN);
		canvas.drawText(a, 32, 4 + 16, paint);
		railwaymanager = null;
		lines = null;
	}
	@Override
	public void played(RailwayManager railwaymanager) {
		invalidateinit();
	}
	@Override
	public void stopped(RailwayManager railwaymanager) {
		invalidateinit();
	}
	@Override
	public void updated(RailwayManager railwaymanager) {
		invalidateinit();
	}
	@Override
	public void addLine(RailwayManager railwaymanager, Line line) {
		resize();
		invalidateinit();
	}
	@Override
	public void addTrain(RailwayManager railwaymanager, Train train) {
		resize();
		invalidateinit();
	}
	@Override
	public void clearRailway(RailwayManager railwaymanager) {
		resize();
		invalidateinit();
	}
	@Override
	public void trainBroken(RailwayManager railwaymanager, Train train) {
		setBackgroundColor(Color.RED);
		aaa = true;
		invalidateinit();
	}

	@Override
	public void trainStopped(RailwayManager railwaymanager, Train train, Station station) {
		invalidateinit();
	}

	public void invalidateinit() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				invalidate();
			}
		});
	}

	public void resize() {
		int x = main.getWindowManager().getDefaultDisplay().getWidth();
		List<Line> lines = main.getRailwayManager().getLines();
		for (int a = 0; a < lines.size(); a++) {
			x = Math.max(x, (int) lines.get(a).getLength() + 64);
		}
		setLayoutParams(params = new LinearLayout.LayoutParams(x, Math.max(main.getWindowManager().getDefaultDisplay().getHeight(), 128 * lines.size())));
	}
}

