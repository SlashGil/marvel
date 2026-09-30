package com.slashgil.marvel.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slashgil.marvel.R
import com.slashgil.marvel.ui.theme.BgMain
import com.slashgil.marvel.ui.theme.MarvelRed
import kotlinx.coroutines.delay

private enum class SplashPhase {
    DEV_FLAVOR_SHOW,
    TRANSITION,
    PROD_FLAVOR_SHOW,
    FINISHED
}

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    var phase by remember { mutableStateOf(SplashPhase.DEV_FLAVOR_SHOW) }

    val devAlpha = remember { Animatable(0f) }
    val devScale = remember { Animatable(0.6f) }
    val devRotationY = remember { Animatable(0f) }

    val prodAlpha = remember { Animatable(0f) }
    val prodScale = remember { Animatable(0.4f) }

    val pulseScale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        // Phase 1: Fade & Scale in DEV App Icon
        devAlpha.animateTo(1f, animationSpec = tween(500, easing = FastOutSlowInEasing))
        devScale.animateTo(
            1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        delay(800)
        phase = SplashPhase.TRANSITION

        // Phase 2: 3D Flip DEV Icon away while revealing PROD Icon
        devRotationY.animateTo(90f, animationSpec = tween(350, easing = FastOutSlowInEasing))
        devAlpha.animateTo(0f, animationSpec = tween(150))

        phase = SplashPhase.PROD_FLAVOR_SHOW
        prodAlpha.animateTo(1f, animationSpec = tween(300))
        prodScale.animateTo(
            1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )

        // Pulse glow effect on PROD Icon
        pulseScale.animateTo(1.15f, animationSpec = tween(250, easing = LinearEasing))
        pulseScale.animateTo(1f, animationSpec = tween(250, easing = FastOutSlowInEasing))

        delay(700)
        phase = SplashPhase.FINISHED
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        if (phase == SplashPhase.DEV_FLAVOR_SHOW) Color(0xFF151A22) else Color(0xFF3D090B),
                        BgMain
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon Container Box holding BOTH the pulse ring and icon perfectly concentric in the exact middle
            Box(
                modifier = Modifier.size(240.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background Pulse Ring perfectly centered in this 240.dp container
                Box(
                    modifier = Modifier
                        .size(230.dp)
                        .scale(pulseScale.value)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = if (phase == SplashPhase.DEV_FLAVOR_SHOW) Color(0xFF38BDF8).copy(alpha = 0.5f) else MarvelRed.copy(alpha = 0.6f),
                            shape = CircleShape
                        )
                )

                // DEV Product Flavor App Icon (160.dp x 160.dp)
                if (devAlpha.value > 0f) {
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .scale(devScale.value)
                            .alpha(devAlpha.value)
                            .graphicsLayer {
                                rotationY = devRotationY.value
                                cameraDistance = 12f * density
                            }
                            .clip(RoundedCornerShape(32.dp))
                            .border(2.dp, Color(0xFF38BDF8), RoundedCornerShape(32.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_app_icon_dev_bg),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_app_icon_dev_fg),
                            contentDescription = "Dev App Icon",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // PROD Product Flavor App Icon (160.dp x 160.dp)
                if (prodAlpha.value > 0f) {
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .scale(prodScale.value)
                            .alpha(prodAlpha.value)
                            .clip(RoundedCornerShape(32.dp))
                            .border(2.dp, MarvelRed, RoundedCornerShape(32.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_app_icon_prod_bg),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_app_icon_prod_fg),
                            contentDescription = "Prod App Icon",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Subtitle Tag
            val labelText = when (phase) {
                SplashPhase.DEV_FLAVOR_SHOW -> "DEV FLAVOR (BLUEPRINT)"
                SplashPhase.TRANSITION -> "SWITCHING FLAVOR..."
                SplashPhase.PROD_FLAVOR_SHOW, SplashPhase.FINISHED -> "PROD FLAVOR (MARVEL)"
            }

            val labelColor = when (phase) {
                SplashPhase.DEV_FLAVOR_SHOW -> Color(0xFF38BDF8)
                SplashPhase.TRANSITION -> Color(0xFFFACC15)
                SplashPhase.PROD_FLAVOR_SHOW, SplashPhase.FINISHED -> MarvelRed
            }

            Text(
                text = labelText,
                color = labelColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.65f))
                    .border(1.dp, labelColor.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}
