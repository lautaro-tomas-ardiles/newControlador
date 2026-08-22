package com.example.newcontrolador.utilitis

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color.HSVToColor
import android.graphics.Color.colorToHSV
import android.graphics.ComposeShader
import android.graphics.LinearGradient
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import com.example.newcontrolador.data.enums.ColorsEnum
import com.example.newcontrolador.storage.DataStoreViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Shader
import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.core.graphics.toRect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ColorPicker {

	@Composable
	private fun SaturationSelector(
		hue: Float,
		setSatVal: (Float, Float) -> Unit
	) {
		val interactionSource = remember { MutableInteractionSource() }
		val scope = rememberCoroutineScope()
		var sat: Float
		var value: Float
		val pressOffset = remember { mutableStateOf(Offset.Zero)}

		Canvas(
			modifier = Modifier
				.width(300.dp)
				.height(150.dp)
				.emitDragGesture(interactionSource)
				.clip(RoundedCornerShape(20.dp))
		) {
			val cornerRadius = 12.dp.toPx()
			val satValSize = size
			val bitmap = createBitmap(size.width.toInt(), size.height.toInt())
			val canvas = android.graphics.Canvas(bitmap)
			val satValPanel = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
			val rgb = HSVToColor(floatArrayOf(hue, 1f, 1f))
			val satShader = LinearGradient(
				satValPanel.left, satValPanel.top, satValPanel.right, satValPanel.top,
				-0x1, rgb, Shader.TileMode.CLAMP
			)
			val valShader = LinearGradient(
				satValPanel.left, satValPanel.top, satValPanel.left, satValPanel.bottom,
				-0x1, -0x1000000, Shader.TileMode.CLAMP
			)
			canvas.drawRoundRect(
				satValPanel,
				cornerRadius,
				cornerRadius,
				Paint().apply {
					shader = ComposeShader(
						valShader,
						satShader,
						PorterDuff.Mode.MULTIPLY
					)
				}
			)
			drawBitmap(
				bitmap = bitmap,
				panel = satValPanel
			)

			fun pointToSatVal(pointX: Float, pointY: Float): Pair<Float, Float> {
				val width = satValPanel.width()
				val height = satValPanel.height()
				val x = when {
					pointX < satValPanel.left -> 0f
					pointX > satValPanel.right -> width
					else -> pointX - satValPanel.left
				}
				val y = when {
					pointY < satValPanel.top -> 0f
					pointY > satValPanel.bottom -> height
					else -> pointY - satValPanel.top
				}
				val satPoint = 1f / width * x
				val valuePoint = 1f - 1f / height * y
				return satPoint to valuePoint
			}
			scope.collectForPress(interactionSource) { pressPosition ->
				val pressPositionOffset = Offset(
					pressPosition.x.coerceIn(0f..satValSize.width),
					pressPosition.y.coerceIn(0f..satValSize.height)
				)

				pressOffset.value = pressPositionOffset
				val (satPoint, valuePoint) = pointToSatVal(
					pressPositionOffset.x,
					pressPositionOffset.y
				)
				sat = satPoint
				value = valuePoint
				setSatVal(sat, value)
			}
			drawCircle(
				color = Color.White,
				radius = 8.dp.toPx(),
				center = pressOffset.value,
				style = Stroke(
					width = 2.dp.toPx()
				)
			)
			drawCircle(
				color = Color.White,
				radius = 2.dp.toPx(),
				center = pressOffset.value,
			)
		}
	}

	@Composable
	private fun HueSlider(setColor: (Float) -> Unit) {
		val scope = rememberCoroutineScope()
		val interactionSource = remember { MutableInteractionSource() }
		val pressOffset = remember { mutableStateOf(Offset.Zero) }

		Canvas(
			modifier = Modifier
				.height(40.dp)
				.width(300.dp)
				.clip(RoundedCornerShape(50))
				.emitDragGesture(interactionSource)
		) {
			val drawScopeSize = size
			val bitmap = createBitmap(size.width.toInt(), size.height.toInt())
			val hueCanvas = android.graphics.Canvas(bitmap)
			val huePanel = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
			val hueColors = IntArray((huePanel.width()).toInt())
			var hue = 0f
			for (i in hueColors.indices) {
				hueColors[i] = HSVToColor(floatArrayOf(hue, 1f, 1f))
				hue += 360f / hueColors.size
			}
			val linePaint = Paint()
			linePaint.strokeWidth = 0F
			for (i in hueColors.indices) {
				linePaint.color = hueColors[i]
				hueCanvas.drawLine(i.toFloat(), 0F, i.toFloat(), huePanel.bottom, linePaint)
			}
			drawBitmap(
				bitmap = bitmap,
				panel = huePanel
			)
			fun pointToHue(pointX: Float): Float {
				val width = huePanel.width()
				val x = when {
					pointX < huePanel.left -> 0F
					pointX > huePanel.right -> width
					else -> pointX - huePanel.left
				}
				return x * 360f / width
			}

			scope.collectForPress(interactionSource) { pressPosition ->
				val pressPos = pressPosition.x.coerceIn(0f..drawScopeSize.width)
				pressOffset.value = Offset(pressPos, 0f)
				val selectedHue = pointToHue(pressPos)
				setColor(selectedHue)
			}

			drawCircle(
				Color.White,
				radius = size.height / 2,
				center = Offset(pressOffset.value.x, size.height / 2),
				style = Stroke(
					width = 2.dp.toPx()
				)
			)
		}
	}

	private fun CoroutineScope.collectForPress(
		interactionSource: InteractionSource,
		setOffset: (Offset) -> Unit
	) {
		launch {
			interactionSource.interactions.collect { interaction ->
				(interaction as? PressInteraction.Press)
					?.pressPosition
					?.let(setOffset)
			}
		}
	}

	private fun Modifier.emitDragGesture(
		interactionSource: MutableInteractionSource
	): Modifier = composed {
		val scope = rememberCoroutineScope()
		pointerInput(Unit) {
			detectDragGestures { input, _ ->
				scope.launch {
					interactionSource.emit(PressInteraction.Press(input.position))
				}
			}
		}.clickable(interactionSource, null) {
		}
	}

	private fun DrawScope.drawBitmap(
		bitmap: Bitmap,
		panel: RectF
	) {
		drawIntoCanvas {
			it.nativeCanvas.drawBitmap(
				bitmap,
				null,
				panel.toRect(),
				null
			)
		}
	}

	@Composable
	private fun PaletItem(
		color: Long,
		isSelected: Boolean,
		colorEnum: ColorsEnum,
		onClick: (ColorsEnum) -> Unit
	) {
		Box(
			modifier = Modifier
				.size(50.dp)
				.background(Color(color))
				.border(
					width = 3.dp,
					color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
				)
				.clickable(onClick = { onClick(colorEnum) })
		)
		Toast.makeText(
			LocalContext.current,
			"Color ${colorEnum.name}",
			Toast.LENGTH_SHORT
		).show()
	}

	@Composable
	private fun PaletPicker(
		colors: Map<ColorsEnum, Long>,
		colorSelected: ColorsEnum,
		onColorSelected: (ColorsEnum) -> Unit
	) {
		val scroll = rememberScrollState()

		Row(Modifier.horizontalScroll(scroll)) {
			ColorsEnum.entries.forEach { colorEnum ->
				PaletItem(
					color = colors[colorEnum] ?: 0xFFFFFFFF,
					isSelected = colorSelected == colorEnum,
					colorEnum = colorEnum,
					onClick = onColorSelected
				)
				Spacer(Modifier.width(8.dp))
			}
		}
	}

	@SuppressLint("NotConstructor")
	@Composable
	fun ColorPicker(viewModel: DataStoreViewModel) {
		var colorSelected by remember { mutableStateOf(ColorsEnum.PRIMARY) }
		var colors by remember {
			mutableStateOf(
				mapOf(
					ColorsEnum.PRIMARY to 0xFF008DD5,
					ColorsEnum.SECONDARY to 0xFFFBD552,
					ColorsEnum.TERTIARY to 0xFF00ECBA,
					ColorsEnum.BACKGROUND to 0xFF202C39,
					ColorsEnum.ON_SECONDARY to 0xFFDEB93B,
					ColorsEnum.ON_TERTIARY to 0xFF009071,
					ColorsEnum.ON_BACKGROUND to 0xFF222222
				)
			)
		}
		val hsv = remember {
			val hsvArray = floatArrayOf(0f, 0f, 0f)

			colorToHSV(colors[ColorsEnum.PRIMARY]!!.toInt(), hsvArray)
			mutableStateOf(Triple(hsvArray[0], hsvArray[1], hsvArray[2]))
		}

		val selectedColor = HSVToColor(
			floatArrayOf(hsv.value.first, hsv.value.second, hsv.value.third)
		).toLong()

		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(horizontal = 30.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			SaturationSelector(hsv.value.first) { sat, value ->
				hsv.value = Triple(hsv.value.first, sat, value)

				val newColor = HSVToColor(floatArrayOf(hsv.value.first, sat, value)).toLong()

				colors = colors.toMutableMap().apply { this[colorSelected] = newColor }
			}
			LaunchedEffect(hsv.value, colorSelected) {
				delay(300)

				val newColor = HSVToColor(
					floatArrayOf(hsv.value.first, hsv.value.second, hsv.value.third)
				).toLong()

				viewModel.setColors(newColor, colorSelected)
			}
			Spacer(Modifier.height(20.dp))

			HueSlider { hue ->
				hsv.value = Triple(hue, hsv.value.second, hsv.value.third)

				colors = colors.toMutableMap().apply {
					this[colorSelected] = HSVToColor(
						floatArrayOf(hue, hsv.value.second, hsv.value.third)
					).toLong()
				}
			}
			Spacer(Modifier.height(20.dp))

			PaletPicker(
				colors = colors,
				colorSelected = colorSelected
			) { selected ->
				colorSelected = selected

				val color = colors[selected] ?: 0xFFFFFFFF
				val hsvArray = floatArrayOf(0f, 0f, 0f)

				colorToHSV(color.toInt(), hsvArray)

				hsv.value = Triple(hsvArray[0], hsvArray[1], hsvArray[2])
			}
		}
	}
}