package at.jonastuechler;

import at.jonastuechler.entities.AppConfig;
import at.jonastuechler.entities.Bot;
import at.jonastuechler.services.Qafk;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerQueryInfo;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static Bot bot;
    public static AppConfig config;
    public static Qafk qafk;

    public static void main(String[] args) {
        System.out.println("Starting the application...");

        // Load the config
        config = new AppConfig();
        String host = config.getPropertyValue("teamspeak.host");
        int port = Integer.parseInt(config.getPropertyValue("teamspeak.port"));
        String queryName = config.getPropertyValue("teamspeak.query.name");
        String queryPassword = config.getPropertyValue("teamspeak.query.password");
        String nickname = config.getPropertyValue("teamspeak.bot.nickname");
        System.out.println("Config loaded.");

        // Connect to the teamspeak server
        System.out.println("Connecting to teamspeak server...");
        bot = new Bot();
        bot.connect(host, port, queryName, queryPassword);
        bot.setNickname(nickname);
        System.out.println("Connected to \"" + bot.getApi().getServerInfo().getName() + "\"");

        // Shutdown hook for disconnecting the bot properly
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Application shutdown - disconnecting the bot properly...");
            bot.stop();
        }));

        // Initialize the events
        Events.loadEvents();
        System.out.println("Events loaded.");

        // Start the QAFK Service
        qafk = new Qafk(bot.getApi());
        qafk.startService();
        System.out.println("Quick AFK Service started.");

        // Set contact channel description
        setContactChannelDescription();
        System.out.println("Contact channel description set.");

        System.out.println("Application fully started.");
    }

    /**
     * Sets the contact channel description with a reference to the bot
     */
    private static void setContactChannelDescription() {
        int contactChannelId = Integer.parseInt(config.getPropertyValue("teamspeak.contactChannel"));

        ServerQueryInfo qi = bot.getApi().whoAmI();
        // [URL=client://35/2PchdVpDKFat86j04xQb+AQaCBc=~Alfie224-Bot]Alfie224-Bot[/URL]
        String description = String.format("[URL=client://%s/%s~%s]Click to contact the bot.[/URL]",
                qi.getId(),
                qi.getUniqueIdentifier(),
                qi.getNickname()
        );

        Map<ChannelProperty, String> channelProperties = new HashMap<>();
        channelProperties.put(ChannelProperty.CHANNEL_DESCRIPTION, description);

        bot.getApi().editChannel(contactChannelId, channelProperties);
    }
}