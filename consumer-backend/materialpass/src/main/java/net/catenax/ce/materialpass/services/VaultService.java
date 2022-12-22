package net.catenax.ce.materialpass.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catenax.ce.materialpass.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import tools.configTools;
import tools.fileTools;
import tools.logTools;
import tools.yamlTools;

import java.nio.file.Path;
import java.util.Map;

@Service
public class VaultService {
    public static final configTools configuration = new configTools();
    @Autowired
    private VaultTemplate vaultTemplate;
    private final String tokenFile = (String) configuration.getConfigurationParam("vault.token-file", ".", null);
    public Object mapSecret(String secretPath, Class ClassType) {
        try {
            VaultResponse vaultResponse = vaultTemplate.read(secretPath);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(vaultResponse.getData().get("data"), ClassType);
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"mapSecret",
                    e,
                    "It was not possible to map secret from vault.");
        }
    }
    public Object getSecret(String secretPath) {
        try {
            VaultResponse vaultResponse = vaultTemplate.read(secretPath);
            return vaultResponse.getData().get("data");
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getSecret",
                    e,
                    "It was not possible to get secret from vault.");
        }
    }

    public Object getLocalSecret(String localSecretPath) {
        try {
            String secret = null;
            String dataDir = fileTools.createDataDir("VaultConfig");
            String filePath = Path.of(dataDir, tokenFile).toAbsolutePath().toString();
            if(!fileTools.pathExists(filePath)){
                logTools.printMessage("No vault token file found, creating yaml file in ["+filePath+"]");
                fileTools.toFile(filePath, "token: ''\nclient: \n\tid: ''\n\tsecret: ''", false); // Create YAML token file
            };

            Map<String, Object> content = yamlTools.readFile(filePath);
            try {
                secret = (String) content.get(localSecretPath);
            }catch (Exception e){
                throw new ServiceException(this.getClass().getName()+"."+"getLocalSecret", e, "It was not possible to get secrets credentials from file");
            }
            return secret;
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getLocalSecret",
                    e,
                    "It was not possible to get secret from file.");
        }
    }

}
