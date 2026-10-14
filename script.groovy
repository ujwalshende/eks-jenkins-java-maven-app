def buildJar(){
    echo "building the application..."
    sh "mvn package"
}
def buildImage(){
    echo "building the adocker image..."
    withCredentials([usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]){
        sh 'docker build -t uds10/demo-app:jma-2.0 .'
        sh 'echo $PASS | docker login -u $USER --password-stdin'
        sh 'docker push uds10/demo-app:jma-2.0'
    }
}
def deployApp(){
    echo "deploying the application..."
}
return this
