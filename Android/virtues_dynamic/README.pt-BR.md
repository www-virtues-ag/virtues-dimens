# COMPOSE

## 📚 Documentação Core do Virtues (COMPOSE)

O core do **Virtues** fornece a inteligência por trás do sistema de escalonamento, oferecendo três APIs principais:

1.  **`VirtuesDynamic`**: Escalonamento adaptativo avançado (percentual, ignora multi-janela).
2.  **`VirtuesFixed`**: Escalonamento responsivo com Aspect Ratio (logarítmico).
3.  **`VirtuesPhysicalUnits`**: Conversão de unidades físicas (polegadas, mm, cm) para Dp, Sp e Px.

-----

## 1\. 🌐 VirtuesDynamic: Dimensionamento Percentual Adaptativo

O `VirtuesDynamic` aplica um tipo de dimensionamento "percentual", onde o valor de DP base é adaptado proporcionalmente à dimensão da tela (Largura ou Altura), semelhante a uma porcentagem.

### 📌 Conceito Central

Em vez de usar fatores de ajuste incrementais, o `VirtuesDynamic` calcula:

$$\text{Valor Final} = \text{Dimensão da Tela (W ou H)} \times \left( \frac{\text{DP Base}}{\text{Largura de Ref. (360dp)}} \right)$$

Isso garante que um elemento de $100\text{dp}$ em uma tela de $360\text{dp}$ ocupe a mesma proporção em uma tela de $720\text{dp}$.

### Métodos e Getters Principais

A classe `Dynamic` é iniciada com um valor e um tipo de tela, determinando se o escalonamento será baseado na **maior** (`HIGHEST`) ou **menor** (`LOWEST`) dimensão do dispositivo.

| Iniciação | Descrição |
| :--- | :--- |
| `Int.dynamic(ScreenType.LOWEST)` | Inicia a cadeia, usando o DP base e a **menor** dimensão da tela (padrão do Android) para o cálculo percentual. |
| `Dp.dynamic(ScreenType.HIGHEST)` | Inicia a cadeia, usando o DP base e a **maior** dimensão da tela para o cálculo percentual. |

#### Getters de Resolução (`@Composable`)

Após a iniciação, resolva o valor no contexto do Compose:

| Getter | Tipo de Unidade Final |
| :--- | :--- |
| **`.dp`** | Retorna o valor final em **Dp**. |
| **`.sp`** | Retorna o valor final em **TextUnit (Sp)**. |
| **`.em`** | Retorna o valor final em **TextUnit (Em)**. |
| **`.px`** | Retorna o valor final em **Float (Pixels)**. |

### Recurso Exclusivo: Ignorar Ajuste em Multi-Window

O Dynamic Adjustment possui um parâmetro crucial para estabilidade:

| Função | Parâmetro | Descrição |
| :--- | :--- | :--- |
| `.dynamic(...)` | `ignoreMultiViewAdjustment: Boolean` | **Padrão é `true`**. Se estiver em modo de Multi-Window (tela dividida ou Pop-up) e o `smallestWidthDp` for muito diferente da largura atual, o escalonamento dinâmico é **ignorado** e o valor original é usado. Isso evita que elementos fiquem exageradamente grandes em janelas pequenas. |

#### Exemplo de Uso:

```kotlin
@Composable
fun DynamicSizing() {
    // Calcula um Box que sempre ocupará a mesma proporção da largura da tela.
    val dynamicWidth = 100.dynamic(ScreenType.LOWEST).dp 

    // O padding é escalado, mas IGNORARÁ o escalonamento se estiver em modo Multi-Window.
    val dynamicPadding = 16.dynamic(ScreenType.LOWEST).ignoreMultiViewAdjustment(true).dp 

    Box(
        modifier = Modifier
            .width(dynamicWidth)
            .padding(dynamicPadding)
    )
}
```

-----

## 2\. 🎯 VirtuesFixed: Dimensionamento Fixo com Aspect Ratio

