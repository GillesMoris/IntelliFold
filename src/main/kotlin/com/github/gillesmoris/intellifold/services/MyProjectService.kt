package com.github.gillesmoris.intellifold.services

import com.github.gillesmoris.intellifold.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
