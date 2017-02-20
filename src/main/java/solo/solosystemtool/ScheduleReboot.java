package solo.solosystemtool;

import java.io.File;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;

import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;

public class ScheduleReboot extends BaseService{
	
	public HashSet<Integer> dates = new HashSet<Integer>();
	public HashSet<String> times = new HashSet<String>();
	
	public boolean started = false;
	
	@SuppressWarnings({ "serial", "deprecation" })
	public ScheduleReboot(){
		Config config = new Config(new File(Main.getInstance().getDataFolder(), "scheduleReboot.yml"), Config.YAML, new LinkedHashMap<String, Object>(){{
			put("reboot-date", "월,화,수,목,금,토,일");
			put("reboot-time", "4:00,17:00");
		}});
		for(String date : ((String) config.getString("reboot-date")).split(",")){
			switch(date){
				case "월": this.dates.add(Calendar.MONDAY); break;
				case "화": this.dates.add(Calendar.TUESDAY); break;
				case "수": this.dates.add(Calendar.WEDNESDAY); break;
				case "목": this.dates.add(Calendar.THURSDAY); break;
				case "금": this.dates.add(Calendar.FRIDAY); break;
				case "토": this.dates.add(Calendar.SATURDAY); break;
				case "일": this.dates.add(Calendar.SUNDAY); break;
			}
		}
		for(String time : ((String) config.getString("reboot-time")).split(",")){
			this.times.add(time);
		}
	}
	
	@Override
	public void onStart(){
		Server.getInstance().getScheduler().scheduleRepeatingTask(new Task(){
			@Override
			public void onRun(int currentTick){
				if(check() && ! started){
					started = true;
					reboot();
				}
			}
		}, 40);
	}

	public boolean check(){
		Calendar calendar = Calendar.getInstance();
		return (
				this.dates.contains(calendar.get(Calendar.DAY_OF_WEEK)) &&
				this.times.contains(Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(calendar.get(Calendar.MINUTE)))
			);
	}
	
	public void reboot(){
		Server.getInstance().broadcastMessage("§a서버가 15초후 재부팅됩니다.");
		Server.getInstance().getScheduler().scheduleDelayedTask(new Task(){
			@Override
			public void onRun(int currentTick){
				Server.getInstance().broadcastMessage("§a서버가 재부팅됩니다...");
				Server.getInstance().getOnlinePlayers().values().forEach((p) -> { p.save(); p.kick("§a서버가 곧 재부팅됩니다. 10초후 재접속해주세요."); });
				Server.getInstance().getLevels().values().forEach((l) -> l.save(true));
				Server.getInstance().shutdown();
			}
		}, 300);
	}
}