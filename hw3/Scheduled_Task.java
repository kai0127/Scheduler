package hw3;

public class Scheduled_Task implements ScheduledTask{
	// state variables
	private Runnable task;
	private long scheduled_time;
	private long loop_time;
	private long fire_time;
	private boolean isrun_ed;
	private boolean iscancelled;
	private boolean isevery;
	
	// default constructor
	public Scheduled_Task() {
		
	}
	// constructor for isevery task
	public Scheduled_Task(long loop_time,Runnable task) {
		this.task = task;
		this.loop_time = loop_time;
		this.isrun_ed = false;
		this.iscancelled = false;
		this.isevery = true;
	}
	
	//constructor for normal-specified-time task
	public Scheduled_Task(Runnable task, long scheduled_time) {
		this.task = task;
		this.scheduled_time = scheduled_time;
		this.isrun_ed = false;
		this.iscancelled = false;
		this.isevery = false;
	}
	/**
	 *  Getters
	 */
	public long get_scheduled_time() {
		return this.scheduled_time;
	}
	
	public long get_loop_time() {
		return this.loop_time;
	}
	
	public long get_fire_time() {
		return this.loop_time;
	}
	
	public boolean get_isrun_ed() {
		return this.isrun_ed;
	}
	
	public boolean get_iscancelled() {
		return this.iscancelled;
	}
	
	public boolean get_isevery() {
		return this.isevery;
	}
	
	public Runnable get_task() {
		return this.task;
	}
	/**
	 *  Setters
	 */
	public void set_scheduled_time(long scheduled_time) {
		this.scheduled_time = scheduled_time;
	}
	
	public void set_loop_time(long loop_time) {
		this.loop_time = loop_time;
	}
	
	public void set_fire_time(long fire_time) {
		this.fire_time = fire_time;
	}
	
	public void set_isrun_ed(boolean isrun_ed) {
		this.isrun_ed = isrun_ed;
	}
	
	public void set_iscancelled(boolean iscancelled) {
		this.iscancelled = iscancelled;
	}
	
	public void set_isevery(boolean isevery) {
		this.isevery = isevery;
	}
	
	public void set_task(Runnable task) {
		this.task = task;
	}
	/**
	 * Returns true if the corresponding task has already run or cancelled.
	 * 
	 * @return true if the corresponding task has already run
	 */
	public boolean isDone() {
		if(this.iscancelled == true || this.isrun_ed == true) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Cancels the task and it will never run.
	 */
	public void cancel() {
		this.iscancelled = true;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.isrun_ed = true;
		System.out.print("Time: " + this.fire_time + " Checking the status of ");
		this.task.run();
		System.out.println(" which is originally scheduled at " + this.scheduled_time);
	}
}