O `VirtuesFixed` aplica o dimensionamento responsivo baseado no cálculo de **Fatores de Ajuste** pré-calculados. Este é o método que utiliza o **Aspect Ratio (AR)** e o ajuste logarítmico para um dimensionamento mais sutil e matematicamente refinado.

### 📌 Conceito Central

O ajuste de dimensão é uma função do **Fator de Ajuste** (que é baseado na `smallestWidthDp` ou dimensão mais alta) mais um **Incremento Final** ajustado pelo Aspect Ratio.

$$\text{Valor Final} = \text{DP Base} \times \text{Fator de Ajuste Final}$$

### Métodos e Getters Principais

A classe `Fixed` é iniciada com um valor e um tipo de tela, determinando qual fator base de ajuste deve ser usado (`LOWEST` ou `HIGHEST`).

| Iniciação | Descrição |
| :--- | :--- |
| `Int.fixed(ScreenType.LOWEST)` | Inicia a cadeia, usando o DP base e o fator de ajuste calculado com a **menor** dimensão. |
| `Dp.fixed(ScreenType.HIGHEST)` | Inicia a cadeia, usando o DP base e o fator de ajuste calculado com a **maior** dimensão. |

#### Parâmetros de Ajuste Fino

Você pode customizar o comportamento do escalonamento:

| Método | Parâmetro | Descrição |
| :--- | :--- | :--- |
| `.withAspectRatio()` | `isEnabled: Boolean` | **Padrão é `true`**. Ativa o ajuste fino logarítmico baseado no Aspect Ratio da tela. |
| `.withCustomSensitivity()` | `sensitivityK: Double` | Permite definir um fator **$K$** customizado para o ajuste logarítmico do Aspect Ratio, controlando a agressividade do ajuste. |

#### Getters de Resolução (`@Composable`)

| Getter | Tipo de Unidade Final |
| :--- | :--- |
| **`.dp`** | Retorna o valor final em **Dp**. |
| **`.sp`** | Retorna o valor final em **TextUnit (Sp)**. |
| **`.em`** | Retorna o valor final em **TextUnit (Em)**. |
| **`.px`** | Retorna o valor final em **Float (Pixels)**. |

#### Exemplo de Uso:

```kotlin
@Composable
fun FixedSizing() {
    // Escalonamento padrão (LOWEST + AR habilitado)
    val defaultHeight = 50.fixed(ScreenType.LOWEST).dp

    // Escalonamento baseado na dimensão mais alta, sem ajuste de AR (AR desabilitado)
    val noArPadding = 16.fixed(ScreenType.HIGHEST).withAspectRatio(false).dp 

    // Escalonamento com sensibilidade AR CUSTOMIZADA
    val customArText = 20.fixed(ScreenType.LOWEST).withCustomSensitivity(sensitivityK = 0.5).sp

    Column(
        modifier = Modifier
            .height(defaultHeight)
            .padding(noArPadding)
    ) {
        Text(
            text = "Texto com AR customizado",
            fontSize = customArText
        )
    }
}
```

-----

## 3\. 📏 VirtuesPhysicalUnits: Conversão de Unidades Físicas

O `VirtuesPhysicalUnits` fornece utilitários para converter unidades de medida físicas (Polegada, Centímetro, Milímetro) para as unidades de tela do Android (Dp, Sp, Px), além de utilitários de geometria.

### Métodos de Conversão

As funções de extensão permitem converter um `Float` ou `Int` (que representa a medida física) para a unidade de tela desejada:

| Método | Unidade de Origem | Unidade de Destino | Descrição |
| :--- | :--- | :--- | :--- |
| `Int/Float.toDp(UnitType)` | INCH, CM, MM | **Dp** | Converte para Pixels Lógicos (Density-independent Pixels). |
| `Int/Float.toSp(UnitType)` | INCH, CM, MM | **Sp** | Converte para Pixels de Escala (Scale-independent Pixels). |
| `Int/Float.toPx(UnitType)` | INCH, CM, MM | **Px** | Converte para Pixels Reais da Tela. |

