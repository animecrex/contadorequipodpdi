package com.example.contar.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.FilledIconButton
import androidx.wear.compose.material3.FilledTonalIconButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.TimeText
import com.example.contar.presentation.theme.ContarTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ContadorApp()
        }
    }
}

@Composable
fun ContadorApp() {
    var count by remember { mutableIntStateOf(0) }
    var step by remember { mutableIntStateOf(1) }
    var lastOperation by remember { mutableStateOf("none") } // "increment", "decrement", "reset"

    val haptic = LocalHapticFeedback.current
    val dialInteractionSource = remember { MutableInteractionSource() }
    val isDialPressed by dialInteractionSource.collectIsPressedAsState()

    val dialScale by animateFloatAsState(
        targetValue = if (isDialPressed) 0.94f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "dialScale"
    )

    fun increment() {
        lastOperation = "increment"
        count += step
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    fun decrement() {
        if (count > 0) {
            lastOperation = "decrement"
            count = maxOf(0, count - step)
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    fun reset() {
        if (count > 0) {
            lastOperation = "reset"
            count = 0
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    fun cycleStep() {
        step = when (step) {
            1 -> 5
            5 -> 10
            else -> 1
        }
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    ContarTheme {
        AppScaffold {
            ScreenScaffold(
                timeText = { TimeText() }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Top: Step selector pill
                        Box(
                            modifier = Modifier
                                .padding(top = 18.dp)
                                .clip(RoundedCornerShape(50))
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(50)
                                )
                                .clickable { cycleStep() }
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "PASO +$step",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.sp
                            )
                        }

                        // Center: Interactive Hero Counter Dial
                        Box(
                            modifier = Modifier
                                .size(106.dp)
                                .scale(dialScale)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF003840),
                                            Color(0xFF061418),
                                            Color(0xFF040A0D)
                                        )
                                    )
                                )
                                .border(
                                    border = BorderStroke(
                                        width = 2.dp,
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                            )
                                        )
                                    ),
                                    shape = CircleShape
                                )
                                .clickable(
                                    interactionSource = dialInteractionSource,
                                    indication = null
                                ) {
                                    increment()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                AnimatedContent(
                                    targetState = count,
                                    transitionSpec = {
                                        when (lastOperation) {
                                            "increment" -> {
                                                (slideInVertically { height -> height } + fadeIn()) togetherWith
                                                        (slideOutVertically { height -> -height } + fadeOut())
                                            }
                                            "decrement" -> {
                                                (slideInVertically { height -> -height } + fadeIn()) togetherWith
                                                        (slideOutVertically { height -> height } + fadeOut())
                                            }
                                            else -> {
                                                fadeIn() togetherWith fadeOut()
                                            }
                                        }
                                    },
                                    label = "countAnimation"
                                ) { targetCount ->
                                    val fontSize = when {
                                        targetCount >= 10000 -> 24.sp
                                        targetCount >= 1000 -> 30.sp
                                        targetCount >= 100 -> 38.sp
                                        else -> 44.sp
                                    }

                                    Text(
                                        text = targetCount.toString(),
                                        fontSize = fontSize,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.SansSerif,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                Text(
                                    text = "TOCA",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.75f),
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 1.2.sp
                                )
                            }
                        }

                        // Bottom: Ergonomic Quick-Action Row
                        Row(
                            modifier = Modifier
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Decrement Button (-)
                            FilledTonalIconButton(
                                onClick = { decrement() },
                                enabled = count > 0,
                                modifier = Modifier.size(38.dp),
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Canvas(modifier = Modifier.size(14.dp)) {
                                    val strokeWidth = 2.dp.toPx()
                                    val color = if (count > 0) Color.White else Color.Gray.copy(alpha = 0.4f)
                                    drawLine(
                                        color = color,
                                        start = Offset(0f, size.height / 2),
                                        end = Offset(size.width, size.height / 2),
                                        strokeWidth = strokeWidth,
                                        cap = StrokeCap.Round
                                    )
                                }
                            }

                            // Reset Button
                            FilledTonalIconButton(
                                onClick = { reset() },
                                enabled = count > 0,
                                modifier = Modifier.size(38.dp),
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reiniciar",
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Increment Button (+)
                            FilledIconButton(
                                onClick = { increment() },
                                modifier = Modifier.size(42.dp),
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Sumar",
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    device = "id:wearos_small_round",
    showSystemUi = true
)
@Composable
fun DefaultPreview() {
    ContadorApp()
}

@Preview(
    device = "id:wearos_large_round",
    showSystemUi = true
)
@Composable
fun LargeRoundPreview() {
    ContadorApp()
}
