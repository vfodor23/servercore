package hu.telcat.servercore.network.stress;

import hu.telcat.servercore.ServerCore;
import hu.telcat.servercore.network.packet.common.C01ServerConnectionStart;
import org.bukkit.Bukkit;

public class StressTest{

    public static boolean stop = true;
    public static int amount = 1;
    private static int timer;
    public static int sent = 0;

    public static void startStress() {
        timer = Bukkit.getScheduler().runTaskTimerAsynchronously(ServerCore.getInstance(), () -> {
            if(stop){
                return;
            }
            for(int i = 0; i < amount; i++){
                ServerCore.getInstance().getCerberusQueue().queuePacket(new C01ServerConnectionStart());
                sent++;
            }
        }, 0, 20L).getTaskId();
    }

    public static void setAmount(int i) {
        stop = false;
        amount = i;
    }

    public static void killStress() {
        Bukkit.getScheduler().cancelTask(timer);
    }

    public static void stopStress() {
        stop = true;
    }
}
