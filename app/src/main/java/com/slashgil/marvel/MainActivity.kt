package com.slashgil.marvel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.slashgil.marvel.presentation.characters.CharactersScreen
import com.slashgil.marvel.presentation.characters.CharactersViewModel
import com.slashgil.marvel.ui.theme.MarvelTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: CharactersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarvelTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                CharactersScreen(
                    state = state,
                    onSearchQueryChanged = viewModel::onSearchQueryChanged,
                    onCharacterSelected = viewModel::onCharacterSelected,
                    onDismissDetail = viewModel::onDismissDetail,
                    onRetry = viewModel::loadCharacters
                )
            }
        }
    }
}
