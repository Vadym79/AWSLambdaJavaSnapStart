0) Install Docker, Install git or use Cloud9 EC2 intance with those pre-installed

1) Install SDKMAN   (https://sdkman.io/)
curl -s "https://get.sdkman.io" | bash

source "/home/ec2-user/.sdkman/bin/sdkman-init.sh"

2) Install GraalVM 21  (https://www.graalvm.org/latest/docs/getting-started/linux/)
sdk install java 21.0.2-graal

3) Install Native Image  (https://www.graalvm.org/latest/reference-manual/native-image/)

sudo yum install gcc glibc-devel zlib-devel
sudo dnf install gcc glibc-devel zlib-devel libstdc++-static

4) Install Maven  (https://docs.aws.amazon.com/cloud9/latest/user-guide/sample-java.html#sample-java-sdk-maven)

sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
sudo yum install -y apache-maven