package com.example.sampletask

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ScratchCardApp() {
    var showDialog by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEEEEE)),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { showDialog = true }) {
            Text("Show Scratch Card")
        }

        if (showDialog) {
            ScratchCardDialog(onDismiss = { showDialog = false })
        }
    }
}

@Composable
fun ScratchCardDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable(enabled = false) {},
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 40.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }

            ScratchCardContent()
        }
    }
}

@Composable
fun ScratchCardContent() {
    var showTerms by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(320.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            RewardContent(onTermsClick = { showTerms = !showTerms })

            ScratchOverlay(
                modifier = Modifier.fillMaxSize()
            )
        }

        AnimatedVisibility(visible = showTerms) {
            TermsSection()
        }
    }
}

@Composable
fun ScratchOverlay(modifier: Modifier = Modifier) {
    val path = remember { Path() }
    var pathVersion by remember { mutableStateOf(0) }
    var isRevealed by remember { mutableStateOf(false) }
    val scratchPoints = remember { mutableListOf<Offset>() }

    if (isRevealed) return

    Box(
        modifier = modifier
            .graphicsLayer { alpha = 0.99f }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF4285F4)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val confettiColors = listOf(Color.Cyan, Color.Blue, Color(0xFF1967D2))
                repeat(25) {
                    drawCircle(
                        color = confettiColors.random(),
                        radius = (4..10).random().dp.toPx(),
                        center = Offset(
                            x = (0..size.width.toInt()).random().toFloat(),
                            y = (0..size.height.toInt()).random().toFloat()
                        ),
                        alpha = 0.4f
                    )
                }
            }

            Text(
                text = "Scratch me",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            path.moveTo(offset.x, offset.y)
                            scratchPoints.add(offset)
                            pathVersion++
                        },
                        onDrag = { change, _ ->
                            path.lineTo(change.position.x, change.position.y)
                            scratchPoints.add(change.position)
                            pathVersion++

                            if (scratchPoints.size > 150) {
                                isRevealed = true
                            }
                        }
                    )
                }
        ) {
            if (pathVersion >= 0) {
                drawPath(
                    path = path,
                    color = Color.Transparent,
                    style = Stroke(width = 80f, cap = StrokeCap.Round, join = StrokeJoin.Round),
                    blendMode = BlendMode.Clear
                )
            }
        }
    }
}

@Composable
fun RewardContent(onTermsClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color.White, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "₹100", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.Black)
                Text(text = "Cashback", fontSize = 10.sp, color = Color.Black)
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Offer from AppVersal",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "Cashback on mobile and recharge",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFDAA520), RoundedCornerShape(8.dp))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "APPVERSAL1001",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text("Claim offer now", color = Color.White)
        }

        Text(
            text = "Terms & Conditions >",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.clickable { onTermsClick() }
        )
    }
}

@Composable
fun TermsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .background(Color(0xFF121212), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Eligibility:",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Text(
            text = "Only fake users who meet the campaign criteria (as defined by the brand/platform) are eligible to participate in the Scratch CRC program.",
            color = Color.Gray,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = "Non-Transferable & One-Time Use:",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Text(
            text = "Each scratch code/reward is unique, valid for a single use, and cannot be transferred, exchanged, or redeemed for cash unless explicitly stated.",
            color = Color.Gray,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = "Fraud Prevention:",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Text(
            text = "Any misuse, duplication, unauthorized distribution, or suspicious activity related to the scratch code will result in immediate disqualification and potential blocking of the user/account.",
            color = Color.Gray,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}