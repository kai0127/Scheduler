package hw3;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class SchedulerImpl implements Scheduler{
	protected List<Scheduled_Task> events;
	protected List<Scheduled_Task> isevery_events;
	
	public SchedulerImpl() {
		events = new LinkedList<>();
		isevery_events = new LinkedList<>();
	}
	
	/**
	 * Adds a {@code Runnable} to be executed at the specified time.
	 * 
	 * @param task the task to add to the scheduler
	 * 		  time used to set the scheduled_time
	 * @throws NullPointerException if {@code task} is null
	 * @throws IllegalArgumentException if time is negative
	 */
	@Override
	public Scheduled_Task scheduleAt(long time, Runnable task) {
		// TODO Auto-generated method stub
		Objects.requireNonNull(task, "Input Runnable is null.");
		if (time < 0) {
			throw new IllegalArgumentException("Input time is negative.");
		}
		else {
			Scheduled_Task scheduled_task = new Scheduled_Task(task,time);
			if (events.isEmpty()) {	
				events.add(scheduled_task);
				return scheduled_task;
			}
			// looking for the events that has lowest priority than our container.
			var index = 0;
			for (; index < events.size(); ++index) {
				if (events.get(index).get_scheduled_time() > time) {
					break;
				}
			}
			
			events.add(index, scheduled_task);
			return scheduled_task;
		}
		
	}
	/**
	 * Adds a {@code Runnable} to be executed at the specified time.
	 * 
	 * @param task the task to add to the scheduler
	 * 		  scheduled_time used to set the scheduled_time
	 * 		  loop_time used to set the loop_time
	 * @throws NullPointerException if {@code task} is null
	 * @throws IllegalArgumentException if time is negative
	 */
	
	public Scheduled_Task looping_task_scheduleAt(long scheduled_time, long loop_time,Runnable task) {
		// TODO Auto-generated method stub
		Objects.requireNonNull(task, "Input Runnable is null.");
		if (scheduled_time < 0 || loop_time < 0) {
			throw new IllegalArgumentException("Input time is negative.");
		}
		else {
			Scheduled_Task scheduled_task = new Scheduled_Task(loop_time,task);
			scheduled_task.set_scheduled_time(scheduled_time);
			if (events.isEmpty()) {	
				events.add(scheduled_task);
				return scheduled_task;
			}
			// looking for the events that has lowest priority than our container.
			var index = 0;
			for (; index < events.size(); ++index) {
				if (events.get(index).get_scheduled_time() > scheduled_time) {
					break;
				}
			}
			
			events.add(index, scheduled_task);
			return scheduled_task;
		}
		
	}
	
	/**
	 * Adds a {@code Runnable} to be executed at the specified time.
	 * 
	 * @param task the task to add to the scheduler
	 * 		  time used to set the  loop_time
	 * 		  
	 * @throws NullPointerException if {@code task} is null
	 * @throws IllegalArgumentException if time is negative
	 */
	@Override
	public Scheduled_Task scheduleEvery(long time, Runnable task) {
		// TODO Auto-generated method stub
		Objects.requireNonNull(task, "Input Runnable is null.");
		if (time < 0) {
			throw new IllegalArgumentException("Input time is negative.");
		}
		else {
			Scheduled_Task scheduled_task = new Scheduled_Task(time,task);
			isevery_events.add(scheduled_task);
			return scheduled_task;
		}
		
	}
	/**
	 * 1. run and remove the scheduled_task object stored in the -isevery_events-
	 * 	  list, then replace it in the -events- list.
	 * 
	 * 2. for every looping task stored in the -events- list that has a 
	 * 	  scheduled_time before or at the input -time-, run it and reschedule it.
	 * 
	 * 3. for "healthy" normal scheduled_task, run it and remove it from the
	 * 	  -events- list.
	 * 
	 * @param time used to set the  execute time
	 * 		  
	 * 
	 * @throws IllegalArgumentException if time is negative
	 */
	@Override
	public void fire(long time) {
		if (time < 0) {
			throw new IllegalArgumentException("Input time is negative.");
		}
		else {
			// first process isevery_events
			for (int i = 0; i< isevery_events.size(); i++) {
				if (isevery_events.get(0).isDone() ) {
					isevery_events.remove(0);
				}
				else {
					isevery_events.get(i).set_scheduled_time(time+isevery_events.get(i).get_loop_time());
					isevery_events.get(i).set_fire_time(time);
					Scheduled_Task temp_task = new Scheduled_Task(isevery_events.get(i).get_loop_time(),isevery_events.get(i).get_task());
					temp_task.set_scheduled_time(time);
					temp_task.set_fire_time(time);
					temp_task.run();
					
					if (events.isEmpty()) {	
						events.add(isevery_events.get(i));
						
					}
					else {
						var index = 0;
						for (; index < events.size(); ++index) {
							if (events.get(index).get_scheduled_time() > isevery_events.get(i).get_scheduled_time()) {
								break;
							}
						}
						
						events.add(index, isevery_events.get(i));
					}
					isevery_events.remove(i);
					
				}
				
			}
			// now isevery_events is empty
			while (!events.isEmpty() && events.get(0).get_scheduled_time() <= time) {
				// if the scheduled_task has already been cancelled or executed we simply
				// just remove it from the list
				if(events.get(0).isDone() ) {
					events.remove(0);
				}
				// if the scheduled_task hasn't yet been cancelled or executed but it's a 
				// looping task we remove it from the list and execute it. After that we 
				// schedule a new time for this task
				else if(events.get(0).get_isevery() == true) {
					
					Scheduled_Task temp_task = new Scheduled_Task(events.get(0).get_loop_time(),events.get(0).get_task());
					temp_task.set_scheduled_time(events.get(0).get_scheduled_time());
					temp_task.set_fire_time(time);
					events.get(0).set_scheduled_time(time+events.get(0).get_loop_time());
					events.get(0).set_fire_time(time);
					temp_task.run();
					
					if (events.isEmpty()) {	
						events.add(events.get(0));
						
					}
					else {
						var index = 0;
						for (; index < events.size(); ++index) {
							if (events.get(index).get_scheduled_time() > events.get(0).get_scheduled_time()) {
								break;
							}
						}
						
						events.add(index, events.get(0));
					}
					events.remove(0);
					
				}
				// if the scheduled_task is a healthy normal task, we simply remove it 
				// and execute it
				else {
					events.get(0).set_fire_time(time);
					events.remove(0).run();
				}
					
			}
		}
		
	}
	
	public String toString() {
		String string_events = "";
		if(this.events.size() == 0) {
			string_events = "NO element ";
		}
		else {
			for(int i = 0; i <this.events.size(); i++ ) {
				string_events += " " + this.events.get(i).get_scheduled_time() + "-"
						+ this.events.get(i).get_isevery() + "-" +this.events.get(i).get_iscancelled();
			}
		}
		
		String string_events_every = "";
		if(this.isevery_events.size() == 0) {
			string_events_every = " NO element";
		}
		else {
			for(int i = 0; i <this.isevery_events.size(); i++ ) {
				string_events_every += " " + this.isevery_events.get(i).get_scheduled_time()+
						"-" +this.isevery_events.get(i).get_loop_time()+
						"-" + this.isevery_events.get(i).get_isevery()
						+"-" + this.isevery_events.get(i).get_iscancelled();
			}
		}
		
		String total = "events list -> (scheduled time-looping-iscancelled) ||| isevery_events list -> (scheduled time-loop time-looping-iscancelled): \n" 
					+ string_events + " ||| " + "" + string_events_every + "\n";
		return total;
	}

}