**Unidades Suportadas (`UnitType`)**: `INCH`, `CM`, `MM`.

#### Exemplo de Conversão:

```kotlin
@Composable
fun PhysicalConversion() {
    // 1 Polegada convertida para Dp
    val oneInchInDp: Dp = 1.0f.toDp(UnitType.INCH) 

    // 5 Milímetros convertidos para TextUnit (Sp)
    val fiveMmInSp: TextUnit = 5.toSp(UnitType.MM)

    Box(
        modifier = Modifier.size(oneInchInDp) // 1 polegada em Dp
    ) {
        Text(
            text = "5mm em SP",
            fontSize = fiveMmInSp // 5 milímetros em Sp
        )
    }
}
```

### Utilitários de Geometria e Medição

| Método | Descrição |
| :--- | :--- |
| `Int/Float.radius(UnitType)` | Calcula o **Raio** de um dispositivo esférico (como um relógio) em **Pixels (Px)**, com base em sua medida física (ex: $\text{38mm}$ de diâmetro). |
| `Int/Float.measureDiameter(isCircumference: Boolean)` | Ajusta uma medida física (ex: de um relógio) para refletir o **diâmetro** ou a **circunferência**. |

#### Exemplo de Geometria (Wear OS):

```kotlin
@Composable
fun WearableRadius() {
    // Para um relógio com 42 milímetros de diâmetro, calcula o raio em Px
    val watchDiameterMm = 42
    
    // Obtém o Raio em Px.
    val radiusPx = watchDiameterMm.radius(UnitType.MM)
    
    // A medição de 42mm representa o diâmetro (isCircumference = false)
    val diameterMeasure = 42.measureDiameter(isCircumference = false) 
    
    // ... use radiusPx para desenhar um círculo no Canvas
    // ... use diameterMeasure para cálculos subsequentes
}
```

# 🎯 Dimensionamento Virtues: Fixed (FX) vs. Dynamic (DY)

As extensões `fxdp` e `dydp` são atalhos simplificados para iniciar os métodos `Int.fixed()` e `Int.dynamic()`, respectivamente, nos arquivos de dimensão, mas com uma convenção de nomenclatura específica para **XML Views**.

| Categoria | Método Base (Compose) | Filosofia de Escalonamento |
| :---: | :--- | :--- |
| **Fixed** | `Int.fixed(...)` | **Ajuste Logarítmico/Incremental** com *Aspect Ratio*. Ideal para ajuste sutil de componentes. |
| **Dynamic** | `Int.dynamic(...)` | **Ajuste Percentual/Proporcional**. Ideal para manter proporção em telas extremas. |

-----

## 1\. 📏 Fixed Dimensions (FX): `fxdp`, `fxsp`, `fxpx`

O dimensionamento Fixed (`fx`) é o sistema principal do **Virtues** para um ajuste de escala **responsivo, mas sutil**. Ele se baseia em fatores de ajuste pré-calculados que levam em consideração a diferença entre a dimensão da tela e uma largura de referência (geralmente $360\text{dp}$).

### Filosofia (Ajuste Logarítmico)

Em vez de um simples fator de multiplicação, o Fixed utiliza:

1.  **Fator de Ajuste Base:** Calculado a partir da $\text{smallestWidthDp}$ da tela.
2.  **Ajuste de Aspect Ratio (AR):** Aplica uma função **logarítmica** ao incremento para suavizar o ajuste em telas com proporções extremas (ex: TVs muito wide).

Este modelo é ideal para manter a **sensação de design** consistente, permitindo que as dimensões cresçam ou diminuam de forma controlada e refinada.

### Extensões no Compose

