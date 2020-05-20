package _Self.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

object ExpressApp : BuildType({
    name = "Express App"

    artifactRules = "coverage/** => coverage.zip"

    params {
        param("build", "%teamcity.build.branch%-%build.counter%")
        param("teamcity.vcsTrigger.runBuildInNewEmptyBranch", "false")
    }
    vcs {
        root(Cucumber)
    }
    steps {
        step {
            name = "Run Test"
            type = "jonnyzzz.npm"
            param("npm_commands", """
                install
                test
            """.trimIndent())
        }
        script {
            name = "testing"
            scriptContent = """
                npm install cucumber-teamcity-formatter --save-dev
                ./node_modules/.bin/cucumber-js --format node_modules/cucumber-teamcity-formatter
            """.trimIndent()
        }
    }

    triggers {
        vcs {
            perCheckinTriggering = true
            enableQueueOptimization = false
        }
    }

    features {
        pullRequests {
            enabled = false
            vcsRootExtId = "${HttpsGitlabComMiteshITadcornerTestingProjectGitRefsHeadsMaster.id}"
            provider = gitlab {
                authType = token {
                    token = "zxxddf13904f10b58a7d73448df922d7ee3b49df7f62ca5787d"
                }
            }
        }
        commitStatusPublisher {
            vcsRootExtId = "${HttpsGitlabComMiteshITadcornerTestingProjectGitRefsHeadsMaster.id}"
            publisher = gitlab {
                gitlabApiUrl = "https://gitlab.com/api/v4"
                accessToken = "zxxddf13904f10b58a7d73448df922d7ee3b49df7f62ca5787d"
            }
        }
    }
})
