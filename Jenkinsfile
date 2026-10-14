pipeline{
    agent any
    tools{
        maven 'maven-3.9.16'
        
    }
    stages {
        stage("increment version") {
            steps{
                script{
                    echo "incrementing app version..."
                    sh 'mvn build-helper:parse-version versions:set \
                    -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                    versions:commit'
                    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
                    def version = matcher[0][1]
                    env.IMAGE_NAME = "$version-$BUILD_NUMBER" 
                }
            }
        }
        stage("build jar") {
            steps{
                script{
                    echo "building the application..."
                    sh "mvn clean package"

                }
            }
        }
        stage("build image") {
            steps{
                script{
                    echo "building the adocker image..."
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]){
                        sh "docker build -t uds10/demo-app:${IMAGE_NAME} ."
                        sh 'echo $PASS | docker login -u $USER --password-stdin'
                        sh "docker push uds10/demo-app:${IMAGE_NAME}"
                    }

                }
            }
        }
        stage("deploy"){
            environment{
                AWS_ACCESS_KEY_ID = credentials('jenkins_aws_access_key_id')
                AWS_SECRET_ACCESS_KEY = credentials('jenkins_aws_secret_access_key')
                APP_NAME = 'java-maven-app'
            }
                
            steps {
                script {
                    echo "deploying the app..."
                    sh 'envsubst < kubernetes/deployment.yaml | kubectl apply -f '
                    sh 'envsubst < kubernetes/service.yaml | kubectl apply -f '
                }
            } 
        }      
        stage('commit version update'){
            steps{
                script{
                    withCredentials([usernamePassword(credentialsId: 'github-password', passwordVariable: 'PASS', usernameVariable: 'USER'), string(credentialsId: 'gitlab-token', variable: 'GITHUB_TOKEN')]){
                        sh 'git config user.email "jenkins@example.com"'
                        sh 'git config user.name "jenkins"'
                        sh 'git status'
                        sh 'git branch'
                        sh 'git config --list'
                        sh "git remote set-url origin https://${USER}:${GITHUB_TOKEN}@github.com/ujwalshende/eks-jenkins-java-maven-app.git"
                        sh "git add ."
                        sh 'git commit -m "ci: version bump"'
                        sh "git push origin HEAD:deploy-on-k8s"
                    }
                }
            }
        }
    }
}