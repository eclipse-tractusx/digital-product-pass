package net.catenax.ce.materialpass.services;

import net.catenax.ce.materialpass.exceptions.ServiceException;
import net.catenax.ce.materialpass.models.Catalog;
import org.springframework.stereotype.Service;
import tools.configTools;
import tools.httpTools;

import java.util.Map;

@Service
public class AasService {
    public static final configTools configuration = new configTools();
    public final String serverUrl = (String) configuration.getConfigurationParam("variables.serverUrl", ".", null);
    public final String APIKey = (String) configuration.getConfigurationParam("variables.apiKey", ".", null);
    public final String registryUrl = (String) configuration.getConfigurationParam("variables.registryUrl", ".", null);

    public Catalog queryDigitalTwin() {
        try {
            String path = " /registry/lookup/shells";
            String url = registryUrl + path;
            Map<String, Object> params = httpTools.getParams();
            params.put("assetIds","");

        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "queryDigitalTwin",
                    e,
                    "It was not possible to retrieve shell");
        }
    }



}
