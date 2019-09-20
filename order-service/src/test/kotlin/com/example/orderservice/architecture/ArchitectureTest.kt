package com.example.orderservice.architecture

import com.structurizr.Workspace
import com.structurizr.analysis.ComponentFinder
import com.structurizr.analysis.ReferencedTypesSupportingTypesStrategy
import com.structurizr.analysis.SourceCodeComponentFinderStrategy
import com.structurizr.analysis.SpringComponentFinderStrategy
import com.structurizr.api.StructurizrClient
import com.structurizr.model.Container
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.File


class ArchitectureTest {

    private val workspaceId = 46652L
    private val apiKey = "efe06412-a3df-4325-bb7c-a0a6af61725b"
    private val apiSecret = "cdbc04e6-ad93-44c2-9df9-1ea4da76ac99"
    private val structClient = StructurizrClient(apiKey, apiSecret)
    private val sourceRoot = File("/Users/anmi/git/service-mesh-github/order-service")

    init {
        structClient.workspaceArchiveLocation = File("build/resources/main")
    }

    @Test
    fun shouldFindActualComponentsAndUploadToServer() {
        val workspace = Workspace("Demo Workspace", "Visualize Microservice Architecture")
        val demoSystem =
            workspace.model.addSoftwareSystem("Demo Software System", "Software System for the purpose of a demo")
        val webApplication =
            demoSystem.addContainer("Web Container", "Web Container for the purpose of a demo", "Spring Boot, Java 11")

        val springComponentFinderStrategy = SpringComponentFinderStrategy(ReferencedTypesSupportingTypesStrategy(true))

        springComponentFinderStrategy.setIncludePublicTypesOnly(false)

        val componentFinder = ComponentFinder(
            webApplication,
            "com.example",
            springComponentFinderStrategy,
            SourceCodeComponentFinderStrategy(File(sourceRoot, "/src/main/kotlin/"), 150)
        )

        val actualComponents = componentFinder.findComponents()

        linkComponentsWithSourceTree(webApplication)

        assertThat(actualComponents).hasSize(3)

        val componentView = workspace.views.createComponentView(
            webApplication,
            "orderservice_component",
            "Component Diagram for OrderView Service"
        )
        componentView.addAllElements()

        structClient.putWorkspace(workspaceId, workspace)
    }

    private fun linkComponentsWithSourceTree(webApplication: Container) {
        for (component in webApplication.components) {
            for (codeElement in component.code) {
                val sourcePath = codeElement.url
                if (sourcePath != null) {
                    codeElement.url = sourcePath.replace(
                        sourceRoot.toURI().toString(),
                        "https://github.com/menya84/servicemesh-in-microservices/tree/arch-model/"
                    )  //          "https://github.com/menya84/servicemesh-in-microservices/tree/arch-model/order-service/src/main/kotlin/"
                }
            }
        }
    }
}