| Extensão (Int) | Unidade Final | Método Base de Cálculo |
| :--- | :--- | :--- |
| **`.fxdp`** | `Dp` | $\text{Int.fixed(ScreenType.LOWEST).dp}$ |
| **`.fxsp`** | `TextUnit (Sp)` | $\text{Int.fixed(ScreenType.LOWEST).sp}$ |
| **`.fxem`** | `TextUnit (Em)` | $\text{Int.fixed(ScreenType.LOWEST).em}$ |
| **`.fxpx`** | `Float (Px)` | $\text{Int.fixed(ScreenType.LOWEST).px}$ |

**Exemplo:**

```kotlin
// Usa o ajuste logarítmico do Virtues para o tamanho e a fonte.
val buttonHeight = 56.fxdp
val textSize = 18.fxsp
```

-----

## 2\. 🚀 Dynamic Dimensions (DY): `dydp`, `dysp`, `dypx`

O dimensionamento Dynamic (`dy`) aplica um escalonamento **puramente proporcional** (ou percentual) baseado na dimensão da tela (Largura ou Altura).

### Filosofia (Ajuste Percentual/Proporcional)

O Dynamic calcula o valor final mantendo a mesma **proporção da dimensão de referência**.

$$\text{Valor Final} = \text{Dimensão da Tela (W ou H)} \times \left( \frac{\text{DP Base}}{\text{Largura de Ref. (360dp)}} \right)$$

Este modelo é ideal para componentes que precisam preencher uma **proporção constante** da tela, como um carrossel que deve ter $1/3$ da largura total, ou para garantir que em telas muito grandes, o elemento **cresça agressivamente** para preencher o espaço.

### Extensões no Compose

| Extensão (Int) | Unidade Final | Método Base de Cálculo |
| :--- | :--- | :--- |
| **`.dydp`** | `Dp` | $\text{Int.dynamic(ScreenType.LOWEST).dp}$ |
| **`.dysp`** | `TextUnit (Sp)` | $\text{Int.dynamic(ScreenType.LOWEST).sp}$ |
| **`.dyem`** | `TextUnit (Em)` | $\text{Int.dynamic(ScreenType.LOWEST).em}$ |
| **`.dypx`** | `Float (Px)` | $\text{Int.dynamic(ScreenType.LOWEST).px}$ |

**Recurso Adicional:** Por padrão, o Dynamic **ignora** o escalonamento em modo Multi-Window para evitar superdimensionamento em janelas pequenas.

**Exemplo:**

```kotlin
// O padding será proporcional à largura da tela.
val contentPadding = 24.dydp

// O texto será proporcional à largura da tela.
val proportionalText = 18.dysp
```

-----

## 🔑 Resumo e Diferença Chave

| Característica | Fixed (FX) | Dynamic (DY) |
| :--- | :--- | :--- |
| **Cálculo Principal** | Fator de Ajuste Logarítmico + AR | Proporção Percentual |
| **Agressividade do Crescimento** | **Sutil e Refinado** | **Agressivo e Proporcional** |
| **Uso Ideal** | Alturas de botões, paddings internos, tamanhos de ícones. | Largura/Altura de containers, dimensões que dependem da proporção do viewport. |
| **Multi-Window** | Não implementado no atalho, mas pode ser ativado no `fixed()`. | **Ignora escalonamento** por padrão para evitar que o UI quebre em janelas pequenas. |

-----

Com certeza\! A função `calculateAvailableItemCount` é um utilitário essencial para criar layouts responsivos baseados no número de colunas/linhas que cabem na tela, especialmente em **Views (XML)**.

Vou detalhar o funcionamento e o uso dessa função, incluindo sua versão para Compose, conforme encontrado no arquivo `Virtues.kt`.

-----

# 📐 Utilitário: `calculateAvailableItemCount`

A função `calculateAvailableItemCount` calcula quantos itens de um determinado tamanho (`itemSizeDp`) e espaçamento (`itemPaddingDp`) podem ser dispostos lado a lado ou um sobre o outro dentro de um contêiner com uma dimensão conhecida em Pixels (`containerSizePx`).

### Assinatura do Composable

