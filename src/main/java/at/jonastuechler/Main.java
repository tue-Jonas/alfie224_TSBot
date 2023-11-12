package at.jonastuechler;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting the application...");

        System.out.println("Loading config...");
        AppConfig config = new AppConfig();
        String host = config.getPropertyValue("teamspeak.host");
        int port = Integer.parseInt(config.getPropertyValue("teamspeak.port"));
        String queryName = config.getPropertyValue("teamspeak.query.name");
        String queryPassword = config.getPropertyValue("teamspeak.query.password");
        String nickname = config.getPropertyValue("teamspeak.bot.nickname");
        System.out.println("Config loaded.");

        System.out.println("Connecting to teamspeak server...");
        Bot bot = new Bot();
        bot.connect(host, port, queryName, queryPassword);
        bot.setNickname(nickname);
        System.out.println("Connected to \"" + bot.getApi().getServerInfo().getName() + "\"");

        System.out.println("Application fully started.");
    }
}