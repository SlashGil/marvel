package com.slashgil.marvel.presentation.characters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.presentation.components.CharacterCard
import com.slashgil.marvel.presentation.components.CharacterDetailSheet
import com.slashgil.marvel.presentation.components.MarvelHeader
import com.slashgil.marvel.presentation.components.PublisherFilterRow
import com.slashgil.marvel.ui.theme.BgMain
import com.slashgil.marvel.ui.theme.BgSurface
import com.slashgil.marvel.ui.theme.MarvelRed
import com.slashgil.marvel.ui.theme.TextSecondary

@Composable
fun CharactersScreen(
    state: CharactersUiState,
    onSearchQueryChanged: (String) -> Unit,
    onPublisherSelected: (String) -> Unit,
    onCharacterSelected: (Character) -> Unit,
    onDismissDetail: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { MarvelHeader() },
        containerColor = BgMain,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Search Bar with outer margin
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = onSearchQueryChanged,
                    placeholder = {
                        Text("Search characters (e.g. Spider-Man, Batman)", color = TextSecondary)
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = BgSurface,
                        unfocusedContainerColor = BgSurface,
                        focusedBorderColor = MarvelRed,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                )

                // Publisher Filter Chips
                PublisherFilterRow(
                    publishers = state.availablePublishers,
                    selectedPublisher = state.selectedPublisher,
                    onPublisherSelected = onPublisherSelected
                )

                if (state.isLoading && state.characters.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MarvelRed)
                    }
                } else if (state.error != null && state.characters.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = state.error,
                            color = Color.White,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onRetry,
                            colors = ButtonDefaults.buttonColors(containerColor = MarvelRed)
                        ) {
                            Text("Retry", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 150.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.characters, key = { it.id }) { character ->
                            CharacterCard(
                                character = character,
                                onClick = { onCharacterSelected(character) }
                            )
                        }
                    }
                }
            }

            // Character Detail Spoke View overlay
            CharacterDetailSheet(
                character = state.selectedCharacter,
                isDetailLoading = state.isDetailLoading,
                comics = state.characterComics,
                onDismiss = onDismissDetail,
                onPublisherClick = { publisher ->
                    onPublisherSelected(publisher)
                }
            )
        }
    }
}
