package Project;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.BDDAssertions;
/**
 * BDD test for Scheduler
 * 
 * @author kaiwen
 */
class Test_Scheduler_BDD {
	// test set-up
	SchedulerImpl scheduler = new SchedulerImpl();
	Runnable test_runnable = () -> System.out.println("test runnable");
	
	@Test //check exceptions for some important methods
	void methods_NPE_IAE_throw() {
		// when/then NPE for -scheduleAt- method
		BDDAssertions.thenThrownBy(() -> scheduler.scheduleAt(1, null))
			.isInstanceOf(NullPointerException.class);
		
		// when/then IAE for -scheduleAt- method
		BDDAssertions.thenThrownBy(() -> scheduler.scheduleAt(-1, test_runnable))
			.isInstanceOf(IllegalArgumentException.class);
		
		// when/then NPE for -looping_task_scheduleAt- method
		BDDAssertions.thenThrownBy(() -> scheduler.looping_task_scheduleAt(1, 1, null))
			.isInstanceOf(NullPointerException.class);
				
		// when/then IAE for -looping_task_scheduleAt- method
		BDDAssertions.thenThrownBy(() -> scheduler.looping_task_scheduleAt(-1, 1, test_runnable))
			.isInstanceOf(IllegalArgumentException.class);
		
		// when/then IAE for -looping_task_scheduleAt- method
		BDDAssertions.thenThrownBy(() -> scheduler.looping_task_scheduleAt(1, -1, test_runnable))
			.isInstanceOf(IllegalArgumentException.class);
		
		// when/then NPE for -scheduleEvery- method
		BDDAssertions.thenThrownBy(() -> scheduler.scheduleEvery(1, null))
			.isInstanceOf(NullPointerException.class);
				
		// when/then IAE for -scheduleEvery- method
		BDDAssertions.thenThrownBy(() -> scheduler.scheduleEvery(-1, test_runnable))
			.isInstanceOf(IllegalArgumentException.class);
		
		// when/then IAE for -fire- method
		BDDAssertions.thenThrownBy(() -> scheduler.fire(-1))
			.isInstanceOf(IllegalArgumentException.class);
	}
	@Test
	void adding_normal_task_and_loop_task() {
		
		// when : add normal runnable task1
		scheduler.scheduleAt(1,() -> System.out.print("task1"));
		 
		// then : check the state of -events- list and -isevery_events- list
		BDDAssertions.then(scheduler.toString()).isEqualTo("events list -> (scheduled time-looping-iscancelled) ||| isevery_events list -> (scheduled time-loop time-looping-iscancelled): \n"
				+ " 1-false-false |||  NO element" + "\n");
		// when : add looping runnable task1
		scheduler.scheduleEvery(3,() -> System.out.print("task3"));
		
		// then : check the state of -events- list and -isevery_events- list
		BDDAssertions.then(scheduler.toString()).isEqualTo("events list -> (scheduled time-looping-iscancelled) ||| isevery_events list -> (scheduled time-loop time-looping-iscancelled): \n"
				+ " 1-false-false |||  0-3-true-false" + "\n");
		
	}
	
	@Test
	void fire_normal_task_and_loop_task() {
		// for
		var output = new ByteArrayOutputStream();
		var testStream = new PrintStream(output);
		System.setOut(testStream);
		
		// for : schedule normal tasks task1 and task2
		scheduler.scheduleAt(1,() -> System.out.print("task1"));
		scheduler.scheduleAt(3,() -> System.out.print("task2"));
		
		// when : execute at time 2
		scheduler.fire(2);
		
		// then : task1 executed
		BDDAssertions.then(output.toString()).isEqualTo("Time: 2 Checking the status of task1 which is originally scheduled at 1\n");
		
		// for : schedule looping task task3
		scheduler.scheduleEvery(3,() -> System.out.print("task3"));
		
		// when : execute at time 4
		scheduler.fire(4);
		
		// then : task2, task3 executed
		BDDAssertions.then(output.toString()).isEqualTo("Time: 2 Checking the status of task1 which is originally scheduled at 1\n"+
				"Time: 4 Checking the status of task3 which is originally scheduled at 4\n" +
				"Time: 4 Checking the status of task2 which is originally scheduled at 3\n");
		
		// when : execute at time 6
		scheduler.fire(6);
				
		// then: doesn't change because the loop-task had been scheduled a new time
		// 		 that is 7
		BDDAssertions.then(output.toString()).isEqualTo("Time: 2 Checking the status of task1 which is originally scheduled at 1\n"+
				"Time: 4 Checking the status of task3 which is originally scheduled at 4\n" +
				"Time: 4 Checking the status of task2 which is originally scheduled at 3\n");
		
		// when: now fire at time 7
		scheduler.fire(7);
						
		// then: task3 executed
		BDDAssertions.then(output.toString()).isEqualTo("Time: 2 Checking the status of task1 which is originally scheduled at 1\n"+
				"Time: 4 Checking the status of task3 which is originally scheduled at 4\n" +
				"Time: 4 Checking the status of task2 which is originally scheduled at 3\n"
				+"Time: 7 Checking the status of task3 which is originally scheduled at 7\n");
		
		// We know the task3 now has been scheduled to time 10, what if we fire
		// time 11
		
		// when: now fire at time 11
		scheduler.fire(11);
								
		// then: AS EXPECTED: task3 executed
		BDDAssertions.then(output.toString()).isEqualTo("Time: 2 Checking the status of task1 which is originally scheduled at 1\n"+
				"Time: 4 Checking the status of task3 which is originally scheduled at 4\n" +
				"Time: 4 Checking the status of task2 which is originally scheduled at 3\n"
				+"Time: 7 Checking the status of task3 which is originally scheduled at 7\n"
				+"Time: 11 Checking the status of task3 which is originally scheduled at 10\n");
	}
	
