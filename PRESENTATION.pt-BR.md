# 📖 Apresentação da Biblioteca Virtues: O Essencial da Responsividade

**Virtues** é um sistema de gerenciamento de dimensões unificado, projetado para resolver o desafio central do desenvolvimento moderno de interfaces: **garantir que o layout e a experiência do usuário permaneçam consistentes e visualmente confortáveis em qualquer tamanho, proporção e tipo de tela.**

Ele transcende a limitação do dimensionamento padrão de pixels independentes de densidade (**Dp/Pt**), tratando esses valores como meros pontos de partida, que são então ajustados por algoritmos matemáticos sofisticados.

---

```kotlin
dependencies {
    // Core (Dynamic + Fixed)
    implementation("com.github.bodenberg.virtues:virtues-dynamic:1.0.0")

    // SDP & SSP scaling (optional)
    implementation("com.github.bodenberg.virtues:virtues-sdps:1.0.0")
    implementation("com.github.bodenberg.virtues:virtues-ssps:1.0.0")

    // All in one
    implementation("com.github.bodenberg.virtues:virtues-all:1.0.0")
}

maven { url 'https://jitpack.io' } //or maven central
```

```kotlin
dependencies {
    // Core (Dynamic + Fixed)
    implementation("ag.virtues.dimens:virtues-dynamic:1.0.0")

    // SDP & SSP scaling (optional)
    implementation("ag.virtues.dimens:virtues-sdps:1.0.0")
    implementation("ag.virtues.dimens:virtues-ssps:1.0.0")

    // All in one
    implementation("ag.virtues.dimens:virtues-all:1.0.0")
}

mavenCentral()
```



### 🌐 Princípio Unificado e Compatibilidade Multiplataforma

O **Virtues** foi concebido com uma arquitetura agnóstica à plataforma. A lógica central de cálculo de fatores de ajuste é idêntica para todas as plataformas, garantindo que o dimensionamento seja consistente onde quer que seu código esteja.

| Plataforma | Integração | Paradigmas Suportados |
| :--- | :--- | :--- |
| **Android** | Nativa. Bibliotecas de extensão para **Jetpack Compose** e **View System (XML/Kotlin/Java)**. | Uso de `Dp`, `Sp` e `Px` ajustados dinamicamente. |
| **iOS/Multiplataforma** | A lógica central é implementada para ser usada em **Swift/SwiftUI** ou **Kotlin Multiplatform (KMP)**. | Uso de `CGFloat` ajustado dinamicamente para `Pt` ou `Px` escaláveis. |

### 🧠 O Coração da Biblioteca: Dois Modelos de Escalonamento

A força do Virtues reside na oferta de dois modelos matemáticos de ajuste. O desenvolvedor escolhe a abordagem ideal para cada componente, alcançando uma responsividade que vai além da simples regra de "tamanho de tela".

#### 1. Fixed (FX): O Ajuste Sutil e Logarítmico
* **Filosofia:** Crescimento **controlado** e **suave**. O ajuste se baseia no menor lado da tela (**Smallest Width DP**) e é modulado por uma **função logarítmica** que considera o **Aspect Ratio (Proporção da Tela)**.
* **Destaque:** Impede que elementos como paddings e margens cresçam de forma exagerada em tablets wide ou monitores 4K. É a escolha ideal para manter a densidade da informação.
* **Melhor Uso:** Paddings, margens, alturas de componentes de interação (botões, campos de texto) e pequenos ícones.

#### 2. Dynamic (DY): O Ajuste Proporcional e Agressivo
* **Filosofia:** Crescimento **agressivo** e **proporcional**. O valor ajustado mantém a **mesma porcentagem da tela** de referência. Se um elemento ocupa 25% da largura da tela de um telefone, ele continuará ocupando 25% em um tablet.
* **Destaque:** Possui lógica integrada para mitigar problemas em modo **Multi-Window (Split Screen)**, desativando o escalonamento agressivo quando a interface é espremida, prevenindo quebras de layout.
* **Melhor Uso:** Largura de contêineres, elementos grandes (ex: imagens de destaque) ou quando se deseja que o tamanho do texto acompanhe o crescimento do viewport.

---

### ⚡ Desempenho e Arquitetura

O Virtues foi otimizado para a **máxima eficiência em tempo de execução**.

* **Fatores Cheados:** Os fatores de ajuste matemáticos (**FX** e **DY**) são calculados apenas uma vez por mudança de configuração de tela (ex: rotação). O resultado é armazenado em *cache* (ou memorizado no Compose) e reutilizado instantaneamente.
* **Baixo Overhead:** O cálculo final de uma dimensão é a simples multiplicação do valor base Dp pelo fator de ajuste já calculado. Isso resulta em um *overhead* de processamento **insignificante** na renderização de UI.
* **Vantagem sobre Soluções Estáticas:** Evita a necessidade de gerar centenas ou milhares de arquivos XML de recursos (`values-sw600dp/dimens.xml`), comum em outras bibliotecas, oferecendo flexibilidade total para qualquer valor Dp/Pt.

---

### ✨ Destaques e Funcionalidades Exclusivas

1.  **Unidades Físicas (MM, CM, INCH):** Um subsistema de conversão permite definir dimensões com base em medidas do mundo real (ex: $1\text{cm}$ de margem), essencial para aplicações de precisão (wearables, impressão).
2.  **Qualificadores Avançados:** Permite que o ajuste seja baseado em qualificadores customizados, como `WIDTH` (largura total), `HEIGHT` (altura total) ou `SMALL_WIDTH` (o menor lado da tela), e pode ser condicionado ao tipo de dispositivo (**UI Mode Type**: TV, Carro, Relógio).
3.  **Utilitário de Layout Inteligente:** A função `calculateAvailableItemCount` calcula quantos itens de um tamanho Dp/Pt e espaçamento definidos cabem no espaço disponível do contêiner, otimizando layouts de grade dinamicamente.
4.  **Integração com Views (Android):** O objeto **`Virtues`** atua como um *Gateway* para o sistema tradicional de Views, permitindo a conversão direta para Pixels (`fixedPx`, `dynamicPx`) e integração transparente via **Data Binding Adapters** no XML.

### 🛑 Limitações e Considerações

* **Dependência de Código:** Ao contrário de soluções baseadas em arquivos de recursos, o Virtues exige que o desenvolvedor use as extensões de código (`.fxdp`, `.dydp`) ou os métodos Gateway.
* **Precisão da Conversão Física:** A precisão das conversões de Unidades Físicas (MM/INCH) depende da acurácia dos metadados de densidade (`xdpi`, `ydpi`) fornecidos pelo sistema operacional do dispositivo.

Em resumo, o **Virtues** oferece um conjunto de ferramentas poderosas e matematicamente sólidas para criar interfaces verdadeiramente adaptáveis, independentemente do dispositivo ou plataforma.

