/*
 * synchro
 * 5431010121
 * Nuttapon Pattanavijit
 */

public class LogisticBelt implements Runnable {
	private int id;
	final private static int sleepTime = 150;
	FactoryBelt factory;

	public LogisticBelt(int id, FactoryBelt factory) {
		super();
		this.id = id;
		this.factory = factory;
	}

	public void run() {
		try {
			Object lock = factory.getBeltLockObject(id);
			while (true) {
				if (!factory.getIsBeltRunning(id)) {
					synchronized (lock) {
						lock.wait();
					}
				} else if (!factory.decreaseFactoryBottle())
					synchronized (factory) {
						factory.wait();
					}
				else {
					factory.increaseBelt(id);
				}
				Thread.sleep(sleepTime);
			}
		} catch (InterruptedException e) {

		}
	}
}
