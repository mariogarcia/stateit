package com.github.grooviter.stateit

import com.github.grooviter.stateit.core.Plan
import com.github.grooviter.stateit.core.PlanExecutor
import com.github.grooviter.stateit.core.Resource
import com.github.grooviter.stateit.core.Result
import com.github.grooviter.stateit.core.StateProps
import com.github.grooviter.stateit.core.StateProvider
import com.github.grooviter.stateit.core.variables.Variables
import com.github.grooviter.stateit.core.variables.VariablesLoader

import static com.github.grooviter.stateit.core.ClosureUtils.applyPropsToClosure

class DSL {
    private List<Resource> declaredResources = []
    private List<Resource> stateResources = []
    private StateProvider stateProvider
    private Variables variables

    private DSL(Variables variables) {
        this.variables = variables
    }

    static Result<Plan> validate(Plan plan) {
        return new PlanExecutor(plan).validate()
    }

    static Result<Plan> execute(Plan plan) {
        return new PlanExecutor(plan).execute()
    }

    static Result<Plan> destroy(Plan plan){
        return new PlanExecutor(plan).destroy()
    }

    static Plan stateit(VariablesLoader variablesLoader, @DelegatesTo(DSL) Closure closure) {
        DSL dsl = applyPropsToClosure(new DSL(variablesLoader.load()), closure)
        return Plan.builder()
                .stateProvider(dsl.stateProvider)
                .resourcesDeclared(dsl.declaredResources)
                .resourcesInState(dsl.stateResources)
                .build()
    }

    static Plan stateit(@DelegatesTo(DSL) Closure closure) {
        return stateit(new VariablesLoader(), closure)
    }

    DSL state(@DelegatesTo(StateProps) Closure closure) {
        StateProps stateProps = applyPropsToClosure(new StateProps(), closure)
        this.stateProvider = stateProps.provider

        stateProps.provider
            .load()
            .sideEffect {plan ->
                this.stateResources = plan.resourcesInState
            }

        return this
    }

    Map<String,?> getVar_() {
        return this.variables.vars
    }

    Map<String,?> getSec_() {
        return this.variables.secrets
    }

    void addDeclaredResource(Resource resource) {
        this.declaredResources.add(resource)
    }
}