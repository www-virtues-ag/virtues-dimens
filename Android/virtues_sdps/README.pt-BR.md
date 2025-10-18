# 📐 Virtues SDP: Dimensionamento Dinâmico com Lógica Condicional

A biblioteca `Virtues` fornece um sistema robusto para garantir a consistência do layout em qualquer tela. O módulo **Virtues SDP** gerencia o escalonamento de unidades de dimensão (**Dp**) no Compose, introduzindo um sistema de prioridade para a aplicação de regras condicionais.

A classe **`Scaled`** (para Dp) permite que você defina um valor base de `Dp` e aplique regras de substituição baseadas no **Modo de UI** (`CAR`, `TELEVISION`, etc.) e em **Qualificadores de DP** (Largura Mínima, Altura, Largura), com um sistema de prioridades claro.

## 🚀 Utilização da Classe `Scaled` (para Dp)

A classe `Scaled` permite que você defina um valor base de `Dp` e aplique regras condicionais para substituí-lo em diferentes configurações de tela.

### 1\. Iniciação da Cadeia

Você pode iniciar a cadeia de construção `Scaled` a partir de um `Int` ou um `Dp`.

| Função | Descrição |
| :--- | :--- |
| `Int.scaledDp()` | Inicia a cadeia a partir de um valor `Int` (será convertido para `Dp`). |
| `Dp.scaledDp()` | Inicia a cadeia a partir de um valor `Dp` existente. |

#### Exemplo de Iniciação:

```kotlin
// Inicia com o valor base de 16dp
val baseSize = 16.scaledDp()

// Ou a partir de um Dp já definido
val baseSize = 16.dp.scaledDp()
```

-----

### 2\. Definição de Regras Condicionais

A classe `Scaled` oferece três níveis de prioridade para a definição de regras, garantindo que a regra mais específica seja aplicada primeiro.

A ordem de **resolução** é crucial: as entradas são classificadas por **prioridade ascendente** (1, 2, 3) e, secundariamente, pelo **valor do qualificador de DP descendente** (o maior valor de DP é avaliado primeiro).

| Prioridade | Método | Condição para Aplicação |
| :---: | :--- | :--- |
| **1 (Mais Alta)** | `screen(uiModeType, qualifierType, qualifierValue, customValue)` | O **Modo de UI** e o **Qualificador de DP** devem **casar**. |
| **2 (Média)** | `screen(type: UiModeType, customValue)` | Apenas o **Modo de UI** deve **casar**. |
| **3 (Mais Baixa)** | `screen(type: DpQualifier, value, customValue)` | Apenas o **Qualificador de DP** deve ser **maior ou igual** ao valor. |

#### 📝 Exemplo Detalhado de Uso:

Este exemplo demonstra como definir diferentes tamanhos de `Dp` para diferentes dispositivos e tamanhos de tela:

```kotlin
@Composable
fun DynamicBox() {
    val boxSize = 80.scaledDp() // Valor base em 80dp
        // Prioridade 1 (Máxima)
        // Se for um 'Relógio' (Wear OS) E a largura mínima for >= 200dp, use 40dp.
        .screen(
            uiModeType = UiModeType.WATCH,
            qualifierType = DpQualifier.SMALL_WIDTH,
            qualifierValue = 200,
            customValue = 40.dp // Tipo Dp
        )
        // Se for um 'Carro' (Android Auto), use 120dp (prioridade 2).
        .screen(
            type = UiModeType.CAR,
            customValue = 120.dp
        )
        // Prioridade 3 (Mais Baixa)
        // Se a tela tiver 'Largura Mínima' >= 720dp (Tablet Grande), use 150dp.
        .screen(
            type = DpQualifier.SMALL_WIDTH,
            value = 720,
            customValue = 150 // Tipo Int (será convertido para Dp)
        )

    Box(
        modifier = Modifier
            // A resolução final é feita aqui, aplicando o escalonamento dinâmico
            // com base no qualificador Smallest Width.
            .size(boxSize.sdp)
            .background(Color.Blue)
    )
}
```

-----

### 3\. Resolução Final do Valor (Getter Composable)

Após definir todas as regras, o valor final é resolvido usando um dos *getters* de propriedade `Composable`. O *getter* determina o **qualificador de dimensionamento dinâmico** a ser aplicado ao valor base ou customizado.

| Getter | Qualificador Base (Escalonamento Dinâmico) | Uso Ideal |
| :--- | :--- | :--- |
| **`.sdp`** | **Smallest Width (sw)**: `DpQualifier.SMALL_WIDTH` | Padrão, utiliza a dimensão mais restritiva (`smallestScreenWidthDp`) para o escalonamento base. |
| **`.hdp`** | **Height (h)**: `DpQualifier.HEIGHT` | Para elementos que devem escalar com base na altura da tela (`screenHeightDp`). |
| **`.wdp`** | **Width (w)**: `DpQualifier.WIDTH` | Para elementos que devem escalar com base na largura da tela (`screenWidthDp`). |

