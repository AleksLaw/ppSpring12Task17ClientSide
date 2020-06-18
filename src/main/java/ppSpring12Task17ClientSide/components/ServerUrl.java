package ppSpring12Task17ClientSide.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author AkiraRokudo on 04.05.2020 in one of sun day
 */
@Component
public class ServerUrl {

    @Value("${default.server.protocol}")
    private String serverProtocol;

    @Value("${default.server.domain}")
    private String serverDomain;

    @Value("${default.server.port}")
    private String serverPort;

    public String getServerProtocol() {
        return serverProtocol;
    }

    public String getServerDomain() {
        return serverDomain;
    }

    public String getServerPort() {
        return serverPort;
    }

    public String getServerUrl() {
        StringBuilder sb = new StringBuilder();
        return sb
                .append(getServerProtocol())
                .append("://")
                .append(getServerDomain())
                .append(":")
                .append(getServerPort())
                .toString();
    }
}
