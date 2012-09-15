import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FactoryBelt implements Runnable {

	private JTextField allBottle, beltBottle[];
	private boolean isFactoryRunning, isBeltRunning[];

	public static void main(String[] args) {
		FactoryBelt factory = new FactoryBelt();
		LogisticBelt belt1 = new LogisticBelt(1, factory);
		LogisticBelt belt2 = new LogisticBelt(2, factory);
		factory.createGUI();
		Thread f = new Thread(factory);
		Thread g = new Thread(belt1);
		Thread h = new Thread(belt2);
		f.start();
		g.start();
		h.start();
	}

	public boolean getBeltStatus(int i) {
		return isBeltRunning[i];
	}
	
	public JTextField[] getBeltBottleArray()
	{
		return beltBottle;
	}
	public FactoryBelt() {
		isBeltRunning = new boolean[3];
		beltBottle = new JTextField[3];
	}

	public synchronized void increaseFactoryBottle() {
		allBottle.setText(getFactoryBottle() + 1 + "");
	}
	
	public synchronized boolean decreaseFactoryBottle() {
		if(getFactoryBottle() > 0)
		{
			allBottle.setText(getFactoryBottle() - 1 + "");
			synchronized (allBottle) {
				allBottle.notifyAll();
			}
			return true;
		}
		return false;
	}
	
	public synchronized void increaseBeltBottle(int id) {
		beltBottle[id].setText(Integer.parseInt(beltBottle[id].getText()) + 1 + "");
	}

	public int getFactoryBottle() {
		return Integer.parseInt(allBottle.getText());
	}

	public void createGUI() {

		// Initial Frame
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(320, 320));
		frame.setTitle("5431010121_synchro");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// get frame content pane
		Container c = frame.getContentPane();
		// set layout to grid
		c.setLayout(new FlowLayout());
		c.setPreferredSize(new Dimension(320, 320));
		((FlowLayout) c.getLayout()).setHgap(0);
		((FlowLayout) c.getLayout()).setVgap(0);

		// Create subpanel
		JPanel topLeftPanel = new JPanel();
		JPanel topRightPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		JPanel emptyPanel = new JPanel();
		topLeftPanel.setPreferredSize(new Dimension(140, 150));
		topRightPanel.setPreferredSize(new Dimension(140, 150));
		bottomPanel.setPreferredSize(new Dimension(140, 150));
		emptyPanel.setPreferredSize(new Dimension(40, 150));
		topLeftPanel.setLayout(new FlowLayout());
		topRightPanel.setLayout(new FlowLayout());
		bottomPanel.setLayout(new FlowLayout());

		// factory bottle field
		allBottle = new JTextField();
		allBottle.setPreferredSize(new Dimension(140, 20));
		allBottle.setEditable(false);
		allBottle.setHorizontalAlignment((int) JTextField.CENTER_ALIGNMENT);
		allBottle.setText("0");
		// factory start button
		JButton factoryStart = new JButton();
		factoryStart.setPreferredSize(new Dimension(140, 30));
		factoryStart.setText("Toggle Factory");
		factoryStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isFactoryRunning = !isFactoryRunning;
				System.out.println("now factory is "+ isFactoryRunning);
				synchronized (allBottle) {
					allBottle.notifyAll();
				}
				
			}
		});
		// Belt1 filed
		beltBottle[1] = new JTextField();
		beltBottle[1].setPreferredSize(new Dimension(140, 20));
		beltBottle[1].setEditable(false);
		beltBottle[1].setHorizontalAlignment((int) JTextField.CENTER_ALIGNMENT);
		beltBottle[1].setText("0");
		// belt1 start button
		JButton belt1Start = new JButton();
		belt1Start.setPreferredSize(new Dimension(140, 30));
		belt1Start.setText("Toggle Belt 1");
		belt1Start.addActionListener(new BeltListener(1));

		// Belt2 filed
		beltBottle[2] = new JTextField();
		beltBottle[2].setPreferredSize(new Dimension(140, 20));
		beltBottle[2].setEditable(false);
		beltBottle[2].setHorizontalAlignment((int) JTextField.CENTER_ALIGNMENT);
		beltBottle[2].setText("0");
		// belt1 start button
		JButton belt2Start = new JButton();
		belt2Start.setPreferredSize(new Dimension(140, 30));
		belt2Start.setText("Toggle Belt 2");
		belt2Start.addActionListener(new BeltListener(2));

		// Add component to panel
		topLeftPanel.add(beltBottle[1]);
		topLeftPanel.add(belt1Start);
		topRightPanel.add(beltBottle[2]);
		topRightPanel.add(belt2Start);
		bottomPanel.add(allBottle);
		bottomPanel.add(factoryStart);

		// add panel to frame
		c.add(topLeftPanel);
		c.add(emptyPanel);
		c.add(topRightPanel);
		c.add(bottomPanel);
		// Set frame to visible
		frame.pack();
		frame.setVisible(true);
	}

	public void run() {
		try {
			while (true) {
				if (!isFactoryRunning) {
						System.out.println("waiting");
						synchronized (allBottle) {
							allBottle.wait();
						}
						System.out.println("stop waiting");
				} else if (getFactoryBottle() < 50)
					this.increaseFactoryBottle();
				else {
					while (true) {
						synchronized (allBottle) {
							allBottle.wait();
							System.out.println("get someone take bottle");
						}
						if (getFactoryBottle() < 30)
							break;
					}
				}

				Thread.sleep(150);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Inner Class for belt Toggle
	class BeltListener implements ActionListener {
		int id;

		public BeltListener(int id) {
			this.id = id;
		}

		@Override
		public synchronized void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			isBeltRunning[id] = !isBeltRunning[id];
			synchronized(beltBottle)
			{
				beltBottle.notifyAll();
			}
		}

	}
}
