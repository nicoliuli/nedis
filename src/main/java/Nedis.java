import com.nedis.client.Client;
import com.nedis.config.ConfigUtil;
import com.nedis.model.RedisServerConfig;
import com.nedis.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Nedis {
    private Logger logger = LoggerFactory.getLogger(Nedis.class);
    public static void main(String[] args) throws Exception {
        // 启动server,连接redis
        List<RedisServerConfig> redisServerConfigList = ConfigUtil.config.getRedisServerConfigList();
        Server [] servers = new Server[redisServerConfigList.size()];
        for (int i = 0; i < redisServerConfigList.size(); i++) {
            Server aServer = new Server(redisServerConfigList.get(i).getIp(), redisServerConfigList.get(i).getPort());
            servers[i] = aServer;
            int serverChannel = ConfigUtil.config.getServerChannel();
            for (int j = 0; j < serverChannel; j++) {
                aServer.start();
            }
        }


        // 启动client，连接netty-server，和redis-cli
        Client client = new Client(ConfigUtil.config.getClientPort(), servers);
        client.start();

    }
}
