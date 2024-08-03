package com.jnasser.run.presentation.run_overview

import com.jnasser.core.domain.run.Run
import com.jnasser.run.presentation.run_overview.model.RunUi

data class RunOverviewState(
    val runs: List<RunUi> = emptyList()
)
