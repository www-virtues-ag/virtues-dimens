/**
 * @author Bodenberg
 * GIT: https://github.com/www-virtues-ag/virtues-dimens.git
 */
package ag.virtues.dimens.example.compose.pt

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ag.virtues.dimens.dynamic.compose.Virtues
import ag.virtues.dimens.dynamic.compose.Virtues.fxdp
import ag.virtues.dimens.dynamic.compose.Virtues.fxsp
import ag.virtues.dimens.dynamic.compose.VirtuesPhysicalUnits
import ag.virtues.dimens.library.DpQualifier
import ag.virtues.dimens.library.ScreenType
import ag.virtues.dimens.library.UnitType
import ag.virtues.dimens.library.UiModeType
import java.util.Locale

/**
 * [EN] Main demonstration activity.
 * 
 * [PT] Activity principal de demonstração.
 */
@OptIn(ExperimentalMaterial3Api::class)
class FixedExampleActivity : ComponentActivity() {
    /**
     * [EN] Called when the activity is first created.
     * 
     * [PT] Chamado quando a atividade é criada pela primeira vez.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                VirtuesDemoScreen()
            }
        }
    }
}

/**
 * [EN] Main screen that demonstrates the features of Virtues.
 * 
 * [PT] Tela principal que demonstra os recursos do Virtues.
 */
@SuppressLint("LocalContextResourcesRead")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtuesDemoScreen() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val ctx = LocalContext.current

