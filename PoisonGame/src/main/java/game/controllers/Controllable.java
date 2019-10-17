package game.controllers;

public interface Controllable {
	
	/**
	 *This interface is only used by scene controllers that the manager controller controls
	 *and will use to display scenes.
	 *
	 *This works so the controllables can have reference to the manager to call change methods. 
	 */
	
	void setManagerController(ManagerController mc);

}