	@Test
	void cancel_normal_task_and_loop_task() {
		// for
		var output = new ByteArrayOutputStream();
		var testStream = new PrintStream(output);
		System.setOut(testStream);
		
		// for : schedule 2 normal tasks, task1 and task2
		var task1 = scheduler.scheduleAt(1,() -> System.out.print("task1"));
		scheduler.scheduleAt(3,() -> System.out.print("task2"));
		
		// when: we know if we fire(2) now, task1 will be execute
		//       now if we cancel task1 and then fire(2), we should
		//       not see anything printing out, and its -iscancelled-
		//       should be true
		task1.cancel();
		// then : check the state of -events- list and -isevery_events- list
		BDDAssertions.then(scheduler.toString()).isEqualTo("events list -> (scheduled time-looping-iscancelled) ||| isevery_events list -> (scheduled time-loop time-looping-iscancelled): \n"
				+ " 1-false-true 3-false-false |||  NO element" + "\n");
		
		//when	: fire at time 2
		scheduler.fire(2);
		// then : check the state of -events- list and -isevery_events- list
		BDDAssertions.then(scheduler.toString()).isEqualTo("events list -> (scheduled time-looping-iscancelled) ||| isevery_events list -> (scheduled time-loop time-looping-iscancelled): \n"
				+ " 3-false-false |||  NO element" + "\n");
		// then
		BDDAssertions.then(output.toString()).isEqualTo("");
		
		// when: now we add a looping task
		var task3 = scheduler.scheduleEvery(3,() -> System.out.print("task3"));
		
		//then : check the state of -events- list and -isevery_events- list
		BDDAssertions.then(scheduler.toString()).isEqualTo("events list -> (scheduled time-looping-iscancelled) ||| isevery_events list -> (scheduled time-loop time-looping-iscancelled): \n"
				+ " 3-false-false |||  0-3-true-false" + "\n");
		
		// when  we know if we fire(4) now, task3 will be execute
		//       now if we cancel task3 and then fire(4), we should
		//       not see execution of task3 printing out, and its -iscancelled-
		//       should be true
		task3.cancel();
		
		//then : check the state of -events- list and -isevery_events- list
		BDDAssertions.then(scheduler.toString()).isEqualTo("events list -> (scheduled time-looping-iscancelled) ||| isevery_events list -> (scheduled time-loop time-looping-iscancelled): \n"
					+ " 3-false-false |||  0-3-true-true" + "\n");
		
		//when : fire at time 4
		scheduler.fire(4);
		
		//then : check the state of -events- list and -isevery_events- list
		BDDAssertions.then(scheduler.toString()).isEqualTo("events list -> (scheduled time-looping-iscancelled) ||| isevery_events list -> (scheduled time-loop time-looping-iscancelled): \n"
					+ "NO element  |||  NO element" + "\n");
		// then
		BDDAssertions.then(output.toString()).isEqualTo("Time: 4 Checking the status of task2 which is originally scheduled at 3\n");
		
		// what if we cancel a looping task when it's looping (not the first loop)
		// when: now we add a looping task
		var task4 = scheduler.scheduleEvery(4,() -> System.out.print("task4"));
				
		//then : check the state of -events- list and -isevery_events- list
		BDDAssertions.then(scheduler.toString()).isEqualTo("events list -> (scheduled time-looping-iscancelled) ||| isevery_events list -> (scheduled time-loop time-looping-iscancelled): \n"
				+ "NO element  |||  0-4-true-false" + "\n");
		
		//when : fire at time 6
		scheduler.fire(6);
		
		//then : check the state of -events- list and -isevery_events- list
		BDDAssertions.then(scheduler.toString()).isEqualTo("events list -> (scheduled time-looping-iscancelled) ||| isevery_events list -> (scheduled time-loop time-looping-iscancelled): \n"
				+ " 10-true-false |||  NO element" + "\n");
		// then
		BDDAssertions.then(output.toString()).isEqualTo("Time: 4 Checking the status of task2 which is originally scheduled at 3\n"
				+"Time: 6 Checking the status of task4 which is originally scheduled at 6\n");
				
				
		// when  we know if we fire(10) now, task4 will be execute
		//       now if we cancel task4 and then fire(10), we should
		//       not see execution of task4 printing out, and its -iscancelled-
		//       should be true
		task4.cancel();
		//then : check the state of -events- list and -isevery_events- list
		BDDAssertions.then(scheduler.toString()).isEqualTo("events list -> (scheduled time-looping-iscancelled) ||| isevery_events list -> (scheduled time-loop time-looping-iscancelled): \n"
						+ " 10-true-true |||  NO element" + "\n");	
		
			
		// when : fire at time 10
		scheduler.fire(10);
				
		// then : check the state of -events- list and -isevery_events- list
		BDDAssertions.then(scheduler.toString()).isEqualTo("events list -> (scheduled time-looping-iscancelled) ||| isevery_events list -> (scheduled time-loop time-looping-iscancelled): \n"
							+ "NO element  |||  NO element" + "\n");
		// then : 
		BDDAssertions.then(output.toString()).isEqualTo("Time: 4 Checking the status of task2 which is originally scheduled at 3\n"
				+"Time: 6 Checking the status of task4 which is originally scheduled at 6\n");
		
	}
}
