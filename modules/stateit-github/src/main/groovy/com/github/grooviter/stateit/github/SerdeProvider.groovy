package com.github.grooviter.stateit.github

import com.github.grooviter.stateit.core.Provider
import com.github.grooviter.stateit.core.ResourceSerde

class SerdeProvider implements Provider {
    @Override
    List<ResourceSerde> getResourceSerdeList() {
        return []
    }
}
