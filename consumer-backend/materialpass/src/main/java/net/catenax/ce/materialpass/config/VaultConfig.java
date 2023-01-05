package net.catenax.ce.materialpass.config;

import net.catenax.ce.materialpass.exceptions.ConfigException;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;
import tools.*;

import java.net.URI;
import java.util.Map;

@Configuration
public class VaultConfig extends AbstractVaultConfiguration {

    public static final configTools configuration = new configTools();
    private final String vaultType = (String) configuration.getConfigurationParam("vault.type", ".", null);
    private final String vaultUri = (String) configuration.getConfigurationParam("vault.uri", ".", null);
    private final String tokenFile = (String) configuration.getConfigurationParam("vault.file", ".", null);
    public String dataDir;

    @Override
    public ClientAuthentication clientAuthentication() {
        try{
            this.dataDir = fileTools.createDataDir("VaultConfig");
            String filePath = vaultTools.createLocalVaultFile();

            Map<String, Object> content = yamlTools.readFile(filePath);
            String token = (String) content.get("token");
            if(stringTools.isEmpty(token)){
                logTools.printFatal("Please add the Vault token to ["+filePath+"] file, in order to start the application.");
                //throw new ConfigException("VaultConfig.clientAuthentication", "Token field is empty in ["+filePath+"], no token is set.");
                token = "00000000000-00000000000000000-00000000000";
            }
            return (TokenAuthentication) new TokenAuthentication(token);
        } catch (Exception e) {
            throw new ConfigException("VaultConfig.clientAuthentication", e, "It was not possible to set the ClientAuthentication Token");
        }
    }

    @Override
    public VaultEndpoint vaultEndpoint() {
        try {
            return VaultEndpoint.from(new URI(vaultUri));
        } catch (Exception e) {
            throw new ConfigException("VaultConfig.vaultEndpoint", e, "It was not possible to connect to the VaultEndpoint URI ["+this.vaultUri+"]");
        }
    }
}
