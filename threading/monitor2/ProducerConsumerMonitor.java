package threading.monitor2;

// Theory of wait(), notify(), notifyAll() 
// https://medium.com/@adam.rizk9/demystifying-java-wait-notify-and-join-methods-for-multithreading-an-in-depth-look-ffb43a514bbc

/*
 * Both threats better use the same monitor object, as wait and notify are called
 * through its instance: monitor.wait(), monitor.notify()
 * 
 * With wait() the other thread must fulfill the condition of calling notify() and releasing the monitor
 * With join() the other thread must fulfill the condition of terminating
 **/ 


public class ProducerConsumerMonitor {
	public static void main(String[] args) {
		testMonitor();
	}

	private static void testMonitor() {
		Object monitor = new Object();
		
		KitchenTasks kitchen = new KitchenTasks(monitor);

		Thread chefThread = new Thread(() -> {
			System.out.println("Chef starting");
			while(true) {
				kitchen.cooking();
			}
		});
		
		Thread clientThread = new Thread(() ->{
			System.out.println("Client starting");
			while(true) {
				kitchen.eating();
			}
		});
		
		chefThread.start();
		clientThread.start();
		
		try {
			chefThread.join();
			clientThread.join();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		
		
		
		
	}

	
	
}


/**/