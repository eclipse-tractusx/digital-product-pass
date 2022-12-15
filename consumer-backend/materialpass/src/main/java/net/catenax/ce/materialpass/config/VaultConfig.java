package net.catenax.ce.materialpass.config;

import net.catenax.ce.materialpass.exceptions.ConfigException;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractReactiveVaultConfiguration;
import tools.*;

import java.net.URI;
import java.nio.file.Path;
import java.util.Map;

@Configuration
public class VaultConfig extends AbstractReactiveVaultConfiguration {

    public static final configTools configuration = new configTools();
    private final String vaultUri = (String) configuration.getConfigurationParam("vault.uri", ".", null);
    private final String tokenFile = (String) configuration.getConfigurationParam("vault.token-file", ".", null);
    public String dataDir;

    @Override
    public ClientAuthentication clientAuthentication() {
        try{
            this.dataDir = fileTools.createDataDir("VaultConfig");
            String filePath = Path.of(this.dataDir, tokenFile).toAbsolutePath().toString();
            if(!fileTools.pathExists(filePath)){
                logTools.printMessage("No vault token file found, creating yaml file in ["+filePath+"]");
                fileTools.toFile(filePath, "token: ''", false); // Create YAML token file
            };

            Map<String, Object> content = yamlTools.readFile(filePath);
            String token = (String) content.get("token");
            if(stringTools.isEmpty(token)){
                logTools.printFatalLog("Please add the Vault token to ["+filePath+"] file, in order to start the application.");
                throw new ConfigException("VaultConfig.clientAuthentication", "Token field is empty in ["+filePath+"], no token is set.");
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
