package com.github.grooviter.stateit.cli

import com.github.grooviter.stateit.DSL
import com.github.grooviter.stateit.core.Plan
import com.github.grooviter.stateit.core.Result

trait PlanLoader {
    static Plan loadPlan(String path) {
        GroovyShell shell = new GroovyShell(new Binding([DSL: DSL]))
        File scriptFile = new File(path)
        String scriptText = "DSL.stateit { ${scriptFile.text } }"
        return shell.evaluate(scriptText) as Plan
    }

    static Integer evaluateCommandExit(Result result) {
        return result.success ? 0 : 1
    }
}
