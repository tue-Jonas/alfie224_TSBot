package at.jonastuechler.services;

import at.jonastuechler.Main;
import com.github.theholywaffle.teamspeak3.TS3Api;

import java.util.*;

public class Qafk {

    private HashMap<String, Long> qafkUsers;
    private TS3Api api;

    public Qafk(TS3Api api) {
        qafkUsers = new HashMap<>();
        this.api = api;
    }

    public void listUser(String uuid) {
        qafkUsers.put(uuid, System.currentTimeMillis());
    }

    public void startService() {
        scheduler();
    }

    // service
    private void scheduler() {
        int minutes = Integer.parseInt(Main.config.getPropertyValue("teamspeak.afkChannel.quick.time"));

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (qafkUsers.isEmpty()) return;

                Iterator<Map.Entry<String, Long>> iterator = qafkUsers.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Long> entry = iterator.next();
                    String uuid = entry.getKey();
                    Long time = entry.getValue();

                    System.out.println("Checking user " + uuid);
                    // After time specified in config
                    if (System.currentTimeMillis() - time > 1000L * 60 * minutes) {
                        // kick the user from the server if he is still in the afk channel
                        if (api.getClientByUId(uuid).getChannelId() == Integer.parseInt(Main.config.getPropertyValue("teamspeak.afkChannel.quick"))) {
                            api.kickClientFromServer("You are afk for " + minutes + " minutes now.", api.getClientByUId(uuid));
                        }
                        // remove the user from the list
                        iterator.remove();
                    }
                }
            }
        }, 0, 1000 * 10);
    }

}
