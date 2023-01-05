package net.catenax.ce.materialpass.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catenax.ce.materialpass.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import tools.*;

import java.util.Map;

@Service
public class VaultService {
    public static final configTools configuration = new configTools();
    @Autowired
    private VaultTemplate vaultTemplate;
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
            String filePath = vaultTools.createLocalVaultFile();
            Map<String, Object> content = yamlTools.readFile(filePath);
            try {
                secret = (String) jsonTools.getValue(content,localSecretPath, ".",null);
            }catch (Exception e){
                //logTools.printException(e, "["+this.getClass().getName()+"."+"getLocalSecret] " + "There was a error while searching the secret ["+localSecretPath+"] in file!");
                throw new ServiceException(this.getClass().getName()+"."+"getLocalSecret", e, "There was a error while searching the secret ["+localSecretPath+"] in file!");
            }
            if(secret == null){
                //logTools.printError("["+this.getClass().getName()+"."+"getLocalSecret] " + "Secret ["+localSecretPath+"] not found in file!");
                throw new ServiceException(this.getClass().getName()+"."+"getLocalSecret", "Secret ["+localSecretPath+"] not found in file!");
            }
            return secret;
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getLocalSecret",
                    e,
                    "It was not possible to get secret from file.");
        }
    }


}
