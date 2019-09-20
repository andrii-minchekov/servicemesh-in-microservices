package com.example.orderservice.architecture

import com.structurizr.analysis.*
import com.structurizr.model.Component
import com.structurizr.model.Container
import java.util.*
import java.util.function.Consumer

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
            componentFinderStrategy.setIncludePublicTypesOnly(false)
            componentFinderStrategy.setComponentFinder(getComponentFinder())
            supportingTypesStrategies.forEach(Consumer<SupportingTypesStrategy> { componentFinderStrategy.addSupportingTypesStrategy(it) })
            componentFinderStrategy.setDuplicateComponentStrategy { _, _, _, _, _ -> null }
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