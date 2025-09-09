pipeline {
    agent any

    environment {
        BRANCH_NAME = 'main'
        ECLIPSE_WORKSPACE = 'C:\\Users\\SAIMO\\OneDrive\\Desktop\\CapstoneProjecct' // Ensure correct folder name
        COMMIT_MESSAGE = 'Jenkins: Auto-commit after build'
    }

    // Poll GitHub for changes every 1st day of month
    triggers {
        pollSCM('30 2 1 * *')
    }

    stages {
        stage('Checkout from GitHub') {
            steps {
                git branch: "${env.BRANCH_NAME}",
                    url: 'https://github.com/saisai18018/CapstoneRepo.git'
            }
        }

        stage('Clean Previous Build & Screenshots') {
    		steps {
        		echo 'Cleaning old build and screenshots...'
        		bat """
        		rmdir /S /Q "reports\\Screenshots"
        		mkdir "reports\\Screenshots"
        		rmdir /S /Q "target"
        		"""
    		}
		}


        stage('Copy Eclipse Workspace Files') {
            steps {
                bat """
                echo Copying files from Eclipse workspace...
                xcopy /E /Y /I "${ECLIPSE_WORKSPACE}\\*" "."
                """
            }
        }

        stage('Build & Test') {
            steps {
                // Run TestNG tests using testng.xml
                bat 'mvn clean test -DsuiteXmlFile=testng.xml'
            }
        }

        stage('Commit & Push Changes') {
            steps {
                script {
                    withCredentials([usernamePassword(
                        credentialsId: 'CapstoneProject',
                        usernameVariable: 'GIT_USER',
                        passwordVariable: 'GIT_TOKEN')]) {

                        bat """
                        git config user.email "saimomdad99@gmail.com"
                        git config user.name "Sai Bharath"

                        REM Add all files including screenshots
                        git add .

                        REM Commit only if there are changes
                        git diff --cached --quiet || git commit -m "${COMMIT_MESSAGE}" || echo "No changes to commit"

                        REM Push using token safely
                        git push https://%GIT_USER%:%GIT_TOKEN%@github.com/saisai18018/CapstoneRepo.git ${BRANCH_NAME}
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Listing report files for debugging...'
            bat 'dir /s reports'

            // Archive screenshots
            archiveArtifacts artifacts: 'reports/Screenshots/*', fingerprint: true

            // Publish HTML reports
            publishHTML(target: [
                reportDir: 'reports/ExtentReports',
                reportFiles: 'index.html',
                reportName: 'Extent Report',
                alwaysLinkToLastBuild: true,
                keepAll: true
            ])
        }
    }
}
