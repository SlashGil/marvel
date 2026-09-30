package com.slashgil.marvel.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.slashgil.marvel.domain.model.Character
import com.slashgil.marvel.ui.theme.BgMain
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
    onDismiss: () -> Unit,
    onPublisherClick: (String) -> Unit,
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
                    // Header Image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(Color(0xFF222222))
                    ) {
                        if (character.imageUrl.isNotBlank()) {
                            AsyncImage(
                                model = character.imageUrl.replace("/md/", "/lg/"),
                                contentDescription = character.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // Gradient overlay
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
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(20.dp)
                        ) {
                            Text(
                                text = character.name,
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black
                            )
                            if (character.biography.fullName.isNotBlank()) {
                                Text(
                                    text = character.biography.fullName,
                                    color = TextSecondary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .background(MarvelRed)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Clickable Publisher Chip
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "PUBLISHER:",
                                color = TextSecondary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MarvelRed,
                                modifier = Modifier.clickable {
                                    onPublisherClick(character.biography.publisher)
                                }
                            ) {
                                Text(
                                    text = "${character.biography.publisher}  ➔",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }
                        }

                        // Powerstats Section
                        Column {
                            Text(
                                text = "POWERSTATS",
                                color = MarvelRed,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            PowerstatsBarChart(powerstats = character.powerstats)
                        }

                        // Biography Details
                        Column {
                            Text(
                                text = "BIOGRAPHY & ORIGIN",
                                color = MarvelRed,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            DetailRow(label = "First Appearance", value = character.biography.firstAppearance)
                            DetailRow(label = "Place of Birth", value = character.biography.placeOfBirth)
                            DetailRow(label = "Alignment", value = character.biography.alignment.uppercase())
                            DetailRow(label = "Race", value = character.appearance.race)
                        }

                        // Connections & Teams
                        if (character.connections.groupAffiliation.isNotBlank()) {
                            Column {
                                Text(
                                    text = "AFFILIATIONS",
                                    color = MarvelRed,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = character.connections.groupAffiliation,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    if (value.isNotBlank()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = TextSecondary, fontSize = 13.sp)
            Text(text = value, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
