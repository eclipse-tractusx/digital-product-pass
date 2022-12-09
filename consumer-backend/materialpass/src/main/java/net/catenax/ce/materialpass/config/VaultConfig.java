package net.catenax.ce.materialpass.config;

import net.catenax.ce.materialpass.exceptions.ConfigException;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;
import tools.configTools;
import tools.fileTools;
import tools.yamlTools;

import java.net.URI;
import java.nio.file.Path;

@Configuration
public class VaultConfig extends AbstractVaultConfiguration {
    public static final configTools configuration = new configTools();
    private final String vaultUri = (String) configuration.getConfigurationParam("vault.uri", ".", null);
    public String dataDir;

    @Override
    public ClientAuthentication clientAuthentication() {
        try{
            this.dataDir = fileTools.createDataDir(this.getClass().getName());
            String path = Path.of()
            fileTools.fileExists()
            String vaultToken = yamlTools.readFile()
            return new TokenAuthentication();
        } catch (Exception e) {
            throw new ConfigException("VaultConfig.clientAuthentication", e, "It was not possible to set the ClientAuthentication Token");
        }
    }

    @Override
    public VaultEndpoint vaultEndpoint() {
        try {
            return VaultEndpoint.from(new URI(this.vaultUri));
        } catch (Exception e) {
            throw new ConfigException("VaultConfig.vaultEndpoint", e, "It was not possible to connect to the VaultEndpoint URI ["+this.vaultUri+"]");
        }
    }
}
