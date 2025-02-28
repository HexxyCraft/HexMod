#!/usr/bin/env groovy

pipeline {
    agent any
    tools {
        jdk "jdk-17.0.1"
    }
    environment {
        discordWebhook = credentials('discordWebhook')
    }
    stages {
        stage('Clean') {
            steps {
                echo 'Cleaning Project'
                sh 'chmod +x gradlew'
                sh './gradlew clean'
            }
        }
        stage('Build') {
            steps {
                echo 'Building'
                sh './gradlew build'
            }
        }
        stage('Publish') {
            when { anyOf {
             branch 'main'
             branch '1.18'
            } }
            steps {
                echo 'Deploying to Maven'
                sh './gradlew publish sendWebhook'
            }
        }
    }
    post {
        always {
            archiveArtifacts 'Common/build/libs/**.jar'
            archiveArtifacts 'Forge/build/libs/**.jar'
            archiveArtifacts 'Fabric/build/libs/**.jar'
        }
    }
}