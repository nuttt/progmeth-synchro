
public class Thread_test implements Runnable{

	int val;
	int id;
	public Thread_test(int id) {
		// TODO Auto-generated constructor stub
		val = 0;
		this.id = id;
	}
	
	public synchronized int read()
	{
		return val;
	}
	
	public synchronized void write(int val)
	{
		this.val = val;
	}
	
	public void run()
	{
		while(true)
		{
			if((int)(Math.random()*2) == 1)
			{
				int t = (int)(Math.random()*10);
				System.out.println("Thread"+id+" write "+t);
				write(t);
			}
			else
			{
				System.out.println("Thread"+id+" read "+read());
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args)
	{
		Thread a = new Thread(new Thread_test(1));
		Thread b = new Thread(new Thread_test(2));
		a.start();
		b.start();
	}

}
