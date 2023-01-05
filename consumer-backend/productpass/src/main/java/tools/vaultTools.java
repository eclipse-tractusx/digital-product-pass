package tools;

import tools.exceptions.ToolException;

import java.nio.file.Path;

public final class vaultTools {

    public static final String TEMPLATE_VAULT_FILE = "token: ''\nclient:\n  id: ''\n  secret: ''\napiKey: ''";

    public static final configTools configuration = new configTools();
    private static final String tokenFile = (String) configuration.getConfigurationParam("vault.file", ".", null);
    public static String createLocalVaultFile(){
        try {
            String dataDir = fileTools.createDataDir("VaultConfig");
            String filePath = Path.of(dataDir, tokenFile).toAbsolutePath().toString();
            if(!fileTools.pathExists(filePath)){
                logTools.printMessage("No vault token file found, creating yaml file in ["+filePath+"]");
                fileTools.toFile(filePath, TEMPLATE_VAULT_FILE, false); // Create YAML token file
            };
            return filePath;
        }catch (Exception e){
            throw new ToolException(vaultTools.class,
                    e,
                    "It was not possible to create secrets file");
        }
    }
}
