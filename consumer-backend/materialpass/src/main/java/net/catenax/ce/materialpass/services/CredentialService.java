package net.catenax.ce.materialpass.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catenax.ce.materialpass.models.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

@Service
public class CredentialService {

    @Autowired
    private VaultTemplate vaultTemplate;

    public Credential accessCredentials() {

        VaultResponse response = vaultTemplate.read("");
        ObjectMapper objectMapper = new ObjectMapper();
        assert response != null;
        return objectMapper.convertValue(response.getData().get("data"), Credential.class);
    }

}
