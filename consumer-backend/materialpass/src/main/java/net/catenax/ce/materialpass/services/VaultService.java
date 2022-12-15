package net.catenax.ce.materialpass.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catenax.ce.materialpass.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.ReactiveVaultTemplate;
import org.springframework.vault.support.VaultResponse;
import reactor.core.publisher.Mono;

@Service
public class VaultService {

    @Autowired
    private ReactiveVaultTemplate vaultTemplate;

    public Object mapSecret(String secretPath, Class ClassType) {
        try {
            Mono<VaultResponse> vaultResponse = vaultTemplate.read(secretPath);
            VaultResponse response = vaultResponse.block();
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
            Mono<VaultResponse> vaultResponse = vaultTemplate.read(secretPath);
            VaultResponse response = vaultResponse.block();
            return response.getData();
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getSecret",
                    e,
                    "It was not possible to get secret from vault.");
        }
    }

}
