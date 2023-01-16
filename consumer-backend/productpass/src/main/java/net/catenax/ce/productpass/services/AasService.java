package net.catenax.ce.productpass.services;

import net.catenax.ce.productpass.exceptions.ServiceException;
import net.catenax.ce.productpass.exceptions.ServiceInitializationException;
import net.catenax.ce.productpass.models.service.BaseService;
import net.catenax.ce.productpass.models.dtregistry.DigitalTwin;
import net.catenax.ce.productpass.models.auth.JwtToken;
import net.catenax.ce.productpass.models.dtregistry.SubModel;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.configTools;
import tools.httpTools;
import tools.jsonTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AasService extends BaseService {
    public static final configTools configuration = new configTools();
    public final String registryUrl = (String) configuration.getConfigurationParam("variables.registryUrl", ".", null);
    @Autowired
    private AuthenticationService authService;

    public AasService() throws ServiceInitializationException {
        this.checkEmptyVariables();
    }

    @Override
    public List<String> getEmptyVariables() {
        List<String> missingVariables = new ArrayList<>();
        if (registryUrl == null || registryUrl.isEmpty()) {
            missingVariables.add("registryUrl");
        }
        return missingVariables;
    }
    public SubModel searchSubModelInDigitalTwin(String assetType,String assetId, Integer position){
        try {
            ArrayList<String> digitalTwinIds = this.queryDigitalTwin(assetType, assetId);
            if(digitalTwinIds==null || digitalTwinIds.size()==0){
                throw new ServiceException(this.getClass().getName() + "." + "getIdShort",
                        "It was not possible to get digital twin for the selected asset type and the the selected assetId");
            }
            if(position > digitalTwinIds.size()){
                throw new ServiceException(this.getClass().getName() + "." + "getIdShort",
                        "It was not possible to get digital twin in the selected position for the selected asset type and the the selected assetId");
            }

            String digitalTwinId = digitalTwinIds.get(position);
            DigitalTwin digitalTwin = this.getDigitalTwin(digitalTwinId);
            if(digitalTwin == null){
                throw new ServiceException(this.getClass().getName() + "." + "getIdShort",
                        "It was not possible to get digital twin in the selected position for the selected asset type and the the selected assetId");
            }
            SubModel subModel = this.getSubModelFromDigitalTwin(digitalTwin, position);
            if(subModel == null){
                throw new ServiceException(this.getClass().getName() + "." + "getIdShort",
                        "It was not possible to get digital twin in the selected position for the selected asset type and the the selected assetId");
            }
            return subModel;
        }
        catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getIdShort",
                    e,
                    "It was not possible to get subModel!");
        }
    }

    public SubModel searchSubModel(String assetType,String assetId, Integer position){
        try {
            ArrayList<String> digitalTwinIds = this.queryDigitalTwin(assetType, assetId);
            if(digitalTwinIds==null || digitalTwinIds.size()==0){
                throw new ServiceException(this.getClass().getName() + "." + "getIdShort",
                        "It was not possible to find digital twin for asset type: " + assetType + " and assetId "+assetId);
            }
            if(position > digitalTwinIds.size()){
                throw new ServiceException(this.getClass().getName() + "." + "getIdShort",
                        "It was not possible to get the digital twin id in position "+position+" for asset type: " + assetType + " and assetId "+assetId);
            }

            String digitalTwinId = digitalTwinIds.get(position);
            DigitalTwin digitalTwin = this.getDigitalTwin(digitalTwinId);
            if(digitalTwin == null){
                throw new ServiceException(this.getClass().getName() + "." + "getIdShort",
                        "It was not possible to get digital twin in position: " + position + " for asset type: " + assetType + " and assetId " + assetId);
            }
            SubModel subModel = this.getSubModel(digitalTwin, position);
            if(subModel == null){
                throw new ServiceException(this.getClass().getName() + "." + "getIdShort",
                        "It was not possible to get subModel of digitalTwin  in position: " + position + " for asset type: " + assetType + " and assetId " + assetId);
            }
            return subModel;
        }
        catch (Exception e) {
                throw new ServiceException(this.getClass().getName() + "." + "getIdShort",
                        e,
                        "It was not possible to get idShort!");
            }
    }


    public DigitalTwin getDigitalTwin(String digitalTwinId) {
            try {
                String path = "/registry/registry/shell-descriptors";
                String url = registryUrl + path + "/" + digitalTwinId;
                Map<String, Object> params = httpTools.getParams();
                JwtToken token = authService.getToken();
                HttpHeaders headers = httpTools.getHeadersWithToken(token.getAccessToken());
                ResponseEntity<?> response = httpTools.doGet(url, String.class, headers, params, true, false);
                String responseBody = (String) response.getBody();
                return (DigitalTwin) jsonTools.bindJsonNode(jsonTools.toJsonNode(responseBody), DigitalTwin.class);
            } catch (Exception e) {
                throw new ServiceException(this.getClass().getName() + "." + "queryDigitalTwin",
                        e,
                        "It was not possible to get digital twin!");
            }

    }

    public SubModel getSubModelFromDigitalTwin(DigitalTwin digitalTwin, Integer position) {
        try {
            ArrayList<SubModel> subModels = digitalTwin.getSubmodelDescriptors();
            if (position > subModels.size()) {
                throw new ServiceException(this.getClass().getName() + "." + "getSubModelFromDigitalTwin",
                        "Position selected for subModel is out of range!");
            }
            return subModels.get(position);
        }
        catch (Exception e) {
                throw new ServiceException(this.getClass().getName() + "." + "getSubModelFromDigitalTwin",
                        e,
                        "It was not possible to get subModel from digital twin!");
            }
    }

    public SubModel getSubModel(DigitalTwin digitalTwin, Integer position) {
        try {
            String path = "/registry/registry/shell-descriptors";
            SubModel subModel = this.getSubModelFromDigitalTwin(digitalTwin, position);
            String url = registryUrl + path + "/" + digitalTwin.getIdentification() + "/submodel-descriptors/" + subModel.getIdentification();
            Map<String, Object> params = httpTools.getParams();
            JwtToken token = authService.getToken();
            HttpHeaders headers = httpTools.getHeadersWithToken(token.getAccessToken());
            ResponseEntity<?> response = httpTools.doGet(url, String.class, headers, params, true, false);
            String responseBody = (String) response.getBody();
            return (SubModel) jsonTools.bindJsonNode(jsonTools.toJsonNode(responseBody), SubModel.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getSubModel",
                    e,
                    "It was not possible to get subModel!");
        }
    }
    public SubModel getSubModel(String digitalTwinId, String subModelId) {
        try {
            String path = "/registry/registry/shell-descriptors";
            String url = registryUrl + path + "/" + digitalTwinId + "/submodel-descriptors/" + subModelId;
            Map<String, Object> params = httpTools.getParams();
            JwtToken token = authService.getToken();
            HttpHeaders headers = httpTools.getHeadersWithToken(token.getAccessToken());
            ResponseEntity<?> response = httpTools.doGet(url, String.class, headers, params, true, false);
            String responseBody = (String) response.getBody();
            return (SubModel) jsonTools.bindJsonNode(jsonTools.toJsonNode(responseBody), SubModel.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getSubModel",
                    e,
                    "It was not possible to get subModel!");
        }
    }
    public ArrayList<String> queryDigitalTwin(String assetType,String assetId) {
        try {
            String path = "/registry/lookup/shells";
            String url = registryUrl + path;
            Map<String, Object> params = httpTools.getParams();
            Map<String, ?> assetIds = Map.of(
                    "key", assetType,
                    "value", assetId
            );
            JwtToken token = authService.getToken();
            HttpHeaders headers = httpTools.getHeadersWithToken(token.getAccessToken());
            String jsonString = jsonTools.dumpJson(new JSONObject(assetIds),0);
            params.put("assetIds", jsonString);
            ResponseEntity<?> response = httpTools.doGet(url, ArrayList.class, headers, params, true, false);
            ArrayList<String> responseBody = (ArrayList<String>) response.getBody();
            return responseBody;

        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "queryDigitalTwinString",
                    e,
                    "It was not possible to retrieve shell");
        }
    }

    public class DigitalTwinRegistryQuery implements Runnable{
        private volatile SubModel subModel;
        private final String assetId;
        private final String idType;

        private final Integer index;

        public DigitalTwinRegistryQuery(String assetId,String idTyp, Integer index){
            this.assetId = assetId;
            this.idType = idTyp;
            this.index = index;
        }

        @Override
        public void run() {
            this.subModel = searchSubModel(this.idType, this.assetId, this.index);
        }

        public SubModel getSubModel() {
            return this.subModel;
        }

    }

}
