pipeline {
    agent {
        label "java8"
    }
    stages {
        stage('Build') {
            steps {
                sh './gradlew --info --console=plain --parallel assemble compileTest'
                recordIssues enabledForFailure: true, tools: [java()]
            }
        }
        stage('Analytics') {
            steps {
                sh './gradlew --info --console=plain --parallel javadoc check'
                junit testResults: '**/build/test-results/test/*.xml'
                recordIssues tools: [
                  javaDoc(),
                  taskScanner(includePattern: '**/*.java,**/*.groovy,**/*.gradle,**/*.kts', lowTags: 'WIBNIF', normalTags: 'TODO', highTags: 'ASAP')
                ]
                //Note: Javadoc archiver only works for one directory :-(
                javadoc javadocDir: 'gestalt-entity-system/build/docs/javadoc', keepAll: false
            }
        }
        stage('Publish') {
            when {
                anyOf {
                    branch 'master'
                    branch pattern: "release/v\\d+.x", comparator: "REGEXP"
                }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'artifactory-gooey', usernameVariable: 'artifactoryUser', passwordVariable: 'artifactoryPass')]) {
                    sh './gradlew --info --console=plain -Dorg.gradle.internal.publish.checksums.insecure=true publish -PmavenUser=${artifactoryUser} -PmavenPass=${artifactoryPass}'
                }
            }
        }
    }
}