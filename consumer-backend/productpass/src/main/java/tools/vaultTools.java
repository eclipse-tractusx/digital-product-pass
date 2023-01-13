package tools;

import tools.exceptions.ToolException;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class vaultTools {

    public static final String TEMPLATE_VAULT_FILE = "token: ''\nclient:\n  id: ''\n  secret: ''\napiKey: ''";

    public static final configTools configuration = new configTools();
    public static final String ATTRIBUTES_PATH_SEP = ".";
    public static final List<String> VAULT_ATTRIBUTES = List.of("token", "client.id", "client.secret", "apiKey");

    private static final String tokenFile = (String) configuration.getConfigurationParam("vault.file", ".", null);
    public static String createLocalVaultFile(){
        try {
            String dataDir = fileTools.createDataDir("VaultConfig");
            String filePath = Path.of(dataDir, tokenFile).toAbsolutePath().toString();
            if(!fileTools.pathExists(filePath)){
                logTools.printMessage("No vault token file found, creating yaml file in ["+filePath+"]");
                fileTools.toFile(filePath, TEMPLATE_VAULT_FILE, false); // Create YAML token file
                return filePath;
            }
            String fileContent = fileTools.readFile(filePath);
            if(fileContent == null || fileContent.isEmpty() || fileContent.isBlank()){
                logTools.printMessage("Vault token is empty, recreating yaml file in ["+filePath+"]");
                fileTools.toFile(filePath, TEMPLATE_VAULT_FILE, false); // Create YAML token file
            }

            Map<String, Object> vaultFileContent =  yamlTools.parseYml(fileContent);
            boolean update = false;
            for (String attribute: VAULT_ATTRIBUTES){
                if(jsonTools.getValue(vaultFileContent, attribute, ATTRIBUTES_PATH_SEP, null) != null) {
                    continue;
                }
                try {
                    vaultFileContent = (Map<String, Object>) jsonTools.setValue(vaultFileContent, attribute, "", ".", null);
                }catch (Exception e){
                    throw new ToolException(vaultTools.class,
                            e,
                            "It was not possible to set value in attribute "+attribute);
                }
                if(vaultFileContent == null){
                    throw new ToolException(vaultTools.class,
                            "It was not possible to set value in attribute "+attribute);
                }
                update = true;
            }

            if(update){
                fileTools.toFile(filePath, yamlTools.dumpYml(vaultFileContent), false); // Create YAML token file
            }





            return filePath;
        }catch (Exception e){
            throw new ToolException(vaultTools.class,
                    e,
                    "It was not possible to create secrets file");
        }
    }
}
