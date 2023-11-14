package at.jonastuechler;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
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
                if (e.getMessage().equalsIgnoreCase("!channels")) channelsCommand(e);
                if (e.getMessage().equalsIgnoreCase("!afk")) afkCommand(e);
                if (e.getMessage().equalsIgnoreCase("!qafk")) quickAfkCommand(e);
            }

            @Override
            public void onClientJoin(ClientJoinEvent e) {
                if (Main.config.getPropertyValue("teamspeak.welcomeMessage.active").equalsIgnoreCase("false")) return;
                String message = Main.config.getPropertyValue("teamspeak.welcomeMessage");
                message = message.replace("{name}", e.getClientNickname());
                api.sendPrivateMessage(e.getClientId(), message);
            }
        });
    }

    private static void channelsCommand(TextMessageEvent e) {
        String channelList = "All channels:\n";
        channelList += api.getChannels().stream()
                .map(channel -> "- " + channel.getName() + " (" + channel.getId() + ")")
                .collect(Collectors.joining("\n"));
        api.sendPrivateMessage(e.getInvokerId(), channelList);
    }

    private static void afkCommand(TextMessageEvent e) {
        api.moveClient(e.getInvokerId(), Integer.parseInt(Main.config.getPropertyValue("teamspeak.afkChannel")));
        api.sendPrivateMessage(e.getInvokerId(), "Moved you to the AFK channel.");
    }

    private static void quickAfkCommand(TextMessageEvent e) {
        Main.qafk.listUser(e.getInvokerUniqueId());
        api.moveClient(e.getInvokerId(), Integer.parseInt(Main.config.getPropertyValue("teamspeak.afkChannel.quick")));
        api.sendPrivateMessage(e.getInvokerId(), "Moved you to the quick AFK channel.");
    }

}
