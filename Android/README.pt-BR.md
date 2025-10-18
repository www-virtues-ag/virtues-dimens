# 📚 Virtues: Documentação Abrangente

## 1\. O que é a Biblioteca Virtues?

**Virtues** é um sistema de dimensionamento que substitui o uso direto de valores fixos em **Dp** (Density-independent Pixel) por valores ajustados dinamicamente com base nas dimensões reais da tela.

Enquanto o Dp padrão do Android (1 Dp = 1/160 polegada) é constante, o Virtues o trata como um **valor base** que é escalado de forma inteligente (e previsível) em telas de diferentes tamanhos e proporções.

### 🎯 Público-Alvo

Desenvolvedores que buscam:

1.  **Consistência Visual:** Garantir que um botão de `48dp` pareça visualmente equilibrado em um celular de $360\text{dp}$ e em um tablet de $1024\text{dp}$.
2.  **Responsividade Refinada:** Evitar o crescimento excessivo de elementos em telas ultra-grandes (ex: $4K$ TV) ou o encolhimento de textos em telas pequenas.
3.  **Suporte Unificado:** Integrar facilmente sistemas de dimensionamento avançado em **Views/XML** (via Gateway e Data Binding) e **Jetpack Compose** (via Extensions).

-----

## 2\. O Coração Matemático: FXDP (Fixed) vs. DYDP (Dynamic)

A biblioteca oferece dois modelos de escalonamento principais, cada um com uma filosofia matemática distinta, atendendo a diferentes necessidades de design.

| Característica | Fixed (FX) - Ajuste Sutil (fxdp, fxsp, fxpx, fxem) | Dynamic (DY) - Ajuste Proporcional (dydp, dysp, dypx, dyem) |
| :--- | :--- | :--- |
| **Filosofia** | **Ajuste Logarítmico (Refinado)**. O crescimento é suave e desacelera em telas muito grandes. É o modelo padrão recomendado para a maioria dos componentes. | **Ajuste Percentual (Agressivo)**. Mantém a mesma proporção percentual da largura/altura da tela de referência. Cresce agressivamente em telas grandes. |
| **Cálculo Chave** | Fator de Ajuste Base $\times$ **Ajuste Logarítmico de Aspect Ratio (AR)**. | $\text{Valor Ajustado} = \frac{\text{DP Base}}{\text{DP Referência}} \times \text{Dimensão da Tela}$ |
| **Destaque** | O fator AR (**Aspect Ratio**) suaviza o ajuste em telas muito wide (ex: TVs) ou muito altas, prevenindo que o elemento cresça demais. | Útil para elementos que devem **preencher uma fração constante** do espaço disponível, como larguras de cards em um `LazyGrid`. |
| **Controle Extra** | Permite customizar o **coeficiente de sensibilidade (K)** do ajuste logarítmico. | Possui lógica integrada para **ignorar o escalonamento** em modo **Multi-Window** (Split Screen), evitando que a UI quebre em janelas pequenas. |
| **Uso Típico** | Altura de botões, paddings, ícones, fontes de títulos (para conforto visual). | Largura de contêineres, separadores de largura/altura, dimensões que dependem do viewport. |

### Quando usar cada opção?

  * **Escolha FX (Fixed):** Para componentes críticos que devem manter a "sensação" do design. Uma margem de $16\text{dp}$ deve crescer para $20\text{dp}$ ou $24\text{dp}$ em um tablet, mas não para $40\text{dp}$. O FX garante esse crescimento sutil e controlado.
  * **Escolha DY (Dynamic):** Para o layout principal. Se um card tem $100\text{dp}$ em um telefone de $360\text{dp}$ (27.7% da largura), o Dynamic garantirá que ele ocupe aproximadamente $27.7\%$ da largura em qualquer tela (se usado o `ScreenType.LOWEST`).

-----

## 3\. Arquitetura e Performance

A biblioteca foi projetada para ter um impacto mínimo no desempenho de renderização.

### Estratégia de Performance

1.  **Cálculo Único de Fatores:** Os fatores de ajuste (`ScreenAdjustmentFactors`) são calculados apenas **uma vez** por mudança de configuração de tela (ex: rotação, redimensionamento de janela). Estes fatores são memorizados (`remember` no Compose, ou estaticamente no View System).
2.  **Extensões/Gateway Otimizados:** O uso das extensões de `Int` ou `Float` (ex: `56.fxdp`) ou dos métodos de Gateway (ex: `Virtues.fixedPx()`) garantem que o cálculo final seja rápido, aplicando o fator memorizado ao valor base.