```kotlin
@Composable
fun CalculateAvailableItemCount(
    itemSize: Dp,
    itemPadding: Dp,
    direction: DpQualifier = DpQualifier.WIDTH,
    modifier: Modifier = Modifier.fillMaxSize(),
    onResult: (count: Int) -> Unit
)
```

### Detalhes dos Parâmetros (Compose)

| Parâmetro | Tipo | Descrição |
| :--- | :--- | :--- |
| **`itemSize`** | `Dp` | O tamanho (Largura ou Altura) de um item em **Dp**. |
| **`itemPadding`** | `Dp` | O **padding total (em Dp)** em um lado (ex: se for `4.dp`, a função considera `8.dp` de espaço total para o item). |
| **`direction`** | `DpQualifier` | A direção a ser medida: `WIDTH` (largura), `HEIGHT` (altura) ou `SMALL_WIDTH` (menor dimensão). **Padrão é `WIDTH`**. |
| **`modifier`** | `Modifier` | O `Modifier` que define o contêiner a ser medido. |
| **`onResult`** | `(count: Int) -> Unit` | **Callback** que retorna a contagem de itens calculada. |

### Exemplo de Uso Prático (Compose)

Este exemplo usa a função dentro de um `LazyRow` para definir dinamicamente o número de itens que cabem.

```kotlin
@Composable
fun ItemGridScreen() {
    var spanCount by remember { mutableStateOf(3) }
    
    // 1. Usa o Composable para medir o espaço e calcular o número de colunas
    Virtues.CalculateAvailableItemCount(
        itemSize = 100.dp, // Largura base do item
        itemPadding = 4.dp, // Padding de 4.dp (total de 8.dp horizontal)
        direction = DpQualifier.WIDTH,
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
        onResult = { count ->
            // 2. Atualiza a contagem de itens (spanCount) com o resultado
            if (count > 0) {
                spanCount = count
            }
        }
    )

    // 3. Exibe os itens no Layout
    LazyVerticalGrid(
        columns = GridCells.Fixed(spanCount), // Usa o valor calculado
        contentPadding = PaddingValues(16.dp)
    ) {
        items(20) { index ->
            // Seu item
        }
    }
}
```

-----

# KOTLIN, JAVA - CODE

## 🚪 `Virtues` Core Object: Gateway e Utilitários de Layout

O objeto **`Virtues`** é o ponto de entrada estático para o dimensionamento no **View System** (XML) e fornece métodos práticos de utilidade que não estão vinculados ao Compose. Ele expõe a capacidade de calcular dimensões *Fixed*, *Dynamic* e converter *Unidades Físicas* diretamente no código Kotlin/Java usando o objeto `Resources`.

## 1\. ⚙️ Funções Gateway de Dimensionamento (Views/XML)

Essas funções recebem um valor **base em DP** e o objeto `Resources` do seu `Context` e retornam o valor final ajustado em **Float** ou **Int** (já convertido para Pixels, Px).

### Dimensionamento **FIXED** (Logarítmico/AR)

O `Fixed` é a melhor escolha para um **ajuste sutil e refinado** que considera o Aspect Ratio da tela.

| Função | Unidade de Retorno | Descrição |
| :--- | :--- | :--- |
| `fixedDp(value, screenType, resources)` | **Float (DP)** | Retorna o valor ajustado em DP (não convertido para Px). |
| `fixedPx(value, screenType, resources)` | **Float (PX)** | Retorna o valor ajustado em DP, *convertido para Pixels reais* (melhor para `layout_width`/`layout_height`). |
| `fixedSp(value, screenType, resources)` | **Float (PX)** | Retorna o valor ajustado em DP, *convertido para Pixels de Escala (SP)*. |
| `fixedEm(value, screenType, resources)` | **Float (PX)** | Retorna o valor ajustado em DP, *convertido para Pixels de Escala (EM)*. |

**Exemplo de Uso em Código Kotlin (Views/XML):**

