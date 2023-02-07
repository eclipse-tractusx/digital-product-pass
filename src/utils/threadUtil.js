// Copyright 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

export default {
  
  async executeWithTimeout(asyncPromise, timeLimit, callbackFunction){
    let timeoutHandle;

    const timeoutPromise = new Promise((_resolve, reject) => {
      timeoutHandle = setTimeout(
        () => reject(new Error('Async call timeout limit reached')),
        timeLimit
      );
    });

    return Promise.race([asyncPromise, timeoutPromise]).then(result => {
      clearTimeout(timeoutHandle);
      callbackFunction(result);
      return result;
    });
  }
};