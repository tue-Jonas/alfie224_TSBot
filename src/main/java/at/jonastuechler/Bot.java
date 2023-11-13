package at.jonastuechler;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;

/**
 * This class is used to initialize and configure a Bot
 *
 * @author Jonas Tuechler
 */
public class Bot {


    // FIELDS ---------------------------------------------------------------------------------------------------------

    private TS3Config config;
    private TS3Query query;
    private TS3Api api;


    // INSTANCE CREATION ----------------------------------------------------------------------------------------------

    /**
     * Creates the bot instance
     */
    public Bot() {
    }


    // METHODS --------------------------------------------------------------------------------------------------------

    /**
     * Connects the bot to the server
     *
     * @param host          server to be connected to
     * @param port          port of the server
     * @param queryName     query name to login with
     * @param queryPassword query password to login with
     */
    public void connect(String host, int port, String queryName, String queryPassword) {
        config = new TS3Config().setHost(host);
        config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());

        query = new TS3Query(config);
        query.connect();

        api = query.getApi();
        api.login(queryName, queryPassword);
        api.selectVirtualServerByPort(port);
    }

    /**
     * Sets the nickname of the bot
     * @param nickname name of the bot
     */
    public void setNickname(String nickname) {
        if (!api.whoAmI().getNickname().equals(nickname))
            api.setNickname(nickname);
    }

    /**
     * Stops the bot
     */
    public void stop() {
        api.logout();
        query.exit();
    }


    // ACCESSORS ------------------------------------------------------------------------------------------------------

    public TS3Config getConfig() {
        return config;
    }

    public TS3Query getQuery() {
        return query;
    }

    public TS3Api getApi() {
        return api;
    }
}