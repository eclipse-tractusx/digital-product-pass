package net.catenax.ce.materialpass.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catenax.ce.materialpass.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

@Service
public class VaultService {

    @Autowired
    private VaultTemplate vaultTemplate;

    public Object mapSecret(String secretPath, Class ClassType) {
        try {
            VaultResponse response = vaultTemplate.read(secretPath);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.convertValue(response.getData().get("data"), ClassType);
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"mapSecret",
                    e,
                    "It was not possible to map secret from vault.");
        }
    }
    public Object getSecret(String secretPath) {
        try {
            VaultResponse response = vaultTemplate.read(secretPath);
            return response.getData();
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getSecret",
                    e,
                    "It was not possible to get secret from vault.");
        }
    }

}
