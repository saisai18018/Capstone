pipeline {

    agent any

    environment {
        BRANCH_NAME = 'main'
        ECLIPSE_WORKSPACE = 'C:\\Users\\SAIMO\\OneDrive\\Desktop\\CapstoneProjecct'
        COMMIT_MESSAGE = 'Jenkins: Auto-commit after build'
    }

    // Auto-trigger every day (adjust schedule as needed)
    triggers {
        pollSCM('H H * * *')
    }

    stages {

        stage('Checkout from Git') {
            steps {
                git branch: "${env.BRANCH_NAME}",
                    url: 'https://github.com/saisai18018/Capstone.git'
            }
        }

        stage('Copy Files from Eclipse Workspace') {
            steps {
                bat """
                echo Copying files from Eclipse workspace...
                xcopy /E /Y /I "${ECLIPSE_WORKSPACE}\\*" "."
                """
            }
        }

        stage('Build & Test') {
            steps {
                bat 'mvn clean test -DsuiteXmlFile=testng.xml'
            }
        }

        stage('Commit & Push Changes') {
            steps {
                script {
                    echo 'Checking for changes to push...'
                    withCredentials([usernamePassword(
                        credentialsId: 'CapstoneProject',
                        usernameVariable: 'GIT_USER',
                        passwordVariable: 'GIT_TOKEN')]) {

                        bat """
                            git config user.email "saimomdad99@gmail.com"
                            git config user.name "Sai"

                            git status
                            git add .

                            REM Commit only if there are changes
                            git diff --cached --quiet || git commit -m "${COMMIT_MESSAGE}"

                            REM Push using token
                            git push https://%GIT_USER%:%GIT_TOKEN%@github.com/saisai18018/Capstone.git ${BRANCH_NAME}
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            // Archive screenshots
            archiveArtifacts artifacts: 'reports/Screenshots/*', fingerprint: true

            // Publish Extent Report
            publishHTML(target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports/ExtentReports',
                reportFiles: 'index.html',
                reportName: 'Extent Report'
            ])
        }
    }
}