O processo de resolução (`resolve`):

1.  Lê a configuração de tela atual.
2.  Avalia as regras customizadas em ordem de prioridade (1 a 3).
3.  Se uma regra casar (ex: Prioridade 1), o `customValue` dessa regra é selecionado.
4.  Se nenhuma regra customizada casar, o `initialBaseDp` é usado.
5.  O valor de `Dp` selecionado tem seu valor inteiro extraído (`.value.toInt()`).
6.  O **escalonamento dinâmico** é aplicado a este valor inteiro usando a lógica `toDynamicScaledDp` e o qualificador (s, h, ou w) definido pelo *getter* (`.sdp`, `.hdp`, `.wdp`).

-----

## ⚙️ Extensões de Escalamento Direto

Para dimensionamento de Dp simples, sem a lógica condicional da classe `Scaled`, você pode usar as extensões diretas em `Int`. Elas aplicam o escalonamento dinâmico imediatamente, buscando um recurso de dimensão pré-calculado (ex: `@dimen/_16sdp`).

| Extensão | Qualificador Base (Escalonamento Dinâmico) | Recurso Buscado (Ex: para 16) |
| :--- | :--- | :--- |
| **`Int.sdp`** | `DpQualifier.SMALL_WIDTH` | `_16sdp` |
| **`Int.hdp`** | `DpQualifier.HEIGHT` | `_16hdp` |
| **`Int.wdp`** | `DpQualifier.WIDTH` | `_16wdp` |

#### Exemplo de Uso das Extensões:

```kotlin
@Composable
fun SimpleDimension() {
    Spacer(
        modifier = Modifier
            // Altura escalada dinamicamente por Smallest Width (sw)
            .height(18.sdp) 
            // Largura escalada dinamicamente por Width (w)
            .width(100.wdp)
    )
}
```

-----

## 💻 Suporte a XML Views e Recursos de Dimensão

O escalonamento dinâmico da biblioteca `Virtues` é construído com base na convenção de nomenclatura de **recursos de dimensão pré-calculados** no seu projeto Android. O sistema de escalonamento `sdp`, `hdp`, e `wdp` funciona buscando recursos de dimensão (`dimen`) que seguem um padrão específico.

### Formato dos Recursos de Dimensão

O `Virtues SDP` espera que os recursos de dimensão escalados sigam o padrão:

`@dimen/_<prefixo_negativo_opcional><valor><qualificador>dp`

Onde:

  * **`<valor>`**: O valor base da unidade (ex: `10`, `16`, `24`).
  * **`<qualificador>`**: O tipo de escalonamento:
      * **`s`** para **Smallest Width**.
      * **`h`** para **Height**.
      * **`w`** para **Width**.
  * **`<prefixo_negativo_opcional>`**: Se o valor for negativo (ex: para margens negativas), o prefixo é **`minus`** (ex: `_minus16sdp`).

### Como Usar em XML

Você pode usar esses recursos diretamente em atributos de dimensão (`android:layout_width`, `android:padding`, etc.) em seus arquivos de layout XML:

#### Exemplos de Uso em XML:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <TextView
        android:layout_width="@dimen/_100sdp"
        android:layout_height="wrap_content"
        android:layout_marginTop="@dimen/_16sdp"
        android:text="Largura e Margem SW" />

    <View
        android:layout_width="match_parent"
        android:layout_height="@dimen/_32hdp"
        android:background="#FF0000" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="@dimen/_minus8wdp"
        android:text="Margem Negativa W" />

</LinearLayout>
```

### Diferença Crítica: `Scaled` (Compose) vs. XML

É crucial entender que a **lógica condicional** da classe `Scaled` (com prioridades de `UiModeType` e `DpQualifier`) **só é executada no Compose**.

  * **Uso em XML (`@dimen/_16sdp`)**: Você obtém apenas o valor **escalado dinamicamente** do recurso `dimen` pré-calculado.
  * **Uso no Compose (`16.scaledDp().screen(...).sdp`)**: O sistema primeiro avalia as condições para trocar o valor base (ex: trocar `16dp` por `24dp` se for um tablet) e *depois* aplica o escalonamento dinâmico (`sdp`, `hdp`, ou `wdp`) ao valor base final.

O uso em XML é ideal para aplicar apenas o **escalonamento dinâmico** puro, enquanto o uso no Compose com `Scaled` permite a **customização condicional completa**.
