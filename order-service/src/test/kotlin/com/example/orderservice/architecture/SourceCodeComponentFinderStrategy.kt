package com.example.orderservice.architecture

import com.structurizr.analysis.ComponentFinder
import com.structurizr.analysis.ComponentFinderStrategy
import com.structurizr.model.CodeElementRole
import com.structurizr.model.Component
import com.sun.tools.javadoc.Start
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.lang.reflect.Constructor
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


/**
 * This component finder strategy doesn't really find components, it instead:
 *
 *  * Extracts the top-level Javadoc comment from the code so that this can be added to existing component definitions.
 *  * Calculates the size of components based upon the number of lines of source code.
 *
 */
class SourceCodeComponentFinderStrategy : ComponentFinderStrategy {

    private var componentFinder: ComponentFinder? = null

    private var sourcePath: File? = null
    private var maxDescriptionLength: Int? = null
    private var encoding: String? = null

    private val typeToSourceFile = HashMap<String, File>()
    private val typeToDescription = HashMap<String, String>()

    constructor(sourcePath: File) {
        this.sourcePath = sourcePath
    }

    constructor(sourcePath: File, maxDescriptionLength: Int) {
        this.sourcePath = sourcePath
        this.maxDescriptionLength = maxDescriptionLength
    }

    override fun setComponentFinder(componentFinder: ComponentFinder) {
        this.componentFinder = componentFinder
    }

    fun setEncoding(encoding: String) {
        this.encoding = encoding
    }

    @Throws(Exception::class)
    override fun beforeFindComponents() {
    }

    @Throws(Exception::class)
    override fun findComponents(): Set<Component> {
        return HashSet() // this component finder doesn't find components
    }

    @Throws(Exception::class)
    override fun afterFindComponents() {
        runJavaDoc()

        val filter = JavadocCommentFilter(maxDescriptionLength)
        for (classDoc in RootInit.ROOTDOC!!.classes()) {
            val type = classDoc.qualifiedTypeName()
            val comment = filter.filterAndTruncate(classDoc.commentText())
            val pathToSourceFile = classDoc.position().file().canonicalPath

            typeToSourceFile[type] = File(pathToSourceFile)
            typeToDescription[type] = comment ?: ""
        }

        for (component in componentFinder!!.container.components) {
            var count: Long = 0

            for (codeElement in component.code) {
                if (typeToDescription.containsKey(codeElement.type)) {
                    codeElement.description = typeToDescription[codeElement.type]

                    // additionally set the description on the component, if it's not already been set
                    if (codeElement.role == CodeElementRole.Primary) {
                        if (component.description == null || component.description.trim { it <= ' ' }.length == 0) {
                            component.description = typeToDescription[component.type.type]
                        }
                    }
                }

                val sourceFile = typeToSourceFile[codeElement.type]
                if (sourceFile != null) {
                    val numberOfLinesInFile = Files.lines(Paths.get(sourceFile.toURI())).count()
                    codeElement.url = sourceFile.toURI().toString()
                    codeElement.size = numberOfLinesInFile
                    count += numberOfLinesInFile
                }
            }

            if (count > 0) {
                component.size = count
            }
        }
    }

    @Throws(Exception::class)
    private fun runJavaDoc() {
        val parameters = LinkedList<String>()
        parameters.add("-sourcepath")
        parameters.add(sourcePath!!.canonicalPath)
        parameters.add("-subpackages")
        parameters.add(componentFinder!!.packageNames.joinToString(":"))

        if (encoding != null) {
            parameters.add("-encoding")
            parameters.add(encoding!!)
        }

        parameters.add("-private")

        val outOriginal = System.out
        val errOriginal = System.err
        val bytes = ByteArrayOutputStream()
        System.setOut(PrintStream(bytes))
        System.setErr(System.out)

        try {
            val array = parameters.toTypedArray<String>()
            val constructor: Constructor<Start> = Start::class.java.getDeclaredConstructor(String::class.java, String::class.java)
            constructor.isAccessible = true
            val start: Start = constructor
                .newInstance("StructurizrDoclet", RootInit::class.java.name)
            val m = Start::class.java.getDeclaredMethod("begin", Array<String>::class.java)
            m.isAccessible = true
            m.invoke(start, array)
        } catch (t: Throwable) {
            outOriginal.write(bytes.toByteArray())
            outOriginal.flush()
            throw t
        } finally {
            System.setOut(outOriginal)
            System.setOut(errOriginal)
        }
    }
}