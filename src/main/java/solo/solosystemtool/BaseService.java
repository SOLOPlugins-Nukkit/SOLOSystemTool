package solo.solosystemtool;

import cn.nukkit.Server;

public abstract class BaseService{
	
	public void onStart(){
		
	}
	
	public void onStop(){
		
	}
	
	public void log(String message){
		Server.getInstance().getLogger().info("§d[" + this.getClass().getName() + "] " + message);
	}
	
}