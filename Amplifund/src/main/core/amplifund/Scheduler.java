package main.core.amplifund;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.main.business.Constants;

import main.core.AMPLIFUND;
import main.core.Plugin.statusType;


/**
 * 
 * @author Jon Cornado
 * 
 *         As the name suggests, this class call XSLReader class at regular
 *         intervals.
 * 
 *
 */
public class Scheduler {

	Scheduler schedule;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public void beepForAnHour() {
		final Runnable beeper = new Runnable() {
			public void run() {
				new XSLReader().readDocument();
			}
		};
		final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 10, 60*60*24, TimeUnit.SECONDS);
		scheduler.schedule(new Runnable() {
			public void run() {
				System.out.println("in the scheduler");
				// beeperHandle.cancel(true);
			}
		}, 20, TimeUnit.MINUTES);
	}

	/**
	 * start the scheduler, changes the state of the plugin accordingly
	 */
	public void startScheduler() {
		Constants.ReadConstants();
		System.out.println("starting schedules");
		AMPLIFUND.setStatus(statusType.loading);
		schedule = new Scheduler();
		schedule.beepForAnHour();
		AMPLIFUND.setStatus(statusType.running);

	}

	/**
	 * Stops the scheduler, changes the state of the plugin accordingly
	 */
	public void stopScheculer() {
		System.out.println("stopping schedules");
		AMPLIFUND.setStatus(statusType.loading);
		schedule.scheduler.shutdown();
		try {
			schedule.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AMPLIFUND.setStatus(statusType.stopped);

	}

	public static void main(String[] args) {

		new Scheduler().beepForAnHour();

	}

}