```kotlin
// Em uma Activity/Fragment:
val resources = context.resources
val adjustedWidthPx = Virtues.fixedPx(
    value = 200f,
    screenType = ScreenType.LOWEST,
    resources = resources
).toInt()

myView.layoutParams.width = adjustedWidthPx
```

### Dimensionamento **DYNAMIC** (Percentual/Proporcional)

O `Dynamic` é ideal para um **ajuste agressivo e proporcional** que tenta manter a mesma porcentagem da largura/altura total da tela.

| Função | Unidade de Retorno | Descrição |
| :--- | :--- | :--- |
| `dynamicDp(value, screenType, resources)` | **Float (DP)** | Retorna o valor ajustado em DP (não convertido para Px). |
| `dynamicPx(value, screenType, resources)` | **Float (PX)** | Retorna o valor ajustado em DP, *convertido para Pixels reais*. |
| `dynamicSp(value, screenType, resources)` | **Float (PX)** | Retorna o valor ajustado em DP, *convertido para Pixels de Escala (SP)*. |
| `dynamicEm(value, screenType, resources)` | **Float (PX)** | Retorna o valor ajustado em DP, *convertido para Pixels de Escala (EM)*. |

**Exemplo de Uso em Código Kotlin (Views/XML):**

```kotlin
// Em uma Activity/Fragment:
val resources = context.resources
val dynamicTextSizeSp = Virtues.dynamicSp(
    value = 18f, // 18dp convertido para SP
    screenType = ScreenType.LOWEST,
    resources = resources
)

myTextView.textSize = dynamicTextSizeSp // Já está em PX, pronto para ser usado como SP
```

-----

## 2\. 📐 Utilitários de Conversão de Unidades Físicas (MM, CM, INCH)

O objeto `Virtues` também expõe o sistema de conversão de unidades físicas (fornecido por `VirtuesPhysicalUnits`) para uso direto no View System.

| Função | Unidade de Origem | Unidade de Retorno | Descrição |
| :--- | :--- | :--- | :--- |
| `toDp(value, type, resources)` | INCH, CM, MM | **Float (DP)** | Converte a medida física para Pixels Lógicos (DP). |
| `toSp(value, type, resources)` | INCH, CM, MM | **Float (PX)** | Converte a medida física para Pixels de Escala (SP), já em PX. |
| `toPx(value, type, resources)` | INCH, CM, MM | **Float (PX)** | Converte a medida física para Pixels reais (PX). |

**Exemplo de Conversão:**

```kotlin
// 15 Milímetros convertidos para Dp
val fifteenMmInDp = Virtues.toDp(15f, UnitType.MM, resources)

// 1 Polegada convertida para Px (ideal para telas com densidade conhecida)
val oneInchInPx = Virtues.toPx(1.0f, UnitType.INCH, resources) 
```

-----

## 3\. 📦 Utilitários de Layout (Cálculo de Itens Disponíveis)

Um dos utilitários mais poderosos fornecidos pelo objeto `Virtues` é a capacidade de calcular quantos itens cabem em um contêiner, o que é essencial para layouts como `RecyclerViews` e `GridViews`.

### Função: `calculateAvailableItemCount`

Esta função determina quantos itens com um determinado tamanho e padding podem ser dispostos dentro de um contêiner, dada a dimensão do contêiner em **Pixels (Px)**.

| Parâmetro | Tipo | Descrição |
| :--- | :--- | :--- |
| `containerSizePx` | `Int` | O tamanho (Largura ou Altura) do contêiner **em Pixels (PX)**. |
| `itemSizeDp` | `Float` | O tamanho base de um único item **em DP**. |
| `itemMarginDp` | `Float` | O padding total ao redor do item **em DP**. |
| `resources` | `Resources` | O objeto Resources para conversão DP $\rightarrow$ Px. |
| **Retorno** | `Int` | A contagem de itens calculada. |

**Exemplo de Uso em Código Kotlin (RecyclerView/GridView):**

