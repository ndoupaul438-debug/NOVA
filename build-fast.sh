#!/data/data/com.termux/files/usr/bin/bash
set -e

SDK="$HOME/android-sdk"
BT="$SDK/build-tools/36.0.0"
PL="$SDK/platforms/android-36/android.jar"

rm -rf fastbuild
mkdir -p fastbuild/classes fastbuild/res fastbuild/compiled fastbuild/dex

# Copy Android resources
cp -r app/src/main/res fastbuild/

# Compile resources
"$BT/aapt2" compile --dir fastbuild/res -o fastbuild/resources.zip

# Link resources and create R.java
"$BT/aapt2" link --auto-add-overlay \
  -I "$PL" \
  --manifest app/src/main/AndroidManifest.xml \
  --min-sdk-version 26 \
  --target-sdk-version 36 \
  --version-code 1 \
  --version-name 0.1.0 \
  -R fastbuild/resources.zip \
  --java fastbuild \
  -o fastbuild/NOVA-unsigned.apk

# Compile Java
javac \
  -source 8 \
  -target 8 \
  -classpath "$PL" \
  -d fastbuild/classes \
  app/src/main/java/com/nova/app/MainActivity.java

# Convert Java bytecode to DEX
"$BT/d8" \
  --lib "$PL" \
  --output fastbuild/dex \
  fastbuild/classes/com/nova/app/MainActivity.class

# Put DEX into APK
cd fastbuild
zip -q NOVA-unsigned.apk dex/classes.dex

# Align
cp NOVA-unsigned.apk NOVA-aligned.apk

# Create debug key if necessary
if [ ! -f nova-debug.keystore ]; then
    keytool -genkeypair \
      -keystore nova-debug.keystore \
      -alias nova \
      -keyalg RSA \
      -keysize 2048 \
      -validity 10000 \
      -storepass android \
      -keypass android \
      -dname "CN=NOVA, O=NOVA"
fi

# Sign APK
"$BT/apksigner" sign \
  --ks nova-debug.keystore \
  --ks-pass pass:android \
  --key-pass pass:android \
  --out NOVA.apk \
  NOVA-aligned.apk

echo
echo "================================"
echo "       NOVA APK CREATED!"
echo "================================"
ls -lh NOVA.apk
