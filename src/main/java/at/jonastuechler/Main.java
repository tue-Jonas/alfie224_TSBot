package at.jonastuechler;

import at.jonastuechler.entities.AppConfig;
import at.jonastuechler.entities.Bot;
import at.jonastuechler.services.Qafk;

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

        // Initialize the events
        Events.loadEvents();
        System.out.println("Events loaded.");

        // Start the QAFK Service
        qafk = new Qafk(bot.getApi());
        qafk.startService();
        System.out.println("Quick AFK Service started.");

        // Shutdown hook for disconnecting the bot properly
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Application shutdown - disconnecting the bot properly...");
            bot.stop();
        }));

        System.out.println("Application fully started.");
    }
}