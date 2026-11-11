# Project: Complete CI/CD Pipeline with EKS and AWS ECR

## Goal
The goal of this project is to set up a complete CI/CD pipeline in Jenkins that performs all the actions to take the code from commit to a deployment on EKS cluster

## Steps performed by the pipeline
1. Increment the version that will act as tag for the Docker image that will be produced in the later steps of the pipeline.
2. Build the jar artifacts that will be used to build the docker image.
3. Build the docker image using the `docker build` command. This command used the Dockerfile to build the docker image.
4. Push the Docker image to the ECR repository created in AWS. In this step, the `ecr-credentials` stored on the Jenkins server are used to authenticate with the repository.
5. Deploy the app to EKS by authenticating with the EKS Cluster using AWS using `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` for the admin user, which are stored as credntials in Jenkins. This is done for simplicity. Although, the best practice is to actually create a separate Jenkins user in AWS with appropriate roles assigned to it. The deployment takes place using `kubectl apply -f` command. It uses the deployment.yaml and service.yaml files. We use environment variables to substitute values in the yaml files.
6. The Jenkins user updates the version of the app by committing what state we had after performing steps 1 and 2.