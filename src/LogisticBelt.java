
public class LogisticBelt implements Runnable {
	private int id;
	final private static int sleepTime = 150;
	FactoryBelt factory;
	public LogisticBelt(int id, FactoryBelt factory) {
		super();
		this.id = id;
		this.factory = factory;
	}
	
	public void run()
	{
		try {
			while (true) {
				if (!factory.getBeltStatus(id)) {
					synchronized(factory.getBeltBottleArray())
					{
						factory.getBeltBottleArray().wait();
					}
				}
				else
				{
					if(factory.decreaseFactoryBottle())
					factory.increaseBeltBottle(id);
				}

				Thread.sleep(150);
			}
		}
		catch(InterruptedException e)
		{
		
		}
	}
}
