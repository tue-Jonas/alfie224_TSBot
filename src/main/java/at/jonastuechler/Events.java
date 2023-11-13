package at.jonastuechler;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import java.util.stream.Collectors;

/**
 * @author Jonas Tuechler
 */
public class Events {

    private static TS3Api api = Main.bot.getApi();

    public static void loadEvents() {
        api.registerAllEvents();
        api.addTS3Listeners(new TS3EventAdapter() {
            @Override
            public void onTextMessage(TextMessageEvent e) {
                if (e.getMessage().equalsIgnoreCase("!channels")) {
                    String channelList = "All channels:\n";
                    channelList += api.getChannels().stream()
                            .map(channel -> "- " + channel.getName() + " (" + channel.getId() + ")")
                            .collect(Collectors.joining("\n"));
                    api.sendPrivateMessage(e.getInvokerId(), channelList);
                }
            }
        });
    }

}
