# Copyright 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#     http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

ROOT_DIR=/usr/share/nginx/html

echo "Replacing docker environment constants in JavaScript files"

for file in $ROOT_DIR/assets/index-*.js* $ROOT_DIR/index.html;
do
	echo "Processing $file ...";
	sed -i 's|VUE_APP_CLIENT_ID|'${VUE_APP_CLIENT_ID}'|g' $file
	sed -i 's|VUE_APP_CLIENT_SECRET|'${VUE_APP_CLIENT_SECRET}'|g' $file
	sed -i 's|X_API_KEY|'${X_API_KEY}'|g' $file
	sed -i 's|APP_BACK|'${BACKEND}'|g' $file
	sed -i 's|APP_VER|'${VERSION}'|g' $file
	sed -i 's|APP_API_TIMEOUT|'${API_TIMEOUT}'|g' $file
	sed -i 's|APP_API_DELAY|'${API_DELAY}'|g' $file
	sed -i 's|APP_API_MAX_RETRIES|'${API_MAX_RETRIES}'|g' $file

done

exec "$@"
