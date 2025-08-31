package com.example.gymnasticappnew

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymnasticappnew.ui.theme.GymnasticAppNewTheme
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GymnasticAppNewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GymnasticsApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GymnasticsApp(modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    var score by rememberSaveable { mutableStateOf(0) }
    var currentElement by rememberSaveable { mutableStateOf(0) }
    var isDeductionTaken by rememberSaveable { mutableStateOf(false) }
    var isRoutineComplete by rememberSaveable { mutableStateOf(false) }

    val scoreColor = when (currentElement) {
        in 1..3 -> Color.Blue
        in 4..7 -> Color.Green
        in 8..10 -> Color(0xFFFFA500) // Orange
        else -> Color.Black
    }

    fun updateScoreForElement() {
        if (!isDeductionTaken && !isRoutineComplete && score < 20) {
            currentElement++
            when (currentElement) {
                in 1..3 -> score += 1
                in 4..7 -> score += 2
                in 8..10 -> score += 3
            }
            if (score > 20) score = 20
            if (currentElement == 10) {
                isRoutineComplete = true
            }
            Log.d("GymnasticsApp", "Perform clicked: element=$currentElement, score=$score")
        }
    }

    fun applyDeduction() {
        if (currentElement > 0 && !isRoutineComplete) {
            score = (score - 2).coerceAtLeast(0)
            isDeductionTaken = true
            Log.d("GymnasticsApp", "Deduction clicked: score=$score")
        }
    }

    fun resetScore() {
        score = 0
        currentElement = 0
        isDeductionTaken = false
        isRoutineComplete = false
        Log.d("GymnasticsApp", "Reset clicked")
    }

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            LandscapeLayout(
                score = score,
                currentElement = currentElement,
                isDeductionTaken = isDeductionTaken,
                isRoutineComplete = isRoutineComplete,
                scoreColor = scoreColor,
                onPerformClick = { updateScoreForElement() },
                onDeductionClick = { applyDeduction() },
                onResetClick = { resetScore() },
                modifier = modifier
            )
        }
        else -> {
            PortraitLayout(
                score = score,
                currentElement = currentElement,
                isDeductionTaken = isDeductionTaken,
                isRoutineComplete = isRoutineComplete,
                scoreColor = scoreColor,
                onPerformClick = { updateScoreForElement() },
                onDeductionClick = { applyDeduction() },
                onResetClick = { resetScore() },
                modifier = modifier
            )
        }
    }
}

@Composable
fun PortraitLayout(
    score: Int,
    currentElement: Int,
    isDeductionTaken: Boolean,
    isRoutineComplete: Boolean,
    scoreColor: Color,
    onPerformClick: () -> Unit,
    onDeductionClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_title),
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = stringResource(R.string.score_label, score),
            fontSize = 20.sp,
            color = scoreColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(R.string.element_label, currentElement),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = when {
                isRoutineComplete -> stringResource(R.string.routine_complete)
                isDeductionTaken -> stringResource(R.string.deduction_taken)
                else -> ""
            },
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = onPerformClick,
            enabled = !isDeductionTaken && !isRoutineComplete && score < 20,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(stringResource(R.string.perform_button))
        }
        Button(
            onClick = onDeductionClick,
            enabled = currentElement > 0 && !isRoutineComplete,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(stringResource(R.string.deduction_button))
        }
        Button(
            onClick = onResetClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.reset_button))
        }
    }
}

@Composable
fun LandscapeLayout(
    score: Int,
    currentElement: Int,
    isDeductionTaken: Boolean,
    isRoutineComplete: Boolean,
    scoreColor: Color,
    onPerformClick: () -> Unit,
    onDeductionClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_title),
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = stringResource(R.string.score_label, score),
                fontSize = 20.sp,
                color = scoreColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.element_label, currentElement),
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = when {
                    isRoutineComplete -> stringResource(R.string.routine_complete)
                    isDeductionTaken -> stringResource(R.string.deduction_taken)
                    else -> ""
                },
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onPerformClick,
                enabled = !isDeductionTaken && !isRoutineComplete && score < 20,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(stringResource(R.string.perform_button))
            }
            Button(
                onClick = onDeductionClick,
                enabled = currentElement > 0 && !isRoutineComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(stringResource(R.string.deduction_button))
            }
            Button(
                onClick = onResetClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.reset_button))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PortraitPreview() {
    GymnasticAppNewTheme {
        PortraitLayout(
            score = 0,
            currentElement = 0,
            isDeductionTaken = false,
            isRoutineComplete = false,
            scoreColor = Color.Black,
            onPerformClick = {},
            onDeductionClick = {},
            onResetClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true, device = "spec:width=800dp,height=360dp,orientation=landscape")
@Composable
fun LandscapePreview() {
    GymnasticAppNewTheme {
        LandscapeLayout(
            score = 0,
            currentElement = 0,
            isDeductionTaken = false,
            isRoutineComplete = false,
            scoreColor = Color.Black,
            onPerformClick = {},
            onDeductionClick = {},
            onResetClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}