    // [EN] Toggleable state used in the demo to show how changing ScreenType affects the results.
    // [PT] Estado alternável usado na demo para mostrar como a mudança de ScreenType afeta os resultados.
    var currentScreenType by remember { mutableStateOf(ScreenType.LOWEST) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Virtues — Compose demo", fontWeight = FontWeight.SemiBold) })
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                // [EN] Using the fxdp (fixed dp) extension from Virtues.
                // [PT] Usando a extensão fxdp (fixed dp) do Virtues.
                .padding(12.fxdp),
            verticalArrangement = Arrangement.spacedBy(12.fxdp)
        ) {
            item {
                InfoCard(
                    title = "Métricas do Dispositivo",
                    subtitle = "Leitura rápida das métricas da tela (dp e px)"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.fxdp)) {
                        Text("screenWidthDp = ${configuration.screenWidthDp} dp")
                        Text("screenHeightDp = ${configuration.screenHeightDp} dp")
                        Text("smallestScreenWidthDp = ${configuration.smallestScreenWidthDp} dp")
                        val pxPerDp = density.density
                        Text(String.format(Locale.getDefault(), "density = %.2f (px por dp)", pxPerDp))
                    }
                }
            }

            item {
                UsageCard(title = "Fixo (Fixed) vs Dinâmico (Dynamic) (básico)") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.fxdp)) {
                        val baseDp = 16.dp

                        // [EN] Using the Virtues extension helpers within the Virtues scope.
                        // [PT] Usando os helpers de extensão do Virtues dentro do escopo Virtues.
                        val fixedDp = with(Virtues) { baseDp.fixedDp(type = currentScreenType) }
                        val fixedSp = with(Virtues) { baseDp.fixedSp(type = currentScreenType) }
                        val fixedPx = with(Virtues) { baseDp.fixedPx(type = currentScreenType) }

                        val dynamicDp = with(Virtues) { baseDp.dynamicDp(type = currentScreenType) }
                        val dynamicSp = with(Virtues) { baseDp.dynamicSp(type = currentScreenType) }
                        val dynamicPx = with(Virtues) { baseDp.dynamicPx(type = currentScreenType) }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.fxdp)) {
                            // [EN] Visual demonstration of fixed and dynamic size.
                            // [PT] Demonstração visual do tamanho fixo e dinâmico.
                            DemoTile(size = fixedDp, label = "fixo dp = ${fixedDp.value.toInt()}dp")
                            DemoTile(size = dynamicDp, label = "dinâmico dp = ${dynamicDp.value.toInt()}dp")
                        }

                        Text("fixo sp = ${fixedSp.value}sp, fixo px ≈ ${fixedPx.toInt()}px")
                        Text("dinâmico sp = ${dynamicSp.value}sp, dinâmico px ≈ ${dynamicPx.toInt()}px")

                        Column (verticalArrangement = Arrangement.spacedBy(8.fxdp)) {
                            Button(onClick = {
                                // [EN] Toggles between LOWEST (smaller screen) and HIGHEST (larger screen).
                                // [PT] Alterna entre LOWEST (menor tela) e HIGHEST (maior tela).
                                currentScreenType = if (currentScreenType == ScreenType.LOWEST) ScreenType.HIGHEST else ScreenType.LOWEST
                            }) {
                                Text("Alternar ScreenType (atual: $currentScreenType)")
                            }
                            Text("Dica: Alternar o tipo de tela recalcula a dimensão subjacente.")
                        }
                    }
                }
            }

            item {
                UsageCard(title = "Propriedades de conveniência (fxdp / fxsp / dydp / dypx)") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.fxdp)) {
                        val sampleDp = 24.dp
                        // [EN] Convenience extensions:
                        // [PT] Extensões de conveniência:
                        val convFxDp = with(Virtues) { sampleDp.fxdp }
                        val convFxSp = with(Virtues) { 14.fxsp }
                        // [EN] dynamicPerDp calculates a percentage of the screen dimension.
                        // [PT] dynamicPerDp calcula uma porcentagem da dimensão da tela.
                        val convDyDp = with(Virtues) { 0.10f.dynamicPerDp() }

                        Text("base da amostra = ${sampleDp.value}dp")
                        Text("fxdp -> ${convFxDp.value}dp")
                        Text("fxsp(14.dp) -> ${convFxSp.value}sp")
                        Text("0.10f.dynamicPercentageDp() -> ${convDyDp.value}dp (10% da tela)")

                        // [EN] Demo: using fxsp for font size.
                        // [PT] Demo: usar fxsp para tamanho da fonte.
                        Text(
                            "Este texto usa Virtues.fxsp (14.dp.fxsp)",
                            fontSize = with(Virtues) { 14.fxsp }
                        )
                    }
                }
            }

            item {
                UsageCard(title = "Qualificadores personalizados (tela / modo UI / interseção)") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.fxdp)) {

                        // [EN] Example: custom value for width qualifier.
                        // [PT] Exemplo: valor personalizado para qualificador de largura.
                        val customWidthDp = with(Virtues) {
                            16.dp.fixed()
                                // [EN] If screen width is >= 360, use 20dp as base.
                                // [PT] Se a largura da tela for >= 360, usa 20dp como base.
                                .screen(DpQualifier.WIDTH, 360, 20)
                                .type(currentScreenType)
                                .dp
                        }

                        Text("customWidthDp (LARGURA >= 360) -> ${customWidthDp.value}dp")

                        // [EN] UiModeType describes the device/UI mode (car, tv, watch, appliance, etc.).
                        // [PT] UiModeType descreve o modo do dispositivo/UI (carro, tv, relógio, eletrodoméstico, etc.).
                        // [EN] The purpose here is to show how to get the values to create a custom qualifier.
                        // [PT] O objetivo aqui é mostrar como obter os valores para criar um qualificador personalizado.
                        val uiModeValues = runCatching { UiModeType::class.java.enumConstants?.toList() ?: emptyList() }.getOrNull() ?: emptyList()
                        var selectedUiMode by remember { mutableStateOf(uiModeValues.firstOrNull()) }

                        Text("UiMode atual detectado: ${UiModeType.fromConfiguration(configuration.uiMode)}")

                        if (uiModeValues.isNotEmpty()) {
                            // [EN] List of buttons to select a UiModeType.
                            // [PT] Lista de botões para selecionar um UiModeType.
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.fxdp)) {
                                items(uiModeValues.size) { index ->
                                    val mode = uiModeValues[index]
                                    Button(
                                        onClick = { selectedUiMode = mode },
                                        modifier = Modifier.height(36.fxdp),
                                        // [EN] Highlights the selected mode.
                                        // [PT] Destaca o modo selecionado.
                                        enabled = selectedUiMode != mode
                                    ) {
                                        Text(mode.name)
                                    }
                                }
                            }

                            Spacer(Modifier.height(6.fxdp))

                            // [EN] Custom intersection example using the selected UiModeType.
                            // [PT] Exemplo de interseção personalizada usando o UiModeType selecionado.
                            val customUiModeDp = selectedUiMode?.let { sel ->
                                with(Virtues) {
                                    18.fixed()
                                        // [EN] Intersection: (selected device mode) AND (small width >= 600) -> use 22dp.
                                        // [PT] Interseção: (modo do dispositivo selecionado) E (largura pequena >= 600) -> usa 22dp.
                                        .screen(sel, DpQualifier.SMALL_WIDTH, 600, 22)
                                        .type(currentScreenType)
                                        .dp
                                }
                            }

                            Text("UiMode selecionado (para valor customizado): ${selectedUiMode?.name ?: "—"}")
                            Text("customUiModeDp (interseção se selecionado) -> ${customUiModeDp?.value?.toInt() ?: "n/a"}dp")

                        } else {
                            Text("Os valores do enum UiModeType não são detectáveis em runtime. Verifique sua biblioteca Virtues para os valores constantes disponíveis (ex: CAR, TELEVISION, WATCH, APPLIANCE, etc.).")
                        }

                        Text("Estes exemplos mostram a cadeia de prioridade: INTERSEÇÃO > MODO UI > QUALIFICADOR")
                    }
                }
            }

            item {
                UsageCard(title = "Unidades físicas (mm / cm / polegada) e raio") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.fxdp)) {
                        // [EN] Using the composable properties provided by VirtuesPhysicalUnits.
                        // [PT] Usando as propriedades composable fornecidas por VirtuesPhysicalUnits.
                        val tenMmPx = with(VirtuesPhysicalUnits) { 10f.mm }
                        val twoInchPx = with(VirtuesPhysicalUnits) { 2f.inch }
                        val threeCmPx = with(VirtuesPhysicalUnits) { 3f.cm }

                        Text("10 mm ≈ ${tenMmPx.toInt()} px")
                        Text("2 in ≈ ${twoInchPx.toInt()} px")
                        Text("3 cm ≈ ${threeCmPx.toInt()} px")

                        // [EN] Radius helper (composable extension) - if provided by the library.
                        // [PT] Helper de raio (extensão composable) - se fornecido pela biblioteca.
                        // [EN] Calculates the radius in pixels from a diameter value in a physical unit.
                        // [PT] Calcula o raio em pixels a partir de um valor de diâmetro em uma unidade física.
                        val radiusFromDiameterPx = with(VirtuesPhysicalUnits) { 30f.radius(UnitType.CM) }
                        Text("Diâmetro de 30 cm -> raio ≈ ${radiusFromDiameterPx.toInt()} px")

                        // [EN] Size in pixels for a physical unit.
                        // [PT] Tamanho em pixels para uma unidade física.
                        val oneCmPx = VirtuesPhysicalUnits.unitSizePerPx(UnitType.CM, ctx.resources)
                        Text("1 cm neste dispositivo ≈ ${oneCmPx.toInt()} px")
                    }
                }
            }

            item {
                UsageCard(title = "CalculateAvailableItemCount (helper de layout)") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.fxdp)) {
                        var availableCount by remember { mutableStateOf(-1) }

                        Text("Contêiner (largura total) com tamanho de item fixo de 80dp")

                        // [EN] The helper measures the area of the provided Box and calls onResult.
                        // [PT] O helper mede a área do Box fornecido e chama onResult.
                        with(Virtues) {
                            CalculateAvailableItemCount(
                                itemSize = 80.fxdp, // [EN] Size of each item.
                                                    // [PT] Tamanho de cada item.
                                itemPadding = 4.fxdp, // [EN] Spacing between items.
                                                      // [PT] Espaçamento entre os itens.
                                direction = DpQualifier.WIDTH, // [EN] Width (horizontal).
                                                             // [PT] Largura (horizontal).
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.fxdp)
                                    .border(1.fxdp, Color.Gray, RoundedCornerShape(6.fxdp)),
                                onResult = { availableCount = it } // [EN] Receives the number of items that fit.
                                                                   // [PT] Recebe o número de itens que cabem.
                            )
                        }

                        Text("availableCount = $availableCount")

                        if (availableCount > 0) {
                            // [EN] Renders the items that fit.
                            // [PT] Renderiza os itens que cabem.
                            Row(horizontalArrangement = Arrangement.spacedBy(8.fxdp)) {
                                repeat(availableCount.coerceAtMost(6)) { i ->
                                    DemoTile(size = 80.fxdp, label = "item ${i + 1}")
                                }
                            }
                        }
                    }
                }
            }

            item {
                UsageCard(title = "Outros exemplos e utilities") {
                    Column(verticalArrangement = Arrangement.spacedBy(8.fxdp)) {
                        // [EN] dynamicPercentage helper.
                        // [PT] Helper dynamicPercentage.
                        val tenPercentDp = with(Virtues) { dynamicPercentageDp(0.10f, ScreenType.LOWEST) }
                        Text("10% da dimensão (mais baixa) da tela -> ${tenPercentDp.value.toInt()}dp")

                        val tenPercentDp2 = with(Virtues) { dynamicPercentageDp(0.10f, ScreenType.HIGHEST) }
                        Text("10% da dimensão (mais alta) da tela -> ${tenPercentDp2.value.toInt()}dp")

                        // [EN] Conversions and px values.
                        // [PT] Conversões e valores de px.
                        val pxFromSp = with(Virtues) { 18.fixedPx() }
                        Text("18sp fixedPx -> ${pxFromSp.toInt()} px")

                        // [EN] Shows how to return raw float/px when needed.
                        // [PT] Mostra como retornar float/px brutos quando necessário.
                        val diameterPx = with(VirtuesPhysicalUnits) { 5f.inch }
                        // [EN] Calculates the circumference based on the diameter in pixels.
                        // [PT] Calcula a circunferência com base no diâmetro em pixels.
                        val circumference = VirtuesPhysicalUnits.displayMeasureDiameter(diameterPx, true)
                        Text("5in -> diâmetro px = ${diameterPx.toInt()} px, circunferência px ≈ ${circumference.toInt()} px")
                    }
                }
            }
        }
    }
}

