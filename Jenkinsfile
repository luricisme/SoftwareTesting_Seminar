pipeline {
    agent any

    environment {
        // DockerHub credentials ID trong Jenkins
        DOCKER_CREDENTIALS = 'DockerHubCredentials'
        DOCKER_IMAGE = 'devopshcmus/software-testing-seminar'
        // Render API key (lưu trong Jenkins Credential)
        RENDER_API_KEY = credentials('RenderCredentials')

        DEPLOY_HOOK_DEV = credentials('DeployHookDev')
        DEPLOY_HOOK_STAGING = credentials('DeployHookStaging')
        DEPLOY_HOOK_PROD = credentials('DeployHookProd')
    }

    options {
        // Chặn các build cùng lúc của cùng một pipeline.
        disableConcurrentBuilds()
        // Chỉ giữ 10 build gần nhất, các build cũ hơn sẽ tự động xóa.
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh './mvnw clean test'
            }
            post {
                always {
                    // Jacoco report
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java'
                    )
                }
            }
        }

        stage('Package') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def tag = env.BRANCH_NAME == 'main' ? 'latest' : env.BRANCH_NAME
                    sh "docker build -t ${DOCKER_IMAGE}:${tag} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    def tag = env.BRANCH_NAME == 'main' ? 'latest' : env.BRANCH_NAME
                    withDockerRegistry([credentialsId: "${DOCKER_CREDENTIALS}", url: '']) {
                        sh "docker push ${DOCKER_IMAGE}:${tag}"
                    }
                }
            }
        }

        stage('Deploy to Render') {
            steps {
                script {
                    def tag = env.BRANCH_NAME == 'main' ? 'latest' : env.BRANCH_NAME

                    if (env.BRANCH_NAME == 'dev') {
                        sh """
                        curl -X POST "${DEPLOY_HOOK_DEV}?imgURL=docker.io%2Fdevopshcmus%2Fsoftware-testing-seminar%3Adev" \
                             -H "Authorization: Bearer ${RENDER_API_KEY}" \
                             -H "Content-Type: application/json"
                        """
                    } else if (env.BRANCH_NAME == 'staging') {
                        sh """
                        curl -X POST "${DEPLOY_HOOK_STAGING}?imgURL=docker.io%2Fdevopshcmus%2Fsoftware-testing-seminar%3Astaging" \
                             -H "Authorization: Bearer ${RENDER_API_KEY}" \
                             -H "Content-Type: application/json"
                        """
                    } else if (env.BRANCH_NAME == 'main') {
                        sh """
                        curl -X POST "${DEPLOY_HOOK_PROD}?imgURL=docker.io%2Fdevopshcmus%2Fsoftware-testing-seminar%3Alatest" \
                             -H "Authorization: Bearer ${RENDER_API_KEY}" \
                             -H "Content-Type: application/json"
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo "✅ Build & Deploy SUCCESSFUL for branch ${env.BRANCH_NAME}"
        }
        failure {
            echo "❌ Build FAILED for branch ${env.BRANCH_NAME}"
        }
    }
}
