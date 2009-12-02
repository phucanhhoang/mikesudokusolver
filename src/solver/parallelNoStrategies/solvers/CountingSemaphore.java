package solver.parallelNoStrategies.solvers;

import java.util.concurrent.Semaphore;

public class CountingSemaphore {
	private Semaphore lock;
	private int value = 0;

	public CountingSemaphore() {
		this(null);
	}

	public CountingSemaphore(Semaphore lock) {
		if (lock == null) {
			this.lock = new Semaphore(1);
		} else {
			this.lock = lock;
		}
		value = 0;
	}

	public void clear() {
		try {
			lock.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("ERROR AQUIRING SEMAPHORE LOCK");
		}
		value = 0;
		lock.release();
	}

	public void decrementCounter() {
		try {
			lock.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("ERROR AQUIRING SEMAPHORE LOCK");
		}
		value--;
		lock.release();
	}

	public int getValue() {
		return value;
	}

	public void incrementCounter() {
		try {
			lock.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("ERROR AQUIRING SEMAPHORE LOCK");
		}
		value++;
		lock.release();
	}

	public void waitForThreadsToFinish() {
		while (getValue() > 0) {
			try {
				Thread.sleep(50); // do nothing for 50 miliseconds
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		clear();
	}
}
