/*
 * Copyright (C) 2026  PranavSagarXD
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.sagar.momento.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import com.sagar.momento.core.data_classes.Project
import com.sagar.momento.core.interfaces.MontageState
import com.sagar.momento.core.interfaces.ProjectRepository
import com.sagar.momento.presentation.home.HomeAction
import com.sagar.momento.presentation.home.HomeState

@KoinViewModel
class HomeViewModel(
    private val stateLayer: SharedState,
    private val projectRepository: ProjectRepository,
) : ViewModel() {
    private val _state = stateLayer.homeState
    val state =
        _state
            .asStateFlow()
            .onStart { observeProjects() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = HomeState(),
            )

    fun onAction(action: HomeAction) =
        viewModelScope.launch {
            when (action) {
                is OnChangeProject ->
                    stateLayer.projectState.update {
                        it.copy(
                            project = action.project,
                            days = emptyList(),
                            montage = MontageState.ProcessingImages(),
                        )
                    }

                is OnAddProject -> {
                    projectRepository.upsertProject(
                        Project(title = action.title, description = action.description)
                    )
                }
            }
        }

    private fun observeProjects() =
        viewModelScope.launch {
            projectRepository
                .getProjectListData()
                .onEach { projects -> _state.update { it.copy(projects = projects) } }
                .launchIn(this)
        }
}
