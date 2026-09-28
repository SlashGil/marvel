package com.slashgil.marvel.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.domain.model.Comic
import com.slashgil.marvel.ui.theme.BgMain
import com.slashgil.marvel.ui.theme.BgSurface
import com.slashgil.marvel.ui.theme.MarvelRed
import com.slashgil.marvel.ui.theme.TextSecondary

private val ArrowBackIcon: ImageVector
    get() = ImageVector.Builder(
        name = "ArrowBack",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        addPath(
            pathData = listOf(
                PathNode.MoveTo(20f, 11f),
                PathNode.LineTo(7.83f, 11f),
                PathNode.RelativeLineTo(5.59f, -5.59f),
                PathNode.LineTo(12f, 4f),
                PathNode.RelativeLineTo(-8f, 8f),
                PathNode.RelativeLineTo(8f, 8f),
                PathNode.RelativeLineTo(1.41f, -1.41f),
                PathNode.LineTo(7.83f, 13f),
                PathNode.LineTo(20f, 13f),
                PathNode.Close
            ),
            fill = SolidColor(Color.White)
        )
    }.build()

@Composable
fun CharacterDetailSheet(
    character: Character?,
    isDetailLoading: Boolean,
    comics: List<Comic>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = character != null,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it }),
        modifier = modifier.fillMaxSize()
    ) {
        if (character != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BgMain)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Spoke Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .background(Color(0xFF222222))
                    ) {
                        if (character.thumbnailUrl.isNotBlank()) {
                            AsyncImage(
                                model = character.thumbnailUrl,
                                contentDescription = character.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // Gradient overlay for contrast
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.4f))
                        )

                        // Back Button
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .padding(16.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f))
                                .align(Alignment.TopStart)
                        ) {
                            Icon(
                                imageVector = ArrowBackIcon,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        // Title in header
                        Text(
                            text = character.name,
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(MarvelRed)
                    )

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "BIOGRAPHY",
                            color = MarvelRed,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (character.description.isNotBlank()) {
                                character.description
                            } else {
                                "No description available for ${character.name}."
                            },
                            color = Color.White,
                            fontSize = 15.sp,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "FEATURED COMICS (${comics.size})",
                                color = MarvelRed,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (isDetailLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = MarvelRed)
                            }
                        } else if (comics.isEmpty()) {
                            Text(
                                text = "No comics found for this character.",
                                color = TextSecondary,
                                fontSize = 14.sp
                            )
                        } else {
                            LazyRow(
                                contentPadding = PaddingValues(end = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(comics) { comic ->
                                    ComicCardItem(comic = comic)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ComicCardItem(
    comic: Comic,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(130.dp)
            .clip(RoundedCornerShape(6.dp)),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = BgSurface)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFF222222))
            ) {
                if (comic.thumbnailUrl.isNotBlank()) {
                    AsyncImage(
                        model = comic.thumbnailUrl,
                        contentDescription = comic.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(MarvelRed)
            )
            Text(
                text = comic.title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
