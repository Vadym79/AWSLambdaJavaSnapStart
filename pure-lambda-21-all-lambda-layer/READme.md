mvn clean package  
mkdir java  
mkdir java/lib  
cp -r target/aws-pure-java-21-all-lambda-layer-1.0.0-SNAPSHOT.jar java/lib/  
zip -r aws-pure-java-21-all-lambda-layer-content.zip java  


aws lambda publish-layer-version --layer-name aws-pure-java-21-all-lambda-layer --zip-file fileb://aws-pure-java-21-all-lambda-layer-content.zip --compatible-runtimes java21  