// [EN] -----------------------------------------------------------------------------
// [PT] -----------------------------------------------------------------------------
// [EN] -- Small helper composables used above --
// [PT] -- Pequenos composables auxiliares usados acima --
// [EN] -----------------------------------------------------------------------------
// [PT] -----------------------------------------------------------------------------

/**
 * [EN] A card for displaying general information.
 * 
 * [PT] Um card para exibir informações gerais.
 */
@Composable
private fun InfoCard(title: String, subtitle: String, content: @Composable () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.fxdp),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.fxdp)
    ) {
        Column(modifier = Modifier.padding(12.fxdp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.fxdp))
            Text(subtitle, fontSize = 12.fxsp, color = Color.Gray)
            Spacer(Modifier.height(8.fxdp))
            content()
        }
    }
}

/**
 * [EN] A card for wrapping usage examples.
 * 
 * [PT] Um card para envolver exemplos de uso.
 */
@Composable
private fun UsageCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.fxdp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.fxdp)
    ) {
        Column(modifier = Modifier.padding(14.fxdp)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.fxdp))
            content()
        }
    }
}

/**
 * [EN] A simple tile to visually represent a size.
 * 
 * [PT] Um tile simples para representar visualmente um tamanho.
 */
@Composable
private fun DemoTile(size: Dp, label: String) {
    Box(
        modifier = Modifier
            .size(size)
            .background(Color(0xFFEEEEEE), shape = RoundedCornerShape(6.fxdp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(6.fxdp)),
        contentAlignment = Alignment.Center
    ) {
        Text(label, fontSize = 11.fxsp)
    }
}

// [EN] -----------------------------------------------------------------------------
// [PT] -----------------------------------------------------------------------------
// [EN] --- Preview for Visualization ---
// [PT] --- Preview para Visualização ---
// [EN] -----------------------------------------------------------------------------
// [PT] -----------------------------------------------------------------------------
/**
 * [EN] A preview for the Virtues demo screen.
 * 
 * [PT] Uma pré-visualização para a tela de demonstração do Virtues.
 */
@Preview(
    showBackground = true,
    device = "id:pixel_5", showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun VirtuesPreview() {
    MaterialTheme {
        VirtuesDemoScreen()
    }
}