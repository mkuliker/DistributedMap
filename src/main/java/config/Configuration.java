package config;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Configuration {
    public static List<ServerInfo> servers;

    static {
        Yaml yaml = new Yaml();
        InputStream inputStream = Configuration.class
                .getClassLoader()
                .getResourceAsStream("application.yml");

        if (inputStream != null) {
            Map<String, Object> obj = yaml.load(inputStream);
            if (obj != null) {
                servers = ((List<Map<String, Object>>) obj.get("servers"))
                        .stream()
                        .map(ServerInfo::new)
                        .collect(Collectors.toList());
            }
        } else {
            System.out.println("No configuration file");
        }
    }

    public static class ServerInfo {
        String host;
        Integer port;

        public ServerInfo(String host, Integer port) {
            this.host = host;
            this.port = port;
        }

        public ServerInfo(Map<String, Object> stringObjectMap) {
            host = (String) stringObjectMap.get("host");
            port = (Integer) stringObjectMap.get("port");
        }

        public String getHost() {
            return host;
        }

        public Integer getPort() {
            return port;
        }

        @Override
        public String toString() {
            return host + ":" +port;
        }
    }
}
