package solo.solosystemtool;

import java.util.HashMap;

import cn.nukkit.plugin.PluginBase;

public class Main extends PluginBase{
	
	public static Main instance;
	
	public static Main getInstance(){
		return instance;
	}
	
	public HashMap<String, BaseService> services = new HashMap<String, BaseService>();
	
	@Override
	public void onLoad(){
		instance = this;
		this.getDataFolder().mkdirs();
		
		services.put("AutoGC", new AutoGC());
		services.put("ScheduleReboot", new ScheduleReboot());
	}
	
	@Override
	public void onEnable(){
		services.values().forEach((s) -> s.onStart());
	}
	
	@Override
	public void onDisable(){
		services.values().forEach((s) -> s.onStop());
	}
}