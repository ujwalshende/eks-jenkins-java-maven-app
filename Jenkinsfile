def gv

pipeline {   
    agent any
    tools {
        maven 'maven-3.9.16'
    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage("build jar") {
            steps {
                script {
                    gv.buildJar()

                }
            }
        }

        stage("build image") {
            steps {
                script {
                    gv.buildImage()
                }
            }
        }

        stage("deploy") {
            steps {
                script {
                    gv.deployApp()
                    withKubeConfig([credentialsId: 'lke-credentials', serverUrl: 'https://1bd5d3fc-025f-4264-bbd8-736ed7b9f7cb.eu-central-3-gw.linodelke.net']){
                        sh 'kubectl create deployment nginx-deployment --image=nginx'
                    }
                    
                }
            }
        }               
    }
} 
