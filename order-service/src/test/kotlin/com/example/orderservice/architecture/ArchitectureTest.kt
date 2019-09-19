package com.example.orderservice.architecture

import com.structurizr.Workspace
import com.structurizr.analysis.*
import com.structurizr.api.StructurizrClient
import com.structurizr.model.Component
import com.structurizr.model.Container
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.File
import java.util.*
import java.util.function.Consumer

class ArchitectureTest {

    private val workspaceId = 46652L
    private val apiKey = "efe06412-a3df-4325-bb7c-a0a6af61725b"
    private val apiSecret = "cdbc04e6-ad93-44c2-9df9-1ea4da76ac99"
    private val structClient = StructurizrClient(apiKey, apiSecret)

    init {
        structClient.workspaceArchiveLocation = File("build/resources/main/spec")
    }

    @Test
    fun shouldActualComponentsComplyWithArchitectureModel() {
        val workspace = Workspace("Demo Workspace", "Visualize Microservice Architecture")
        val demoSystem = workspace.model.addSoftwareSystem("Demo Software System", "Software System for the purpose of a demo")
        val webApplication = demoSystem.addContainer("Web Container", "Web Container for the purpose of a demo", "Spring Boot, Java 11")

        val springComponentFinderStrategy = IgnoreDuplicateSpringComponentFinderStrategy(ReferencedTypesSupportingTypesStrategy())

        springComponentFinderStrategy.setIncludePublicTypesOnly(false)

        val componentFinder = ComponentFinder(
                webApplication,
                "com.example",
                springComponentFinderStrategy,
                SourceCodeComponentFinderStrategy(File("/src/main/java/"), 150))

        val actualComponents = componentFinder.findComponents()

        assertThat(actualComponents).hasSize(3)

        val componentView = workspace.views.createComponentView(webApplication, "orderservice_component", "Component Diagram for OrderView Service")
        componentView.addAllElements()

        structClient.putWorkspace(workspaceId, workspace)
    }
}

class IgnoreDuplicateSpringComponentFinderStrategy(vararg strategies: SupportingTypesStrategy) : AbstractSpringComponentFinderStrategy(*strategies) {
    private val componentFinderStrategies = LinkedList<AbstractSpringComponentFinderStrategy>()

    override fun beforeFindComponents() {
        super.beforeFindComponents()
        componentFinderStrategies.add(SpringRestControllerComponentFinderStrategy())
        componentFinderStrategies.add(SpringMvcControllerComponentFinderStrategy())
        componentFinderStrategies.add(SpringServiceComponentFinderStrategy())
        componentFinderStrategies.add(SpringComponentComponentFinderStrategy())
        componentFinderStrategies.add(SpringRepositoryComponentFinderStrategy())

        for (componentFinderStrategy in componentFinderStrategies) {
            componentFinderStrategy.setIncludePublicTypesOnly(includePublicTypesOnly)
            componentFinderStrategy.setComponentFinder(getComponentFinder())
            supportingTypesStrategies.forEach(Consumer<SupportingTypesStrategy> { componentFinderStrategy.addSupportingTypesStrategy(it) })
            componentFinderStrategy.setDuplicateComponentStrategy { component, name, type, description, technology -> null }
            componentFinderStrategy.beforeFindComponents()
        }
    }

    override fun doFindComponents(): Set<Component> {
        val components = HashSet<Component>()

        for (componentFinderStrategy in componentFinderStrategies) {
            components.addAll(componentFinderStrategy.findComponents())
        }

        return components
    }

    override fun addComponent(container: Container, name: String, type: String, description: String, technology: String): Component? {
        return if (container.getComponentWithName(name) == null) {
            container.addComponent(name, type, description, technology)
        } else {
            null
        }
    }
}
