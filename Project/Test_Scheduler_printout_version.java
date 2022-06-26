package Project;
/**
 * test for Scheduler
 * 
 * @author kaiwen
 */
public class Test_Scheduler_printout_version {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SchedulerImpl scheduler = new SchedulerImpl();
		// syntax of the new lambda expressions
		Runnable r1 = () -> System.out.println("thread1");
		
		// Add 2 normal tasks 
		Scheduled_Task scheduled_task1 = scheduler.scheduleAt(1,() -> System.out.print("task1"));
		System.out.println(scheduler.toString());
		
		Scheduled_Task scheduled_task2 = scheduler.scheduleAt(3,() -> System.out.print("task2"));
		System.out.println(scheduler.toString());
		
		// check cancel() on normal task
		scheduled_task2.cancel();
		System.out.println(scheduler.toString());
		
		scheduler.fire(2);
		System.out.println(scheduler.toString());
		
		scheduler.fire(4);
		System.out.println(scheduler.toString());
		
		// Add looping task taskx
		var taskx = scheduler.scheduleEvery(3,() -> System.out.print("taskx"));
		//System.out.println(scheduler.isevery_events.size());
		System.out.println(scheduler.toString());
		
		scheduler.fire(5);
		System.out.println(scheduler.toString());
		
		scheduler.fire(6);
		System.out.println(scheduler.toString());
		scheduler.fire(7);
		System.out.println(scheduler.toString());
		
		// check looping
		System.out.println(scheduler.toString());
		scheduler.fire(9);
		System.out.println(scheduler.toString());
		// check cancel() on looping task 
		taskx.cancel();
		scheduler.fire(10);
		System.out.println(scheduler.toString());
		scheduler.fire(11);
		System.out.println(scheduler.toString());
		scheduler.fire(13);
		// AS EXPECTED
		System.out.println(scheduler.toString());
	}

}