```kotlin
// 1. Obtenha a largura do RecyclerView em Pixels (após o layout ser resolvido)
val recyclerViewWidthPx: Int = myRecyclerView.width 

// 2. Calcule quantos cards cabem na largura, considerando um Card de 100dp com 8dp de padding
val itemCount = Virtues.calculateAvailableItemCount(
    containerSizePx = recyclerViewWidthPx,
    itemSizeDp = 100f, // 100dp
    itemMarginDp = 8f,  // 8dp (padding total, ex: 4dp esq + 4dp dir)
    resources = resources
)

// 3. Use o itemCount para definir o spanCount no GridLayoutManager
// val layoutManager = GridLayoutManager(context, itemCount)
```

-----

# 🔎 Análise Detalhada: Integração com Views e Data Binding

## 1\. Data Binding Adapters (`DimensBindingAdapters.kt/.java`)

Os `BindingAdapters` são a ponte essencial que permite usar valores base em DP diretamente no XML e ter o ajuste dinâmico do **Virtues** aplicado em tempo de execução.

### Como Funciona

1.  **XML:** No layout (`activity_dynamic_data_binding.xml`), você declara atributos customizados, como `app:dynamicWidthDp`.
2.  **`BindingAdapter`:** O Android encontra a função correspondente anotada com `@BindingAdapter` (ex: `setDynamicWidth`).
3.  **Cálculo:** A função recebe o `View` e o valor base em `Float` (ex: `48f`). Ela usa o método de *gateway* do **Virtues** para fazer o cálculo dinâmico:
    ```kotlin
    val pxValue = Virtues.dynamic(dpValue).toPx(view.resources)
    ```
4.  **Aplicação:** O valor ajustado em Pixels (`pxValue`) é então aplicado diretamente ao `layoutParams` da `View`.

### Exemplos de Adaptadores Customizados

| Atributo XML | Finalidade | Conversão Interna | Arquivos |
| :--- | :--- | :--- | :--- |
| `app:dynamicWidthDp` | Largura da `View` | DP Dinâmico $\rightarrow$ **PX** | `View.layoutParams.width` |
| `app:dynamicHeightDp` | Altura da `View` | DP Dinâmico $\rightarrow$ **PX** | `View.layoutParams.height` |
| `app:dynamicTextSizeDp` | Tamanho da Fonte | DP Dinâmico $\rightarrow$ **SP** (em PX) | `TextView.textSize` |
| `app:dynamicTextSizeEm` | Tamanho da Fonte | DP Dinâmico $\rightarrow$ **EM** (em PX) | `TextView.textSize` |

### Uso no XML (`activity_dynamic_data_binding.xml`)

O layout utiliza uma variável `dimenValue` (definida como `48f` na `MainActivity`) para demonstrar a aplicação do ajuste dinâmico:

```xml
<View
    android:id="@+id/dynamic_db_view"
    app:dynamicWidthDp="@{dimenValue}"  app:dynamicHeightDp="@{24f}"        android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    ... />

<TextView
    app:dynamicTextSizeDp="@{20f}"      android:text="Adjusted Dynamic Text"
    ... />
```

-----

## 2\. Uso Direto no Código (`MainActivity.kt/.java`)

A `MainActivity` demonstra como usar o objeto `Virtues` para calcular dimensões manualmente no código, o que é necessário para layouts mais complexos, manipulação de margens (`MarginLayoutParams`) e cálculos de percentual.

### Exemplo 1: Fixed vs. Dynamic (Manual)

O código demonstra a diferença entre os dois sistemas de dimensionamento ao calcular um valor base de `16dp`.

| Método | Filosofia | Resultado (em PX) |
| :--- | :--- | :--- |
| **`Virtues.fixed(16f).toPx(...)`** | Ajuste Sutil (Logarítmico/AR) | `adjustedFixedPx` |
| **`Virtues.dynamic(16f).toPx(...)`** | Ajuste Proporcional (Percentual) | `adjustedDynamicPx` |

**Exemplo (Kotlin):**

