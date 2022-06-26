package hw3;

/**
 * A type of priority queue to execute tasks based on time.
 * 
 * @author eran
 */
public interface Scheduler {
	
	/**
	 * Adds a {@code Runnable} to be executed at the specified time.
	 * 
	 * @param task the task to add to the scheduler
	 * @throws NullPointerException if {@code task} is null
	 * @throws IllegalArgumentException if time is negative
	 */
	public ScheduledTask scheduleAt(long time, Runnable task);
	
	/**
	 * Adds a {@code Runnable} to be executed every {@code time}. 
	 * The first execution is at the first time the fire method is invoked.
	 * The next execution is the specified time after the last execution.
	 * 
	 * For example: If you schedule a runnable to run every 1000.
	 * The first time it will run is when we called fire, say fire(2345).
	 * After it was invoked the next invocation should be at 3345. and so on. 
	 * 
	 * @param task the task to add to the scheduler.
	 * @param time the delta between consecutive invocations.
	 */
	public ScheduledTask scheduleEvery(long time, Runnable task);
	
	/**
	 * Executes all the tasks before or at the specified time.
	 * 
	 * @param time the time
	 */
	public void fire(long time);

}
