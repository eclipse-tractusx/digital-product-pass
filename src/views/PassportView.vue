<template>
  <Spinner v-if="loading" class="spinner-container" />
  <div v-else>
    <Header :batteryId="data.generalInformation" />
    <div class="pass-container">
      <GeneralInformation sectionTitle="General information" :generalInformation="data.generalInformation" />
      <BatteryComposition sectionTitle="Battery Composition" :batteryComposition="data.batteryComposition" />
      <StateOfHealth sectionTitle="State of Health" :stateOfHealth="data.stateOfHealth" />
      <ParametersOfTheBattery sectionTitle="Parameters of The Battery"
        :parametersOfTheBattery="data.parametersOfTheBattery" />
      <DismantlingProcedures sectionTitle="Dismantling procedures"
        :dismantlingProcedures="data.dismantlingProcedures" />
      <SafetyInformation sectionTitle="Safety information" :safetyMeasures="data.safetyMeasures" />
      <InformationResponsibleSourcing sectionTitle="Information responsible sourcing"
        :informationResponsibleSourcing="data.informationResponsibleSourcing" />

      <AdditionalInformation sectionTitle="Additional information"
        :additionalInformation="data.additionalInformation" />
    </div>
    <Footer />
  </div>
</template>

<script>
// @ is an alias to /src
import GeneralInformation from "@/components/GeneralInformation.vue";
import BatteryComposition from "@/components/BatteryComposition.vue";
import StateOfHealth from "@/components/StateOfHealth.vue";
import ParametersOfTheBattery from "@/components/ParametersOfTheBattery.vue";
import DismantlingProcedures from "@/components/DismantlingProcedures.vue";
import SafetyInformation from "@/components/SafetyInformation.vue";
import InformationResponsibleSourcing from "@/components/InformationResponsibleSourcing.vue";
import AdditionalInformation from "@/components/AdditionalInformation.vue";
import Spinner from "@/components/Spinner.vue";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import axios from "axios";
import { AAS_PROXY_URL } from "@/services/service.const";
import { SERVER_URL } from "@/services/service.const";
import { inject } from 'vue'

