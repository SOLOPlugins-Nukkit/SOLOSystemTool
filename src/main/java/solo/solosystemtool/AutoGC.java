package solo.solosystemtool;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.scheduler.Task;

public class AutoGC extends BaseService{
	
	public AutoGC(){
		
	}
	
	@Override
	public void onStart(){
		Server.getInstance().getScheduler().scheduleRepeatingTask(new Task(){
			@Override
			public void onRun(int currentTick){
				if(check()){
					doGC();
				}
			}
		}, 50);
	}
	
	public boolean check(){
		Runtime runtime = Runtime.getRuntime();
		double usage = (runtime.totalMemory() - runtime.freeMemory()) / runtime.maxMemory() * 100;
		return (usage > 85);
	}
	
	public void doGC(){
		long memory = Runtime.getRuntime().freeMemory();
		for (Level level : Server.getInstance().getLevels().values()){
			level.doChunkGarbageCollection();
			level.unloadChunks(true);
			level.clearCache(true);
		}
		System.gc();
		long freedMemory = Runtime.getRuntime().freeMemory() - memory;

		this.log("메모리가 부족하여 Garbage Collector를 실행하였습니다.");
		this.log("확보된 메모리 : " + NukkitMath.round((freedMemory / 1024d / 1024d), 2) + " MB");
	}
	
	@Override
	public void onStop(){
		
	}
}