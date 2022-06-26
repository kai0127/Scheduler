package Project;

/**
 * A container for a timed task.
 * 
 * @author eran
 */
public interface ScheduledTask extends Runnable {
	
	/**
	 * Returns true if the corresponding task has already run or cancelled.
	 * 
	 * @return true if the corresponding task has already run
	 */
	public boolean isDone();
	
	/**
	 * Cancels the task and it will never run.
	 */
	public void cancel();

}
