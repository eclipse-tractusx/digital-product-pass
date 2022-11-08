ROOT_DIR=/usr/share/nginx/html

echo "Replacing docker environment constants in JavaScript files"

for file in $ROOT_DIR/js/app.*.js* $ROOT_DIR/index.html;
do
	echo "Processing $file ...";
	sed -i 's|VUE_APP_CLIENT_ID|'${VUE_APP_CLIENT_ID}'|g' $file
	sed -i 's|VUE_APP_CLIENT_SECRET|'${VUE_APP_CLIENT_SECRET}'|g' $file
	sed -i 's|X_API_KEY|'${X_API_KEY}'|g' $file
done

exec "$@"
