package threading.monitor2;

public class KitchenTasks {
	private final int MAX_PIZZAS = 20;
	private final int MAX_PIZZAS_COOKING = 4;
	private final int MAX_PIZZAS_TO_EAT = 4;
	private final int TIME_EXECUTING_SECS = 3;
	
	/* Any object can potentially be a monitor. Avoid using String "literals",
	 * as something unexpected can occur by String Constant Pool optimization*/
	private Object monitor;
	
	private int pizzas;
	private boolean chefSleeping;
	private boolean clientSleeping;
	
	public KitchenTasks(Object monitor) {
		this.monitor = monitor;
	}
	
	public void cooking() {
		synchronized(monitor) {

			int newPizzas;
			int minPizzas;

			executing("\nChef cooking");

			minPizzas = Math.min(MAX_PIZZAS - pizzas, MAX_PIZZAS_COOKING);
			
			newPizzas = getRandom(1, minPizzas);
			
			pizzas += newPizzas;
			
			System.out.println("(+++) " + newPizzas + " pizzas ");
			showPizzas();
			timePasses(1000);
			
			if(clientSleeping) {
				wakeUpBro();		// notify call
				clientSleeping = false;
				System.out.println("Client awakes. Thanks bro");
				timePasses(1000);
			}
			
			if(pizzas >= MAX_PIZZAS) {
				System.out.println("PIZZAS FULL, chef sleeping");
				timePasses(1000);
				chefSleeping = true;
				goSleep(); 			// wait call
			}

		}
	}
	
	public void eating() {
		synchronized(monitor) {
			
			if(pizzas > 0 ) {
				int pizzasToEat = Math.min(MAX_PIZZAS_TO_EAT, pizzas);
				
				pizzasToEat = getRandom(1, pizzasToEat); // Bug. If consumer takes monitor first, there's gonna be negative pizzas
				
				executing("\nClient eating");
				
				pizzas -= pizzasToEat;
				
				System.out.println("(---) " + pizzasToEat + " pizzas");
				showPizzas();
				timePasses(1000);
			}
			
			
			if(chefSleeping) {
				wakeUpBro();			// notify call
				chefSleeping = false;
				System.out.println("Chef awakes. Thanks bro");
				timePasses(1000);
			}
			
			if(pizzas <= 0) {
				System.out.println("PIZZAS EMPTY, client sleeping");
				timePasses(1000);
				clientSleeping = true;
				goSleep(); 			// wait call

			}
		}
	}
	
	public void showPizzas() {
		synchronized(monitor){
			System.out.print("(" + pizzas + ")PIZZAS - ");
			for(int i = 1; i <= pizzas; i++) {
				System.out.print("@ ");
			}
			System.out.println();
		}
	}
	
	
	
	public int getRandom( int min, int max) {

		return min + (int)(Math.random() * (max - min + 1));
	}
	
	/*Simulates thread working time, printing a dot every second*/
	public void executing(String task) {
		synchronized(monitor){
			System.out.print(task);
			
			for(int i = 0; i < TIME_EXECUTING_SECS; i++) {
				System.out.print(".");
				timePasses(1000); // 	1 sec
			}
			
			System.out.println();
		}
		
	}
	
	/* Thread.sleep call*/
	private  void timePasses(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/* Calls wait method to stop current thread execution, until another thread calls
	 * notify method
	 * */
	private void goSleep() {
		try {
			monitor.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/* Current monitor's owner awakes a waiting thread */
	private void wakeUpBro() {
		monitor.notify();
	}
	

}