```kotlin
// 2. Uso Fixo/Sutil: Calcula a margem esquerda com o fator de ajuste Fixed
val adjustedFixedPx = Virtues.fixed(16f).toPx(resources).toInt()
val fixedLayoutParams = binding.fixedView.layoutParams as ViewGroup.MarginLayoutParams
fixedLayoutParams.marginStart = adjustedFixedPx

// 2. Uso Dinâmico/Agressivo: Calcula a largura
val adjustedDynamicPx = Virtues.dynamic(48f).toPx(resources).toInt()
binding.dynamicView.layoutParams.width = adjustedDynamicPx
```

### Exemplo 2: Dynamic Percentage (`dynamicPercentagePx`)

O **Virtues** fornece uma função dedicada para calcular uma porcentagem específica da largura ou altura da tela em Pixels.

| Função | Parâmetros | Finalidade |
| :--- | :--- | :--- |
| **`dynamicPercentagePx`** | `percentage: 0.80f`, `type: ScreenType.LOWEST` | Calcula 80% da dimensão mínima da tela (largura ou altura). |

**Exemplo (Kotlin/Java):**

```kotlin
// Calcula 80% da dimensão LOWEST (menor dimensão da tela, W ou H) em PX
val percentagePx = Virtues.dynamicPercentagePx(
    percentage = 0.80f,
    type = ScreenType.LOWEST,
    resources = resources
)

// Define a largura de uma View como 80%
binding.percentageView.layoutParams.width = percentagePx.toInt()
```

### Exemplo 3: Unidades Físicas (`VirtuesPhysicalUnits`)

A `MainActivity` demonstra a conversão de unidades físicas (milímetros) para Pixels (PX).

| Unidade Base | Função | Aplicação |
| :--- | :--- | :--- |
| 5.0 Milímetros | `VirtuesPhysicalUnits.toMm(mmValue, resources)` | Conversão para `mmInPx` |

**Exemplo (Kotlin/Java):**

```kotlin
val mmValue = 5.0f // 5 milímetros
val mmInPx = VirtuesPhysicalUnits.toMm(mmValue, resources)

// Define a margem superior da View usando a conversão de MM para PX
val layoutParams = binding.physicalUnitView.layoutParams as ViewGroup.MarginLayoutParams
layoutParams.topMargin = mmInPx.toInt()
```

-----

## 3\. Exemplos Adicionais: Casos de Uso Práticos

### A. Cálculo de Layout Dinâmico (RecyclerView)

É comum precisar de um `RecyclerView` que exiba o número máximo de itens com base no espaço disponível. O utilitário `calculateAvailableItemCount` é ideal para isso:

```kotlin
// Exemplo em uma Activity/Fragment:

// Largura da tela em Pixels
val containerWidthPx = resources.displayMetrics.widthPixels 

// Tamanho base dos cards (em DP)
val cardSizeDp = 120f
val cardPaddingDp = 8f 

// Determina quantos cards de 120dp cabem na largura atual da tela
val spanCount = Virtues.calculateAvailableItemCount(
    containerSizePx = containerWidthPx,
    itemSizeDp = cardSizeDp,
    itemMarginDp = cardPaddingDp * 2, // 8dp de margin total (4dp esq + 4dp dir)
    resources = resources
)

// Aplica ao LayoutManager
// myRecyclerView.layoutManager = GridLayoutManager(context, spanCount)
```

### B. Uso de Dimensões Fixas com Qualificador Customizado

Você pode aplicar o ajuste `Fixed` (mais suave) com customizações:

```kotlin
// Calcula um tamanho de texto ajustado, mas com sensibilidade menor (K=0.5)
// Isso suaviza a influência do Aspect Ratio.
val customSensitivityTextSize = VirtuesFixed(
    dpValue = 18f, 
    screenType = ScreenType.LOWEST
)
.withCustomSensitivity(sensitivityK = 0.5)
.toSp(resources)

myTextView.textSize = customSensitivityTextSize
```