### Comparação com Soluções de Qualificador (Ex: sdp/ssp)

| Solução | Abordagem | Vantagens | Desvantagens |
| :--- | :--- | :--- | :--- |
| **Virtues FX/DY** | **Cálculo em Tempo de Execução** (Runtime Calculation). | Flexibilidade total, **tamanhos customizados** (`17dp`, `49dp`), menor número de arquivos de recurso. | Pequeno *overhead* de cálculo por dimensão (o fator é fixo). |
| **SDP/SSP Tradicional** | **Valores Estáticos Qualificados** (Valores em `dimens.xml` por `sw600dp`). | Zero *overhead* em tempo de execução. | Requer a geração de milhares de arquivos XML de dimensão (ex: $1\text{dp}$ a $600\text{dp}$), dificultando tamanhos customizados. |

**Conclusão de Performance:** Virtues troca a complexidade estática de arquivos XML (SDP) por um **cálculo otimizado e memorizado** em tempo de execução, oferecendo maior flexibilidade e suporte a dimensões customizadas.

-----

## 4\. Destaques e Funcionalidades Avançadas

### A. Qualificadores Dinâmicos e UiMode

A biblioteca permite customizar o cálculo do ajuste com base nas dimensões da tela (via `DpQualifier`) e no modo de interface do usuário (via `UiModeType`).

| Classe/Enum | Propósito | Tipos Disponíveis |
| :--- | :--- | :--- |
| **`DpQualifier`** | Define qual dimensão de tela deve ser a base para o ajuste customizado. | `SMALL_WIDTH` (`smallestWidthDp`), `WIDTH` (largura), `HEIGHT` (altura) |
| **`UiModeType`** | Permite aplicar diferentes fatores de ajuste para tipos de dispositivos específicos (maior prioridade). | `TELEVISION`, `CAR`, `WATCH`, `NORMAL`, `VR_HEADSET`, etc. |

### B. Unidades Físicas (`VirtuesPhysicalUnits`)

O subsistema de Unidades Físicas (MM, CM, INCH) permite definir dimensões com base em medidas reais. Isso é crucial para wearables (Wear OS) ou aplicações que exigem precisão absoluta, como em impressão ou sistemas de medição.

  * **Uso:** `VirtuesPhysicalUnits.toMm(5.0f, resources)` converte $5\text{mm}$ para **Pixels (PX)**.
  * **Aplicações:** Calcular o raio de um dispositivo redondo (ex: Wear OS) com precisão (função `radius()`).

### C. Utilitário de Layout Inteligente (`calculateAvailableItemCount`)

Esta função calcula quantos itens de um determinado tamanho cabem em um contêiner, resolvendo o problema de definir o `spanCount` dinamicamente em `GridLayoutManager` ou `LazyVerticalGrid`.

  * **Views/XML:** `Virtues.calculateAvailableItemCount(containerSizePx, itemSizeDp, itemMarginDp, resources)`.

-----

## 5\. Limitações e Requisitos

| Área | Limitação/Requisito | Impacto |
| :--- | :--- | :--- |
| **Performance** | O cálculo do fator de ajuste ocorre no *runtime*. | Embora otimizado, se o *overhead* de cálculo for uma preocupação extrema, a solução tradicional de $\text{XML}$-somente pode ser preferível. |
| **Unidades Físicas** | A precisão depende dos metadados fornecidos pelo dispositivo Android (`xdpi`, `ydpi` do `DisplayMetrics`). | Em emuladores ou dispositivos com densidade incorreta, a conversão física pode ser imprecisa. |
| **Uso em Views** | Requer o uso do objeto `Virtues` ou de `BindingAdapters` customizados. | Não é um sistema `plug-and-play` como um gerador de XML. O código ou o XML deve ser adaptado para chamar as funções da biblioteca. |
| **Tamanho de Valores** | Algumas extensões (como em soluções Sdp-like) podem ter um limite prático de valores a serem usados (ex: $\pm 600$), para otimização de recursos e evitar *overhead* excessivo na inicialização. | O desenvolvedor deve garantir que os valores DP base estejam em uma faixa razoável. |\<ctrl63\>