export default {
  name: "PassportView",
  components: {
    Header,
    GeneralInformation,
    BatteryComposition,
    StateOfHealth,
    ParametersOfTheBattery,
    DismantlingProcedures,
    SafetyInformation,
    InformationResponsibleSourcing,
    AdditionalInformation,
    Footer,
    Spinner,
  },

  data() {
    return {
      auth: inject('authentication'),
      data: {
        "meta": {
          "batteryId": 334593247,
          "passportCreatedDate": {
            "day": 12,
            "month": 5,
            "year": 2022
          },
          "issuer": {
            "name": "ImpBattMan GmbH",
            "address": {
              "street": "Street 9",
              "city": "Berlin",
              "postal": "12-345",
              "country": "Germany",
              "phoneNumber": "0056 345 567 456",
              "email": "contact@import.de"
            }
          }
        },
        "generalInformation": {
          "passportNumber": "12345",
          "batteryId": 334593247,
          "batteryType": "Lithium-Ion",
          "batteryModel": "2432-5665",
          "batteryProducer": "BattProducer",
          "chemistry": "Lithium nickel manganese cobalt (LiNMC)",
          "manufacuringDate": {
            "day": 12,
            "month": 5,
            "year": 2022
          },

          "manufacturingPlace": "No. 35, Xinjing Road, Haicang, Xiamen, China",
          "importerInformation": {
            "importerName": "ImpBattMan GmbH",
            "address": {
              "street": "Street 9",
              "city": "Berlin",
              "postal": "12-345",
              "country": "Germany",
              "phoneNumber": "0056 345 567 456",
              "email": "contact@import.de",
              "website": "www.impbattman.com"
            }
          },
          "placedToMarketDate": {
            "day": 12,
            "month": 5,
            "year": 2022
          },
          "batteryBatchNumber": 999999,
          "serial": "0815-4711",
          "stateOfHealth": 80,
          "stateOfCharge": 50,
          "warranty": "7 years",
          "status": "First life",
          "co2": {
            "co2FootprintTotalKG": {
              "value": 40,
              "unit": "kg"
            },
            "co2FootprintPerLifecycleKG": "5",
            "co2FootprintVerification": "this is the statement of an idependet third party",
            "co2FootprintMethodology": "CO2 metrics calculated in accordance with THIS methodology"
          },
          "dimensions": {
            "length": {
              "value": 40,
              "unit": "cm"
            },
            "height": {
              "value": 30,
              "unit": "cm"
            },
            "width": {
              "value": 20,
              "unit": "cm"
            }
          },
          "weight": {
            "value": 120,
            "unit": "kg"
          }
        },
        "batteryComposition": {
          "anodeContent": {
            "naturalGraphiteContent": {
              "value": 2450,
              "unit": "g"
            },
            "anodeComposition": ["Carboxymethyl cellulose", "Styren butadien"]
          },
          "LiContent": {
            "value": 7832,
            "unit": "g"
          },
          "NiContent": {
            "value": 1267,
            "unit": "g"
          },
          "CoContent": {
            "value": 367,
            "unit": "g"
          },
          "electrolyteComposition": [
            "LiPF6",
            "Ethylene Carbonate",
            "Dimethyl Carbonate- LiNO3"
          ],
          "cathodeComposition": {
            "liContent": {
              "value": 7832,
              "unit": "g"
            },
            "niContent": {
              "value": 1267,
              "unit": "g"
            },
            "coContent": {
              "value": 367,
              "unit": "g"
            },
            "otherCathodeComposition": ["cobalt", "manganese"]
          },
          "crm": ["Lithium", "Natural graphite", "Bauxite"],
          "niRecyclateContent": {
            "value": 12,
            "unit": "%"
          },
          "liRecyclateContent": {
            "value": 34,
            "unit": "%"
          },
          "coRecyclateContent": {
            "value": 5,
            "unit": "%"
          },
          "pbRecyclateContent": {
            "value": 2,
            "unit": "%"
          }
        },
        "stateOfHealth": {
          "remainingCapacity": {
            "value": 12,
            "unit": "%"
          },
          "overallCapacityFade": {
            "value": 88,
            "unit": "%"
          },
          "remainingPowerCapabilityAndPowerFade": {
            "value": 58,
            "unit": "%"
          },
          "remainingRoundTripEfficiency": {
            "value": 39,
            "unit": "%"
          },
          "actualCoolingDemand": {
            "value": 21,
            "unit": "%"
          },
          "evolutionOfSelfDischargingRates": {
            "value": 76,
            "unit": "%"
          },
          "ohmicResistanceAndOrElectrochemicalImpedance": {
            "value": 54,
            "unit": "Ω"
          },
          "theDatesOfManufacturingOfTheBattery": {
            "day": 12,
            "month": 5,
            "year": 2022
          },
          "theDatesOfPuttingBatteryIntoService": {
            "day": 22,
            "month": 7,
            "year": 2022
          },
          "energyThroughput": {
            "value": 59,
            "unit": "%"
          }
        },
        "parametersOfTheBattery": {
          "stateOfCharge": {
            "value": 12,
            "unit": "%"
          },
          "maximumAllowedBatteryPower": {
            "value": 8460,
            "unit": "W"
          },
          "minimalVoltageWithTemperatureRangesWhenRelevant": {
            "value": "680,2",
            "unit": "V",
            "tempRanges": {
              "min": {
                "value": "-0,5",
                "unit": "°C"
              },
              "max": {
                "value": 56,
                "unit": "°C"
              }
            },
            "refTest": "manual test"
          },
          "nominalVoltageWithTemperatureRangesWhenRelevant": {
            "value": "620,2",
            "unit": "V",
            "tempRanges": {
              "min": {
                "value": "-0,5",
                "unit": "°C"
              },
              "max": {
                "value": 56,
                "unit": "°C"
              }
            },
            "refTest": "manual test"
          },
          "maximumVoltageWithTemperatureRangesWhenRelevant": {
            "value": "710,2",
            "unit": "V",
            "tempRanges": {
              "min": {
                "value": "-0,5",
                "unit": "°C"
              },
              "max": {
                "value": 56,
                "unit": "°C"
              }
            },
            "refTest": "automatic test"
          },
          "originalPowerCapabilityWithTemperatureRangesWhenRelevant": {
            "value": "34",
            "unit": "W",
            "tempRanges": {
              "min": {
                "value": "-0,5",
                "unit": "°C"
              },
              "max": {
                "value": 56,
                "unit": "°C"
              }
            },
            "refTest": "manual test"
          },
          "capacityThresholdForExhaustion": {
            "value": 54,
            "unit": "%"
          },
          "temperatureRangeTheBatteryCanWithstandWhenNotInUse": {
            "tempRanges": {
              "min": {
                "value": "-0,5",
                "unit": "°C"
              },
              "max": {
                "value": 56,
                "unit": "°C"
              }
            },
            "refTest": "nuclear test"
          },
          "cRateOfRelevantCycleLifeTest": {
            "value": "7,4"
          },
          "energyRoundtripEfficiency": {
            "value": 59,
            "unit": "%"
          },
          "ratedCapacity": {
            "value": 59,
            "unit": "Ah"
          },
          "appliedDischargeRate": {
            "value": 1,
            "unit": "A"
          },
          "appliedChargeRate": {
            "value": 5,
            "unit": "A"
          },
          "capacityFade": {
            "value": 12,
            "unit": "%"
          },

          "originalPower": {
            "value": 50,
            "unit": "W"
          },
          "powerCapabilityAt80StateOfCharge": {
            "value": 30,
            "unit": "Wh"
          },

          "powerCapabilityAt20StateOfCharge": {
            "value": 15,
            "unit": "Wh"
          },

          "temperatureRanges": {
            "min": {
              "value": "-0,7",
              "unit": "°C"
            },
            "max": {
              "value": 70,
              "unit": "°C"
            }
          },

          "powerFade": {
            "value": 12,
            "unit": "%"
          },
          "maximumAllowedBatteryEnergy": {
            "value": 56,
            "unit": "Wh"
          },
          "ratioBetweenMaximumAllowedBatteryPowerWAndBatteryEnergyWh": {
            "value": 56,
            "unit": "Wh"
          },
          "internalResistanceCell": {
            "value": 12,
            "unit": "Ω"
          },
          "internalResistancePack": {
            "value": 54,
            "unit": "Ω"
          },
          "internalResistanceIncreasePack": {
            "value": 45,
            "unit": "%"
          },
          "expectedLifetime": {
            "value": "567 # of cycles",
            "unit": ""
          },
          "depthOfDischargeInTheCycleLifeTest": {
            "value": 12,
            "unit": "%"
          }
        },
        "dismantlingProcedures": {
          "vehicleDismantlingProcedure": {
            "value": "Vehicle dismantling procedure",
            "url": "https://www.google.com/"
          },
          "batteryDismantlingProcedure": {
            "value": "Battery dismantling procedure",
            "url": "https://www.google.com/"
          }
        },
        "safetyMeasures": {
          "occupationalSafety": {
            "value": "Occupational safety",
            "url": "https://www.google.com/"
          },
          "fireProtection": {
            "value": "Fire protection",
            "url": "https://www.google.com/"
          },
          "usableExtinguishingAgent": {
            "value": "Usable extinguishing agent",
            "url": "https://www.google.com/"
          },
          "instructionsForSafelyPackagingBatteries": {
            "value": "Instructions for safely packaging batteries",
            "url": "https://www.google.com/"
          },
          "instructionsForSafelyTransportingBatteries": {
            "value": "Instructions  for safely transporting batteries",
            "url": "https://www.google.com/"
          }
        },
        "informationResponsibleSourcing": {
          "responsibleRawMaterialsReport": {
            "value": "Responsible raw materials report",
            "url": "https://www.google.com/"
          }
        },
        "additionalInformation": {
          "linkToTheLabelElement": {
            "value": "Link to the label element",
            "url": "https://www.google.com/"
          },
          "symbolIndicatingSeparateCollectionAndHeavyMetalContent": {
            "value": "Cd",
            "icon": "trashcan"
          },
          "carbonFootprintDeclaration": {
            "value": "Link to the document",
            "url": "https://www.google.com/"
          },
          "carbonFootprintPerformanceClass": {
            "value": "A+++"
          },
          "informationAsAResultOfBatteryUse": {
            "value": "Link to the document",
            "url": "https://www.google.com/"
          },
          "preventionAndManagementOfWasteBatteriesInformation": {
            "value": "Link to the document",
            "url": "https://www.google.com/"
          },
          "euDeclarationOfConformity": {
            "value": "Link to the PDF document",
            "url": "https://www.google.com/"
          }
        },
        "specifications": {
          "power": {
            "EnergyTotal": "20",
            "EnergyUsable": "15",
            "PowerCapability80percent": "75",
            "PowerCapability20percent": "15"
          },
          "voltage": {
            "maxVoltage": "",
            "minVoltage": "",
            "nominalVoltage": ""
          },
          "internalResistance": 100,
          "weight": 15.6,

          "expectedLifetime": 10000,
          "temperature": {
            "OperationalTemperatureRange": "",
            "TemperatureRange": ""
          },
          "numberOfCells": 20
        },
        "recyclateContent": {
          "aluminiumPerc": 10,
          "leadPerc": 20,
          "lithiumPerc": 30,
          "cobaltPerc": 40
        },
        "handling": {
          "packagingInstructions": "ressourceLink",
          "transportationInstructions": "ressourceLink",
          "safetyInstructions": "ressourceLink",
          "personalProtectiveEquipment": "ressourceLink"
        },
        "CE": {
          "mark": "true",
          "notifiedBodyId": "1234asdf",
          "notifiedBodyName": "EU declaration of confirmity",
          "notifiedBodyAddress": "Street 1, 12345 Bruessels, Belgium",
          "issuedCertificate": "CertificationId"
        },
        "manufacturer": {
          "ame": "Panasonic Inc.",
          "brandName": "Panasonic",
          "gpdmId": 12345,
          "address": "Street 12, 1006 Kadoma, Japan",
          "phoneNumber": "12345-12345 234",
          "email": "emailadressofManufacturerin@BatteryPass.eu",
          "nationalID": "WTF",
          "TradeRegister": "hopefully not stored in batterypass",
          "manufacturingFacilityLocation": "Hintertupfing JWD"
        },
        "ESGDueDilligence": {
          "step1": "air",
          "step2": "water",
          "humanHealth": "",
          "humanRights": ""
        }
      }
      ,
      loading: false,
      errors: [],
    };
  },
  // methods: {
  //   getDigitalTwinId: function (assetIds) {

  //     let query = {

  //       "query": {
  //         "assetIds": [{
  //           "semanticId": {
  //             "value": ["urn:bamm:io.catenax.batch:1.0.0#Batch"]
  //           },
  //           "key": "ManufacturerId",
  //           "value": "BPNL00000003AXS3"
  //         }]
  //       }
  //     }

  //     const token = this.auth.getAccessToken();

  //     return new Promise((resolve) => {
  //       let encodedAssetIds = encodeURIComponent(assetIds);
  //       // .get(`${AAS_PROXY_URL}/lookup/shells?assetIds=${encodedAssetIds}`)
  //       axios
  //         .get(`${SERVER_URL}/registry/lookup/shells?assetIds=${encodedAssetIds}`, query,
  //           {
  //             headers: {
  //               Authorization: 'Bearer ' + token,
  //               accept: 'application/json'
  //             },
  //           }
  //           , query)
  //         .then((response) => {
  //           console.log("PassportView (Digital Twin):", response.data);
  //           resolve(response.data);
  //         })
  //         .catch((e) => {
  //           this.errors.push(e);
  //           resolve("rejected");
  //         });
  //     });
  //   },
  //   getDigitalTwinObjectById: function (digitalTwinId) {
  //     //const res =  axios.get("http://localhost:4243/registry/shell-descriptors/urn:uuid:365e6fbe-bb34-11ec-8422-0242ac120001"); // Without AAS Proxy
  //     return new Promise((resolve) => {
  //       axios
  //         .get(`${AAS_PROXY_URL}/registry/shell-descriptors/${digitalTwinId}`)
  //         .then((response) => {
  //           console.log("PassportView (Digital Twin Object):", response.data);
  //           resolve(response.data);
  //         })
  //         .catch((e) => {
  //           this.errors.push(e);
  //           resolve("rejected");
  //         });
  //     });
  //   },
  //   getSubmodelData: function (digitalTwin) {
  //     //const res =  axios.get("http://localhost:8193/api/service/urn:uuid:365e6fbe-bb34-11ec-8422-0242ac120001-urn:uuid:61125dc3-5e6f-4f4b-838d-447432b97919/submodel?provider-connector-url=http://provider-control-plane:8282"); // Without AAS Proxy
  //     //Calling with AAS Proxy
  //     return new Promise((resolve) => {
  //       axios
  //         .get(
  //           `${AAS_PROXY_URL}/shells/${digitalTwin.identification}/aas/${digitalTwin.submodelDescriptors[0].identification}/submodel?content=value&extent=withBlobValue`,
  //           {
  //             auth: {
  //               username: "someuser",
  //               password: "somepassword",
  //             },
  //           }
  //         )
  //         .then((response) => {
  //           console.log("PassportView (SubModel):", response.data);
  //           resolve(response.data);
  //         })
  //         .catch((e) => {
  //           this.errors.push(e);
  //           resolve("rejected");
  //         });
  //     });
  //   },
  //   async getPassport(assetIds) {
  //     const digitalTwinId = await this.getDigitalTwinId(assetIds);
  //     const digitalTwin = await this.getDigitalTwinObjectById(digitalTwinId);
  //     const response = await this.getSubmodelData(digitalTwin);
  //     return response;
  //   },
  // },
  // async created() {
  //   let assetIds = this.$route.params.assetIds;
  //   this.data = await this.getPassport(assetIds);
  //   this.loading = false;
  // },
};
</script>

<style>
.pass-container {
  width: 76%;
  margin: 0 12% 0 12%;
}

.spinner-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.spinner {
  margin: auto;
  width: 8vh;
  animation: rotate 3s infinite;
}

@keyframes rotate {
  100% {
    transform: rotate(360deg);
  }
}

@media (max-width: 750px) {
  .pass-container {
    width: 100%;
    margin: 0;
  }
}
</style>
